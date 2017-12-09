package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntitySilentCreeper extends EntityTakumiAbstractCreeper {
    
    public EntitySilentCreeper(World worldIn) {
        super(worldIn);
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return null;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return null;
    }
    
    @Override
    protected SoundEvent getFallSound(int heightIn) {
        return null;
    }
    
    @Override
    protected float getSoundVolume() {
        return 0f;
    }
    
    @Override
    protected void playStepSound(BlockPos pos, Block blockIn) {
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
        return EnumTakumiType.WIND;
    }
    
    @Override
    public int getExplosionPower() {
        return 3;
    }
    
    @Override
    public int getSecondaryColor() {
        return 11184810;
    }
    
    @Override
    public boolean isCustomSpawn() {
        return false;
    }
    
    @Override
    public String getRegisterName() {
        return "silentcreeper";
    }
    
    @Override
    public int getRegisterID() {
        return 3;
    }
}
