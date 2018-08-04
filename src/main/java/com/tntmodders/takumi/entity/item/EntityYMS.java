package com.tntmodders.takumi.entity.item;

import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.core.TakumiPacketCore;
import com.tntmodders.takumi.network.MessageMSMove;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class EntityYMS extends EntityFlying {

    public EntityYMS(World worldIn) {
        super(worldIn);
        this.setSize(8F, 6F);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.taskEntries.clear();
        this.targetTasks.taskEntries.clear();
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.getControllingPassenger() instanceof EntityPlayer) {
            if (this.getControllingPassenger() instanceof EntityPlayerSP) {
                this.clientUpdate();
            }
            this.rotationYaw = this.rotationYawHead = ((EntityPlayer) this.getControllingPassenger()).rotationYawHead;
            this.rotationPitch = this.getControllingPassenger().rotationPitch;
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        if (this.getControllingPassenger() instanceof EntityPlayer) {
            compound.setString("crew", this.getControllingPassenger().getName());
        } else {
            compound.setString("crew", "");
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.getString("crew") != null && !compound.getString("crew").isEmpty()) {
            if (this.world.getPlayerEntityByName(compound.getString("crew")) != null &&
                    (this.getPassengers().isEmpty() || this.getPassengers().stream().noneMatch(
                            entity -> entity instanceof EntityPlayer &&
                                    entity.getName().equals(compound.getString("crew"))))) {
                this.world.getPlayerEntityByName(compound.getString("crew")).startRiding(this, true);
            }
        }
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    protected boolean processInteract(EntityPlayer entityPlayer, EnumHand hand) {
        if (this.getControllingPassenger() == null && !this.world.isRemote) {
            entityPlayer.startRiding(this, true);
        }
        if (this.getPassengers().stream().anyMatch(entity -> entity == entityPlayer) && !this.world.isRemote) {
            EntityTNTPrimed primed = new EntityTNTPrimed(this.world);
            primed.setPosition(this.posX, this.posY, this.posZ);
            primed.setFuse(30);
            this.world.spawnEntity(primed);
        }
        return true;
    }

    @SideOnly(Side.CLIENT)
    private void clientUpdate() {
        if (((EntityPlayerSP) this.getControllingPassenger()).movementInput.forwardKeyDown) {
            TakumiPacketCore.INSTANCE.sendToServer(new MessageMSMove((byte) 0));
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.getControllingPassenger() == null && source.getTrueSource() instanceof EntityPlayer &&
                !source.isExplosion()) {
            if (!this.world.isRemote) {
                EntityItem item = new EntityItem(this.world, this.posX, this.posY, this.posZ,
                        new ItemStack(TakumiItemCore.TAKUMI_YMS, 1));
                this.world.spawnEntity(item);
            }
            this.setDead();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onDeath(DamageSource cause) {
        if (!this.world.isRemote) {
            this.world.newExplosion(this, this.posX, this.posY, this.posZ, 8f, true, true);
        }
        super.onDeath(cause);
    }

    @Override
    protected boolean isMovementBlocked() {
        return true;
    }

    @Override
    public void jump() {
        super.jump();
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        if (!(entityIn instanceof EntityPlayer)) {
            super.collideWithEntity(entityIn);
        }
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    protected void doWaterSplashEffect() {
        super.doWaterSplashEffect();
    }

    @Override
    public double getMountedYOffset() {
        return this.height * 0.2;
    }

    @Override
    public boolean shouldDismountInWater(Entity rider) {
        return false;
    }

    @Nullable
    @Override
    public Entity getControllingPassenger() {
        if (this.getPassengers().isEmpty()) {
            return null;
        }
        return this.getPassengers().get(0);
    }
}
