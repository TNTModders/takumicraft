package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.entity.mobs.EntityGunoreCreeper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockTakumiDummyGunOre extends Block {

    public BlockTakumiDummyGunOre() {
        super(Material.ROCK);
        this.setRegistryName(TakumiCraftCore.MODID, "dummy_gunore");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("dummy_gunore");
        this.setHardness(5f);
        this.setResistance(0f);
        this.setHarvestLevel("pickaxe", 1);
    }

    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
        if (!worldIn.isRemote) {
            this.explode(worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
    }

    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (!worldIn.isRemote) {
            EntityGunoreCreeper creeper = new EntityGunoreCreeper(worldIn);
            creeper.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            worldIn.spawnEntity(creeper);
        }
    }

    public void explode(World world, int x, int y, int z) {
        world.createExplosion(null, x + 0.5, y + 0.5, z + 0.5, getPower(), true);
    }

    float getPower() {
        return 1.5f;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.GUNPOWDER;
    }

    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
        return 0;
    }

    // 幸運でドロップする量の設定。(幸運で掘った時にドロップする量をランダムにできる)
    @Override
    public int quantityDroppedWithBonus(int level, Random random) {
        return 0;
    }

    public int quantityDropped(Random random) {
        return 0;
    }

    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return true;
    }
}
