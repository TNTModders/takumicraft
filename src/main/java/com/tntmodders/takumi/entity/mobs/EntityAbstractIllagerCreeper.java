package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class EntityAbstractIllagerCreeper extends EntityTakumiAbstractCreeper {
    
    protected static final DataParameter <Boolean> AGGRESSIVE = EntityDataManager.createKey(EntityAbstractIllagerCreeper.class, DataSerializers
            .BOOLEAN);
    
    public EntityAbstractIllagerCreeper(World worldIn) {
        super(worldIn);
    }
    
    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        //this.dataManager.register(AGGRESSIVE, (byte) 0);
        this.dataManager.register(AGGRESSIVE, false);
    }
    
    @SideOnly(Side.CLIENT)
    protected boolean isAggressive(int p_193078_1_) {
/*        int i = this.dataManager.get(AGGRESSIVE);
        return (i & p_193078_1_) != 0;*/
        return this.dataManager.get(AGGRESSIVE);
    }
    
    protected void setAggressive(int p_193079_1_, boolean p_193079_2_) {
        /*int i = this.dataManager.get(AGGRESSIVE);
        
        if (p_193079_2_) {
            i = i | p_193079_1_;
        } else {
            i = i & ~p_193079_1_;
        }
        
        this.dataManager.set(AGGRESSIVE, (byte) (i & 255));*/
        this.dataManager.set(AGGRESSIVE, p_193079_2_);
    }
    
    /**
     * Get this Entity's EnumCreatureAttribute
     */
    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.ILLAGER;
    }
    
    @SideOnly(Side.CLIENT)
    public IllagerArmPose getArmPose() {
        return IllagerArmPose.CROSSED;
    }
    
    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }
    
    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL_M;
    }
    
    @SideOnly(Side.CLIENT)
    public enum IllagerArmPose {
        CROSSED, ATTACKING, SPELLCASTING, BOW_AND_ARROW
    }
}
