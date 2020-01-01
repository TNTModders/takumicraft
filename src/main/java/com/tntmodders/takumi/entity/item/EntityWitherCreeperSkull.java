package com.tntmodders.takumi.entity.item;

import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityWitherCreeperSkull extends EntityWitherSkull {
    public EntityWitherCreeperSkull(World worldIn) {
        super(worldIn);
    }

    public EntityWitherCreeperSkull(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ) {
        super(worldIn, shooter, accelX, accelY, accelZ);
    }

    public EntityWitherCreeperSkull(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
        super(worldIn, x, y, z, accelX, accelY, accelZ);
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (!this.world.isRemote) {
            if (result.entityHit != null) {
                if (this.shootingEntity != null) {
                    if (result.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.shootingEntity), 8.0F)) {
                        if (result.entityHit.isEntityAlive()) {
                            this.applyEnchantments(this.shootingEntity, result.entityHit);
                        } else {
                            this.shootingEntity.heal(5.0F);
                        }
                    }
                } else {
                    result.entityHit.attackEntityFrom(DamageSource.MAGIC, 5.0F);
                }

                if (result.entityHit instanceof EntityLivingBase) {
                    int i = 0;

                    if (this.world.getDifficulty() == EnumDifficulty.NORMAL) {
                        i = 10;
                    } else if (this.world.getDifficulty() == EnumDifficulty.HARD) {
                        i = 40;
                    }

                    if (i > 0) {
                        ((EntityLivingBase) result.entityHit).addPotionEffect(new PotionEffect(MobEffects.WITHER, 20 * i, 1));
                    }
                }
            }

            TakumiUtils.takumiCreateExplosion(this.world, this, this.posX, this.posY, this.posZ, 4.0F, true,
                    net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this.shootingEntity));
            this.setDead();
        }
    }
}
