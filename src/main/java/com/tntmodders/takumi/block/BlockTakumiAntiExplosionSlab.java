package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTakumiAntiExplosionSlab extends Block {
    public static final PropertyEnum<BlockTakumiAntiExplosionSlab.EnumBlockHalf> HALF
            = PropertyEnum.create("half", BlockTakumiAntiExplosionSlab.EnumBlockHalf.class);
    protected static final AxisAlignedBB AABB_TOP = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_BOTTOM = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_WEST = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.5D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5D);
    protected static final AxisAlignedBB[] AABB_LIST = {
            AABB_TOP,
            AABB_BOTTOM,
            AABB_NORTH,
            AABB_SOUTH,
            AABB_EAST,
            AABB_WEST};

    public BlockTakumiAntiExplosionSlab(Material material, String s, float hardness, String tool) {
        super(material);
        this.setRegistryName(TakumiCraftCore.MODID, s);
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName(s);
        this.setHardness(hardness);
        this.setResistance(10000000f);
        if (tool != null) {
            this.setHarvestLevel(tool, 2);
        }
        this.setLightOpacity(0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB_LIST[state.getValue(HALF).getMeta()];
    }

    @Override
    public boolean isTopSolid(IBlockState state) {
        return state.getValue(HALF) == EnumBlockHalf.TOP;
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getStateFromMeta(facing.getIndex());
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumBlockHalf half;
        switch (meta) {
            case 1: {
                half = EnumBlockHalf.BOTTOM;
                break;
            }
            case 2: {
                half = EnumBlockHalf.NORTH;
                break;
            }
            case 3: {
                half = EnumBlockHalf.SOUTH;
                break;
            }
            case 4: {
                half = EnumBlockHalf.EAST;
                break;
            }
            case 5: {
                half = EnumBlockHalf.WEST;
                break;
            }
            default: {
                half = EnumBlockHalf.TOP;
            }
        }
        return this.getDefaultState().withProperty(HALF, half);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(HALF).getMeta();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, HALF);
    }

    public enum EnumBlockHalf implements IStringSerializable {
        TOP("top", 0),
        BOTTOM("bottom", 1),
        NORTH("north", 2),
        SOUTH("south", 3),
        EAST("east", 4),
        WEST("west", 5);


        private final String name;
        private final int meta;

        EnumBlockHalf(String name, int meta) {
            this.name = name;
            this.meta = meta;
        }

        public String toString() {
            return this.name;
        }

        public int getMeta() {
            return this.meta;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }
}
