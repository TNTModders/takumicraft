package com.tntmodders.takumi.world.biome;

import com.tntmodders.takumi.world.chunk.TakumiWorldChunkGenerator;

public class BiomeTakumiOcean extends AbstractBiomeTakumiWorld {
    
    public BiomeTakumiOcean() {
        super("takumiocean", AbstractBiomeTakumiWorld.getBaseProperty("takumiocean").setHeightVariation(0.025f).setBaseHeight(-1));
        this.fillerBlock = TakumiWorldChunkGenerator.DIRT;
        this.topBlock = TakumiWorldChunkGenerator.GRASS;
    }
}
