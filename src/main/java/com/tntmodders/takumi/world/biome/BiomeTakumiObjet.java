package com.tntmodders.takumi.world.biome;

import com.tntmodders.takumi.world.gen.WorldGenTakumiObjet;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import java.util.Random;

public class BiomeTakumiObjet extends AbstractBiomeTakumiWorld {

    protected static final WorldGenTakumiObjet OBJET = new WorldGenTakumiObjet(true);

    public BiomeTakumiObjet() {
        super("takumiobjet", AbstractBiomeTakumiWorld.getBaseProperty("takumiobjet"));
        this.decorator.treesPerChunk = 10;
    }

    @Override
    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        return OBJET;
    }
}
