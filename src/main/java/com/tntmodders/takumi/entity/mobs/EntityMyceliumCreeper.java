package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

public class EntityMyceliumCreeper extends EntityTakumiAbstractCreeper {

    public EntityMyceliumCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        for (BlockPos pos : event.getAffectedBlocks()) {
            if (!this.world.isAirBlock(pos)) {
                this.world.setBlockState(pos, Blocks.MYCELIUM.getDefaultState());
            } else {
                this.world.setBlockState(pos, Blocks.RED_MUSHROOM_BLOCK.getDefaultState());
            }
        }
        event.getAffectedBlocks().clear();
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
        return EnumTakumiType.GROUND;
    }

    @Override
    public int getExplosionPower() {
        return 5;
    }

    @Override
    public int getSecondaryColor() {
        return 0x884488;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "myceliumcreeper";
    }

    @Override
    public int getRegisterID() {
        return 89;
    }
}
