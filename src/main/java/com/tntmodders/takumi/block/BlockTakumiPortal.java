package com.tntmodders.takumi.block;

import com.google.common.cache.LoadingCache;
import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.client.particle.ParticleTakumiPortal;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.core.TakumiWorldCore;
import com.tntmodders.takumi.world.teleport.TakumiTeleporter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockPattern.PatternHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockTakumiPortal extends BlockBreakable {

    public static final PropertyEnum<Axis> AXIS = PropertyEnum.create("axis", Axis.class, Axis.X, Axis.Z);
    protected static final AxisAlignedBB X_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D);
    protected static final AxisAlignedBB Z_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 1.0D);
    protected static final AxisAlignedBB Y_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D);

    public BlockTakumiPortal() {
        super(Material.PORTAL, false);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AXIS, Axis.X));
        this.setRegistryName(TakumiCraftCore.MODID, "takumiportal");
        this.setUnlocalizedName("takumiportal");
        this.setTickRandomly(true);
        this.setResistance(10000000f);
        this.setLightLevel(0.5f);
    }

    public boolean trySpawnPortal(World worldIn, BlockPos pos) {
        Size blockportal$size = new Size(worldIn, pos, Axis.X);

        if (blockportal$size.isValid() && blockportal$size.portalBlockCount == 0) {
            blockportal$size.placePortalBlocks();
            return true;
        }
        Size blockportal$size1 = new Size(worldIn, pos, Axis.Z);

        if (blockportal$size1.isValid() && blockportal$size1.portalBlockCount == 0) {
            blockportal$size1.placePortalBlocks();
            return true;
        }
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos,
            EnumFacing side) {
        pos = pos.offset(side);
        Axis enumfacing$axis = null;

        if (blockState.getBlock() == this) {
            enumfacing$axis = blockState.getValue(AXIS);

            if (enumfacing$axis == null) {
                return false;
            }

            if (enumfacing$axis == Axis.Z && side != EnumFacing.EAST && side != EnumFacing.WEST) {
                return false;
            }

            if (enumfacing$axis == Axis.X && side != EnumFacing.SOUTH && side != EnumFacing.NORTH) {
                return false;
            }
        }

        boolean flag = blockAccess.getBlockState(pos.west()).getBlock() == this &&
                blockAccess.getBlockState(pos.west(2)).getBlock() != this;
        boolean flag1 = blockAccess.getBlockState(pos.east()).getBlock() == this &&
                blockAccess.getBlockState(pos.east(2)).getBlock() != this;
        boolean flag2 = blockAccess.getBlockState(pos.north()).getBlock() == this &&
                blockAccess.getBlockState(pos.north(2)).getBlock() != this;
        boolean flag3 = blockAccess.getBlockState(pos.south()).getBlock() == this &&
                blockAccess.getBlockState(pos.south(2)).getBlock() != this;
        boolean flag4 = flag || flag1 || enumfacing$axis == Axis.X;
        boolean flag5 = flag2 || flag3 || enumfacing$axis == Axis.Z;

        return flag4 && side == EnumFacing.WEST || flag4 && side == EnumFacing.EAST ||
                flag5 && side == EnumFacing.NORTH || flag5 && side == EnumFacing.SOUTH;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(AXIS, (meta & 3) == 2 ? Axis.Z : Axis.X);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return getMetaForAxis(state.getValue(AXIS));
    }

    public static int getMetaForAxis(Axis axis) {
        if (axis == Axis.X) {
            return 1;
        }
        return axis == Axis.Z ? 2 : 0;
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        switch (rot) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:

                switch (state.getValue(AXIS)) {
                    case X:
                        return state.withProperty(AXIS, Axis.Z);
                    case Z:
                        return state.withProperty(AXIS, Axis.X);
                    default:
                        return state;
                }

            default:
                return state;
        }
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getValue(AXIS)) {
            case X:
                return X_AABB;
            case Z:
                return Z_AABB;
            default:
                return Y_AABB;
        }
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_,
            EnumFacing p_193383_4_) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(worldIn, pos, state, rand);

        if (worldIn.provider.isSurfaceWorld() && worldIn.getGameRules().getBoolean("doMobSpawning") &&
                rand.nextInt(2000) < worldIn.getDifficulty().getDifficultyId()) {
            int i = pos.getY();
            BlockPos blockpos;

            for (blockpos = pos; !worldIn.getBlockState(blockpos).isTopSolid() && blockpos.getY() > 0;
                 blockpos = blockpos.down()) {
            }

            if (i > 0 && !worldIn.getBlockState(blockpos.up()).isNormalCube()) {
                Entity entity = ItemMonsterPlacer.spawnCreature(worldIn, EntityList.getKey(EntityCreeper.class),
                        (double) blockpos.getX() + 0.5D, (double) blockpos.getY() + 1.1D,
                        (double) blockpos.getZ() + 0.5D);
                entity.onStruckByLightning(null);

                if (entity != null) {
                    entity.timeUntilPortal = entity.getPortalCooldown();
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (rand.nextInt(100) == 0) {
            worldIn.playSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D,
                    SoundEvents.BLOCK_PORTAL_AMBIENT, SoundCategory.BLOCKS, 0.5F, rand.nextFloat() * 0.4F + 0.8F,
                    false);
        }

        for (int i = 0; i < 150; ++i) {
            double d0 = (double) ((float) pos.getX() + rand.nextFloat());
            double d1 = (double) ((float) pos.getY() + rand.nextFloat());
            double d2 = (double) ((float) pos.getZ() + rand.nextFloat());
            double d3 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
            double d4 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
            double d5 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
            int j = rand.nextInt(2) * 2 - 1;

            if (worldIn.getBlockState(pos.west()).getBlock() != this &&
                    worldIn.getBlockState(pos.east()).getBlock() != this) {
                d0 = (double) pos.getX() + 0.5D + 0.25D * (double) j;
                d3 = (double) (rand.nextFloat() * 2.0F * (float) j);
            } else {
                d2 = (double) pos.getZ() + 0.5D + 0.25D * (double) j;
                d5 = (double) (rand.nextFloat() * 2.0F * (float) j);
            }
            //worldIn.spawnParticle(EnumParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
            Minecraft.getMinecraft().effectRenderer.addEffect(
                    new ParticleTakumiPortal(worldIn, d0, d1, d2, d3, d4, d5));
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        Axis enumfacing$axis = state.getValue(AXIS);

        if (enumfacing$axis == Axis.X) {
            Size blockportal$size = new Size(worldIn, pos, Axis.X);

            if (!blockportal$size.isValid() ||
                    blockportal$size.portalBlockCount < blockportal$size.width * blockportal$size.height) {
                worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
            }
        } else if (enumfacing$axis == Axis.Z) {
            Size blockportal$size1 = new Size(worldIn, pos, Axis.Z);

            if (!blockportal$size1.isValid() ||
                    blockportal$size1.portalBlockCount < blockportal$size1.width * blockportal$size1.height) {
                worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
            }
        }
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    /**
     * Called When an Entity Collided with the Block
     */
    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (!entityIn.isRiding() && !entityIn.isBeingRidden() && entityIn.isNonBoss()) {
            MinecraftServer server = worldIn.getMinecraftServer();
            if (server != null && entityIn.timeUntilPortal <= 0) {
                PlayerList playerList = server.getPlayerList();
                int i = entityIn.dimension == DimensionType.OVERWORLD.getId() ? TakumiWorldCore.TAKUMI_WORLD.getId() :
                        DimensionType.OVERWORLD.getId();
                TakumiTeleporter teleporter = new TakumiTeleporter(server.getWorld(i));
                teleporter.setTakumiPortal(entityIn, pos);
                entityIn.timeUntilPortal = entityIn.getPortalCooldown() * 2;
                if (entityIn instanceof EntityPlayerMP) {
                    playerList.transferPlayerToDimension((EntityPlayerMP) entityIn, i, teleporter);
                } else {
                    int origin = entityIn.dimension;
                    entityIn.dimension = i;
                    worldIn.removeEntityDangerously(entityIn);

                    entityIn.isDead = false;

                    playerList.transferEntityToWorld(entityIn, origin, server.getWorld(origin), server.getWorld(i),
                            teleporter);
                }

            }
        }
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return ItemStack.EMPTY;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AXIS);
    }

    public PatternHelper createPatternHelper(World worldIn, BlockPos p_181089_2_) {
        Axis enumfacing$axis = Axis.Z;
        Size blockportal$size = new Size(worldIn, p_181089_2_, Axis.X);
        LoadingCache<BlockPos, BlockWorldState> loadingcache = BlockPattern.createLoadingCache(worldIn, true);

        if (!blockportal$size.isValid()) {
            enumfacing$axis = Axis.X;
            blockportal$size = new Size(worldIn, p_181089_2_, Axis.Z);
        }

        if (!blockportal$size.isValid()) {
            return new PatternHelper(p_181089_2_, EnumFacing.NORTH, EnumFacing.UP, loadingcache, 1, 1, 1);
        }
        int[] aint = new int[AxisDirection.values().length];
        EnumFacing enumfacing = blockportal$size.rightDir.rotateYCCW();
        BlockPos blockpos = blockportal$size.bottomLeft.up(blockportal$size.getHeight() - 1);

        for (AxisDirection enumfacing$axisdirection : AxisDirection.values()) {
            PatternHelper blockpattern$patternhelper = new PatternHelper(
                    enumfacing.getAxisDirection() == enumfacing$axisdirection ? blockpos :
                            blockpos.offset(blockportal$size.rightDir, blockportal$size.getWidth() - 1),
                    EnumFacing.getFacingFromAxis(enumfacing$axisdirection, enumfacing$axis), EnumFacing.UP,
                    loadingcache, blockportal$size.getWidth(), blockportal$size.getHeight(), 1);

            for (int i = 0; i < blockportal$size.getWidth(); ++i) {
                for (int j = 0; j < blockportal$size.getHeight(); ++j) {
                    BlockWorldState blockworldstate = blockpattern$patternhelper.translateOffset(i, j, 1);

                    if (blockworldstate.getBlockState() != null &&
                            blockworldstate.getBlockState().getMaterial() != Material.AIR) {
                        ++aint[enumfacing$axisdirection.ordinal()];
                    }
                }
            }
        }

        AxisDirection enumfacing$axisdirection1 = AxisDirection.POSITIVE;

        for (AxisDirection enumfacing$axisdirection2 : AxisDirection.values()) {
            if (aint[enumfacing$axisdirection2.ordinal()] < aint[enumfacing$axisdirection1.ordinal()]) {
                enumfacing$axisdirection1 = enumfacing$axisdirection2;
            }
        }

        return new PatternHelper(enumfacing.getAxisDirection() == enumfacing$axisdirection1 ? blockpos :
                blockpos.offset(blockportal$size.rightDir, blockportal$size.getWidth() - 1),
                EnumFacing.getFacingFromAxis(enumfacing$axisdirection1, enumfacing$axis), EnumFacing.UP, loadingcache,
                blockportal$size.getWidth(), blockportal$size.getHeight(), 1);
    }

    public static class Size {

        private final World world;
        private final Axis axis;
        private final EnumFacing rightDir;
        private final EnumFacing leftDir;
        private int portalBlockCount;
        private BlockPos bottomLeft;
        private int height;
        private int width;

        public Size(World worldIn, BlockPos p_i45694_2_, Axis p_i45694_3_) {
            this.world = worldIn;
            this.axis = p_i45694_3_;

            if (p_i45694_3_ == Axis.X) {
                this.leftDir = EnumFacing.EAST;
                this.rightDir = EnumFacing.WEST;
            } else {
                this.leftDir = EnumFacing.NORTH;
                this.rightDir = EnumFacing.SOUTH;
            }

            for (BlockPos blockpos = p_i45694_2_; p_i45694_2_.getY() > blockpos.getY() - 21 && p_i45694_2_.getY() > 0 &&
                    this.isEmptyBlock(worldIn.getBlockState(p_i45694_2_.down())); p_i45694_2_ = p_i45694_2_.down()) {
            }

            int i = this.getDistanceUntilEdge(p_i45694_2_, this.leftDir) - 1;

            if (i >= 0) {
                this.bottomLeft = p_i45694_2_.offset(this.leftDir, i);
                this.width = this.getDistanceUntilEdge(this.bottomLeft, this.rightDir);

                if (this.width < 2 || this.width > 21) {
                    this.bottomLeft = null;
                    this.width = 0;
                }
            }

            if (this.bottomLeft != null) {
                this.height = this.calculatePortalHeight();
            }
        }

        protected boolean isEmptyBlock(IBlockState state) {
            Block blockIn = state.getBlock();
            return state.getMaterial() == Material.AIR || blockIn == Blocks.FIRE ||
                    blockIn == TakumiBlockCore.TAKUMI_PORTAL;
        }

        protected int getDistanceUntilEdge(BlockPos p_180120_1_, EnumFacing p_180120_2_) {
            int i;

            for (i = 0; i < 22; ++i) {
                BlockPos blockpos = p_180120_1_.offset(p_180120_2_, i);

                if (!this.isEmptyBlock(this.world.getBlockState(blockpos)) ||
                        this.world.getBlockState(blockpos.down()).getBlock() != TakumiBlockCore.TAKUMI_PORTAL_FRAME) {
                    break;
                }
            }

            Block block = this.world.getBlockState(p_180120_1_.offset(p_180120_2_, i)).getBlock();
            return block == TakumiBlockCore.TAKUMI_PORTAL_FRAME ? i : 0;
        }

        protected int calculatePortalHeight() {
            label56:

            for (this.height = 0; this.height < 21; ++this.height) {
                for (int i = 0; i < this.width; ++i) {
                    BlockPos blockpos = this.bottomLeft.offset(this.rightDir, i).up(this.height);
                    IBlockState state = this.world.getBlockState(blockpos);
                    Block block = state.getBlock();

                    if (!this.isEmptyBlock(state)) {
                        break label56;
                    }

                    if (block == TakumiBlockCore.TAKUMI_PORTAL) {
                        ++this.portalBlockCount;
                    }

                    if (i == 0) {
                        block = this.world.getBlockState(blockpos.offset(this.leftDir)).getBlock();

                        if (block != TakumiBlockCore.TAKUMI_PORTAL_FRAME) {
                            break label56;
                        }
                    } else if (i == this.width - 1) {
                        block = this.world.getBlockState(blockpos.offset(this.rightDir)).getBlock();

                        if (block != TakumiBlockCore.TAKUMI_PORTAL_FRAME) {
                            break label56;
                        }
                    }
                }
            }

            for (int j = 0; j < this.width; ++j) {
                if (this.world.getBlockState(this.bottomLeft.offset(this.rightDir, j).up(this.height)).getBlock() !=
                        TakumiBlockCore.TAKUMI_PORTAL_FRAME) {
                    this.height = 0;
                    break;
                }
            }

            if (this.height <= 21 && this.height >= 3) {
                return this.height;
            }
            this.bottomLeft = null;
            this.width = 0;
            this.height = 0;
            return 0;
        }

        public int getHeight() {
            return this.height;
        }

        public int getWidth() {
            return this.width;
        }

        public boolean isValid() {
            return this.bottomLeft != null && this.width >= 2 && this.width <= 21 && this.height >= 3 &&
                    this.height <= 21;
        }

        public void placePortalBlocks() {
            for (int i = 0; i < this.width; ++i) {
                BlockPos blockpos = this.bottomLeft.offset(this.rightDir, i);

                for (int j = 0; j < this.height; ++j) {
                    this.world.setBlockState(blockpos.up(j),
                            TakumiBlockCore.TAKUMI_PORTAL.getDefaultState().withProperty(AXIS, this.axis), 2);
                }
            }
        }
    }
}
