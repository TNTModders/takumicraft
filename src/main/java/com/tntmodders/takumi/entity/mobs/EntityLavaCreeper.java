package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

public class EntityLavaCreeper extends EntityTakumiAbstractCreeper {
    
    public EntityLavaCreeper(World worldIn) {
        super(worldIn);
    }
    
    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        for (BlockPos pos : event.getAffectedBlocks()) {
            event.getWorld().setBlockState(pos, Blocks.LAVA.getDefaultState());
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
        return EnumTakumiType.FIRE;
    }
    
    @Override
    public int getExplosionPower() {
        return 2;
    }
    
    @Override
    public int getSecondaryColor() {
        return 16711680;
    }
    
    @Override
    public boolean isCustomSpawn() {
        return false;
    }
    
    @Override
    public String getRegisterName() {
        return "lavacreeper";
    }
    
    @Override
    public int getRegisterID() {
        return 6;
    }
}
