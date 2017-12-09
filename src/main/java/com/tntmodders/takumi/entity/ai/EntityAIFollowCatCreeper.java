package com.tntmodders.takumi.entity.ai;

import com.tntmodders.takumi.entity.mobs.EntityCatCreeper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityCreeper;

import java.util.List;

public class EntityAIFollowCatCreeper extends EntityAIBase {
    
    private final EntityCreeper creeper;
    private EntityCatCreeper catCreeper;
    
    public EntityAIFollowCatCreeper(EntityCreeper creeperIn) {
        this.creeper = creeperIn;
        this.setMutexBits(3);
    }
    
    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        if (this.creeper instanceof EntityCatCreeper) {
            return false;
        }
        List <EntityCatCreeper> list = this.creeper.world.getEntitiesWithinAABB(EntityCatCreeper.class, this.creeper.getEntityBoundingBox().grow
                (50.0D, 30.0D, 50.0D));
        
        if (list.isEmpty()) {
            return false;
        } else {
            this.catCreeper = list.get(0);
            return this.catCreeper.getDistanceToEntity(this.creeper) < 2000;
        }
    }
    
    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting() {
        return this.catCreeper != null && this.creeper.getDistanceSqToEntity(this.catCreeper) < 16f && !this.catCreeper.isDead;
    }
    
    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting() {
    }
    
    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    @Override
    public void resetTask() {
        this.catCreeper = null;
        this.creeper.getNavigator().clearPathEntity();
    }
    
    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void updateTask() {
        if (this.catCreeper != null) {
            if (this.creeper.getRNG().nextInt(10) == 0) {
                this.creeper.getLookHelper().setLookPositionWithEntity(this.catCreeper, 30.0F, 30.0F);
            }
            this.creeper.getNavigator().tryMoveToEntityLiving(this.catCreeper, this.creeper.getAttributeMap().getAttributeInstance
                    (SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() * 5);
            if (this.creeper.getDistanceSqToEntity(this.catCreeper) < 6.0D) {
                this.resetTask();
            }
        }
    }
}