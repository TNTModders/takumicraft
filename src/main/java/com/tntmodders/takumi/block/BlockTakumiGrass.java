package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.core.TakumiWorldCore;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDirt.DirtType;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.List;
import java.util.Random;

public class BlockTakumiGrass extends BlockGrass {

    public BlockTakumiGrass() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "takumigrass");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("takumigrass");
        this.setHarvestLevel("shovel", 2);
        this.setHardness(1f);
        this.setResistance(10000000f);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote) {
            if (worldIn.provider.getDimensionType().getId() == TakumiWorldCore.TAKUMI_WORLD.getId()) {
                if (worldIn.loadedEntityList.size() < 500 && rand.nextInt(200) == 0) {
                    List<Biome.SpawnListEntry> list = worldIn.getBiome(pos).getSpawnableList(EnumCreatureType.MONSTER);
                    if (!list.isEmpty()) {
                        Biome.SpawnListEntry entry = list.get(rand.nextInt(list.size()));
                        try {
                            int max = 1 + rand.nextInt(10);
                            for (int i = 0; i < max; i++) {
                                Entity entity = entry.newInstance(worldIn);
                                entity.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
                                if (entity instanceof EntityMob && ((EntityMob) entity).getCanSpawnHere()) {
                                    ((EntityMob) entity).onInitialSpawn(worldIn.getDifficultyForLocation(pos), null);
                                    worldIn.spawnEntity(entity);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (worldIn.getLightFromNeighbors(pos.up()) < 4 &&
                    worldIn.getBlockState(pos.up()).getLightOpacity(worldIn, pos.up()) > 2) {
                worldIn.setBlockState(pos, TakumiBlockCore.TAKUMI_DIRT.getDefaultState());
            } else {
                if (worldIn.getLightFromNeighbors(pos.up()) >= 9) {
                    for (int i = 0; i < 4; ++i) {
                        BlockPos blockpos = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);

                        if (blockpos.getY() >= 0 && blockpos.getY() < 256 && !worldIn.isBlockLoaded(blockpos)) {
                            return;
                        }

                        IBlockState iblockstate = worldIn.getBlockState(blockpos.up());
                        IBlockState iblockstate1 = worldIn.getBlockState(blockpos);

                        if (iblockstate1.getBlock() == TakumiBlockCore.TAKUMI_DIRT &&
                                iblockstate1.getValue(BlockDirt.VARIANT) == DirtType.DIRT &&
                                worldIn.getLightFromNeighbors(blockpos.up()) >= 4 &&
                                iblockstate.getLightOpacity(worldIn, pos.up()) <= 2) {
                            worldIn.setBlockState(blockpos, TakumiBlockCore.TAKUMI_GRASS.getDefaultState());
                        }
                    }
                }
            }
        }
    }

    /**
     * Get the Item that this Block should drop when harvested.
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return TakumiBlockCore.TAKUMI_DIRT.getItemDropped(
                TakumiBlockCore.TAKUMI_DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, DirtType.DIRT), rand,
                fortune);
    }
}
