package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.entity.mobs.EntityCeruleanCreeper;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class BlockTakumiSandStarLow extends Block {

    protected static final AxisAlignedBB AXIS_ALIGNED_BB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D);

    public BlockTakumiSandStarLow() {
        super(Material.SAND, MapColor.CYAN);
        this.setRegistryName(TakumiCraftCore.MODID, "creepersandstarlow");
        if (Item.REGISTRY.containsKey(new ResourceLocation("japaricraftmod", "darksandstar"))) {
            this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        }
        this.setUnlocalizedName("creepersandstarlow");
        this.setResistance(0f);
        this.setHardness(0.5f);
        this.setLightLevel(1f);
    }

    @Deprecated
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Deprecated
    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return AXIS_ALIGNED_BB;
    }

    @Deprecated
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Deprecated
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!this.tryTouchWater(worldIn, pos, state)) {
            super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        }
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!this.tryTouchWater(worldIn, pos, state)) {
            super.onBlockAdded(worldIn, pos, state);
        }
    }

    @Override
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
        if (!worldIn.isRemote && !(explosionIn.getExplosivePlacedBy() instanceof EntityCeruleanCreeper)) {
            worldIn.createExplosion(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3f, true);
        }
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
        if (!worldIn.isRemote && entityIn instanceof EntityLivingBase && !(entityIn instanceof EntityCeruleanCreeper)) {
            worldIn.createExplosion(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3f, true);
        }
    }

    protected boolean tryTouchWater(World worldIn, BlockPos pos, IBlockState state) {
        boolean flag = false;

        for (EnumFacing enumfacing : EnumFacing.values()) {
            if (enumfacing != EnumFacing.DOWN) {
                BlockPos blockpos = pos.offset(enumfacing);

                if (worldIn.getBlockState(blockpos).getMaterial() == Material.WATER) {
                    flag = true;
                    break;
                }
            }
        }

        if (flag) {
            worldIn.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState(), 3);
            worldIn.createExplosion(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, false);
        }
        return flag;
    }
}
