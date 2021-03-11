package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.entity.EntityTakumiLightningBolt;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockMagicBlock extends Block {
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D);

    public BlockMagicBlock() {
        super(Material.IRON);
        this.setRegistryName(TakumiCraftCore.MODID, "magicblock");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("magicblock");
        this.setHarvestLevel("pickaxe", 3);
        this.setHardness(5f);
        this.setResistance(10000000f);
        this.setLightLevel(1f);
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
        if (/*!worldIn.isRemote && */entityIn.ticksExisted % 20 == 0 && !entityIn.isDead) {
            EntityTakumiLightningBolt bolt = new EntityTakumiLightningBolt(worldIn, pos.getX(), pos.getY() + 0.5, pos.getZ(), false);
            worldIn.addWeatherEffect(bolt);
            worldIn.spawnEntity(bolt);
        }
    }
}
