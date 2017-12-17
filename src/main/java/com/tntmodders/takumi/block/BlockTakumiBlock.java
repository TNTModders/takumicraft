package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.tileentity.TileEntityTakumiBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class BlockTakumiBlock extends BlockContainer {
    
    public BlockTakumiBlock() {
        super(Material.BARRIER); this.setRegistryName(TakumiCraftCore.MODID, "takumiblock"); this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("takumiblock"); this.setLightLevel(1f); this.setLightOpacity(255); this.setBlockUnbreakable();
        this.setResistance(10000000);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public boolean isTranslucent(IBlockState state) {
        return true;
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiBlock && ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock() != null) {
            IBlockState blockState = ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock().getActualState(((TileEntityTakumiBlock)
                    worldIn.getTileEntity(pos)).getBlock().getStateFromMeta(((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getMeta()),
                    worldIn, pos).getActualState(worldIn, pos);
            ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).setMeta(((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock()
                    .getMetaFromState(blockState));
            ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).state = blockState; return blockState;
        } return super.getActualState(state, worldIn, pos);
    }
    
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiBlock && ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock() != null) {
            this.getActualState(state, worldIn, pos);
            IBlockState blockState = ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock().getStateFromMeta(((TileEntityTakumiBlock)
                    worldIn.getTileEntity(pos)).getMeta());
            return blockState.getBoundingBox(worldIn, pos);
        } return super.getBoundingBox(state, worldIn, pos);
    }
    
    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List <AxisAlignedBB> collidingBoxes,
            @Nullable Entity entityIn, boolean p_185477_7_) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiBlock && ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock() != null) {
            this.getActualState(state, worldIn, pos);
            IBlockState blockState = ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock().getStateFromMeta(((TileEntityTakumiBlock)
                    worldIn.getTileEntity(pos)).getMeta());
            try {
                blockState.addCollisionBoxToList(worldIn, pos, entityBox, collidingBoxes, entityIn, p_185477_7_);
            } catch (Exception e) {
                //e.printStackTrace();
            } return;
        } super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, p_185477_7_);
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiBlock && ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock() != null) {
            this.getActualState(state, worldIn, pos);
            IBlockState blockState = ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock().getStateFromMeta(((TileEntityTakumiBlock)
                    worldIn.getTileEntity(pos)).getMeta());
            return blockState.getCollisionBoundingBox(worldIn, pos);
        } return super.getCollisionBoundingBox(state, worldIn, pos);
    }
    
    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiBlock && ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock() != null) {
            IBlockState blockState = ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock().getStateFromMeta(((TileEntityTakumiBlock)
                    worldIn.getTileEntity(pos)).getMeta());
            return blockState.getSelectedBoundingBox(worldIn, pos);
        } return super.getSelectedBoundingBox(state, worldIn, pos);
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
    
    @Override
    public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiBlock && ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock() != null) {
            IBlockState state = ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock().getStateFromMeta(((TileEntityTakumiBlock) worldIn
                    .getTileEntity(pos)).getMeta());
            return state.collisionRayTrace(worldIn, pos, start, end);
        } return super.collisionRayTrace(blockState, worldIn, pos, start, end);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float
            hitX, float hitY, float hitZ) {
        if (playerIn.getHeldItem(hand).getItem() instanceof ItemBlock && playerIn.getHeldItem(hand).getItem() != Item.getItemFromBlock(this)) {
            Block block = ((ItemBlock) playerIn.getHeldItem(hand).getItem()).getBlock();
            if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiBlock && block.getRenderType(block.getDefaultState()) == EnumBlockRenderType
                    .MODEL) {
                if (!playerIn.isCreative()) {
                    if (((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock() != null && !worldIn.isRemote) {
                        EntityItem item = new EntityItem(worldIn); item.setPosition(pos.getX(), pos.getY(), pos.getZ());
                        item.setItem(((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock().getItem(worldIn, pos, ((TileEntityTakumiBlock)
                                worldIn.getTileEntity(pos)).getBlock().getStateFromMeta(((TileEntityTakumiBlock) worldIn.getTileEntity(pos))
                                .getMeta())));
                        worldIn.spawnEntity(item);
                    } playerIn.getHeldItem(hand).shrink(1);
                }
                
                ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).setPath(block.getRegistryName().toString());
                IBlockState blockState = block.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, playerIn.getHeldItem(hand).getMetadata
                        (), playerIn, hand);
                ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).setMeta(block.getMetaFromState(blockState));
                worldIn.notifyNeighborsOfStateChange(pos, block, true); this.getActualState(state, worldIn, pos); return true;
            }
        } if (playerIn.getHeldItem(hand).isEmpty() && worldIn.getTileEntity(pos) instanceof TileEntityTakumiBlock) {
            if (!playerIn.isCreative()) {
                if (((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock() != null && !worldIn.isRemote) {
                    EntityItem item = new EntityItem(worldIn); item.setPosition(pos.getX(), pos.getY(), pos.getZ());
                    item.setItem(((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock().getItem(worldIn, pos, ((TileEntityTakumiBlock)
                            worldIn.getTileEntity(pos)).getBlock().getStateFromMeta(((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getMeta())));
                    worldIn.spawnEntity(item);
                }
            } ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).setPath(""); ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).setMeta(0);
            worldIn.notifyNeighborsOfStateChange(pos, this, true); this.getActualState(state, worldIn, pos); return true;
        } return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }
    
    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        super.onBlockClicked(worldIn, pos, playerIn);
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiBlock && !playerIn.isCreative() && !worldIn.isRemote) {
            if (((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock() != null) {
                ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock().dropBlockAsItem(worldIn, pos, ((TileEntityTakumiBlock) worldIn
                        .getTileEntity(pos)).getBlock().getStateFromMeta(((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getMeta()), 0);
            } this.dropBlockAsItem(worldIn, pos, this.getDefaultState(), 0);
        } worldIn.setBlockToAir(pos);
    }
    
    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        return false;
    }
    
    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(world, pos, neighbor); this.getActualState(world.getBlockState(pos), world, pos);
    }
    
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta,
            EntityLivingBase placer, EnumHand hand) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiBlock && ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock() != null) {
            IBlockState state = ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock().getStateForPlacement(worldIn, pos, facing, hitX,
                    hitY, hitZ, meta, placer, hand);
            ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).setMeta(((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock()
                    .getMetaFromState(state));
        } return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
    }
    
    @Override
    public boolean canBeConnectedTo(IBlockAccess worldIn, BlockPos pos, EnumFacing facing) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiBlock && ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock() != null) {
            return ((TileEntityTakumiBlock) worldIn.getTileEntity(pos)).getBlock().canBeConnectedTo(worldIn, pos, facing);
        } return super.canBeConnectedTo(worldIn, pos, facing);
    }
    
    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityTakumiBlock();
    }
}
