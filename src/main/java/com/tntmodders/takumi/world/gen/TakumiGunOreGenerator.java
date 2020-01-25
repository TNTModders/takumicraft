package com.tntmodders.takumi.world.gen;

import com.tntmodders.takumi.block.BlockTakumiOres;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.world.chunk.TakumiWorldChunkGenerator;
import com.tntmodders.takumi.world.provider.TakumiWorldProvider;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TakumiGunOreGenerator implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
            IChunkProvider chunkProvider) {
        if (world.provider instanceof WorldProviderSurface) {
            this.generateOre(world, random, chunkX << 4, chunkZ << 4);
        } else if (world.provider instanceof TakumiWorldProvider) {
            this.generateOreTakumi(world, random, chunkX << 4, chunkZ << 4);
        }
    }

    private void generateOre(World world, Random random, int x, int z) {
        //1チャンクで生成したい回数だけ繰り返す。
        if (world.provider instanceof WorldProviderSurface) {
            for (int i = 0; i < 10; i++) {
                int genX = x + random.nextInt(16);
                int genY = 1 + random.nextInt(64);
                int genZ = z + random.nextInt(16);
                new WorldGenMinable(random.nextInt(50) == 0 ? TakumiBlockCore.DUMMY_GUNORE.getDefaultState() :
                        TakumiBlockCore.GUNORE.getDefaultState(), 25).generate(world, random,
                        new BlockPos(genX, genY, genZ));
            }
        }
    }

    private void generateOreTakumi(World world, Random random, int x, int z) {
        //1チャンクで生成したい回数だけ繰り返す。
        if (world.provider instanceof TakumiWorldProvider) {
            for (int i = 0; i < 40; i++) {
                int genX = x + random.nextInt(16);
                int genY = 1 + random.nextInt(255);
                int genZ = z + random.nextInt(16);
                IBlockState iBlockState;
                List<BlockTakumiOres> blockTakumiOres = new ArrayList<>();
                Block.REGISTRY.forEach(block -> {
                    if (block instanceof BlockTakumiOres) {
                        if (genY < 24 || block != TakumiBlockCore.TAKUMI_ORE_MAGIC) {
                            blockTakumiOres.add((BlockTakumiOres) block);
                        }
                    }
                });
                iBlockState = blockTakumiOres.get(random.nextInt(blockTakumiOres.size())).getDefaultState();
                new WorldGenMinable(iBlockState, 25, input -> input != null &&
                        input.getBlock() == TakumiWorldChunkGenerator.STONE.getBlock()).generate(world, random,
                        new BlockPos(genX, genY, genZ));
            }
        }
    }
}
