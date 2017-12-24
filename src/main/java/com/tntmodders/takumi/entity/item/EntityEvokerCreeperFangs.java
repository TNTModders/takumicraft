package com.tntmodders.takumi.entity.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.world.World;

public class EntityEvokerCreeperFangs extends EntityEvokerFangs {
    
    public EntityEvokerCreeperFangs(World worldIn) {
        super(worldIn);
    }
    
    public EntityEvokerCreeperFangs(World worldIn, double x, double y, double z, float p_i47276_8_, int p_i47276_9_, EntityLivingBase casterIn) {
        super(worldIn, x, y, z, p_i47276_8_, p_i47276_9_, casterIn);
    }
    
    @Override
    public void setDead() {
        if (!this.world.isRemote) {
            this.world.createExplosion(this.getCaster(), this.posX, this.posY, this.posZ, 2, true);
        }
        super.setDead();
    }
}
