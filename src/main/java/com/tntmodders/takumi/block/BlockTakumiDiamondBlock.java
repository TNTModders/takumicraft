package com.tntmodders.takumi.block;

import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.entity.mobs.EntitySuperDiamondCreeper;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockTakumiDiamondBlock extends BlockAbstractTakumiBomb {
    private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D);

    public BlockTakumiDiamondBlock() {
        super("takumidiamondblock", 5f, Material.IRON, MapColor.CYAN);
        this.setLightLevel(1f);
        this.setResistance(10000000f);
    }

    @Override
    float getPower() {
        return 5f;
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return AABB;
    }

    /**
     * Called When an Entity Collided with the Block
     */
    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (!worldIn.isRemote) {
            if (!(entityIn instanceof EntityItem &&
                    (((EntityItem) entityIn).getItem().getItem() == TakumiItemCore.TAKUMI_DIAMOND ||
                            ((EntityItem) entityIn).getItem().getItem() == Item.getItemFromBlock(TakumiBlockCore.TAKUMI_DIAMOND_BLOCK))) &&
                    !entityIn.isSneaking() && !(entityIn instanceof EntitySuperDiamondCreeper)) {
                worldIn.setBlockToAir(pos);
                this.explode(worldIn, pos.getX(), pos.getY(), pos.getZ());
            }
            if (entityIn instanceof EntityFallingBlock && worldIn.rand.nextInt(10) == 0) {
                entityIn.setDead();
            }
        }
    }

    @Override
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
    }

    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
        return 7;
    }
}
