package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

public class EntityConcreteCreeper extends EntityTakumiAbstractCreeper {
    
    public EntityConcreteCreeper(World worldIn) {
        super(worldIn);
    }
    
    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        event.getAffectedBlocks().forEach(pos -> world.setBlockState(pos, Blocks.CONCRETE_POWDER.getStateFromMeta(this.rand.nextInt(16))));
        event.getAffectedBlocks().removeAll(event.getAffectedBlocks());
        return true;
    }
    
    @Override
    public int getPrimaryColor() {
        return 0xaaffaa;
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
        return EnumTakumiType.GROUND_M;
    }
    
    @Override
    public int getExplosionPower() {
        return 2;
    }
    
    @Override
    public int getSecondaryColor() {
        return 12303206;
    }
    
    @Override
    public boolean isCustomSpawn() {
        return false;
    }
    
    @Override
    public String getRegisterName() {
        return "concretecreeper";
    }
    
    @Override
    public int getRegisterID() {
        return 47;
    }
}
