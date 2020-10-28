package com.tntmodders.takumi.entity.item;

import com.tntmodders.takumi.entity.mobs.boss.EntityBigCreeper;
import com.tntmodders.takumi.entity.mobs.boss.EntityKingCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityBigHomingBomb extends EntityShulkerBullet {

    private EntityLivingBase owner;

    public EntityBigHomingBomb(World worldIn) {
        super(worldIn);
    }

    public EntityBigHomingBomb(World worldIn, double x, double y, double z, double motionXIn, double motionYIn,
            double motionZIn) {
        super(worldIn, x, y, z, motionXIn, motionYIn, motionZIn);
    }

    public EntityBigHomingBomb(World worldIn, EntityLivingBase ownerIn, Entity targetIn, EnumFacing.Axis p_i46772_4_) {
        super(worldIn, ownerIn, targetIn, p_i46772_4_);
        this.owner = ownerIn;
    }

    @Override
    public void onUpdate() {
        this.setInvisible(true);
        super.onUpdate();
        if (this.world.isRemote) {
            for (int i = 0; i < 10; i++) {
                this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + this.rand.nextDouble() * 0.2,
                        this.posY + this.rand.nextDouble() * 0.2, this.posZ + this.rand.nextDouble() * 0.2, 0, 0, 0);
            }
        }
    }

    @Override
    protected void bulletHit(RayTraceResult result) {
        if (!(result.entityHit instanceof EntityBigCreeper) && !(result.entityHit instanceof EntityKingCreeper)) {
            if (!this.world.isRemote) {
                TakumiUtils.takumiCreateExplosion(this.world, this, this.posX, this.posY, this.posZ, 4f, true, true);
            }
            if (result.entityHit == null) {
                ((WorldServer) this.world).spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX, this.posY,
                        this.posZ, 2, 0.2D, 0.2D, 0.2D, 0.0D);
                this.playSound(SoundEvents.ENTITY_SHULKER_BULLET_HIT, 1.0F, 1.0F);
            } else {
                boolean flag = result.entityHit.attackEntityFrom(
                        DamageSource.causeIndirectDamage(this, this.owner).setProjectile(), 10.0F);

                if (flag) {
                    this.applyEnchantments(this.owner, result.entityHit);
                }
            }

            this.setDead();
        }
    }
}
