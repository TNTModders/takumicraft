package com.tntmodders.takumi.entity.ai;

/*
import com.tntmodders.takumi.entity.item.EntityAttackBlock;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class EntityAIMoveToAttackBlock extends EntityAIBase {
    protected final int attackInterval = 20;
    protected EntityCreature attacker;
    */
/**
     * An amount of decrementing ticks that allows the entity to attack once the tick reaches 0.
     *//*

    protected int attackTick;
    World world;
    */
/**
     * The speed with which the mob will approach the target
     *//*

    double speedTowardsTarget;
    */
/**
     * When true, the mob will continue chasing its target, even if it can't find a path to them right now.
     *//*

    boolean longMemory;
    */
/**
     * The PathEntity of our entity.
     *//*

    Path entityPathEntity;
    private int delayCounter;
    private double targetX;
    private double targetY;
    private double targetZ;
    private int failedPathFindingPenalty = 0;

    public EntityAIMoveToAttackBlock(EntityCreature creature, double speedIn, boolean useLongMemory) {
        this.attacker = creature;
        this.world = creature.world;
        this.speedTowardsTarget = speedIn;
        this.longMemory = useLongMemory;
        this.setMutexBits(3);
    }

    */
/**
     * Returns whether the EntityAIBase should begin execution.
     *//*

    @Override
    public boolean shouldExecute() {
        EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();

        if (!(entitylivingbase instanceof EntityAttackBlock)) {
            return false;
        } else {
            return entitylivingbase.isEntityAlive();
        }
    }

    */
/**
     * Returns whether an in-progress EntityAIBase should continue executing
     *//*

    @Override
    public boolean shouldContinueExecuting() {
        EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();

        if (!(entitylivingbase instanceof EntityAttackBlock)) {
            return false;
        } else {
            return entitylivingbase.isEntityAlive();
        }
    }

    */
/**
     * Execute a one shot task or start executing a continuous task
     *//*

    @Override
    public void startExecuting() {
        this.attacker.getNavigator().setPath(this.entityPathEntity, this.speedTowardsTarget);
        this.delayCounter = 0;
    }

    */
/**
     * Reset the task's internal state. Called when this task is interrupted by another one
     *//*

    @Override
    public void resetTask() {
        this.attacker.getNavigator().clearPathEntity();
    }

    */
/**
     * Keep ticking a continuous task that has already been started
     *//*

    @Override
    public void updateTask() {
        EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
        if (entitylivingbase != null) {
            this.attacker.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
            this.attacker.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 0.75);
            if (this.attacker.getNavigator().noPath()) {
                double distX = this.attacker.posX - entitylivingbase.posX;
                double distZ = this.attacker.posZ - entitylivingbase.posZ;
                if (distX * distX + distZ * distZ < 36 && this.attacker instanceof EntityCreeper) {
                    ((EntityCreeper) this.attacker).ignite();
                } else {
                    double offX = -8 + this.world.rand.nextInt(17);
                    double offY = -8 + this.world.rand.nextInt(17);
                    double offZ = -8 + this.world.rand.nextInt(17);
                    this.attacker.getMoveHelper().setMoveTo(entitylivingbase.posX + offX, entitylivingbase.posY + offY, entitylivingbase.posZ + offZ, 0.75);
                }
            }
            double d0 = this.attacker.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY,
                    entitylivingbase.posZ);
            --this.delayCounter;

            if (this.delayCounter <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D ||
                    entitylivingbase.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D ||
                    this.attacker.getRNG().nextFloat() < 0.1F)) {
                this.targetX = entitylivingbase.posX;
                this.targetY = entitylivingbase.getEntityBoundingBox().minY;
                this.targetZ = entitylivingbase.posZ;
                this.delayCounter = 4 + this.attacker.getRNG().nextInt(7);
                if (!this.attacker.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.speedTowardsTarget)) {
                    this.delayCounter += 5;
                }
            }

            this.attackTick = Math.max(this.attackTick - 1, 0);
            this.checkAndPerformAttack(entitylivingbase, d0);
        }
    }

    protected void checkAndPerformAttack(EntityLivingBase p_190102_1_, double p_190102_2_) {
        double d0 = this.getAttackReachSqr(p_190102_1_);

        if (p_190102_2_ <= d0 && this.attackTick <= 0) {
            this.attackTick = 20;
            this.attacker.swingArm(EnumHand.MAIN_HAND);
            this.attacker.attackEntityAsMob(p_190102_1_);
        }
    }

    protected double getAttackReachSqr(EntityLivingBase attackTarget) {
        return (double) (this.attacker.width * 2.0F * this.attacker.width * 2.0F + attackTarget.width);
    }
}*/
