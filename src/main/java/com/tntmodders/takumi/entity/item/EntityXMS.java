package com.tntmodders.takumi.entity.item;

import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.core.TakumiPacketCore;
import com.tntmodders.takumi.network.MessageMSMove;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class EntityXMS extends EntityFlying {

    public int attackModeTick;
    public boolean isAttackMode;

    public boolean serverAttackMode;

    public EntityXMS(World worldIn) {
        super(worldIn);
        this.setSize(10, 2);
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
                if (this.isAttackMode && this.attackModeTick < 20) {
                    this.attackModeTick++;
                } else if (this.attackModeTick > 0) {
                    this.attackModeTick--;
                }
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
        if (this.getPassengers().stream().anyMatch(entity -> entity == entityPlayer)) {
            if (this.serverAttackMode) {
                EntityMSRazer razer =
                        new EntityMSRazer(entityPlayer.world, ((EntityXMS) entityPlayer.getRidingEntity()));
                razer.setHeadingFromThrower(entityPlayer.getRidingEntity(),
                        entityPlayer.getRidingEntity().rotationPitch / 2.5f,
                        entityPlayer.getRidingEntity().rotationYaw + 5, 0, 10f, 0f);
                entityPlayer.world.spawnEntity(razer);
                entityPlayer.world.updateEntities();
                razer = new EntityMSRazer(entityPlayer.world, ((EntityXMS) entityPlayer.getRidingEntity()));
                razer.setHeadingFromThrower(entityPlayer.getRidingEntity(),
                        entityPlayer.getRidingEntity().rotationPitch / 2.5f,
                        entityPlayer.getRidingEntity().rotationYaw - 5, 0, 10f, 0f);
                entityPlayer.world.spawnEntity(razer);
                entityPlayer.world.updateEntities();
            }
        } else {
            if (!this.world.isRemote) {
                entityPlayer.startRiding(this, true);
            }
        }
        return true;
    }

    @SideOnly(Side.CLIENT)
    private void clientUpdate() {
        if (((EntityPlayerSP) this.getControllingPassenger()).movementInput.forwardKeyDown) {
            TakumiPacketCore.INSTANCE.sendToServer(new MessageMSMove((byte) (this.isAttackMode ? 0 : 1)));
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.getControllingPassenger() == null && source.getTrueSource() instanceof EntityPlayer &&
                !source.isExplosion()) {
            if (!this.world.isRemote) {
                EntityItem item = new EntityItem(this.world, this.posX, this.posY, this.posZ,
                        new ItemStack(TakumiItemCore.TAKUMI_XMS, 1));
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
