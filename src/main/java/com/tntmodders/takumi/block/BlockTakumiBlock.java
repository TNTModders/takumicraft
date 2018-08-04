package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.tileentity.TileEntityTakumiBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BlockTakumiBlock extends BlockContainer {

    private WorldDummyTakumiBlock dummyWorld;

    public BlockTakumiBlock() {
        super(Material.BARRIER);
        this.setRegistryName(TakumiCraftCore.MODID, "takumiblock");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("takumiblock");
        this.setLightLevel(1f);
        this.setLightOpacity(255);
        this.setBlockUnbreakable();
        this.setResistance(10000000);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityTakumiBlock();
    }

    private class WorldDummyTakumiBlock extends World {

        public final Map<BlockPos, IBlockState> stateMap = new HashMap<>();
        private final World world;

        WorldDummyTakumiBlock(World world) {
            super(world.getSaveHandler(), world.getWorldInfo(), world.provider, world.profiler, world.isRemote);
            this.world = world;
            this.chunkProvider = this.createChunkProvider();
        }

        @Override
        protected IChunkProvider createChunkProvider() {
            return FMLCommonHandler.instance().getSide().isClient() ? new ChunkProviderClient(this) :
                    new ChunkProviderServer(FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(
                            this.world.provider.getDimension()), this.saveHandler.getChunkLoader(this.provider),
                            this.provider.createChunkGenerator());
        }

        @Override
        protected boolean isChunkLoaded(int x, int z, boolean allowEmpty) {
            switch (FMLCommonHandler.instance().getSide()) {
                case CLIENT:
                    return allowEmpty || !this.getChunkProvider().provideChunk(x, z).isEmpty();
                case SERVER:
                    return ((ChunkProviderServer) this.getChunkProvider()).chunkExists(x, z);
            }
            return false;
        }

        @Override
        public boolean setBlockState(BlockPos pos, IBlockState newState, int flags) {
            this.stateMap.put(pos, newState);
            return true;
        }

        @Override
        public IBlockState getBlockState(BlockPos pos) {
            if (this.world.getTileEntity(pos) instanceof TileEntityTakumiBlock &&
                    ((TileEntityTakumiBlock) this.world.getTileEntity(pos)).getBlock() != null) {
                if (((TileEntityTakumiBlock) this.world.getTileEntity(pos)).state == null) {
                    ((TileEntityTakumiBlock) this.world.getTileEntity(pos)).state =
                            ((TileEntityTakumiBlock) this.world.getTileEntity(pos)).getBlock().getActualState(
                                    ((TileEntityTakumiBlock) this.world.getTileEntity(
                                            pos)).getBlock().getDefaultState(), this.world, pos);
                }
                return ((TileEntityTakumiBlock) this.world.getTileEntity(pos)).state;
            }
            return this.world.getBlockState(pos);
        }
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
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiBlock &&
                ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock() != null) {
            IBlockState blockState = ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock().getActualState(
                    ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock().getStateFromMeta(
                            ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getMeta()), worldIn,
                    pos).getActualState(worldIn, pos);
            ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).setMeta(
                    ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock().getMetaFromState(blockState));
            ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).state = blockState;
            return blockState;
        }
        return super.getActualState(state, worldIn, pos);
    }


    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiBlock &&
                ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock() != null) {
            return ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock().isPassable(worldIn, pos);
        }
        return super.isPassable(worldIn, pos);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiBlock &&
                ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock() != null) {
            this.getActualState(state, worldIn, pos);
            IBlockState blockState = ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock().getStateFromMeta(
                    ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getMeta());
            return blockState.getBoundingBox(worldIn, pos);
        }
        return super.getBoundingBox(state, worldIn, pos);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing facing) {
        if (world.getTileEntity(pos) instanceof TileEntityTakumiBlock &&
                ((TileEntityTakumiBlock) world.getTileEntity(pos)).getBlock() != null) {
            return ((TileEntityTakumiBlock) world.getTileEntity(pos)).getBlock().getBlockFaceShape(world,
                    ((TileEntityTakumiBlock) world.getTileEntity(pos)).state, pos, facing);
        }
        return super.getBlockFaceShape(world, state, pos, facing);
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
            List<AxisAlignedBB> collidingBoxes,
            @Nullable
                    Entity entityIn, boolean p_185477_7_) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiBlock &&
                ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock() != null) {
            this.getActualState(state, worldIn, pos);
            IBlockState blockState = ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock().getStateFromMeta(
                    ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getMeta());
            try {
                blockState.addCollisionBoxToList(worldIn, pos, entityBox, collidingBoxes, entityIn, p_185477_7_);
            } catch (Exception e) {
                //e.printStackTrace();
            }
            return;
        }
        super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, p_185477_7_);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiBlock &&
                ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock() != null) {
            this.getActualState(state, worldIn, pos);
            IBlockState blockState = ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock().getStateFromMeta(
                    ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getMeta());
            return blockState.getCollisionBoundingBox(worldIn, pos);
        }
        return super.getCollisionBoundingBox(state, worldIn, pos);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiBlock &&
                ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock() != null) {
            IBlockState blockState = ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock().getStateFromMeta(
                    ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getMeta());
            return blockState.getSelectedBoundingBox(worldIn, pos);
        }
        return super.getSelectedBoundingBox(state, worldIn, pos);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiBlock &&
                ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock() != null) {
            ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock().updateTick(worldIn, pos, state, rand);
        } else {
            super.updateTick(worldIn, pos, state, rand);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiBlock &&
                ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock() != null) {
            if (((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).state == null) {
                ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).state =
                        ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock().getActualState(
                                ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock().getDefaultState(),
                                worldIn, pos);
            }
            ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock().randomDisplayTick(
                    ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).state, worldIn, pos, rand);
        } else {
            super.randomDisplayTick(stateIn, worldIn, pos, rand);
        }
    }

    @Override
    public void neighborChanged(IBlockState stateIn, World world, BlockPos pos, Block blockIn, BlockPos neighbor) {
        if (world.getTileEntity(pos) instanceof TileEntityTakumiBlock &&
                ((TileEntityTakumiBlock) world.getTileEntity(pos)).getBlock() != null) {
            if (this.dummyWorld == null) {
                this.dummyWorld = new WorldDummyTakumiBlock(world);
            }
            if (((TileEntityTakumiBlock) world.getTileEntity(pos)).state == null) {
                ((TileEntityTakumiBlock) world.getTileEntity(pos)).setState(
                        ((TileEntityTakumiBlock) world.getTileEntity(pos)).getBlock().getDefaultState().getActualState(
                                world, pos));
            }
            ((TileEntityTakumiBlock) world.getTileEntity(pos)).state.neighborChanged(this.dummyWorld, pos,
                    ((TileEntityTakumiBlock) world.getTileEntity(pos)).getBlock(), pos);
            if (this.dummyWorld.stateMap.containsKey(pos)) {
                ((TileEntityTakumiBlock) world.getTileEntity(pos)).state = this.dummyWorld.stateMap.get(pos);
                this.dummyWorld.stateMap.remove(pos);
            }
        } else {
            super.neighborChanged(stateIn, world, pos, blockIn, neighbor);
        }
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        if (blockAccess.getTileEntity(pos) instanceof TileEntityTakumiBlock &&
                ((TileEntityTakumiBlock) blockAccess.getTileEntity(pos)).getBlock() != null) {
            return ((TileEntityTakumiBlock) blockAccess.getTileEntity(pos)).getBlock().getWeakPower(blockState,
                    blockAccess, pos, side);
        }
        return blockState.getWeakPower(blockAccess, pos, side);
    }

    @Override
    public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start,
            Vec3d end) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiBlock &&
                ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock() != null) {
            IBlockState state = ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock().getStateFromMeta(
                    ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getMeta());
            return state.collisionRayTrace(worldIn, pos, start, end);
        }
        return super.collisionRayTrace(blockState, worldIn, pos, start, end);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
            EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (playerIn.getHeldItem(hand).getItem() instanceof ItemBlock &&
                playerIn.getHeldItem(hand).getItem() != Item.getItemFromBlock(TakumiBlockCore.TAKUMI_BLOCK)) {
            Block block = ((ItemBlock) playerIn.getHeldItem(hand).getItem()).getBlock();
            if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiBlock &&
                    block.getRenderType(block.getDefaultState()) == EnumBlockRenderType.MODEL) {
                if (!playerIn.isCreative()) {
                    if (((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock() != null && !worldIn.isRemote) {
                        EntityItem item = new EntityItem(worldIn);
                        item.setPosition(pos.getX(), pos.getY(), pos.getZ());
                        item.setItem(
                                ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock().getItem(worldIn, pos,
                                        ((TileEntityTakumiBlock) worldIn.getTileEntity(
                                                pos)).getBlock().getStateFromMeta(
                                                ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getMeta())));
                        worldIn.spawnEntity(item);
                    }
                    playerIn.getHeldItem(hand).shrink(1);
                }

                ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).setPath(block.getRegistryName().toString());
                IBlockState blockState = block.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ,
                        playerIn.getHeldItem(hand).getMetadata(), playerIn, hand);
                ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).setMeta(block.getMetaFromState(blockState));
                worldIn.notifyNeighborsOfStateChange(pos, block, true);
                this.getActualState(state, worldIn, pos);
                return true;
            }
        }
        if (playerIn.getHeldItem(hand).isEmpty() && worldIn.getTileEntity(pos) instanceof TileEntityTakumiBlock) {
            if (!playerIn.isCreative()) {
                if (((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock() != null && !worldIn.isRemote) {
                    EntityItem item = new EntityItem(worldIn);
                    item.setPosition(pos.getX(), pos.getY(), pos.getZ());
                    item.setItem(((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock().getItem(worldIn, pos,
                            ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock().getStateFromMeta(
                                    ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getMeta())));
                    worldIn.spawnEntity(item);
                }
            }
            ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).setPath("");
            ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).setMeta(0);
            worldIn.notifyNeighborsOfStateChange(pos, this, true);
            this.getActualState(state, worldIn, pos);
            return true;
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiBlock &&
                ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock() != null) {
            ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock().onEntityWalk(worldIn, pos, entityIn);
        } else {
            super.onEntityWalk(worldIn, pos, entityIn);
        }
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        super.onBlockClicked(worldIn, pos, playerIn);
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiBlock && !playerIn.isCreative() &&
                !worldIn.isRemote) {
            if (((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock() != null) {
                ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock().dropBlockAsItem(worldIn, pos,
                        ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock().getStateFromMeta(
                                ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getMeta()), 0);
            }
            this.dropBlockAsItem(worldIn, pos, this.getDefaultState(), 0);
        }
        worldIn.setBlockToAir(pos);
    }

    @Override
    public Vec3d modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3d motion) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiBlock &&
                ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock() != null) {
            return ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock().modifyAcceleration(worldIn, pos,
                    entityIn, motion);
        }
        return super.modifyAcceleration(worldIn, pos, entityIn, motion);
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiBlock &&
                ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock() != null) {
            ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock().onEntityCollidedWithBlock(worldIn, pos,
                    state, entityIn);
        } else {
            super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
        }
    }

    @Override
    public EnumPushReaction getMobilityFlag(IBlockState state) {
        return EnumPushReaction.BLOCK;
    }

    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiBlock &&
                ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock() != null) {
            ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock().onFallenUpon(worldIn, pos, entityIn,
                    fallDistance);
        } else {
            super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
        }
    }

    @Override
    public void onLanded(World worldIn, Entity entityIn) {
        if (worldIn.getTileEntity(entityIn.getPosition().down()) instanceof TileEntityTakumiBlock) {
            if (((TileEntityTakumiBlock) worldIn.getTileEntity(entityIn.getPosition().down())).getBlock() != null) {
                ((TileEntityTakumiBlock) worldIn.getTileEntity(entityIn.getPosition().down())).getBlock().onLanded(
                        worldIn, entityIn);
            }
        } else {
            super.onLanded(worldIn, entityIn);
        }
    }

    @Override
    public float getSlipperiness(IBlockState state, IBlockAccess world, BlockPos pos,
            @Nullable
                    Entity entity) {
        if (world.getTileEntity(pos) instanceof TileEntityTakumiBlock) {
            if (((TileEntityTakumiBlock) world.getTileEntity(pos)).getBlock() != null) {
                return ((TileEntityTakumiBlock) world.getTileEntity(pos)).getBlock().getSlipperiness(state, world, pos,
                        entity);
            }
        }
        return super.getSlipperiness(state, world, pos, entity);
    }

    @Override
    public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) {
        if (world.getTileEntity(pos) instanceof TileEntityTakumiBlock &&
                ((TileEntityTakumiBlock) world.getTileEntity(pos)).getBlock() != null) {
            return ((TileEntityTakumiBlock) world.getTileEntity(pos)).getBlock().isLadder(state, world, pos, entity);
        }
        return super.isLadder(state, world, pos, entity);
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (world.getTileEntity(pos) instanceof TileEntityTakumiBlock &&
                ((TileEntityTakumiBlock) world.getTileEntity(pos)).getBlock() != null) {
            return ((TileEntityTakumiBlock) world.getTileEntity(pos)).getBlock().isNormalCube(state, world, pos);
        }
        return super.isNormalCube(state, world, pos);
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        return false;
    }

    @Override
    public boolean isLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (world.getTileEntity(pos) instanceof TileEntityTakumiBlock &&
                ((TileEntityTakumiBlock) world.getTileEntity(pos)).getBlock() != null) {
            return ((TileEntityTakumiBlock) world.getTileEntity(pos)).getBlock().isLeaves(state, world, pos);
        }
        return super.isLeaves(state, world, pos);
    }

    @Override
    public boolean isWood(IBlockAccess world, BlockPos pos) {
        if (world.getTileEntity(pos) instanceof TileEntityTakumiBlock &&
                ((TileEntityTakumiBlock) world.getTileEntity(pos)).getBlock() != null) {
            return ((TileEntityTakumiBlock) world.getTileEntity(pos)).getBlock().isWood(world, pos);
        }
        return super.isWood(world, pos);
    }

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction,
            IPlantable plantable) {
        if (world.getTileEntity(pos) instanceof TileEntityTakumiBlock &&
                ((TileEntityTakumiBlock) world.getTileEntity(pos)).getBlock() != null) {
            return ((TileEntityTakumiBlock) world.getTileEntity(pos)).getBlock().canSustainPlant(state, world, pos,
                    direction, plantable);
        }
        return super.canSustainPlant(state, world, pos, direction, plantable);
    }

    @Override
    public void onPlantGrow(IBlockState state, World world, BlockPos pos, BlockPos source) {
        if (world.getTileEntity(pos) instanceof TileEntityTakumiBlock &&
                ((TileEntityTakumiBlock) world.getTileEntity(pos)).getBlock() != null) {
            ((TileEntityTakumiBlock) world.getTileEntity(pos)).getBlock().onPlantGrow(state, world, pos, source);
        } else {
            super.onPlantGrow(state, world, pos, source);
        }
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(world, pos, neighbor);
        this.getActualState(world.getBlockState(pos), world, pos);
    }

    @Override
    public boolean shouldCheckWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        if (world.getTileEntity(pos) instanceof TileEntityTakumiBlock &&
                ((TileEntityTakumiBlock) world.getTileEntity(pos)).getBlock() != null) {
            return ((TileEntityTakumiBlock) world.getTileEntity(pos)).getBlock().shouldCheckWeakPower(state, world, pos,
                    side);
        }
        return super.shouldCheckWeakPower(state, world, pos, side);
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos,
            @Nullable
                    Entity entity) {
        if (world.getTileEntity(pos) instanceof TileEntityTakumiBlock &&
                ((TileEntityTakumiBlock) world.getTileEntity(pos)).getBlock() != null) {
            return ((TileEntityTakumiBlock) world.getTileEntity(pos)).getBlock().getSoundType(state, world, pos,
                    entity);
        }
        return super.getSoundType(state, world, pos, entity);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY,
            float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiBlock &&
                ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock() != null) {
            IBlockState state =
                    ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock().getStateForPlacement(worldIn, pos,
                            facing, hitX, hitY, hitZ, meta, placer, hand);
            ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).setMeta(
                    ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock().getMetaFromState(state));
        }
        return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
    }

    @Override
    public boolean canBeConnectedTo(IBlockAccess worldIn, BlockPos pos, EnumFacing facing) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiBlock &&
                ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock() != null) {
            return ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock().canBeConnectedTo(worldIn, pos,
                    facing);
        }
        return super.canBeConnectedTo(worldIn, pos, facing);
    }


}
