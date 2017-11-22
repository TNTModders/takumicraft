package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

public class EntityTNTCreeper extends EntityTakumiAbstractCreeper {
    public EntityTNTCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.HIGH;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.FIRE_MD;
    }

    @Override
    public int getExplosionPower() {
        return 6;
    }

    @Override
    public int getSecondaryColor() {
        return 0xffffff;
    }

    @Override
    public boolean isCustomSpawn() {
        return true;
    }

    @Override
    public String getRegisterName() {
        return "tntcreeper";
    }

    @Override
    public int getRegisterID() {
        return 504;
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        if (!this.world.isRemote) {
            for (BlockPos pos : event.getAffectedBlocks()) {
                this.world.setBlockState(pos, Blocks.TNT.getDefaultState());
                if (!event.getAffectedBlocks().contains(pos.up())) {
                    this.world.setBlockState(pos.up(), Blocks.FIRE.getDefaultState());
                }
            }
        }
        event.getAffectedBlocks().clear();
        return true;
    }

    @Override
    public int getPrimaryColor() {
        return 0xff0000;
    }
}
