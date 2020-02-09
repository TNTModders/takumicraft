package com.tntmodders.takumi.block;

import com.google.common.base.Predicate;
import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiBlockCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneComparator;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockTakumiRedstoneComparator extends BlockRedstoneComparator {
    public BlockTakumiRedstoneComparator(boolean powered) {
        super(powered);
        String name = "creeperredstonecomparator_" + (powered ? "on" : "off");
        this.setRegistryName(TakumiCraftCore.MODID, name);
        if (!powered) {
            this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        }
        this.setUnlocalizedName(name);
        this.setHardness(0f);
        this.setResistance(10000000f);
        this.setSoundType(SoundType.WOOD);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(TakumiBlockCore.CREEPER_REDSTONE_COMPARATOR);
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(this.getItemDropped(state, worldIn.rand, 0));
    }

    @Override
    protected IBlockState getPoweredState(IBlockState unpoweredState) {
        Boolean obool = unpoweredState.getValue(POWERED);
        BlockRedstoneComparator.Mode blockredstonecomparator$mode = unpoweredState.getValue(MODE);
        EnumFacing enumfacing = unpoweredState.getValue(FACING);
        return TakumiBlockCore.CREEPER_REDSTONE_COMPARATOR_ON.getDefaultState().withProperty(FACING, enumfacing).withProperty(POWERED, obool).withProperty(MODE, blockredstonecomparator$mode);
    }

    @Override
    protected IBlockState getUnpoweredState(IBlockState poweredState) {
        Boolean obool = poweredState.getValue(POWERED);
        BlockRedstoneComparator.Mode blockredstonecomparator$mode = poweredState.getValue(MODE);
        EnumFacing enumfacing = poweredState.getValue(FACING);
        return TakumiBlockCore.CREEPER_REDSTONE_COMPARATOR.getDefaultState().withProperty(FACING, enumfacing).withProperty(POWERED, obool).withProperty(MODE, blockredstonecomparator$mode);
    }

    @Override
    protected int calculateInputStrength(World worldIn, BlockPos pos, IBlockState state) {
        int i = this.calculateInputStrength_ex(worldIn, pos, state);
        EnumFacing enumfacing = state.getValue(FACING);
        BlockPos blockpos = pos.offset(enumfacing);
        IBlockState iblockstate = worldIn.getBlockState(blockpos);

        if (iblockstate.hasComparatorInputOverride()) {
            i = iblockstate.getComparatorInputOverride(worldIn, blockpos);
        } else if (i < 15 && iblockstate.isNormalCube()) {
            blockpos = blockpos.offset(enumfacing);
            iblockstate = worldIn.getBlockState(blockpos);

            if (iblockstate.hasComparatorInputOverride()) {
                i = iblockstate.getComparatorInputOverride(worldIn, blockpos);
            } else if (iblockstate.getMaterial() == Material.AIR) {
                EntityItemFrame entityitemframe = this.findItemFrame(worldIn, enumfacing, blockpos);

                if (entityitemframe != null) {
                    i = entityitemframe.getAnalogOutput();
                }
            }
        }

        return i;
    }

    protected int calculateInputStrength_ex(World worldIn, BlockPos pos, IBlockState state) {
        EnumFacing enumfacing = state.getValue(FACING);
        BlockPos blockpos = pos.offset(enumfacing);
        int i = worldIn.getRedstonePower(blockpos, enumfacing);
        if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() == Blocks.REDSTONE_WIRE ||
                worldIn.getBlockState(pos.offset(enumfacing)).getBlock() == Blocks.POWERED_REPEATER ||
                worldIn.getBlockState(pos.offset(enumfacing)).getBlock() == Blocks.POWERED_COMPARATOR) {
            i = 0;
        }
        if (i >= 15) {
            return i;
        } else {
            IBlockState iblockstate = worldIn.getBlockState(blockpos);
            return Math.max(i, iblockstate.getBlock() == TakumiBlockCore.CREEPER_REDSTONE_WIRE ? iblockstate.getValue(BlockTakumiRedstoneWire.POWER) : 0);
        }
    }

    @Nullable
    private EntityItemFrame findItemFrame(World worldIn, final EnumFacing facing, BlockPos pos) {
        List<EntityItemFrame> list = worldIn.getEntitiesWithinAABB(EntityItemFrame.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(),
                pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1), (Predicate<Entity>) p_apply_1_ -> p_apply_1_ != null && p_apply_1_.getHorizontalFacing() == facing);
        return list.size() == 1 ? list.get(0) : null;
    }

    @Override
    public boolean isSameDiode(IBlockState state) {
        Block block = state.getBlock();
        return block == this.getPoweredState(this.getDefaultState()).getBlock() || block == this.getUnpoweredState(this.getDefaultState()).getBlock();
    }

    @Override
    protected int getPowerOnSide(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (this.isAlternateInput(iblockstate)) {
            if (block == TakumiBlockCore.CREEPER_REDSTONE_BLOCK) {
                return 15;
            } else {
                return block == TakumiBlockCore.CREEPER_REDSTONE_WIRE ? iblockstate.getValue(BlockTakumiRedstoneWire.POWER) : worldIn.getStrongPower(pos, side);
            }
        } else {
            return 0;
        }
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        if (blockAccess.getBlockState(pos.offset(side.getOpposite())).getBlock() == Blocks.REDSTONE_WIRE ||
                blockAccess.getBlockState(pos.offset(side.getOpposite())).getBlock() == Blocks.UNPOWERED_REPEATER ||
                blockAccess.getBlockState(pos.offset(side.getOpposite())).getBlock() == Blocks.UNPOWERED_COMPARATOR) {
            return 0;
        }
        return super.getWeakPower(blockState, blockAccess, pos, side);
    }
}
