package com.tntmodders.takumi.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityTakumiCannon extends Entity {
    public EntityTakumiCannon(World worldIn) {
        super(worldIn);
        this.setSize(3f, 3f);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.getControllingPassenger() != null) {
            this.rotationPitch = this.getControllingPassenger().rotationPitch;
            this.rotationYaw = this.getControllingPassenger().rotationYaw;
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
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
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
        if (player.getRidingEntity() == null) {
            player.startRiding(this);
        }
        return super.processInitialInteract(player, hand);
    }

    @Override
    public boolean canRiderInteract() {
        return true;
    }
}
