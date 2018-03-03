package com.tntmodders.takumi.world.biome;

import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.world.gen.TakumiWorldGenLiquids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Post;

import java.util.Random;

public class BiomeTakumiHotSpringMountains extends AbstractBiomeTakumiWorld {

    public BiomeTakumiHotSpringMountains() {
        super("takumihotspringmountains",
                AbstractBiomeTakumiWorld.getBaseProperty("takumihotspringmountains").setHeightVariation(0.4f)
                                        .setTemperature(0f).setSnowEnabled());
    }

    @Override
    public BiomeDecorator createBiomeDecorator() {
        return getModdedBiomeDecorator(new BiomeDecorator() {
            @Override
            protected void genDecorations(Biome biomeIn, World worldIn, Random random) {
                /*if (TerrainGen.decorate(worldIn, random, chunkPos, EventType.LAKE_WATER)) */
                {
                    for (int k5 = 0; k5 < 1000; ++k5) {
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
                MinecraftForge.EVENT_BUS.post(new Post(worldIn, random, chunkPos));
            }
        });
    }

    @Override
    public TempCategory getTempCategory() {
        return TempCategory.COLD;
    }
}
