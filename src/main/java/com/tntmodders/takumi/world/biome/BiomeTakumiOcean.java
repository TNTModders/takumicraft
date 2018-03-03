package com.tntmodders.takumi.world.biome;

public class BiomeTakumiOcean extends AbstractBiomeTakumiWorld {

    public BiomeTakumiOcean() {
        super("takumiocean",
                AbstractBiomeTakumiWorld.getBaseProperty("takumiocean").setHeightVariation(0.025f).setBaseHeight(-1));
    }
}
