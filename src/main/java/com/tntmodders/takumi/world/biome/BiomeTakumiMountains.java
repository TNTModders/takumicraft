package com.tntmodders.takumi.world.biome;

import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.world.chunk.TakumiWorldChunkGenerator;
import com.tntmodders.takumi.world.gen.TakumiWorldGenLiquids;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Post;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.Random;

public class BiomeTakumiMountains extends AbstractBiomeTakumiWorld {
    
    private boolean isLava;
    
    public BiomeTakumiMountains(boolean isLava) {
        this("takumi" + (isLava ? "lavamountains" : "mountains"));
        this.isLava = isLava;
        if (isLava) {
            this.topBlock = TakumiWorldChunkGenerator.MAGMA;
            this.fillerBlock = TakumiWorldChunkGenerator.DIRT;
        }
    }
    
    private BiomeTakumiMountains(String name) {
        super(name, AbstractBiomeTakumiWorld.getBaseProperty(name).setRainfall(0f).setHeightVariation(0.4f).setTemperature(2f));
    }
    
    public BiomeTakumiMountains() {
        this("takumimountains");
    }
    
    @Override
    public BiomeDecorator createBiomeDecorator() {
        return getModdedBiomeDecorator(new BiomeDecorator() {
            @Override
            protected void genDecorations(Biome biomeIn, World worldIn, Random random) {
                super.genDecorations(biomeIn, worldIn, random);
                if (this.generateFalls) {
                    if (TerrainGen.decorate(worldIn, random, chunkPos, EventType.LAKE_WATER)) {
                        for (int k5 = 0; k5 < 100; ++k5) {
                            int i10 = random.nextInt(16) + 8;
                            int l13 = random.nextInt(16) + 8;
                            int i17 = random.nextInt(248) + 8;
                            
                            if (i17 > 0) {
                                int k19 = random.nextInt(i17);
                                BlockPos blockpos6 = this.chunkPos.add(i10, k19, l13);
                                new TakumiWorldGenLiquids(TakumiBlockCore.HOT_SPRING).generate(worldIn, random, blockpos6);
                            }
                        }
                    }
                    
                    if (TerrainGen.decorate(worldIn, random, chunkPos, EventType.LAKE_LAVA) || isLava) {
                        for (int l5 = 0; l5 < 100; ++l5) {
                            int j10 = random.nextInt(16) + 8;
                            int i14 = random.nextInt(16) + 8;
                            int j17 = random.nextInt(random.nextInt(random.nextInt(240) + 8) + 8);
                            BlockPos blockpos3 = this.chunkPos.add(j10, j17, i14);
                            new TakumiWorldGenLiquids(Blocks.FLOWING_LAVA).generate(worldIn, random, blockpos3);
                        }
                    }
                }
                MinecraftForge.EVENT_BUS.post(new Post(worldIn, random, chunkPos));
            }
        });
    }
    
    @Override
    public TempCategory getTempCategory() {
        return TempCategory.WARM;
    }
}
