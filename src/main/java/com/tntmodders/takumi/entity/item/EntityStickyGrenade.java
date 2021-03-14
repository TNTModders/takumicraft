package com.tntmodders.takumi.entity.item;

import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityStickyGrenade extends EntityArrow {
    public boolean isDummy = false;
    private Entity entityHit;
    private Vec3d posHit;
    private int detonateTick = -1;

    public EntityStickyGrenade(World worldIn) {
        super(worldIn);
    }

    public EntityStickyGrenade(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public EntityStickyGrenade(World worldIn, EntityLivingBase shooter) {
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
        if (!this.isDummy && this.detonateTick < 0) {
            if (raytraceResultIn.entityHit != null && raytraceResultIn.entityHit != this.shootingEntity) {
                this.entityHit = raytraceResultIn.entityHit;
                if (this.shootingEntity instanceof EntityLivingBase) {
                    this.entityHit.attackEntityFrom(DamageSource.causeMobDamage(((EntityLivingBase) this.shootingEntity)), 4f);
                }
                this.detonateTick = 1;
                this.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0F, 0.5F);
            } else if (raytraceResultIn.typeOfHit == RayTraceResult.Type.BLOCK) {
                this.detonateTick = 1;
                this.posHit = raytraceResultIn.hitVec;
                this.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0F, 0.5F);
            }
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.detonateTick > 0) {
            this.detonateTick++;
            if (this.entityHit != null) {
                this.setPosition(entityHit.posX + 0.2, entityHit.posY + entityHit.getEyeHeight() * 1.1f, entityHit.posZ + 0.2);
                if (this.entityHit.isDead) {
                    this.entityHit = null;
                }
            } else if (this.posHit != null) {
                this.setPosition(this.posHit.x, this.posHit.y, this.posHit.z);
            } else {
                this.detonateTick = -1;
            }

        }
        if (this.detonateTick > 70) {
            TakumiUtils.takumiCreateExplosion(world, this, posX, posY, posZ, 2f, false, true);
            this.setDead();
        }
    }
}