package com.tntmodders.takumi.entity.item;

import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntitySmokeParticle extends EntityAreaEffectCloud {
    public EntitySmokeParticle(World worldIn) {
        super(worldIn);

    }

    public EntitySmokeParticle(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public void smokeInit(EntityLivingBase entity) {
        this.setOwner(entity);
        this.setParticle(EnumParticleTypes.CLOUD);
        this.setRadius(1.5f);
        this.setRadiusPerTick(-0.005f);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.getOwner() != null && !this.getOwner().isEntityAlive()) {
            this.setDead();
        }
        if (this.world.isRemote) {
            boolean flag = this.shouldIgnoreRadius();
            float f = this.getRadius();
            EnumParticleTypes enumparticletypes = this.getParticle();
            int[] aint = new int[enumparticletypes.getArgumentCount()];

            if (aint.length > 0) {
                aint[0] = this.getParticleParam1();
            }

            if (aint.length > 1) {
                aint[1] = this.getParticleParam2();
            }

            if (flag) {
                if (this.rand.nextBoolean()) {
                    for (int i = 0; i < 2; ++i) {
                        float f1 = this.rand.nextFloat() * ((float) Math.PI * 2F);
                        float f2 = MathHelper.sqrt(this.rand.nextFloat()) * 0.2F;
                        float f3 = MathHelper.cos(f1) * f2;
                        float f4 = MathHelper.sin(f1) * f2;
                        float fy = MathHelper.sin(this.rand.nextFloat() * 2f);
                        if (enumparticletypes == EnumParticleTypes.SPELL_MOB) {
                            int j = this.rand.nextBoolean() ? 16777215 : this.getColor();
                            int k = j >> 16 & 255;
                            int l = j >> 8 & 255;
                            int i1 = j & 255;
                            this.world.spawnAlwaysVisibleParticle(EnumParticleTypes.SPELL_MOB.getParticleID(), this.posX + (double) f3, this.posY + fy, this.posZ + (double) f4, (double) ((float) k / 255.0F), (double) ((float) l / 255.0F), (double) ((float) i1 / 255.0F));
                        } else {
                            this.world.spawnAlwaysVisibleParticle(enumparticletypes.getParticleID(), this.posX + (double) f3, this.posY + fy, this.posZ + (double) f4, 0.0D, 0.0D, 0.0D, aint);
                        }
                    }
                }
            } else {
                float f5 = (float) Math.PI * f * f;

                for (int k1 = 0; (float) k1 < f5; ++k1) {
                    float f6 = this.rand.nextFloat() * ((float) Math.PI * 2F);
                    float f7 = MathHelper.sqrt(this.rand.nextFloat()) * f;
                    float f8 = MathHelper.cos(f6) * f7;
                    float f9 = MathHelper.sin(f6) * f7;
                    float fy = MathHelper.sin(this.rand.nextFloat() * 2f);
                    if (enumparticletypes == EnumParticleTypes.SPELL_MOB) {
                        int l1 = this.getColor();
                        int i2 = l1 >> 16 & 255;
                        int j2 = l1 >> 8 & 255;
                        int j1 = l1 & 255;
                        this.world.spawnAlwaysVisibleParticle(EnumParticleTypes.SPELL_MOB.getParticleID(), this.posX + (double) f8, this.posY + fy, this.posZ + (double) f9, (double) ((float) i2 / 255.0F), (double) ((float) j2 / 255.0F), (double) ((float) j1 / 255.0F));
                    } else {
                        this.world.spawnAlwaysVisibleParticle(enumparticletypes.getParticleID(), this.posX + (double) f8, this.posY + fy, this.posZ + (double) f9, (0.5D - this.rand.nextDouble()) * 0.15D, 0.009999999776482582D, (0.5D - this.rand.nextDouble()) * 0.15D, aint);
                    }
                }
            }
        }
    }
}
