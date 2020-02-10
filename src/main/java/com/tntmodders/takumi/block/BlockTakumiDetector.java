package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiBlockCore;
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

    public BlockTakumiDetector(boolean inverted) {
        super(inverted);
        this.inverted = inverted;
        String name = "creeperdetector";
        if (inverted) {
            name = name + "_inv";
        }
        this.setRegistryName(TakumiCraftCore.MODID, name);
        this.setCreativeTab(inverted ? null : TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName(name);
        this.setResistance(10000000f);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (playerIn.isAllowEdit()) {
            if (!worldIn.isRemote) {
                if (this.inverted) {
                    worldIn.setBlockState(pos, TakumiBlockCore.CREEPER_DETECTOR.getDefaultState().withProperty(POWER, state.getValue(POWER)), 4);
                    ((BlockTakumiDetector) TakumiBlockCore.CREEPER_DETECTOR).updatePower(worldIn, pos);
                } else {
                    worldIn.setBlockState(pos, TakumiBlockCore.CREEPER_DETECTOR_INV.getDefaultState().withProperty(POWER, state.getValue(POWER)), 4);
                    ((BlockTakumiDetector) TakumiBlockCore.CREEPER_DETECTOR_INV).updatePower(worldIn, pos);
                }

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
        return Item.getItemFromBlock(TakumiBlockCore.CREEPER_DETECTOR);
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(TakumiBlockCore.CREEPER_DETECTOR);
    }
}
