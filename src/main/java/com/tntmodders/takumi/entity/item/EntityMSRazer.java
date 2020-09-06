package com.tntmodders.takumi.entity.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityMSRazer extends AbstractEntityTakumiGrenade {

    public EntityMSRazer(World worldIn) {
        super(worldIn);
    }

    public EntityMSRazer(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (!(result.entityHit instanceof EntityPlayer || result.entityHit instanceof EntityXMS)) {
            this.count++;
            if (!this.world.isRemote) {
                if (this.count <= this.getCount()) {
                    this.world.createExplosion(this.thrower == null ? null : this.thrower.getControllingPassenger(),
                            this.posX, this.posY, this.posZ, this.getPower(), this.getDestroy());
                } else {
                    this.setDead();
                }
            }
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.ticksExisted > 150) {
            this.setDead();
        }
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public int getPower() {
        return 4;
    }

    @Override
    public boolean getDestroy() {
        return false;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }
}
