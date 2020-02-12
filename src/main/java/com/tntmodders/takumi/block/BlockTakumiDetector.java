package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiBlockCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDaylightDetector;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockTakumiDetector extends BlockDaylightDetector {
    private final boolean inverted;

    public BlockTakumiDetector(boolean inverted, String name) {
        super(inverted);
        this.inverted = inverted;
        this.setRegistryName(TakumiCraftCore.MODID, name);
        this.setCreativeTab(inverted ? null : TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName(name);
        this.setResistance(10000000f);
    }

    @Override
    public int getLightValue(IBlockState state) {
        return state.getValue(POWER) > 7 ? 15 : 0;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (playerIn.isAllowEdit()) {
            if (!worldIn.isRemote) {
                if (this.inverted) {
                    worldIn.setBlockState(pos, this.getNormalBlock().getDefaultState().withProperty(POWER, state.getValue(POWER)), 4);
                } else {
                    worldIn.setBlockState(pos, this.getInvertedBlock().getDefaultState().withProperty(POWER, state.getValue(POWER)), 4);
                }
                ((BlockTakumiDetector) this.getNormalBlock()).updatePower(worldIn, pos);

            }
            return true;
        } else {
            return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
        }
    }

    /**
     * Get the Item that this Block should drop when harvested.
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(this.getNormalBlock());
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(this.getNormalBlock());
    }

    protected Block getNormalBlock() {
        return TakumiBlockCore.CREEPER_DETECTOR;
    }

    protected Block getInvertedBlock() {
        return TakumiBlockCore.CREEPER_DETECTOR_INV;
    }
}
