package com.tntmodders.takumi.entity.item;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityBigCreeperDummy extends Entity {

    public EntityBigCreeperDummy(World worldIn) {
        super(worldIn);
        this.setSize(1, 1);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        TakumiCraftCore.LOGGER.info(this.getPosition());
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

    /**
     * Returns the collision bounding box for this entity
     */
    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox() {
        return this.getEntityBoundingBox();
    }

    @Override
    public boolean hitByEntity(Entity entityIn) {
        if (entityIn instanceof EntityPlayer && ((EntityPlayer) entityIn).isCreative()) {
            this.setDead();
        }
        return false;
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
    public boolean isInRangeToRender3d(double x, double y, double z) {
        return true;
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        return true;
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return true;
    }
}
