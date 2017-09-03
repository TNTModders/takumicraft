package com.tntmodders.takumi.world.gen;

import com.tntmodders.takumi.core.TakumiBlockCore;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class TakumiGunOreGenerator implements IWorldGenerator {
    private void generateOre(World world, Random random, int x, int z) {
        //1チャンクで生成したい回数だけ繰り返す。
        if (world.provider instanceof WorldProviderSurface) {
            for (int i = 0; i < 10; i++) {
                int genX = x + random.nextInt(16);
                int genY = 1 + random.nextInt(64);
                int genZ = z + random.nextInt(16);
                new WorldGenMinable(random.nextInt(50) == 0 ?
                        TakumiBlockCore.DUMMY_GUNORE.getDefaultState() :
                        TakumiBlockCore.GUNORE.getDefaultState(), 25).generate(world, random, new BlockPos(genX, genY, genZ));
            }
        }
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (world.provider instanceof WorldProviderSurface) {
            this.generateOre(world, random, chunkX << 4, chunkZ << 4);
        }
    }
}
