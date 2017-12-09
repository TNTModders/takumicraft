package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityRedStoneCreeper extends EntityTakumiAbstractCreeper {
    
    public EntityRedStoneCreeper(World worldIn) {
        super(worldIn);
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.65);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(128);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10);
    }
    
    @Override
    public void onDeath(DamageSource source) {
        if (!this.world.isRemote) {
            this.dropItem(Item.getItemFromBlock(Blocks.REDSTONE_BLOCK), 1);
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
        return EnumTakumiType.FIRE;
    }
    
    @Override
    public int getExplosionPower() {
        return 5;
    }
    
    @Override
    public int getSecondaryColor() {
        return 0xff0000;
    }
    
    @Override
    public boolean isCustomSpawn() {
        return false;
    }
    
    @Override
    public String getRegisterName() {
        return "redstonecreeper";
    }
    
    @Override
    public int getRegisterID() {
        return 211;
    }
    
    @Override
    public int getPrimaryColor() {
        return 0xa09090;
    }
}
