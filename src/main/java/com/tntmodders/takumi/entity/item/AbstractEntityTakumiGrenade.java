package com.tntmodders.takumi.entity.item;

import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public abstract class AbstractEntityTakumiGrenade extends EntityThrowable {
    
    protected int count = 0;
    
    public AbstractEntityTakumiGrenade(World worldIn) {
        super(worldIn);
    }
    
    public AbstractEntityTakumiGrenade(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }
    
    public AbstractEntityTakumiGrenade(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
    }
    
    @Override
    protected void onImpact(RayTraceResult result) {
        this.count++;
        if (!this.world.isRemote) {
            if (this.count <= this.getCount()) {
                TakumiUtils.takumiCreateExplosion(world, this, this.posX, this.posY, this.posZ, this.getPower(), false, this.getDestroy());
            } else {
                this.setDead();
            }
        }
    }
    
    public abstract int getCount();
    
    public abstract int getPower();
    
    public abstract boolean getDestroy();
}
