package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

public class EntityIceCreeper extends EntityTakumiAbstractCreeper {
    public EntityIceCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        for (BlockPos pos : event.getAffectedBlocks()) {
            event.getWorld().setBlockState(pos, Blocks.PACKED_ICE.getDefaultState());
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
        return 11468799;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "icecreeper";
    }

    @Override
    public int getRegisterID() {
        return 8;
    }
}
