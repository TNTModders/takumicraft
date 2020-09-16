package com.tntmodders.takumi.entity.item;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityTakumiParachute extends Entity {
    public double initY;

    public EntityTakumiParachute(World worldIn) {
        super(worldIn);
        this.setSize(1, 1);
        this.noClip = false;
        this.setEntityInvulnerable(false);
    }

    @Override
    protected void entityInit() {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
    }


    @Override
    public boolean shouldRiderSit() {
        return true;
    }

    @Override
    public double getMountedYOffset() {
        return -this.height * 1.2;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    protected boolean canBeRidden(Entity entityIn) {
        return this.getPassengers().isEmpty();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.motionY = -0.1;
        if (this.getControllingPassenger() != null) {
            this.motionX = this.getControllingPassenger().motionX * 10;
            this.motionZ = this.getControllingPassenger().motionZ * 10;
            this.rotationYaw = this.getControllingPassenger().rotationYaw;
            if (this.initY - this.posY > 50 && this.getControllingPassenger() instanceof EntityPlayerMP) {
                TakumiUtils.giveAdvancementImpossible((EntityPlayerMP) this.getControllingPassenger(),
                        new ResourceLocation(TakumiCraftCore.MODID, "creeperbomb"),
                        new ResourceLocation(TakumiCraftCore.MODID, "parachutefall"));
            }
        }
        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        if (this.onGround || this.getPassengers().stream().anyMatch(entity -> entity.onGround)) {
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, 0f, false);
            this.getPassengers().forEach(entity -> {
                entity.dismountRidingEntity();
                entity.setPosition(entity.posX, entity.posY + 1.5d, entity.posZ);
            });
            this.setDead();
        }
    }

    @Override
    public Entity getControllingPassenger() {
        return this.getPassengers() != null && !this.getPassengers().isEmpty() ? this.getPassengers().get(0) : null;
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    public void updatePassenger(Entity passenger) {
        super.updatePassenger(passenger);
        passenger.fallDistance = 0;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (amount > 2 && !source.isExplosion() && source.getTrueSource() != this.getControllingPassenger()) {
            if (!this.world.isRemote) {
                this.world.createExplosion(this, this.posX, this.posY, this.posZ, 5f, false);
            }
            this.setDead();
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }

    @Override
    public AxisAlignedBB getCollisionBox(Entity entity) {
        return this.getEntityBoundingBox();
    }

/*    @Override
    public AxisAlignedBB getCollisionBoundingBox() {
        return this.getEntityBoundingBox();
    }*/

    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }
}
