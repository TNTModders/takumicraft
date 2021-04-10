package com.tntmodders.takumi.block;

import com.tntmodders.takumi.entity.mobs.EntityGunoreCreeper;
import com.tntmodders.takumi.tileentity.TileEntityTakumiSuperPowered;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockTakumiDummyGunOre extends BlockAbstractTakumiBomb {

    public BlockTakumiDummyGunOre() {
        super("dummy_gunore", 10f, Material.ROCK, MapColor.STONE);
        this.setHarvestLevel("pickaxe", 1);
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.GUNPOWDER;
    }

    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        if (!worldIn.isRemote && entityIn instanceof EntityPlayer && !(worldIn.getTileEntity(pos) instanceof TileEntityTakumiSuperPowered)) {
            EntityGunoreCreeper creeper = new EntityGunoreCreeper(worldIn);
            creeper.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            worldIn.spawnEntity(creeper);
            creeper.ignite();
            creeper.setAttackTarget(((EntityPlayer) entityIn));
            creeper.setCreeperState(1);
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
    public int quantityDroppedWithBonus(int level, Random random) {
        return 0;
    }

    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
        return 0;
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        return new ItemStack(this.getItemDropped(state, new Random(), 0));
    }

    @Override
    protected boolean canSilkHarvest() {
        return false;
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (!worldIn.isRemote) {
            EntityGunoreCreeper creeper = new EntityGunoreCreeper(worldIn);
            creeper.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            worldIn.spawnEntity(creeper);
        }
    }

    @Override
    float getPower() {
        return 5f;
    }
}
