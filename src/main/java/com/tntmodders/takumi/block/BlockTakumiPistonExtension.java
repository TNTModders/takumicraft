package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiBlockCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockTakumiPistonExtension extends BlockPistonExtension {

    public BlockTakumiPistonExtension() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "creeperpiston_head");
        this.setUnlocalizedName("creeperpiston");
        this.setResistance(10000000f);
    }


    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (player.capabilities.isCreativeMode) {
            BlockPos blockpos = pos.offset(state.getValue(FACING).getOpposite());
            Block block = worldIn.getBlockState(blockpos).getBlock();

            if (block == TakumiBlockCore.CREEPER_PISTON || block == TakumiBlockCore.CREEPER_STICKY_PISTON) {
                worldIn.setBlockToAir(blockpos);
            }
        }

        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        EnumFacing enumfacing = state.getValue(FACING).getOpposite();
        pos = pos.offset(enumfacing);
        IBlockState iblockstate = worldIn.getBlockState(pos);

        if ((iblockstate.getBlock() == TakumiBlockCore.CREEPER_PISTON || iblockstate.getBlock() == TakumiBlockCore.CREEPER_STICKY_PISTON) &&
                iblockstate.getValue(BlockPistonBase.EXTENDED)) {
            iblockstate.getBlock().dropBlockAsItem(worldIn, pos, iblockstate, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        EnumFacing enumfacing = state.getValue(FACING);
        BlockPos blockpos = pos.offset(enumfacing.getOpposite());
        IBlockState iblockstate = worldIn.getBlockState(blockpos);

        if (iblockstate.getBlock() != TakumiBlockCore.CREEPER_PISTON && iblockstate.getBlock() != TakumiBlockCore.CREEPER_STICKY_PISTON) {
            worldIn.setBlockToAir(pos);
        } else {
            iblockstate.neighborChanged(worldIn, blockpos, blockIn, fromPos);
        }
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(state.getValue(TYPE) == BlockPistonExtension.EnumPistonType.STICKY ? TakumiBlockCore.CREEPER_STICKY_PISTON : TakumiBlockCore.CREEPER_PISTON);
    }
}
