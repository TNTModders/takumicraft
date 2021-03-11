package com.tntmodders.takumi.entity.item;

import com.tntmodders.takumi.entity.EntityTakumiLightningBolt;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityVergerBall extends EntitySmallFireball {

    public EntityVergerBall(World worldIn) {
        super(worldIn);
    }

    public EntityVergerBall(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ) {
        super(worldIn, shooter, accelX, accelY, accelZ);
    }

    public EntityVergerBall(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
        super(worldIn, x, y, z, accelX, accelY, accelZ);
    }

    @Override
    protected boolean isFireballFiery() {
        return false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.setInvisible(true);
        if (this.world.isRemote) {
            for (int i = 0; i < 5; i++) {
                this.world.spawnParticle(EnumParticleTypes.CRIT_MAGIC, this.posX + this.rand.nextFloat() * 0.3, this.posY + this.rand.nextFloat() * 0.3,
                        this.posZ + this.rand.nextFloat() * 0.3, this.motionX / 3, this.motionY / 3, this.motionZ / 3);
            }
        }
        if (this.ticksExisted % 10 == 0) {
            EntityTakumiLightningBolt bolt = new EntityTakumiLightningBolt(this.world, this.posX, this.posY, this.posZ, false);
            this.world.addWeatherEffect(bolt);
            this.world.spawnEntity(bolt);
        }
        if (this.ticksExisted > 200) {
            this.setDead();
        }
    }


    @Override
    protected float getMotionFactor() {
        return 0.5f;
    }
}
