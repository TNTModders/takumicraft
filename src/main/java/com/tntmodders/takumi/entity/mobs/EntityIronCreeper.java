package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityIronCreeper extends EntityTakumiAbstractCreeper {
    
    public EntityIronCreeper(World worldIn) {
        super(worldIn);
        this.setSize(0.3F, 0.85F);
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(128);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.75f);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10);
    }
    
    @Override
    public void onDeath(DamageSource source) {
        if (!this.world.isRemote) {
            this.dropItem(Item.getItemFromBlock(Blocks.IRON_BLOCK), 1);
        }
        super.onDeath(source);
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
        return 5;
    }
    
    @Override
    public int getSecondaryColor() {
        return 0xffffff;
    }
    
    @Override
    public boolean isCustomSpawn() {
        return false;
    }
    
    @Override
    public String getRegisterName() {
        return "ironcreeper";
    }
    
    @Override
    public int getRegisterID() {
        return 213;
    }
    
    @Override
    public int getPrimaryColor() {
        return 0x909090;
    }
    
    @Override
    public double getSizeAmp() {
        return 0.5;
    }
}
