package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiPotionCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockTakumiStone_EP extends Block {

    public BlockTakumiStone_EP() {
        super(Material.ROCK);
        this.setRegistryName(TakumiCraftCore.MODID, "takumistone_ep");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("takumistone_ep");
        this.setHardness(5f);
        this.setResistance(10000000f);
        this.setHarvestLevel("pickaxe", 2);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(Blocks.COBBLESTONE);
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }

    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        if (!worldIn.isRemote) {
            EntityAreaEffectCloud cloud = new EntityAreaEffectCloud(worldIn);
            cloud.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
            cloud.addEffect(new PotionEffect(TakumiPotionCore.EP, 100));
            cloud.setRadius(2.5f);
            cloud.setRadiusPerTick(-0.2f);
            worldIn.spawnEntity(cloud);
        }
        super.onEntityWalk(worldIn, pos, entityIn);
    }
}
