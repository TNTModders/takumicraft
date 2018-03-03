package com.tntmodders.takumi.entity.mobs;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityBigFishCreeper extends EntityFishCreeper {

    public EntityBigFishCreeper(World worldIn) {
        super(worldIn);
        this.setSize(4F, 3F);
        this.isImmuneToFire = true;
        this.setPathPriority(PathNodeType.LAVA, 1);
        this.setPathPriority(PathNodeType.WATER, 1);
    }

    @Override
    public boolean handleWaterMovement() {
        this.inWater = false;
        return this.inWater;
    }

    @Override
    public boolean isPushedByWater() {
        return false;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(80D);
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public int getExplosionPower() {
        return super.getExplosionPower() * 4;
    }

    @Override
    public boolean isCustomSpawn() {
        return true;
    }

    @Override
    public String getRegisterName() {
        return "bigfishcreeper";
    }

    @Override
    public int getRegisterID() {
        return 237;
    }

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        if (!damageSrc.isFireDamage() && damageSrc != DamageSource.LAVA && damageSrc != DamageSource.DROWN) {
            super.damageEntity(damageSrc, damageAmount);
        }
    }

    @Override
    public double getSizeAmp() {
        return 10;
    }
}
