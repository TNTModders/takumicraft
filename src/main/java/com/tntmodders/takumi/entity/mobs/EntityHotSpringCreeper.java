package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

public class EntityHotSpringCreeper extends EntityTakumiAbstractCreeper {
    
    public EntityHotSpringCreeper(World worldIn) {
        super(worldIn);
    }
    
    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        for (BlockPos pos : event.getAffectedBlocks()) {
            event.getWorld().setBlockState(pos, TakumiBlockCore.HOT_SPRING.getDefaultState());
        }
        event.getAffectedBlocks().removeAll(event.getAffectedBlocks());
        return true;
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
        return EnumTakumiType.WATER;
    }
    
    @Override
    public int getExplosionPower() {
        return 2;
    }
    
    @Override
    public int getSecondaryColor() {
        return 16777215;
    }
    
    @Override
    public boolean isCustomSpawn() {
        return false;
    }
    
    @Override
    public String getRegisterName() {
        return "hotspringcreeper";
    }
    
    @Override
    public int getRegisterID() {
        return 13;
    }
}
