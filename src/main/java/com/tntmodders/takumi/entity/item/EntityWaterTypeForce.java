package com.tntmodders.takumi.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.MoverType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityWaterTypeForce extends EntityLiving {
    public EntityWaterTypeForce(World worldIn) {
        super(worldIn);
        this.setNoGravity(true);
        this.setSize(1f, 1f);
        this.setInvisible(true);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.taskEntries.clear();
        this.targetTasks.taskEntries.clear();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.setInvisible(true);
        if (this.getPassengers().isEmpty()) {
            this.setDead();
        } else {
            this.move(MoverType.SELF, 0, 0.025, 0);
            if (this.ticksExisted % 10 == 0) {
                this.getPassengers().forEach(
                        entity -> entity.attackEntityFrom(DamageSource.DROWN.setDamageIsAbsolute(), 2f));
            }
            if (this.ticksExisted > 100) {
                if (!this.world.isRemote) {
                    this.world.createExplosion(this, this.posX, this.posY, this.posZ, 1.5f, false);
                }
                this.setDead();
            }
        }
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.world.isRemote) {
            for (int i = 0; i < 6; i++) {
                this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + this.rand.nextDouble() - 0.5,
                        this.posY + this.rand.nextDouble() + 1, this.posZ + this.rand.nextDouble() - 0.5, 0, 0, 0);
            }
        }
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }

    @Override
    public boolean shouldRiderSit() {
        return false;
    }

    @Override
    public boolean shouldDismountInWater(Entity rider) {
        return false;
    }
}
