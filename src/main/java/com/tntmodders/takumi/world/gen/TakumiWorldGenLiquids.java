package com.tntmodders.takumi.world.gen;


import com.tntmodders.takumi.world.chunk.TakumiWorldChunkGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class TakumiWorldGenLiquids extends WorldGenerator {
    
    private final Block block;
    
    public TakumiWorldGenLiquids(Block blockIn) {
        this.block = blockIn;
    }
    
    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        if (worldIn.getBlockState(position.up()).getBlock() != TakumiWorldChunkGenerator.STONE.getBlock()) {
            return false;
        }
        if (worldIn.getBlockState(position.down()).getBlock() != TakumiWorldChunkGenerator.STONE.getBlock()) {
            return false;
        }
        IBlockState iblockstate = worldIn.getBlockState(position);
        
        if (!iblockstate.getBlock().isAir(iblockstate, worldIn, position) && iblockstate.getBlock() != TakumiWorldChunkGenerator.STONE.getBlock()) {
            return false;
        }
        int i = 0;
        
        if (worldIn.getBlockState(position.west()).getBlock() == TakumiWorldChunkGenerator.STONE.getBlock()) {
            ++i;
        }
        
        if (worldIn.getBlockState(position.east()).getBlock() == TakumiWorldChunkGenerator.STONE.getBlock()) {
            ++i;
        }
        
        if (worldIn.getBlockState(position.north()).getBlock() == TakumiWorldChunkGenerator.STONE.getBlock()) {
            ++i;
        }
        
        if (worldIn.getBlockState(position.south()).getBlock() == TakumiWorldChunkGenerator.STONE.getBlock()) {
            ++i;
        }
        
        int j = 0;
        
        if (worldIn.isAirBlock(position.west())) {
            ++j;
        }
        
        if (worldIn.isAirBlock(position.east())) {
            ++j;
        }
        
        if (worldIn.isAirBlock(position.north())) {
            ++j;
        }
        
        if (worldIn.isAirBlock(position.south())) {
            ++j;
        }
        
        if (i == 3 && j == 1) {
            IBlockState iblockstate1 = this.block.getDefaultState();
            worldIn.setBlockState(position, iblockstate1, 2);
            worldIn.immediateBlockTick(position, iblockstate1, rand);
        }
        
        return true;
    }
}