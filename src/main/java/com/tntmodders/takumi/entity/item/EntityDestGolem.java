package com.tntmodders.takumi.entity.item;

import com.google.common.base.Predicate;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityDestGolem extends EntityIronGolem {
    public EntityDestGolem(World worldIn) {
        super(worldIn);
        this.setSize(1.4f / 2f, 2.7f / 2f);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.0D, true));
        this.tasks.addTask(2, new EntityAIMoveTowardsTarget(this, 0.9D, 64.0F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.6D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, false,
                false, (Predicate<EntityLiving>) living ->
                living != null && living.isNonBoss()
                        && ((living instanceof EntityTakumiAbstractCreeper && !((EntityTakumiAbstractCreeper) living).takumiType().isDest() && ((EntityTakumiAbstractCreeper) living).takumiRank().getLevel() < 3)
                        || (living instanceof EntityMob && !(living instanceof EntityTakumiAbstractCreeper)))));
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        if (entityIn instanceof IMob && this.getRNG().nextInt(20) == 0) {
            this.setAttackTarget((EntityLivingBase) entityIn);
        }

        super.collideWithEntity(entityIn);
    }

    @Override
    public boolean canAttackClass(Class<? extends EntityLivingBase> cls) {
        return !this.isPlayerCreated() || !EntityPlayer.class.isAssignableFrom(cls);
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
