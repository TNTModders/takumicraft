package com.tntmodders.takumi.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityBigCreeperDummy extends Entity {

    public static final float MAX_HP = 2500;
    public int chargingTicks;
    private boolean isDamaged;
    private boolean charging;
    private float hp;

    public EntityBigCreeperDummy(World worldIn) {
        super(worldIn);
        this.setSize(2, 2);
        this.hp = MAX_HP;
    }

    public boolean isDamaged() {
        return isDamaged;
    }

    public void setDamaged(boolean damaged) {
        isDamaged = damaged;
    }

    public float getHP() {
        return hp;
    }

    public boolean getCharging() {
        return this.charging;
    }

    public void setCharging(boolean charging) {
        this.charging = charging;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.getCharging()) {
            this.chargingTicks++;
        }
    }

    @Override
    protected void entityInit() {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        this.hp = compound.getFloat("bigcreeperHP");
        this.isDamaged = compound.getBoolean("isDamaged");
        this.charging = compound.getBoolean("charging");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setFloat("bigcreeperHP", this.hp);
        compound.setBoolean("isDamaged", this.isDamaged);
        compound.setBoolean("charging", this.charging);
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
        return this.isDamaged ? this.getEntityBoundingBox() : null;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.isExplosion() && this.isDamaged) {
            this.hp = this.hp - amount;
        }
        return false;
    }

    @Override
    public boolean hitByEntity(Entity entityIn) {
        if (entityIn instanceof EntityPlayer && ((EntityPlayer) entityIn).isCreative()) {
            this.setDead();
            return true;
        }
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.isDamaged;
    }

    @Override
    public boolean isImmuneToExplosions() {
        return !this.isDamaged;
    }

    @Override
    public void setDead() {
        super.setDead();
        if (!this.world.isRemote && this.isDamaged && this.hp < 0) {
            this.world.getPlayers(EntityPlayer.class, input -> EntityBigCreeperDummy.this.getDistanceSqToEntity(input) < 10000).forEach(player
                    -> player.sendMessage(new TextComponentTranslation("entity.attackblock.message.dead")));
        }
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
