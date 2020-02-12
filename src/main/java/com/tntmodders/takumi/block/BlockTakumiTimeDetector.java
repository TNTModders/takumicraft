package com.tntmodders.takumi.block;

import com.tntmodders.takumi.core.TakumiBlockCore;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockTakumiTimeDetector extends BlockTakumiDetector {
    private final boolean inverted;

    public BlockTakumiTimeDetector(boolean inverted, String name) {
        super(inverted, name);
        this.inverted = inverted;
    }

    @Override
    public void updatePower(World worldIn, BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        int time = ((int) (worldIn.getWorldTime() % 24000)) - 6000;
        double theta = (Math.PI * time / 12000);
        float f = ((float) Math.cos(theta));
        int i = Math.round(7.5f + 8f * f);
        if (i > 15) {
            i = 15;
        } else if (i < 0) {
            i = 0;
        }
        if (this.inverted) {
            i = 15 - i;
        }
        if (iblockstate.getValue(POWER) != i) {
            worldIn.setBlockState(pos, iblockstate.withProperty(POWER, i), 3);
        }
    }

    @Override
    protected Block getNormalBlock() {
        return TakumiBlockCore.CREEPER_TIME_DETECTOR;
    }

    @Override
    protected Block getInvertedBlock() {
        return TakumiBlockCore.CREEPER_TIME_DETECTOR_INV;
    }
}
