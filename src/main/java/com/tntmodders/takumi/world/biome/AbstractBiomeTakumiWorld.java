package com.tntmodders.takumi.world.biome;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.world.chunk.TakumiWorldChunkGenerator;
import com.tntmodders.takumi.world.gen.TakumiWorldGenBigTree;
import com.tntmodders.takumi.world.gen.TakumiWorldGenTrees;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import java.util.Random;

public abstract class AbstractBiomeTakumiWorld extends Biome {
    
    protected static final TakumiWorldGenTrees TREE_FEATURE = new TakumiWorldGenTrees(true, false, TakumiBlockCore.CREEPER_LOG.getDefaultState(),
            TakumiBlockCore.CREEPER_LEAVES.getDefaultState());
    protected static final TakumiWorldGenBigTree BIG_TREE_FEATURE = new TakumiWorldGenBigTree(true, TakumiBlockCore.CREEPER_LOG.getDefaultState(),
            TakumiBlockCore.CREEPER_LEAVES.getDefaultState());
    
    public AbstractBiomeTakumiWorld(String name, BiomeProperties properties) {
        super(properties);
        this.setRegistryName(TakumiCraftCore.MODID, name);
        this.topBlock = TakumiWorldChunkGenerator.GRASS;
        this.fillerBlock = TakumiWorldChunkGenerator.DIRT;
    }
    
    protected static BiomeProperties getBaseProperty(String name) {
        return new BiomeProperties(name).setWaterColor(0x30ff30).setRainfall(1f).setHeightVariation(0.05f);
    }
    
    @Override
    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        if (this instanceof BiomeTakumiOcean) {
            return super.getRandomTreeFeature(rand);
        }
        return rand.nextInt(5) == 0 ? BIG_TREE_FEATURE : TREE_FEATURE;
    }
}
