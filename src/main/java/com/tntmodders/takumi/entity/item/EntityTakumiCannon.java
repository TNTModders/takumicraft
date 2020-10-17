package com.tntmodders.takumi.entity.item;

import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.core.TakumiPacketCore;
import com.tntmodders.takumi.network.MessageTakumiCannon;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityTakumiCannon extends Entity {
    private EnumFacing facing;
    private int resetTick;

    public EntityTakumiCannon(World worldIn) {
        super(worldIn);
        this.setSize(3f, 3f);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.ticksExisted == 1) {
            if (!this.world.isRemote) {
                TakumiPacketCore.INSTANCE.sendToAll(new MessageTakumiCannon(this.getEntityId(), ((byte) this.getFacing().getHorizontalIndex())));
            }
        }
        if (this.getControllingPassenger() != null) {
            this.rotationPitch = this.getControllingPassenger().rotationPitch;
            if (this.rotationPitch > 0) {
                this.rotationPitch = 0;
            }
        }
        if (this.resetTick > 0) {
            this.resetTick--;
            if (this.world.isRemote) {
                for (int i = 0; i < 5; ++i) {
                    this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL,
                            this.posX + (this.rand.nextDouble() - 0.5D) * this.width,
                            this.posY + this.rand.nextDouble() * this.height,
                            this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    @Override
    public Entity getControllingPassenger() {
        if (this.getPassengers() != null && !this.getPassengers().isEmpty() && this.getPassengers().get(0) instanceof EntityPlayer) {
            return this.getPassengers().get(0);
        }
        return null;
    }

    @Override
    protected void entityInit() {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        this.facing = EnumFacing.getHorizontal(compound.getInteger("facing"));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setInteger("facing", this.facing.getHorizontalIndex());
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBox(Entity entityIn) {
        return this.getEntityBoundingBox();
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox() {
        return this.getEntityBoundingBox().contract(0, 2, 0);
    }

    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        if (this.getControllingPassenger() == null) {
            if (player.isSneaking() && player.getHeldItem(hand).getItem() == TakumiItemCore.ENERGY_CORE && this.resetTick <= 0) {
                if (!this.world.isRemote) {
                    EntityTakumiCannonBall grenede = new EntityTakumiCannonBall(this.world, player);
                    grenede.setHeadingFromThrower(this, this.rotationPitch, this.getFacing().getOpposite().getHorizontalAngle(), 0.0F, 2F, 0.5F);
                    grenede.setPosition(this.posX, this.posY + 1, this.posZ);
                    if (world.spawnEntity(grenede)) {
                        this.world.createExplosion(this, this.posX, this.posY, this.posZ, 0f, false);
                        if (!player.isCreative()) {
                            player.getHeldItem(hand).shrink(1);
                        }
                    }
                }
                this.resetTick = 100;
                return true;
            } else if (!player.isSneaking() && player.getRidingEntity() == null) {
                player.startRiding(this);
            } else {
                if (!this.world.isRemote) {
                    this.setFacing(player.getHorizontalFacing().getOpposite());
                }
            }
        }
        return super.processInitialInteract(player, hand);
    }

    @Override
    public boolean canRiderInteract() {
        return true;
    }

    public EnumFacing getFacing() {
        return this.facing == null ? EnumFacing.NORTH : this.facing;
    }

    public void setFacing(EnumFacing facing) {
        this.facing = facing == EnumFacing.UP || facing == EnumFacing.DOWN ? EnumFacing.NORTH : facing;
        this.rotationYaw = facing.getHorizontalAngle();
        if (!this.world.isRemote) {
            TakumiPacketCore.INSTANCE.sendToAll(new MessageTakumiCannon(this.getEntityId(), ((byte) this.getFacing().getHorizontalIndex())));
        }
    }

    @Override
    public double getMountedYOffset() {
        return this.height * 0.3D;
    }
}
