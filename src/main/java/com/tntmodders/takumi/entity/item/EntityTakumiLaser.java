package com.tntmodders.takumi.entity.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityTakumiLaser extends EntityThrowable {
    public EntityTakumiLaser(World worldIn) {
        super(worldIn);
        this.setSize(0.5f, 0.5f);
    }

    public EntityTakumiLaser(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        this.setSize(0.5f, 0.5f);
    }

    public EntityTakumiLaser(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
        this.setSize(0.5f, 0.5f);
    }

    @Override
    public void onUpdate() {
        this.setGlowing(true);
        super.onUpdate();
        if (!this.world.isRemote) {
            this.world.createExplosion(this, this.lastTickPosX, this.lastTickPosY, this.lastTickPosZ, 2f, true);
        }
        if (this.ticksExisted > 200) {
            this.setDead();
        }
    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }

    public void setThrower(EntityLivingBase base) {
        this.thrower = base;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (result.entityHit != null) {
            result.entityHit.attackEntityFrom(DamageSource.causeIndirectDamage(this, this.getThrower()).setDamageBypassesArmor(), 20f);
        }
        this.setDead();
    }
}
