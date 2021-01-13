package com.tntmodders.takumi.world.gen;

import com.tntmodders.takumi.core.TakumiBlockCore;
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
        return chunkX == 0 && chunkZ == 0;
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
            for (int x = -24; x <= 24; x++) {
                for (int z = -24; z <= 24; z++) {
                    double dY = randY + worldIn.rand.nextInt(16) * Math.sin(Math.PI / 16 * (x + z));
                    for (int y = 0; y < 256; y++) {
                        if ((x == -8 || x == 8 || x == -24 || x == 24 || z == -8 || z == 8 || z == -24 || z == 24 ||
                                y % 16 == 0 || y == 255) && !(x < -8 && z > -8 && z < 8) &&
                                !(x > 8 && z > -8 && z < 8) && !(z < -8 && x > -8 && x < 8) &&
                                !(z > 8 && x > -8 && x < 8) && (y < dY || y <= 64)) {
                            worldIn.setBlockState(new BlockPos(x, y, z), TakumiBlockCore.TT_FOUNDATION.getDefaultState());
                        } else if (!(x < -8 && z > -8 && z < 8) && !(x > 8 && z > -8 && z < 8) &&
                                !(z < -8 && x > -8 && x < 8) && !(z > 8 && x > -8 && x < 8)) {
                            worldIn.setBlockToAir(new BlockPos(x, y, z));
                        }
                    }
                }
            }
        }

        @Override
        public boolean isSizeableStructure() {
            return false;
        }
    }
}