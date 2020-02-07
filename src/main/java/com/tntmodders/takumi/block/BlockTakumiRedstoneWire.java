package com.tntmodders.takumi.block;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.client.particle.ParticleTakumiRedstone;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.item.ItemTakumiRedstone;
import net.minecraft.block.Block;
import net.minecraft.block.BlockObserver;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class BlockTakumiRedstoneWire extends Block implements ITakumiItemBlock{
    public static final PropertyEnum<EnumAttachPosition> NORTH = PropertyEnum.create("north", EnumAttachPosition.class);
    public static final PropertyEnum<EnumAttachPosition> EAST = PropertyEnum.create("east", EnumAttachPosition.class);
    public static final PropertyEnum<EnumAttachPosition> SOUTH = PropertyEnum.create("south", EnumAttachPosition.class);
    public static final PropertyEnum<EnumAttachPosition> WEST = PropertyEnum.create("west", EnumAttachPosition.class);
    public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
    protected static final AxisAlignedBB[] REDSTONE_WIRE_AABB = new AxisAlignedBB[]{new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 0.8125D), new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 0.8125D), new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 1.0D), new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 0.8125D, 0.0625D, 0.8125D), new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 0.8125D, 0.0625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.8125D, 0.0625D, 0.8125D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.8125D, 0.0625D, 1.0D), new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 1.0D, 0.0625D, 0.8125D), new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 1.0D, 0.0625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 1.0D, 0.0625D, 0.8125D), new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 1.0D, 0.0625D, 1.0D), new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 1.0D, 0.0625D, 0.8125D), new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 0.8125D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D)};
    private boolean canProvidePower = true;
    /**
     * List of blocks to update with redstone.
     */
    private final Set<BlockPos> blocksNeedingUpdate = Sets.newHashSet();

    public BlockTakumiRedstoneWire() {
        super(Material.CIRCUITS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, EnumAttachPosition.NONE).withProperty(EAST, EnumAttachPosition.NONE).withProperty(SOUTH, EnumAttachPosition.NONE).withProperty(WEST, EnumAttachPosition.NONE).withProperty(POWER, 0));
        this.setRegistryName(TakumiCraftCore.MODID, "creeperredstonewire");
        this.setUnlocalizedName("creeperredstonewire");
        this.setHardness(0.0f);
        this.setResistance(10000000f);
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return REDSTONE_WIRE_AABB[getAABBIndex(state.getActualState(source, pos))];
    }

    private static int getAABBIndex(IBlockState state) {
        int i = 0;
        boolean flag = state.getValue(NORTH) != EnumAttachPosition.NONE;
        boolean flag1 = state.getValue(EAST) != EnumAttachPosition.NONE;
        boolean flag2 = state.getValue(SOUTH) != EnumAttachPosition.NONE;
        boolean flag3 = state.getValue(WEST) != EnumAttachPosition.NONE;

        if (flag || flag2 && !flag && !flag1 && !flag3) {
            i |= 1 << EnumFacing.NORTH.getHorizontalIndex();
        }

        if (flag1 || flag3 && !flag && !flag1 && !flag2) {
            i |= 1 << EnumFacing.EAST.getHorizontalIndex();
        }

        if (flag2 || flag && !flag1 && !flag2 && !flag3) {
            i |= 1 << EnumFacing.SOUTH.getHorizontalIndex();
        }

        if (flag3 || flag1 && !flag && !flag2 && !flag3) {
            i |= 1 << EnumFacing.WEST.getHorizontalIndex();
        }

        return i;
    }

    /**
     * Get the actual Block state of this Block at the given position. This applies properties not visible in the
     * metadata, such as fence connections.
     */
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        state = state.withProperty(WEST, this.getAttachPosition(worldIn, pos, EnumFacing.WEST));
        state = state.withProperty(EAST, this.getAttachPosition(worldIn, pos, EnumFacing.EAST));
        state = state.withProperty(NORTH, this.getAttachPosition(worldIn, pos, EnumFacing.NORTH));
        state = state.withProperty(SOUTH, this.getAttachPosition(worldIn, pos, EnumFacing.SOUTH));
        return state;
    }

    private EnumAttachPosition getAttachPosition(IBlockAccess worldIn, BlockPos pos, EnumFacing direction) {
        BlockPos blockpos = pos.offset(direction);
        IBlockState iblockstate = worldIn.getBlockState(pos.offset(direction));

        if (!canConnectTo(worldIn.getBlockState(blockpos), direction, worldIn, blockpos) && (iblockstate.isNormalCube() || !canConnectUpwardsTo(worldIn, blockpos.down()))) {
            IBlockState iblockstate1 = worldIn.getBlockState(pos.up());

            if (!iblockstate1.isNormalCube()) {
                boolean flag = worldIn.getBlockState(blockpos).isSideSolid(worldIn, blockpos, EnumFacing.UP) || worldIn.getBlockState(blockpos).getBlock() == Blocks.GLOWSTONE;

                if (flag && canConnectUpwardsTo(worldIn, blockpos.up())) {
                    if (iblockstate.isBlockNormalCube()) {
                        return EnumAttachPosition.UP;
                    }

                    return EnumAttachPosition.SIDE;
                }
            }

            return EnumAttachPosition.NONE;
        } else {
            return EnumAttachPosition.SIDE;
        }
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        IBlockState downState = worldIn.getBlockState(pos.down());
        return downState.isTopSolid() || downState.getBlockFaceShape(worldIn, pos.down(), EnumFacing.UP) == BlockFaceShape.SOLID || worldIn.getBlockState(pos.down()).getBlock() == Blocks.GLOWSTONE;
    }

    private IBlockState updateSurroundingRedstone(World worldIn, BlockPos pos, IBlockState state) {
        state = this.calculateCurrentChanges(worldIn, pos, pos, state);
        List<BlockPos> list = Lists.newArrayList(this.blocksNeedingUpdate);
        this.blocksNeedingUpdate.clear();
        for (BlockPos blockpos : list) {
            worldIn.notifyNeighborsOfStateChange(blockpos, this, false);

        }
        return state;
    }

    private IBlockState calculateCurrentChanges(World worldIn, BlockPos pos1, BlockPos pos2, IBlockState state) {
        IBlockState iblockstate = state;
        int i = state.getValue(POWER);
        int j = 0;
        j = this.getMaxCurrentStrength(worldIn, pos2, j);
        this.canProvidePower = false;
        int k = this.isBlockIndirectlyGettingPowered(worldIn, pos1);
        this.canProvidePower = true;

        if (k > 0 && k > j - 1) {
            j = k;
        }

        int l = 0;

        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            BlockPos blockpos = pos1.offset(enumfacing);
            boolean flag = blockpos.getX() != pos2.getX() || blockpos.getZ() != pos2.getZ();

            if (flag) {
                l = this.getMaxCurrentStrength(worldIn, blockpos, l);
            }

            if (worldIn.getBlockState(blockpos).isNormalCube() && !worldIn.getBlockState(pos1.up()).isNormalCube()) {
                if (flag && pos1.getY() >= pos2.getY()) {
                    l = this.getMaxCurrentStrength(worldIn, blockpos.up(), l);
                }
            } else if (!worldIn.getBlockState(blockpos).isNormalCube() && flag && pos1.getY() <= pos2.getY()) {
                l = this.getMaxCurrentStrength(worldIn, blockpos.down(), l);
            }
        }

        if (l > j) {
            j = l - 1;
        } else if (j > 0) {
            --j;
        } else {
            j = 0;
        }

        if (k > j - 1) {
            j = k;
        }

        if (i != j) {
            state = state.withProperty(POWER, j);

            if (worldIn.getBlockState(pos1) == iblockstate) {
                worldIn.setBlockState(pos1, state, 2);
            }

            this.blocksNeedingUpdate.add(pos1);

            for (EnumFacing enumfacing1 : EnumFacing.values()) {
                this.blocksNeedingUpdate.add(pos1.offset(enumfacing1));
            }
        }

        return state;
    }

    public int isBlockIndirectlyGettingPowered(World worldIn, BlockPos pos) {
        int j2 = 0;

        for (EnumFacing enumfacing : EnumFacing.values()) {
            int k2 = worldIn.getRedstonePower(pos.offset(enumfacing), enumfacing);
            if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() == Blocks.REDSTONE_WIRE) {
                k2 = 0;
            }

            if (k2 >= 15) {
                return 15;
            }

            if (k2 > j2) {
                j2 = k2;
            }
        }

        return j2;
    }

    /**
     * Calls World.notifyNeighborsOfStateChange() for all neighboring blocks, but only if the given block is a redstone
     * wire.
     */
    private void notifyWireNeighborsOfStateChange(World worldIn, BlockPos pos) {
        if (worldIn.getBlockState(pos).getBlock() == this) {
            worldIn.notifyNeighborsOfStateChange(pos, this, false);
            for (EnumFacing enumfacing : EnumFacing.values()) {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
            }
        }
    }

    /**
     * Called after the block is set in the Chunk data, but before the Tile Entity is set
     */
    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            this.updateSurroundingRedstone(worldIn, pos, state);

            for (EnumFacing enumfacing : EnumFacing.Plane.VERTICAL) {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
            }

            for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL) {
                this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(enumfacing1));
            }

            for (EnumFacing enumfacing2 : EnumFacing.Plane.HORIZONTAL) {
                BlockPos blockpos = pos.offset(enumfacing2);

                if (worldIn.getBlockState(blockpos).isNormalCube()) {
                    this.notifyWireNeighborsOfStateChange(worldIn, blockpos.up());
                } else {
                    this.notifyWireNeighborsOfStateChange(worldIn, blockpos.down());
                }
            }
        }
    }

    /**
     * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
     */
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);

        if (!worldIn.isRemote) {
            for (EnumFacing enumfacing : EnumFacing.values()) {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
            }

            this.updateSurroundingRedstone(worldIn, pos, state);

            for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL) {
                this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(enumfacing1));
            }

            for (EnumFacing enumfacing2 : EnumFacing.Plane.HORIZONTAL) {
                BlockPos blockpos = pos.offset(enumfacing2);

                if (worldIn.getBlockState(blockpos).isNormalCube()) {
                    this.notifyWireNeighborsOfStateChange(worldIn, blockpos.up());
                } else {
                    this.notifyWireNeighborsOfStateChange(worldIn, blockpos.down());
                }
            }
        }
    }

    private int getMaxCurrentStrength(World worldIn, BlockPos pos, int strength) {
        if (worldIn.getBlockState(pos).getBlock() == this) {
            int i = worldIn.getBlockState(pos).getValue(POWER);
            return Math.max(i, strength);
        } else {
            return strength;
        }
    }

    /**
     * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
     * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
     * block, etc.
     */
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!worldIn.isRemote) {
            if (this.canPlaceBlockAt(worldIn, pos)) {
                this.updateSurroundingRedstone(worldIn, pos, state);
            } else {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
        }
    }


    @Override
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return !this.canProvidePower ? 0 : blockState.getWeakPower(blockAccess, pos, side);
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        if (!this.canProvidePower) {
            return 0;
        } else {
            if (blockAccess.getBlockState(pos.offset(side.getOpposite())).getBlock() == Blocks.REDSTONE_WIRE) {
                return 0;
            }
            int i = blockState.getValue(POWER);

            if (i == 0) {
                return 0;
            } else if (side == EnumFacing.UP) {
                return i;
            } else {
                EnumSet<EnumFacing> enumset = EnumSet.noneOf(EnumFacing.class);

                for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                    if (this.isPowerSourceAt(blockAccess, pos, enumfacing)) {
                        enumset.add(enumfacing);
                    }
                }

                if (side.getAxis().isHorizontal() && enumset.isEmpty()) {
                    return i;
                } else if (enumset.contains(side) && !enumset.contains(side.rotateYCCW()) && !enumset.contains(side.rotateY())) {
                    return i;
                } else {
                    return 0;
                }
            }
        }
    }

    private boolean isPowerSourceAt(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        BlockPos blockpos = pos.offset(side);
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        boolean flag = iblockstate.isNormalCube();
        boolean flag1 = worldIn.getBlockState(pos.up()).isNormalCube();
        if (!flag1 && flag && canConnectUpwardsTo(worldIn, blockpos.up())) {
            return true;
        } else if (canConnectTo(iblockstate, side, worldIn, pos)) {
            return true;
        } else if (iblockstate.getBlock() == Blocks.POWERED_REPEATER && iblockstate.getValue(BlockRedstoneDiode.FACING) == side) {
            return true;
        } else {
            return !flag && canConnectUpwardsTo(worldIn, blockpos.down());
        }
    }

    protected static boolean canConnectUpwardsTo(IBlockAccess worldIn, BlockPos pos) {
        return canConnectTo(worldIn.getBlockState(pos), null, worldIn, pos);
    }

    protected static boolean canConnectTo(IBlockState blockState, @Nullable EnumFacing side, IBlockAccess world, BlockPos pos) {
        Block block = blockState.getBlock();
        if (block == Blocks.REDSTONE_WIRE) {
            return false;
        } else if (block == TakumiBlockCore.CREEPER_REDSTONE_WIRE) {
            return true;
        } else if (Blocks.UNPOWERED_REPEATER.isSameDiode(blockState)) {
            EnumFacing enumfacing = blockState.getValue(BlockRedstoneRepeater.FACING);
            return enumfacing == side || enumfacing.getOpposite() == side;
        } else if (Blocks.OBSERVER == blockState.getBlock()) {
            return side == blockState.getValue(BlockObserver.FACING);
        } else {
            return blockState.getBlock().canConnectRedstone(blockState, world, pos, side);
        }
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
        return false;
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    @Override
    public boolean canProvidePower(IBlockState state) {
        return this.canProvidePower;
    }

    @SideOnly(Side.CLIENT)
    public static int colorMultiplier(int p_176337_0_) {
        float f = (float) p_176337_0_ / 15.0F;
        float f1 = f * 0.6F + 0.4F;

        if (p_176337_0_ == 0) {
            f1 = 0.3F;
        }

        float f2 = f * f * 0.7F - 0.5F;
        float f3 = f * f * 0.6F - 0.7F;

        if (f2 < 0.0F) {
            f2 = 0.0F;
        }

        if (f3 < 0.0F) {
            f3 = 0.0F;
        }

        int green = MathHelper.clamp((int) (f1 * 255.0F), 0, 255);
        int red = MathHelper.clamp((int) (f2 * 255.0F), 0, 255);
        int blue = MathHelper.clamp((int) (f3 * 255.0F), 0, 255);
        return -16777216 | red << 16 | green << 8 | blue;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        int i = stateIn.getValue(POWER);

        if (i != 0) {
            double d0 = (double) pos.getX() + 0.5D + ((double) rand.nextFloat() - 0.5D) * 0.2D;
            double d1 = (float) pos.getY() + 0.0625F;
            double d2 = (double) pos.getZ() + 0.5D + ((double) rand.nextFloat() - 0.5D) * 0.2D;
            float f = (float) i / 15.0F;
            float f1 = f * 0.6F + 0.4F;
            float f2 = Math.max(0.0F, f * f * 0.7F - 0.5F);
            float f3 = Math.max(0.0F, f * f * 0.6F - 0.7F);
            Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleTakumiRedstone(worldIn, d0, d1, d2, f1, f2, f3));
        }
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(POWER, meta);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(POWER);
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        switch (rot) {
            case CLOCKWISE_180:
                return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(EAST, state.getValue(WEST)).withProperty(SOUTH, state.getValue(NORTH)).withProperty(WEST, state.getValue(EAST));
            case COUNTERCLOCKWISE_90:
                return state.withProperty(NORTH, state.getValue(EAST)).withProperty(EAST, state.getValue(SOUTH)).withProperty(SOUTH, state.getValue(WEST)).withProperty(WEST, state.getValue(NORTH));
            case CLOCKWISE_90:
                return state.withProperty(NORTH, state.getValue(WEST)).withProperty(EAST, state.getValue(NORTH)).withProperty(SOUTH, state.getValue(EAST)).withProperty(WEST, state.getValue(SOUTH));
            default:
                return state;
        }
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        switch (mirrorIn) {
            case LEFT_RIGHT:
                return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(SOUTH, state.getValue(NORTH));
            case FRONT_BACK:
                return state.withProperty(EAST, state.getValue(WEST)).withProperty(WEST, state.getValue(EAST));
            default:
                return super.withMirror(state, mirrorIn);
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, NORTH, EAST, SOUTH, WEST, POWER);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public ItemBlock getItem() {
        return new ItemTakumiRedstone(this);
    }

    enum EnumAttachPosition implements IStringSerializable {
        UP("up"),
        SIDE("side"),
        NONE("none");

        private final String name;

        EnumAttachPosition(String name) {
            this.name = name;
        }

        public String toString() {
            return this.getName();
        }

        @Override
        public String getName() {
            return this.name;
        }
    }
}
