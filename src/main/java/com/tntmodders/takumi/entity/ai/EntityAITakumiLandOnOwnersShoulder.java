package com.tntmodders.takumi.entity.ai;

import com.tntmodders.takumi.entity.mobs.EntityParrotCreeper;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAITakumiLandOnOwnersShoulder extends EntityAIBase {

    private final EntityParrotCreeper entity;
    private EntityPlayer owner;
    private boolean isSittingOnShoulder;

    public EntityAITakumiLandOnOwnersShoulder(EntityParrotCreeper p_i47415_1_) {
        this.entity = p_i47415_1_;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        return this.entity.getAttackTarget() != null && this.entity.getAttackTarget() instanceof EntityPlayer;
    }

    /**
     * Determine if this AI Task is interruptible by a higher (= lower value) priority task. All vanilla AITask have
     * this value set to true.
     */
    @Override
    public boolean isInterruptible() {
        return !this.isSittingOnShoulder;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting() {
        this.owner = (EntityPlayer) this.entity.getAttackTarget();
        this.isSittingOnShoulder = false;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void updateTask() {
        if (!this.isSittingOnShoulder && !this.entity.getLeashed()) {
            if (this.entity.getEntityBoundingBox().intersects(this.owner.getEntityBoundingBox())) {
                this.isSittingOnShoulder = this.entity.setEntityOnShoulder(this.owner);
                this.entity.setAttackTarget(this.owner);
            } else {
                this.entity.setCreeperState(-1);
                this.entity.setAttackTarget(null);
            }
        }
    }
}