package com.tntmodders.takumi.world.biome;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.core.TakumiEntityCore;
import com.tntmodders.takumi.entity.ITakumiEntity;
import com.tntmodders.takumi.entity.ITakumiEntity.EnumTakumiRank;
import com.tntmodders.takumi.entity.mobs.EntityBatCreeper;
import com.tntmodders.takumi.entity.mobs.EntitySeaGuardianCreeper;
import com.tntmodders.takumi.entity.mobs.EntitySquidCreeper;
import com.tntmodders.takumi.world.chunk.TakumiWorldChunkGenerator;
import com.tntmodders.takumi.world.gen.TakumiWorldGenBigTree;
import com.tntmodders.takumi.world.gen.TakumiWorldGenTrees;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSand.EnumType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class AbstractBiomeTakumiWorld extends Biome {

    protected static final TakumiWorldGenTrees TREE_FEATURE =
            new TakumiWorldGenTrees(true, false, TakumiBlockCore.CREEPER_LOG.getDefaultState(),
                    TakumiBlockCore.CREEPER_LEAVES.getDefaultState());
    protected static final TakumiWorldGenBigTree BIG_TREE_FEATURE =
            new TakumiWorldGenBigTree(true, TakumiBlockCore.CREEPER_LOG.getDefaultState(),
                    TakumiBlockCore.CREEPER_LEAVES.getDefaultState());

    public AbstractBiomeTakumiWorld(String name, BiomeProperties properties) {
        super(properties);
        this.setRegistryName(TakumiCraftCore.MODID, name);
        this.topBlock = TakumiWorldChunkGenerator.GRASS;
        this.fillerBlock = TakumiWorldChunkGenerator.DIRT;
        List<SpawnListEntry> entryList = new ArrayList<>();
        entryList.add(new SpawnListEntry(EntitySquidCreeper.class, 2, 1, 1));
        entryList.add(new SpawnListEntry(EntitySeaGuardianCreeper.class, 1, 1, 1));
        this.modSpawnableLists.put(TakumiEntityCore.WATER_TAKUMI, entryList);
        entryList.clear();
        TakumiEntityCore.CLASS_LIST.forEach(clazz -> {
            try {
                ITakumiEntity iTakumiEntity = clazz.getConstructor(World.class).newInstance((World) null);
                if (!(iTakumiEntity instanceof EntitySquidCreeper ||
                        iTakumiEntity instanceof EntitySeaGuardianCreeper) &&
                        (iTakumiEntity.takumiRank() == EnumTakumiRank.LOW ||
                                iTakumiEntity.takumiRank() == EnumTakumiRank.MID)) {
                    int count = iTakumiEntity instanceof EntityBatCreeper ? 1 : 20;
                    entryList.add(new SpawnListEntry(((EntityLiving) iTakumiEntity).getClass(), 10, count, count+1));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        this.spawnableMonsterList.clear();
        this.spawnableMonsterList.addAll(entryList);
        this.spawnableCreatureList.addAll(entryList);
        this.modSpawnableLists.put(EnumCreatureType.MONSTER, entryList);
        this.modSpawnableLists.put(EnumCreatureType.CREATURE, entryList);
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

    @Override
    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
        int i = worldIn.getSeaLevel();
        IBlockState iblockstate = this.topBlock;
        IBlockState iblockstate1 = this.fillerBlock;
        int j = -1;
        int k = (int) (noiseVal / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
        int l = x & 15;
        int i1 = z & 15;
        MutableBlockPos blockpos$mutableblockpos = new MutableBlockPos();

        for (int j1 = 255; j1 >= 0; --j1) {
            if (j1 <= rand.nextInt(5)) {
                chunkPrimerIn.setBlockState(i1, j1, l, BEDROCK);
            } else {
                IBlockState iblockstate2 = chunkPrimerIn.getBlockState(i1, j1, l);

                if (iblockstate2.getMaterial() == Material.AIR) {
                    j = -1;
                } else if (iblockstate2.getBlock() == TakumiWorldChunkGenerator.STONE.getBlock()) {
                    if (j == -1) {
                        if (k <= 0) {
                            iblockstate = AIR;
                            iblockstate1 = TakumiWorldChunkGenerator.STONE;
                        } else if (j1 >= i - 4 && j1 <= i + 1) {
                            iblockstate = this.topBlock;
                            iblockstate1 = this.fillerBlock;
                        }

                        if (j1 < i && (iblockstate == null || iblockstate.getMaterial() == Material.AIR)) {
                            if (this.getFloatTemperature(blockpos$mutableblockpos.setPos(x, j1, z)) < 0.15F) {
                                iblockstate = TakumiWorldChunkGenerator.ICE;
                            } else {
                                iblockstate = TakumiWorldChunkGenerator.WATER;
                            }
                        }

                        j = k;

                        if (j1 >= i - 1) {
                            chunkPrimerIn.setBlockState(i1, j1, l, iblockstate);
                        } else if (j1 < i - 7 - k) {
                            iblockstate = AIR;
                            iblockstate1 = TakumiWorldChunkGenerator.STONE;
                            chunkPrimerIn.setBlockState(i1, j1, l, TakumiWorldChunkGenerator.STONE);
                        } else {
                            chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);
                        }
                    } else if (j > 0) {
                        --j;
                        chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);

                        if (j == 0 && iblockstate1.getBlock() == Blocks.SAND && k > 1) {
                            j = rand.nextInt(4) + Math.max(0, j1 - 63);
                            iblockstate1 =
                                    iblockstate1.getValue(BlockSand.VARIANT) == EnumType.RED_SAND ? RED_SANDSTONE :
                                            SANDSTONE;
                        }
                    }
                }
            }
        }
    }

    @Override
    public float getSpawningChance() {
        return 0.1f;
    }
}
