package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.core.TakumiConfigCore;
import com.tntmodders.takumi.tileentity.TileEntityTakumiSuperPowered;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.GameType;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockTakumiSuperPowered extends BlockContainer {

    public BlockTakumiSuperPowered() {
        super(Material.BARRIER);
        this.setRegistryName(TakumiCraftCore.MODID, "takumiblock");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("takumiblock");
        this.setLightLevel(1f);
        this.setLightOpacity(255);
        this.setBlockUnbreakable();
        this.setResistance(10000000);
        this.setTickRandomly(true);
    }

    public static IBlockState getBlockStateFromTE(IBlockAccess world, BlockPos pos) {
        if (((TileEntityTakumiSuperPowered) world.getTileEntity(pos)).state != null) {
            return ((TileEntityTakumiSuperPowered) world.getTileEntity(pos)).state;
        }
        return ((TileEntityTakumiSuperPowered) world.getTileEntity(pos)).getBlock().getStateFromMeta(
                ((TileEntityTakumiSuperPowered) world.getTileEntity(pos)).getMeta());
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityTakumiSuperPowered();
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
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiSuperPowered &&
                ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getBlock() != null) {
            return ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getBlock().isPassable(worldIn, pos);
        }
        return super.isPassable(worldIn, pos);
    }

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
        return true;
    }

    @Override
    public boolean canSustainLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return true;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (world.getTileEntity(pos) instanceof TileEntityTakumiSuperPowered &&
                ((TileEntityTakumiSuperPowered) world.getTileEntity(pos)).getBlock() != null) {
            try {
                TileEntityTakumiSuperPowered te = ((TileEntityTakumiSuperPowered) world.getTileEntity(pos));
                if (te.state == null) {
                    te.setState(te.getBlock().getStateFromMeta(te.getMeta()));
                }
                te.getBlock().updateTick(world, pos, te.state, rand);
            } catch (Exception e) {
            }
        } else {
            super.updateTick(world, pos, state, rand);
        }
    }

    @Override
    public void randomTick(World world, BlockPos pos, IBlockState state, Random random) {
        if (world.getTileEntity(pos) instanceof TileEntityTakumiSuperPowered &&
                ((TileEntityTakumiSuperPowered) world.getTileEntity(pos)).getBlock() != null) {
            try {
                TileEntityTakumiSuperPowered te = ((TileEntityTakumiSuperPowered) world.getTileEntity(pos));
                if (te.state == null) {
                    te.setState(te.getBlock().getStateFromMeta(te.getMeta()));
                }
                te.getBlock().randomTick(world, pos, te.state, random);
            } catch (Exception e) {
            }
        } else {
            super.randomTick(world, pos, state, random);
        }
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiSuperPowered &&
                ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getBlock() != null) {
            try {
                IBlockState blockState = ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getBlock().getActualState(
                        ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getBlock().getStateFromMeta(
                                ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getMeta()), worldIn,
                        pos).getActualState(worldIn, pos);
                ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).setMeta(
                        ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getBlock().getMetaFromState(blockState));
                ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).state = blockState;
                return blockState;
            } catch (Exception ignored) {
            }
        }
        return super.getActualState(state, worldIn, pos);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiSuperPowered &&
                ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getBlock() != null) {
            this.getActualState(state, worldIn, pos);
            IBlockState blockState = getBlockStateFromTE(worldIn, pos);
            return blockState.getBoundingBox(worldIn, pos);
        }
        return super.getBoundingBox(state, worldIn, pos);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing facing) {
        if (world.getTileEntity(pos) instanceof TileEntityTakumiSuperPowered &&
                ((TileEntityTakumiSuperPowered) world.getTileEntity(pos)).getBlock() != null) {
            try {
                TileEntityTakumiSuperPowered te = ((TileEntityTakumiSuperPowered) world.getTileEntity(pos));
                if (te.state == null) {
                    te.setState(te.getBlock().getStateFromMeta(te.getMeta()));
                }
                return te.getBlock().getBlockFaceShape(world, te.state, pos, facing);
            } catch (Exception e) {
            }
        }
        return super.getBlockFaceShape(world, state, pos, facing);
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
                                      List<AxisAlignedBB> collidingBoxes,
                                      @Nullable
                                              Entity entityIn, boolean p_185477_7_) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiSuperPowered &&
                ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getBlock() != null) {
            this.getActualState(state, worldIn, pos);
            IBlockState blockState = ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getBlock().getStateFromMeta(
                    ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getMeta());
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
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiSuperPowered &&
                ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getBlock() != null) {
            this.getActualState(state, worldIn, pos);
            IBlockState blockState = ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getBlock().getStateFromMeta(
                    ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getMeta());
            return blockState.getCollisionBoundingBox(worldIn, pos);
        }
        return super.getCollisionBoundingBox(state, worldIn, pos);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiSuperPowered &&
                ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getBlock() != null) {
            IBlockState blockState = ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getBlock().getStateFromMeta(
                    ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getMeta());
            return blockState.getSelectedBoundingBox(worldIn, pos);
        }
        return super.getSelectedBoundingBox(state, worldIn, pos);
    }

    @Override
    public void neighborChanged(IBlockState stateIn, World world, BlockPos pos, Block blockIn, BlockPos neighbor) {
        if (world.getTileEntity(pos) instanceof TileEntityTakumiSuperPowered &&
                ((TileEntityTakumiSuperPowered) world.getTileEntity(pos)).getBlock() != null) {
            TileEntityTakumiSuperPowered te = ((TileEntityTakumiSuperPowered) world.getTileEntity(pos));
            if (te.state == null) {
                te.setState(te.getBlock().getStateFromMeta(te.getMeta()));
            }
            if (te.state != null) {
                te.state.neighborChanged(world, pos, blockIn, pos);
            }
        } else {
            super.neighborChanged(stateIn, world, pos, blockIn, neighbor);
        }
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        if (blockAccess.getTileEntity(pos) instanceof TileEntityTakumiSuperPowered &&
                ((TileEntityTakumiSuperPowered) blockAccess.getTileEntity(pos)).getBlock() != null) {
            return ((TileEntityTakumiSuperPowered) blockAccess.getTileEntity(pos)).getBlock().getWeakPower(((TileEntityTakumiSuperPowered) blockAccess.getTileEntity(pos)).state,
                    blockAccess, pos, side);
        }
        return 0;
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiSuperPowered &&
                ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getBlock() != null) {
            if (((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).state == null) {
                ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).state =
                        ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getBlock().getActualState(
                                ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getBlock().getDefaultState(),
                                worldIn, pos);
            }
            ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getBlock().randomDisplayTick(
                    ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).state, worldIn, pos, rand);
        } else {
            super.randomDisplayTick(stateIn, worldIn, pos, rand);
        }
    }

    @Override
    public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start,
                                            Vec3d end) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiSuperPowered &&
                ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getBlock() != null) {
            IBlockState state = ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getBlock().getStateFromMeta(
                    ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getMeta());
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
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiSuperPowered &&
                ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getBlock() != null) {
            ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getBlock().onEntityWalk(worldIn, pos, entityIn);
        } else {
            super.onEntityWalk(worldIn, pos, entityIn);
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
                                    EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!(playerIn.getHeldItem(hand).getItem() instanceof ItemBlock) && !playerIn.getHeldItem(hand).isEmpty()) {
            return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
        }
        if (worldIn.isRemote) {
            return true;
        }
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiSuperPowered &&
                this.canEditBlock(((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)), playerIn, true)) {
            if (playerIn.getHeldItem(hand).getItem() instanceof ItemBlock &&
                    playerIn.getHeldItem(hand).getItem() != Item.getItemFromBlock(TakumiBlockCore.TAKUMI_SUPERPOWERED) &&
                    ((ItemBlock) playerIn.getHeldItem(hand).getItem()).getBlock().getRenderType(
                            ((ItemBlock) playerIn.getHeldItem(hand).getItem()).getBlock().getDefaultState()) ==
                            EnumBlockRenderType.MODEL) {
                Block block = ((ItemBlock) playerIn.getHeldItem(hand).getItem()).getBlock();
                if (block.getRenderType(block.getDefaultState()) == EnumBlockRenderType.MODEL) {
                    if (!playerIn.isCreative()) {
                        if (((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getBlock() != null && !worldIn.isRemote) {
                            EntityItem item = new EntityItem(worldIn);
                            item.setPosition(pos.getX(), pos.getY(), pos.getZ());
                            item.setItem(
                                    ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getBlock().getItem(worldIn, pos,
                                            ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(
                                                    pos)).getBlock().getStateFromMeta(
                                                    ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getMeta())));
                            worldIn.spawnEntity(item);
                        }
                        playerIn.getHeldItem(hand).shrink(1);
                    }

                    ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).setPath(block.getRegistryName().toString());
                    IBlockState blockState = block.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ,
                            playerIn.getHeldItem(hand).getMetadata(), playerIn, hand);
                    ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).setMeta(block.getMetaFromState(blockState));
                    worldIn.notifyNeighborsOfStateChange(pos, block, true);
                    if (!worldIn.isRemote) {
                        worldIn.getMinecraftServer().getPlayerList().sendPacketToAllPlayers(worldIn.getTileEntity(pos).getUpdatePacket());
                    }
                    this.getActualState(state, worldIn, pos);
                    return true;
                }
            }
            if (playerIn.getHeldItem(hand).isEmpty()) {
                if (!playerIn.isCreative()) {
                    if (((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getBlock() != null && !worldIn.isRemote) {
                        EntityItem item = new EntityItem(worldIn);
                        item.setPosition(pos.getX(), pos.getY(), pos.getZ());
                        item.setItem(((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getBlock().getItem(worldIn, pos,
                                ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getBlock().getStateFromMeta(
                                        ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getMeta())));
                        worldIn.spawnEntity(item);
                    }
                }
                ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).setPath("");
                ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).setMeta(0);
                worldIn.notifyNeighborsOfStateChange(pos, this, true);
                if (!worldIn.isRemote) {
                    worldIn.getMinecraftServer().getPlayerList().sendPacketToAllPlayers(worldIn.getTileEntity(pos).getUpdatePacket());
                }
                this.getActualState(state, worldIn, pos);
                return true;
            }
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        super.onBlockClicked(worldIn, pos, playerIn);
        if (!worldIn.isRemote) {
            if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiSuperPowered) {
                if (this.canEditBlock(((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)), playerIn, true)) {
                    if (!playerIn.isCreative()) {
                        if (((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getBlock() != null) {
                            ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getBlock().dropBlockAsItem(worldIn, pos,
                                    ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getBlock().getStateFromMeta(
                                            ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getMeta()), 0);
                        }
                        this.dropBlockAsItem(worldIn, pos, this.getDefaultState(), 0);
                    }
                    worldIn.setBlockToAir(pos);
                    worldIn.removeTileEntity(pos);
                }
            }
        }
    }

    private boolean canEditBlock(TileEntityTakumiSuperPowered tile, EntityPlayer playerIn, boolean showMsg) {
        if (!TakumiConfigCore.ownerLockTakumiBlock) {
            return true;
        }
        boolean flg = playerIn instanceof EntityPlayerMP && ((EntityPlayerMP) playerIn).interactionManager.getGameType() != GameType.ADVENTURE &&
                (tile.isOwner(playerIn) || playerIn.canUseCommand(2, ""));
        if (!flg && showMsg) {
            playerIn.sendStatusMessage(new TextComponentString("§4§l" + TakumiUtils.takumiTranslate("tile.takumiblock.message")), true);
        }
        return flg;
    }

    @Override
    public Vec3d modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3d motion) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiSuperPowered &&
                ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getBlock() != null) {
            try {
                return ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getBlock().modifyAcceleration(worldIn, pos,
                        entityIn, motion);
            } catch (Exception ignored) {
            }
        }
        return super.modifyAcceleration(worldIn, pos, entityIn, motion);
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiSuperPowered &&
                ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getBlock() != null) {
            try {
                ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getBlock().onEntityCollidedWithBlock(worldIn, pos,
                        getBlockStateFromTE(worldIn, pos), entityIn);
            } catch (Exception e) {
                super.onEntityCollidedWithBlock(worldIn, pos, getBlockStateFromTE(worldIn, pos), entityIn);
            }
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
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiSuperPowered &&
                ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getBlock() != null) {
            ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).getBlock().onFallenUpon(worldIn, pos, entityIn,
                    fallDistance);
        } else {
            super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
        }
    }

    @Override
    public void onLanded(World worldIn, Entity entityIn) {
        if (worldIn.getTileEntity(entityIn.getPosition().down()) instanceof TileEntityTakumiSuperPowered) {
            if (((TileEntityTakumiSuperPowered) worldIn.getTileEntity(entityIn.getPosition().down())).getBlock() != null) {
                ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(entityIn.getPosition().down())).getBlock().onLanded(
                        worldIn, entityIn);
            }
        } else {
            super.onLanded(worldIn, entityIn);
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiSuperPowered && placer instanceof EntityPlayer) {
            ((TileEntityTakumiSuperPowered) worldIn.getTileEntity(pos)).setOwner(((EntityPlayer) placer));
        }
    }

    @Override
    public float getSlipperiness(IBlockState state, IBlockAccess world, BlockPos pos,
                                 @Nullable
                                         Entity entity) {
        if (world.getTileEntity(pos) instanceof TileEntityTakumiSuperPowered) {
            if (((TileEntityTakumiSuperPowered) world.getTileEntity(pos)).getBlock() != null) {
                try {
                    return ((TileEntityTakumiSuperPowered) world.getTileEntity(pos)).getBlock().getSlipperiness(getBlockStateFromTE(world, pos), world,
                            pos, entity);
                } catch (Exception ignored) {
                }
            }
        }
        return super.getSlipperiness(state, world, pos, entity);
    }

    @Override
    public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) {
        if (world.getTileEntity(pos) instanceof TileEntityTakumiSuperPowered &&
                ((TileEntityTakumiSuperPowered) world.getTileEntity(pos)).getBlock() != null) {
            return ((TileEntityTakumiSuperPowered) world.getTileEntity(pos)).getBlock().isLadder(getBlockStateFromTE(world, pos), world, pos, entity);
        }
        return super.isLadder(state, world, pos, entity);
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (world.getTileEntity(pos) instanceof TileEntityTakumiSuperPowered &&
                ((TileEntityTakumiSuperPowered) world.getTileEntity(pos)).getBlock() != null) {
            return ((TileEntityTakumiSuperPowered) world.getTileEntity(pos)).getBlock().isNormalCube(getBlockStateFromTE(world, pos), world, pos);
        }
        return super.isNormalCube(state, world, pos);
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        if (world.getTileEntity(pos) instanceof TileEntityTakumiSuperPowered &&
                ((TileEntityTakumiSuperPowered) world.getTileEntity(pos)).getBlock() != null) {
            return getBlockStateFromTE(world, pos).doesSideBlockRendering(world, pos, face);
        } else {
            return false;
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (state.getBlock() == Blocks.AIR) {
            super.breakBlock(worldIn, pos, state);
        }
    }
}
