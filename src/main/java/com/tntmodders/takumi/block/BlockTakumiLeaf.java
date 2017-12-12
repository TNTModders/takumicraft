package com.tntmodders.takumi.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockTakumiLeaf extends BlockAbstractTakumiBomb {
    
    public BlockTakumiLeaf() {
        super("takumileaf", 0.1f, Material.LEAVES, MapColor.GRASS);
    }
    
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }
    
    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return true;
    }
    
    @Override
    float getPower() {
        return 1.5f;
    }
}
