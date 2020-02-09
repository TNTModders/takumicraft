package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.tileentity.TileEntityTakumiPiston;
import net.minecraft.block.BlockPistonMoving;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class BlockTakumiPistonMoving extends BlockPistonMoving {
    public BlockTakumiPistonMoving() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "creeperpiston_moving");
        this.setUnlocalizedName("creeperpiston");
        this.setResistance(10000000f);
    }

    public static TileEntity createTilePiston(IBlockState blockStateIn, EnumFacing facingIn, boolean extendingIn, boolean shouldHeadBeRenderedIn)
    {
        return new TileEntityTakumiPiston(blockStateIn, facingIn, extendingIn, shouldHeadBeRenderedIn);
    }
}
