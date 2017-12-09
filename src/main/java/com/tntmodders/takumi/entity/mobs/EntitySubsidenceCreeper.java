package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.core.TakumiPotionCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

public class EntitySubsidenceCreeper extends EntityTakumiAbstractCreeper {
    
    private float heightOffset = 0.5F;
    private int heightOffsetUpdateTime;
    
    public EntitySubsidenceCreeper(World worldIn) {
        super(worldIn);
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(48.0D);
    }
    
    @Override
    public void fall(float distance, float damageMultiplier) {
    }
    
    @Override
    protected void updateAITasks() {
        --this.heightOffsetUpdateTime;
        if (this.heightOffsetUpdateTime <= 0) {
            this.heightOffsetUpdateTime = 100;
            this.heightOffset = 0.5F + (float) this.rand.nextGaussian() * 3.0F;
        }
        EntityLivingBase entitylivingbase = this.getAttackTarget();
        if (entitylivingbase != null && entitylivingbase.posY + (double) entitylivingbase.getEyeHeight() > this.posY + (double) this.getEyeHeight()
                + (double) this.heightOffset) {
            this.motionY += (0.30000001192092896D - this.motionY) * 0.30000001192092896D;
            this.isAirBorne = true;
        }
        
        super.updateAITasks();
    }
    
    @Override
    protected float getJumpUpwardsMotion() {
        return super.getJumpUpwardsMotion() * 3;
    }
    
    @Override
    public void onLivingUpdate() {
        if (!this.onGround) {
            this.motionY *= 0.6D;
        }
        super.onLivingUpdate();
    }
    
    @Override
    public void takumiExplode() {
    }
    
    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
    }
    
    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.WIND_M;
    }
    
    @Override
    public int getExplosionPower() {
        return 3;
    }
    
    @Override
    public int getSecondaryColor() {
        return 2176960;
    }
    
    @Override
    public boolean isCustomSpawn() {
        return false;
    }
    
    @Override
    public String getRegisterName() {
        return "subsidencecreeper";
    }
    
    @Override
    public int getRegisterID() {
        return 20;
    }
    
    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        for (Entity entity : event.getAffectedEntities()) {
            if (entity instanceof EntityLivingBase) {
                ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(TakumiPotionCore.SUBSIDENCE, 400, 0));
            }
        }
        return true;
    }
}
