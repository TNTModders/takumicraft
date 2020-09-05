package com.tntmodders.takumi.entity.item;

import com.google.common.base.Predicate;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityDestGolem extends EntityIronGolem {
    public EntityDestGolem(World worldIn) {
        super(worldIn);
        this.setSize(1.4f / 2f, 2.7f / 2f);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityLiving.class, 1, false,
                false, (Predicate<EntityLiving>) living ->
                living != null && IMob.VISIBLE_MOB_SELECTOR.apply(living) &&
                        (!(living instanceof EntityTakumiAbstractCreeper) ||
                                (!((EntityTakumiAbstractCreeper) living).takumiType().isDest() && ((EntityTakumiAbstractCreeper) living).takumiRank().getLevel() < 3)) &&
                        living.isNonBoss()) {
            @Override
            protected double getTargetDistance() {
                return 256;
            }
        });
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.world.isRemote && this.ticksExisted % 20 == 0) {
            this.setHealth(this.getHealth() - 1);
        }
        if (this.getHealth() < 1) {
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, 5f, true);
            this.setDead();
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float) (20 + this.rand.nextInt(15)));
        return super.attackEntityAsMob(entityIn);
    }
}
