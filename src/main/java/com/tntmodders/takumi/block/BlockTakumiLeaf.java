package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockTakumiLeaf extends Block {

    public BlockTakumiLeaf() {
        super(Material.LEAVES);
        this.setRegistryName(TakumiCraftCore.MODID, "takumileaf");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("takumileaf");
    }

  /*  @Override
    public BlockPlanks.EnumType getWoodType(int meta) {
        return null;
    }

    @Override
    public NonNullList<ItemStack> onSheared(ItemStack item, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune) {
        return NonNullList.withSize(1, new ItemStack(this, 1));
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BlockTakumiLeaf.CHECK_DECAY, BlockTakumiLeaf.DECAYABLE);
    }

    public int getMetaFromState(IBlockState state) {
        return 0;
    }*/

    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
        if (!worldIn.isRemote) {
            this.explode(worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
    }

    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (!worldIn.isRemote && player.getHeldItem(EnumHand.MAIN_HAND).getItem() != Items.DIAMOND_PICKAXE) {
            this.explode(worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
    }

    public void explode(World world, int x, int y, int z) {
        world.createExplosion(null, x + 0.5, y + 0.5, z + 0.5, getPower(), true);
    }

    float getPower() {
        return 1.5f;
    }
}
