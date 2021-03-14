package com.tntmodders.takumi.entity.item;

import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.client.particle.ParticleCrit;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

//Unused
public class EntityStickyGrenade_KC extends EntityArrow {
    public boolean isDummy = false;
    private Entity entityHit;
    private Vec3d posHit;
    private int detonateTick = -1;

    public EntityStickyGrenade_KC(World worldIn) {
        super(worldIn);
    }

    public EntityStickyGrenade_KC(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public EntityStickyGrenade_KC(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }

    @Override
    protected ItemStack getArrowStack() {
        return ItemStack.EMPTY;
    }

    @Override
    public void playSound(SoundEvent soundIn, float volume, float pitch) {
        if (!this.isDummy) {
            super.playSound(soundIn, volume, pitch);
        }
    }

    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {
        if (!this.world.isRemote && !this.isDummy && this.detonateTick < 0) {
            if (raytraceResultIn.entityHit != null && raytraceResultIn.entityHit != this.shootingEntity) {
                this.entityHit = raytraceResultIn.entityHit;
                if (this.shootingEntity instanceof EntityLivingBase) {
                    this.entityHit.attackEntityFrom(DamageSource.causeMobDamage(((EntityLivingBase) this.shootingEntity)), 4f);
                }
                this.detonateTick = 1;
            } else if (raytraceResultIn.typeOfHit == RayTraceResult.Type.BLOCK) {
                this.detonateTick = 1;
                this.posHit = raytraceResultIn.hitVec;
            }
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.detonateTick > 0) {
            this.detonateTick++;
            if (this.entityHit != null) {
                this.setPosition(entityHit.posX, entityHit.posY + entityHit.getEyeHeight() * 1.1f, entityHit.posZ);
                if (this.entityHit.isDead) {
                    this.entityHit = null;
                }
            } else if (this.posHit != null) {
                this.setPosition(this.posHit.x, this.posHit.y, this.posHit.z);
            } else {
                this.detonateTick = -1;
            }
            if (this.detonateTick % 10 < 7 && this.detonateTick > 10) {
                double d = this.detonateTick / 10;
                for (int i = 0; i < d + 1; i++) {
                    if (this.rand.nextInt(6) < this.detonateTick / 10 && this.rand.nextInt(8) > this.detonateTick % 10 && this.rand.nextDouble() > this.rand.nextGaussian()) {
                        double x = this.posX + (this.rand.nextDouble() * d - d / 2) * 1.25;
                        double y = this.posY + (this.rand.nextDouble() * d - d / 2) / 1.5;
                        double z = this.posZ + (this.rand.nextDouble() * d - d / 2) * 1.25;
                        TakumiUtils.takumiCreateExplosion(this.world, this, x, y, z, ((float) (1f - d / 30)), false, true);
                    }
                }
            }
            if (this.detonateTick > 70) {
                this.setDead();
            }
        }
    }


    public static class ParticleClusterDummy extends ParticleCrit {
        public ParticleClusterDummy(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn) {
            super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0, 0, 0, 1);
            this.particleMaxAge = 1;
            this.motionX = 0;
            this.motionY = 0;
            this.motionZ = 0;
        }

        @Override
        public void move(double x, double y, double z) {
        }
    }
}
