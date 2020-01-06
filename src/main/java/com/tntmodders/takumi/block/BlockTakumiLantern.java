package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTakumiLantern extends BlockHorizontal {

    protected static final AxisAlignedBB[] COCOA_EAST_AABB = new AxisAlignedBB[]{new AxisAlignedBB(0.6875D, 0.4375D, 0.375D, 0.9375D, 0.75D, 0.625D), new AxisAlignedBB(0.5625D, 0.3125D, 0.3125D, 0.9375D, 0.75D, 0.6875D), new AxisAlignedBB(0.4375D, 0.1875D, 0.25D, 0.9375D, 0.75D, 0.75D)};
    protected static final AxisAlignedBB[] COCOA_WEST_AABB = new AxisAlignedBB[]{new AxisAlignedBB(0.0625D, 0.4375D, 0.375D, 0.3125D, 0.75D, 0.625D), new AxisAlignedBB(0.0625D, 0.3125D, 0.3125D, 0.4375D, 0.75D, 0.6875D), new AxisAlignedBB(0.0625D, 0.1875D, 0.25D, 0.5625D, 0.75D, 0.75D)};
    protected static final AxisAlignedBB[] COCOA_NORTH_AABB = new AxisAlignedBB[]{new AxisAlignedBB(0.375D, 0.4375D, 0.0625D, 0.625D, 0.75D, 0.3125D), new AxisAlignedBB(0.3125D, 0.3125D, 0.0625D, 0.6875D, 0.75D, 0.4375D), new AxisAlignedBB(0.25D, 0.1875D, 0.0625D, 0.75D, 0.75D, 0.5625D)};
    protected static final AxisAlignedBB[] COCOA_SOUTH_AABB = new AxisAlignedBB[]{new AxisAlignedBB(0.375D, 0.4375D, 0.6875D, 0.625D, 0.75D, 0.9375D), new AxisAlignedBB(0.3125D, 0.3125D, 0.5625D, 0.6875D, 0.75D, 0.9375D), new AxisAlignedBB(0.25D, 0.1875D, 0.4375D, 0.75D, 0.75D, 0.9375D)};

    public BlockTakumiLantern() {
        super(Material.PLANTS);
        this.setRegistryName(TakumiCraftCore.MODID, "creeperlantern");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("creeperlantern");
        this.setHardness(3f);
        this.setResistance(10000000f);
        this.setLightLevel(1);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getValue(FACING)) {
            case SOUTH:
                return COCOA_SOUTH_AABB[2];
            case NORTH:
            default:
                return COCOA_NORTH_AABB[2];
            case WEST:
                return COCOA_WEST_AABB[2];
            case EAST:
                return COCOA_EAST_AABB[2];
        }
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

/*    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (this.canBlockStay(worldIn, pos, state)) {
            EnumFacing enumfacing = EnumFacing.fromAngle(placer.rotationYaw);
            worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
        } else {
            this.dropBlock(worldIn, pos, state);
        }
    }*/

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
            facing = EnumFacing.fromAngle(placer.rotationYaw);
        }
        return this.blockState.getBaseState().withProperty(FACING, facing.getOpposite());
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        if (!side.getAxis().isHorizontal()) {
            return false;
        }
        pos = pos.offset(side.getOpposite());
        IBlockState iblockstate = worldIn.getBlockState(pos);
        return iblockstate.getBlock() != Blocks.AIR & iblockstate.isFullCube();
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        if (!facing.getAxis().isHorizontal()) {
            facing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, facing.getOpposite());
    }

    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
        pos = pos.offset(state.getValue(FACING));
        IBlockState iblockstate = worldIn.getBlockState(pos);
        return iblockstate.getBlock() != Blocks.AIR & iblockstate.isFullCube();
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!this.canBlockStay(worldIn, pos, state)) {
            this.dropBlock(worldIn, pos, state);
        }
    }

    private void dropBlock(World worldIn, BlockPos pos, IBlockState state) {
        worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        this.dropBlockAsItem(worldIn, pos, state, 0);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
        return BlockFaceShape.UNDEFINED;
    }
}
