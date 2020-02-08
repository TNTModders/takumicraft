package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.client.particle.ParticleTakumiRedstone;
import com.tntmodders.takumi.core.TakumiBlockCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockTakumiRedstoneRepeater extends BlockRedstoneRepeater {
    public BlockTakumiRedstoneRepeater(boolean powered) {
        super(powered);
        String name = "creeperredstonerepeater_" + (powered ? "on" : "off");
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
        return Item.getItemFromBlock(TakumiBlockCore.CREEPER_REDSTONE_REPEATER);
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(this.getItemDropped(state, worldIn.rand, 0));
    }

    @Override
    protected IBlockState getPoweredState(IBlockState unpoweredState) {
        Integer integer = unpoweredState.getValue(DELAY);
        Boolean obool = unpoweredState.getValue(LOCKED);
        EnumFacing enumfacing = unpoweredState.getValue(FACING);
        return TakumiBlockCore.CREEPER_REDSTONE_REPEATER_ON.getDefaultState().withProperty(FACING, enumfacing).withProperty(DELAY, integer).withProperty(LOCKED, obool);
    }

    @Override
    protected IBlockState getUnpoweredState(IBlockState poweredState) {
        Integer integer = poweredState.getValue(DELAY);
        Boolean obool = poweredState.getValue(LOCKED);
        EnumFacing enumfacing = poweredState.getValue(FACING);
        return TakumiBlockCore.CREEPER_REDSTONE_REPEATER.getDefaultState().withProperty(FACING, enumfacing).withProperty(DELAY, integer).withProperty(LOCKED, obool);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (this.isRepeaterPowered) {
            EnumFacing enumfacing = stateIn.getValue(FACING);
            double d0 = (double) ((float) pos.getX() + 0.5F) + (double) (rand.nextFloat() - 0.5F) * 0.2D;
            double d1 = (double) ((float) pos.getY() + 0.4F) + (double) (rand.nextFloat() - 0.5F) * 0.2D;
            double d2 = (double) ((float) pos.getZ() + 0.5F) + (double) (rand.nextFloat() - 0.5F) * 0.2D;
            float f = -5.0F;

            if (rand.nextBoolean()) {
                f = (float) (stateIn.getValue(DELAY) * 2 - 1);
            }

            f = f / 16.0F;
            double d3 = f * (float) enumfacing.getFrontOffsetX();
            double d4 = f * (float) enumfacing.getFrontOffsetZ();
            Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleTakumiRedstone(worldIn, d0 + d3, d1, d2 + d4, 0.0f, 0.0f, 0.0f));
        }
    }

    @Override
    protected boolean isAlternateInput(IBlockState state) {
        return isDiode(state);
    }

    public static boolean isDiode(IBlockState state) {
        return ((BlockTakumiRedstoneRepeater) TakumiBlockCore.CREEPER_REDSTONE_REPEATER).isSameDiode(state)/* || Blocks.UNPOWERED_COMPARATOR.isSameDiode(state)*/;
    }

    @Override
    public boolean isSameDiode(IBlockState state) {
        Block block = state.getBlock();
        return block == this.getPoweredState(this.getDefaultState()).getBlock() || block == this.getUnpoweredState(this.getDefaultState()).getBlock();
    }

    @Override
    protected int calculateInputStrength(World worldIn, BlockPos pos, IBlockState state) {
        EnumFacing enumfacing = state.getValue(FACING);
        BlockPos blockpos = pos.offset(enumfacing);
        int i = worldIn.getRedstonePower(blockpos, enumfacing);

        if (i >= 15) {
            return i;
        } else {
            IBlockState iblockstate = worldIn.getBlockState(blockpos);
            return Math.max(i, iblockstate.getBlock() == TakumiBlockCore.CREEPER_REDSTONE_WIRE ? iblockstate.getValue(BlockTakumiRedstoneWire.POWER) : 0);
        }
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
                blockAccess.getBlockState(pos.offset(side.getOpposite())).getBlock() == Blocks.UNPOWERED_REPEATER) {
            return 0;
        }
        return super.getWeakPower(blockState, blockAccess, pos, side);
    }
}
