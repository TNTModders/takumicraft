package com.tntmodders.takumi.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityTakumiParachute extends Entity {

    public EntityTakumiParachute(World worldIn) {
        super(worldIn);
        this.setSize(1, 1);
        this.noClip = false;
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
            this.motionX = this.getControllingPassenger().motionX * 2;
            this.motionZ = this.getControllingPassenger().motionZ * 2;
            this.rotationYaw = this.getControllingPassenger().rotationYaw;
        }
        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        if (this.onGround || this.getPassengers().stream().anyMatch(entity -> entity.onGround)) {
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, 0f, false);
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
        if (amount > 2 && !source.isExplosion()) {
            if (!this.world.isRemote) {
                this.world.createExplosion(this, this.posX, this.posY, this.posZ, 3f, false);
            }
            this.setDead();
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }
}
