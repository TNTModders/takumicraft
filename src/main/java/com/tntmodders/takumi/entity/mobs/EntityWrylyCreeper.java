package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityWrylyCreeper extends EntityTakumiAbstractCreeper {
    
    public EntityWrylyCreeper(World worldIn) {
        super(worldIn);
    }
    
    @Override
    public void setDead() {
        if (!(this.getHealth() <= 0 || this.world.getDifficulty() == EnumDifficulty.PEACEFUL)) {
            if (!this.world.isRemote) {
                EntityWrylyCreeper wrylyCreeper = new EntityWrylyCreeper(this.world);
                wrylyCreeper.copyLocationAndAnglesFrom(this);
                if (this.getPowered()) {
                    TakumiUtils.takumiSetPowered(wrylyCreeper, true);
                }
                this.world.spawnEntity(wrylyCreeper);
            }
        }
        super.setDead();
    }
    
    @Override
    protected void outOfWorld() {
        this.setHealth(0);
        super.outOfWorld();
    }
    
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return !source.isExplosion() && super.attackEntityFrom(source, amount);
    }
    
    @Override
    public void takumiExplode() {
    }
    
    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
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
        return 8913151;
    }
    
    @Override
    public boolean isCustomSpawn() {
        return false;
    }
    
    @Override
    public String getRegisterName() {
        return "wrylycreeper";
    }
    
    @Override
    public int getRegisterID() {
        return 209;
    }
}
