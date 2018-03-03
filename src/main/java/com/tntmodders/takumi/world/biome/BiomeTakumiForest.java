package com.tntmodders.takumi.world.biome;

public class BiomeTakumiForest extends AbstractBiomeTakumiWorld {

    public BiomeTakumiForest() {
        super("takumiforest", AbstractBiomeTakumiWorld.getBaseProperty("takumiforest"));
        this.decorator.treesPerChunk = 3;
    }
}
