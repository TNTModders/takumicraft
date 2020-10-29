package com.tntmodders.takumi.entity.item;

import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityKingStorm extends EntityMob {
    public EntityKingStorm(World worldIn) {
        super(worldIn);
        this.setSize(1f, 2f);
    }


    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(2, new EntityAIWanderAvoidWater(this, 0.8D));
        this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(3, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(100);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1000);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1);
    }

    @Override
    protected float getWaterSlowDown() {
        return 1f;
    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.setInvisible(true);
        if (!this.world.isRemote) {
            for (int i = 0; i <= 5; i++) {
                for (int d = 0; d < 12; d++) {
                    if (this.rand.nextInt((6 - i) * 2) == 0) {
                        double x = Math.cos(Math.toRadians(d * 30)) * i / 3;
                        double z = Math.sin(Math.toRadians(d * 30)) * i / 3;
                        TakumiUtils.takumiCreateExplosion(this.world, this, this.posX + x * Math.abs(x) * i / 4.5, this.posY + i / 1.5, this.posZ + z * Math.abs(z) * i / 4.5,
                                0f, false, false, 0f, false, false);
                    }
                }
            }
        }
        if (this.ticksExisted > 100) {
            this.setDead();
        }
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        super.onCollideWithPlayer(entityIn);
        this.world.createExplosion(this, entityIn.posX, entityIn.posY, entityIn.posZ, 0f, false);
        entityIn.motionY = 5f * (1.1 - Math.max(this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getAttributeValue(), 1));
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        //super.fall(distance, damageMultiplier);
    }
}
