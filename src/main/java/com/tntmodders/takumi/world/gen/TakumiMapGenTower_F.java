package com.tntmodders.takumi.world.gen;

import com.tntmodders.takumi.block.BlockTTIncantation;
import com.tntmodders.takumi.core.TakumiBlockCore;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureStart;

import java.util.Random;

public class TakumiMapGenTower_F extends MapGenStructure {
    /**
     * None
     */
    private int size;
    private int distance;

    public TakumiMapGenTower_F() {
    }

    @Override
    public String getStructureName() {
        return "TakumiTower_F";
    }

    @Override
    public BlockPos getNearestStructurePos(World worldIn, BlockPos pos, boolean findUnexplored) {
        this.world = worldIn;
        return findNearestStructurePosBySpacing(worldIn, this, pos, this.distance, 8, 10387312, false, 100,
                findUnexplored);
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        return chunkX % 250 == 0 && chunkZ % 250 == 0;
    }

    @Override
    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        return new TakumiMapGenTower_F.Start(this.world, this.rand, chunkX, chunkZ, this.size);
    }

    public static class Start extends StructureStart {
        /**
         * well ... thats what it does
         */
        private boolean hasMoreThanTwoComponents;

        public Start() {
            this.boundingBox = new StructureBoundingBox(-100, 0, -100, 100, 256, 100);
        }

        public Start(World worldIn, Random rand, int fx, int fz, int size) {
            super(fx, fz);
            this.boundingBox = new StructureBoundingBox(-100, 0, -100, 100, 256, 100);
            double randY = 160 + worldIn.rand.nextInt(32);
            //general
            for (int x = -24; x <= 24; x++) {
                for (int z = -24; z <= 24; z++) {
                    double dY = randY + worldIn.rand.nextInt(16) * Math.sin(Math.PI / 16 * (x + z));
                    for (int y = 1; y < 256; y++) {
                        if ((x == -8 || x == 8 || x == -24 || x == 24 || z == -8 || z == 8 || z == -24 || z == 24 ||
                                y % 16 == 0 || y == 1) && !(x < -8 && z > -8 && z < 8) &&
                                !(x > 8 && z > -8 && z < 8) && !(z < -8 && x > -8 && x < 8) &&
                                !(z > 8 && x > -8 && x < 8) && (y < dY || y <= 64)) {
                            worldIn.setBlockState(this.generateStructurePos(x, y, z), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                        } else if (!(x < -8 && z > -8 && z < 8) && !(x > 8 && z > -8 && z < 8) &&
                                !(z < -8 && x > -8 && x < 8) && !(z > 8 && x > -8 && x < 8)) {
                            worldIn.setBlockToAir(this.generateStructurePos(x, y, z));
                        }
                    }
                }
            }
            //room setting
            for (int x = -24; x <= 24; x++) {
                for (int z = -24; z <= 24; z++) {
                    //roomY>0
                    for (int y = 1; y < randY; y++) {
                        if (y < randY && y % 16 == 1 && y > 16 && ((x == 0 && z == 0) || (x == 16 && z == 16) || (x == -16 && z == 16) || (x == -16 && z == -16) || (x == 16 && z == -16))) {
                            int randomGen = y > 140 ? worldIn.rand.nextInt(3) + 3 : worldIn.rand.nextInt(10);
                            //type
                            switch (randomGen) {
                                //water
                                case 0: {
                                    for (int dx = -7; dx <= 7; dx++) {
                                        for (int dz = -7; dz <= 7; dz++) {
                                            for (int dy = 0; dy < 15; dy++) {
                                                worldIn.setBlockState(this.generateStructurePos(x + dx, y + dy, z + dz), TakumiBlockCore.TAKUMI_WATER.getDefaultState());
                                            }
                                        }
                                    }
                                    worldIn.setBlockState(this.generateStructurePos(x, y, z),
                                            TakumiBlockCore.TT_INCANTATION.getDefaultState().withProperty(BlockTTIncantation.INCANTATION, BlockTTIncantation.EnumTTIncantationType.WATER));
                                    break;
                                }
                                //lava
                                case 1: {
                                    for (int dx = -7; dx <= 7; dx++) {
                                        for (int dz = -7; dz <= 7; dz++) {
                                            for (int dy = 1; dy < 7; dy++) {
                                                worldIn.setBlockState(this.generateStructurePos(x + dx, y + dy, z + dz), Blocks.LAVA.getDefaultState());
                                            }
                                        }
                                    }
                                    worldIn.setBlockState(this.generateStructurePos(x, y, z),
                                            TakumiBlockCore.TT_INCANTATION.getDefaultState().withProperty(BlockTTIncantation.INCANTATION, BlockTTIncantation.EnumTTIncantationType.LAVA));
                                    break;
                                }
                                //ex
                                case 2: {
                                    worldIn.setBlockState(this.generateStructurePos(x, y, z),
                                            TakumiBlockCore.TT_INCANTATION.getDefaultState().withProperty(BlockTTIncantation.INCANTATION, BlockTTIncantation.EnumTTIncantationType.EX));
                                    break;
                                }
                                //thunder
                                case 3: {
                                    worldIn.setBlockState(this.generateStructurePos(x, y, z),
                                            TakumiBlockCore.TT_INCANTATION.getDefaultState().withProperty(BlockTTIncantation.INCANTATION, BlockTTIncantation.EnumTTIncantationType.THUNDER));
                                    break;
                                }
                                default: {
                                    worldIn.setBlockState(this.generateStructurePos(x, y, z),
                                            TakumiBlockCore.TT_INCANTATION.getDefaultState().withProperty(BlockTTIncantation.INCANTATION, BlockTTIncantation.EnumTTIncantationType.MONSTER));
                                }
                            }

                            //stairs
                            if (y + 15 < randY) {
                                int stairsID = worldIn.rand.nextInt(4);
                                switch (stairsID) {
                                    case 0: {
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 6, y, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 5, y + 1, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 4, y + 1, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 3, y + 2, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 2, y + 2, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 1, y + 3, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x, y + 3, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 1, y + 4, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 2, y + 4, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 3, y + 5, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 4, y + 5, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 5, y + 6, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 6, y + 6, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 7, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 7, z - 6), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 8, z - 5), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 8, z - 4), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 9, z - 3), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 9, z - 2), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 10, z - 1), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 10, z), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 11, z + 1), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 11, z + 2), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 12, z + 3), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 12, z + 4), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 13, z + 5), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 13, z + 6), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 14, z + 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 6, y + 14, z + 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 6, y + 14, z + 6), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 15, z + 7), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 6, y + 15, z + 7), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 15, z + 6), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 6, y + 15, z + 6), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 15, z + 5), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 6, y + 15, z + 5), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 15, z + 4), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 6, y + 15, z + 4), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                                        break;
                                    }
                                    case 1: {
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y, z - 6), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 1, z - 5), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 1, z - 4), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 2, z - 3), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 2, z - 2), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 3, z - 1), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 3, z), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 4, z + 1), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 4, z + 2), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 5, z + 3), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 5, z + 4), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 6, z + 5), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 6, z + 6), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 7, z + 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 6, y + 7, z + 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 5, y + 8, z + 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 4, y + 8, z + 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 3, y + 9, z + 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 2, y + 9, z + 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 1, y + 10, z + 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x, y + 10, z + 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 1, y + 11, z + 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 2, y + 11, z + 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 3, y + 12, z + 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 4, y + 12, z + 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 5, y + 13, z + 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 6, y + 13, z + 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y + 14, z + 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y + 14, z + 6), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 6, y + 14, z + 6), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y + 15, z + 7), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 6, y + 15, z + 7), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y + 15, z + 6), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 6, y + 15, z + 6), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 5, y + 15, z + 7), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 4, y + 15, z + 7), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 5, y + 15, z + 6), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 4, y + 15, z + 6), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                                        break;
                                    }
                                    case 2: {
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y, z + 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y, z + 6), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y + 1, z + 5), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y + 1, z + 4), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y + 2, z + 3), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y + 2, z + 2), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y + 3, z + 1), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y + 3, z), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y + 4, z - 1), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y + 4, z - 2), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y + 5, z - 3), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y + 5, z - 4), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y + 6, z - 5), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y + 6, z - 6), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y + 7, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 6, y + 7, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 5, y + 8, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 4, y + 8, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 3, y + 9, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 2, y + 9, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 1, y + 10, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x, y + 10, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 1, y + 11, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 2, y + 11, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 3, y + 12, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 4, y + 12, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 5, y + 13, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 6, y + 13, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 14, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 14, z - 6), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 6, y + 14, z - 6), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 15, z - 7), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 6, y + 15, z - 7), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 15, z - 6), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 6, y + 15, z - 6), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 5, y + 15, z - 7), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 4, y + 15, z - 7), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 5, y + 15, z - 6), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 4, y + 15, z - 6), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                                        break;
                                    }
                                    case 3: {
                                        worldIn.setBlockState(this.generateStructurePos(x + 7, y, z + 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 6, y, z + 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 5, y + 1, z + 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 4, y + 1, z + 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 3, y + 2, z + 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 2, y + 2, z + 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x + 1, y + 3, z + 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x, y + 3, z + 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 1, y + 4, z + 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 2, y + 4, z + 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 3, y + 5, z + 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 4, y + 5, z + 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 5, y + 6, z + 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 6, y + 6, z + 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y + 7, z + 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y + 7, z + 6), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y + 8, z + 5), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y + 8, z + 4), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y + 9, z + 3), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y + 9, z + 2), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y + 10, z + 1), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y + 10, z), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y + 11, z - 1), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y + 11, z - 2), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y + 12, z - 3), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y + 12, z - 4), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y + 13, z - 5), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y + 13, z - 6), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y + 14, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 6, y + 14, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 6, y + 14, z - 6), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y + 15, z - 7), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 6, y + 15, z - 7), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y + 15, z - 6), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 6, y + 15, z - 6), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y + 15, z - 5), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 6, y + 15, z - 5), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 7, y + 15, z - 4), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                                        worldIn.setBlockState(this.generateStructurePos(x - 6, y + 15, z - 4), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if ((x == 0 && z == 0) || (x == 16 && z == 16) || (x == -16 && z == 16) || (x == -16 && z == -16) || (x == 16 && z == -16)) {
                        int y = 1;
                        //creeper
                        if (x != 0 && z != 0) {
                            for (int dx = -2; dx <= 2; dx++) {
                                for (int dz = -2; dz <= 2; dz++) {
                                    for (int dy = 1; dy <= 2; dy++) {
                                        if (dx != 0) {
                                            worldIn.setBlockState(this.generateStructurePos(x + dx, y + dy, z + dz), TakumiBlockCore.TT_LIGHT.getDefaultState());
                                        }
                                    }
                                    for (int dy = 3; dy <= 8; dy++) {
                                        if (dx != -2 && dx != 2) {
                                            worldIn.setBlockState(this.generateStructurePos(x + dx, y + dy, z + dz), TakumiBlockCore.TT_LIGHT.getDefaultState());
                                        }
                                    }
                                    for (int dy = 9; dy <= 13; dy++) {
                                        if (dx == -2 || dx == 2 || dz == -2 || dz == 2 || dy == 9 || dy == 13) {
                                            worldIn.setBlockState(this.generateStructurePos(x + dx, y + dy, z + dz), TakumiBlockCore.TT_LIGHT.getDefaultState());
                                        }
                                    }
                                }
                            }
                            worldIn.setBlockState(this.generateStructurePos(x - 2, y + 1, z - 1), TakumiBlockCore.TT_ACTIVATOR.getDefaultState());
                            worldIn.setBlockState(this.generateStructurePos(x - 2, y + 1, z), TakumiBlockCore.TT_ACTIVATOR.getDefaultState());
                            worldIn.setBlockState(this.generateStructurePos(x - 2, y + 1, z + 1), TakumiBlockCore.TT_ACTIVATOR.getDefaultState());
                            worldIn.setBlockState(this.generateStructurePos(x + 2, y + 1, z - 1), TakumiBlockCore.TT_ACTIVATOR.getDefaultState());
                            worldIn.setBlockState(this.generateStructurePos(x + 2, y + 1, z), TakumiBlockCore.TT_ACTIVATOR.getDefaultState());
                            worldIn.setBlockState(this.generateStructurePos(x + 2, y + 1, z + 1), TakumiBlockCore.TT_ACTIVATOR.getDefaultState());

                            worldIn.setBlockState(this.generateStructurePos(x - 2, y + 9, z - 1), TakumiBlockCore.TT_ACTIVATOR.getDefaultState());
                            worldIn.setBlockState(this.generateStructurePos(x - 2, y + 9, z + 1), TakumiBlockCore.TT_ACTIVATOR.getDefaultState());
                            worldIn.setBlockState(this.generateStructurePos(x - 2, y + 10, z - 1), TakumiBlockCore.TT_ACTIVATOR.getDefaultState());
                            worldIn.setBlockState(this.generateStructurePos(x - 2, y + 10, z), TakumiBlockCore.TT_ACTIVATOR.getDefaultState());
                            worldIn.setBlockState(this.generateStructurePos(x - 2, y + 10, z + 1), TakumiBlockCore.TT_ACTIVATOR.getDefaultState());
                            worldIn.setBlockState(this.generateStructurePos(x - 2, y + 11, z), TakumiBlockCore.TT_ACTIVATOR.getDefaultState());
                            worldIn.setBlockState(this.generateStructurePos(x - 2, y + 12, z - 1), TakumiBlockCore.TT_ACTIVATOR.getDefaultState());
                            worldIn.setBlockState(this.generateStructurePos(x - 2, y + 12, z + 1), TakumiBlockCore.TT_ACTIVATOR.getDefaultState());

                            worldIn.setBlockState(this.generateStructurePos(x - 1, y + 10, z), TakumiBlockCore.TT_CREEPERCORE.getDefaultState());
                            for (int dy = 1; dy < 13; dy++) {
                                worldIn.setBlockToAir(this.generateStructurePos(x, y + dy, z));
                            }
                        }
                        //stairs
                        worldIn.setBlockState(this.generateStructurePos(x - 7, y, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x - 6, y, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x - 5, y + 1, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x - 4, y + 1, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x - 3, y + 2, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x - 2, y + 2, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x - 1, y + 3, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x, y + 3, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x + 1, y + 4, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x + 2, y + 4, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x + 3, y + 5, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x + 4, y + 5, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x + 5, y + 6, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x + 6, y + 6, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 7, z - 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 7, z - 6), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 8, z - 5), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 8, z - 4), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 9, z - 3), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 9, z - 2), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 10, z - 1), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 10, z), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 11, z + 1), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 11, z + 2), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 12, z + 3), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 12, z + 4), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 13, z + 5), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 13, z + 6), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 14, z + 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x + 6, y + 14, z + 7), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x + 6, y + 14, z + 6), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 15, z + 7), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x + 6, y + 15, z + 7), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 15, z + 6), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x + 6, y + 15, z + 6), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 15, z + 5), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x + 6, y + 15, z + 5), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x + 7, y + 15, z + 4), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                        worldIn.setBlockState(this.generateStructurePos(x + 6, y + 15, z + 4), TakumiBlockCore.TT_BARRIAR.getDefaultState());
                    }
                }
            }
            //portal
            for (int x = -2; x <= 2; x++) {
                for (int z = -2; z <= 2; z++) {
                    worldIn.setBlockState(this.generateStructurePos(x, 2, z), TakumiBlockCore.TT_LIGHT.getDefaultState());
                }
            }
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    worldIn.setBlockState(this.generateStructurePos(x, 2, z), TakumiBlockCore.TT_PORTAL.getDefaultState());
                }
            }

            //old-entrance
            worldIn.setBlockState(this.generateStructurePos(-7, 2, -1), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
            worldIn.setBlockState(this.generateStructurePos(-7, 2, 0), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
            worldIn.setBlockState(this.generateStructurePos(-7, 2, 1), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
            worldIn.setBlockState(this.generateStructurePos(-9, 3, -2), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
            worldIn.setBlockState(this.generateStructurePos(-7, 3, -2), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
            worldIn.setBlockState(this.generateStructurePos(-8, 3, -1), TakumiBlockCore.TT_CURSE.getDefaultState());
            worldIn.setBlockState(this.generateStructurePos(-8, 3, 0), TakumiBlockCore.TT_CURSE.getDefaultState());
            worldIn.setBlockState(this.generateStructurePos(-8, 3, 1), TakumiBlockCore.TT_CURSE.getDefaultState());
            worldIn.setBlockState(this.generateStructurePos(-7, 3, 2), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
            worldIn.setBlockState(this.generateStructurePos(-9, 3, 2), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
            worldIn.setBlockState(this.generateStructurePos(-9, 4, -2), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
            worldIn.setBlockState(this.generateStructurePos(-7, 4, -2), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
            worldIn.setBlockState(this.generateStructurePos(-8, 4, -1), TakumiBlockCore.TT_CURSE.getDefaultState());
            worldIn.setBlockState(this.generateStructurePos(-8, 4, 0), TakumiBlockCore.TT_CURSE.getDefaultState());
            worldIn.setBlockState(this.generateStructurePos(-8, 4, 1), TakumiBlockCore.TT_CURSE.getDefaultState());
            worldIn.setBlockState(this.generateStructurePos(-7, 4, 2), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
            worldIn.setBlockState(this.generateStructurePos(-9, 4, 2), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
            worldIn.setBlockState(this.generateStructurePos(-9, 5, -2), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
            worldIn.setBlockState(this.generateStructurePos(-7, 5, -2), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
            worldIn.setBlockState(this.generateStructurePos(-8, 5, -1), TakumiBlockCore.TT_CURSE.getDefaultState());
            worldIn.setBlockState(this.generateStructurePos(-8, 5, 0), TakumiBlockCore.TT_CURSE.getDefaultState());
            worldIn.setBlockState(this.generateStructurePos(-8, 5, 1), TakumiBlockCore.TT_CURSE.getDefaultState());
            worldIn.setBlockState(this.generateStructurePos(-7, 5, 2), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
            worldIn.setBlockState(this.generateStructurePos(-9, 5, 2), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
            worldIn.setBlockState(this.generateStructurePos(-9, 2, -1), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
            worldIn.setBlockState(this.generateStructurePos(-9, 2, 0), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
            worldIn.setBlockState(this.generateStructurePos(-9, 2, 1), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
            worldIn.setBlockState(this.generateStructurePos(-9, 6, -1), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
            worldIn.setBlockState(this.generateStructurePos(-9, 6, 0), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
            worldIn.setBlockState(this.generateStructurePos(-9, 6, 1), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
            worldIn.setBlockState(this.generateStructurePos(-7, 6, -1), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
            worldIn.setBlockState(this.generateStructurePos(-7, 6, 0), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
            worldIn.setBlockState(this.generateStructurePos(-7, 6, 1), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
        }

        private BlockPos generateStructurePos(int x, int y, int z) {
            return new BlockPos(this.getChunkPosX() * 16 + x + 8, y, this.getChunkPosZ() * 16 + z + 8);
        }

        @Override
        public boolean isSizeableStructure() {
            return false;
        }
    }
}