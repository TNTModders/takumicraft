package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.core.TakumiEntityCore;
import com.tntmodders.takumi.entity.ITakumiEntity;
import com.tntmodders.takumi.entity.ITakumiEntity.EnumTakumiRank;
import com.tntmodders.takumi.entity.item.EntityAlterDummy;
import com.tntmodders.takumi.entity.mobs.EntityAnnivCreeper;
import com.tntmodders.takumi.entity.mobs.boss.EntityKingCreeper;
import com.tntmodders.takumi.entity.mobs.boss.EntityWitherCreeper;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMaterialMatcher;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class BlockTakumiAltar extends Block {

    private BlockPattern witherBasePattern;
    private BlockPattern witherPattern;

    public BlockTakumiAltar() {
        super(Material.TNT, MapColor.GREEN);
        this.setRegistryName(TakumiCraftCore.MODID, "takumialtar");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("takumialtar");
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
                                    EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        boolean flg = super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
        Entity entity = null;
        if (canSpawnWither(worldIn, pos)) {
            spawnWither(worldIn, pos);
            return true;
        } else if (worldIn.getBlockState(pos.down()).getBlock() == TakumiBlockCore.CREEPER_BOMB) {
            entity = new EntityKingCreeper(worldIn);
        } else if (worldIn.getBlockState(pos.up()).getBlock() == Blocks.CAKE) {
            entity = new EntityAnnivCreeper(worldIn);
        } else {
            List<ITakumiEntity> entities = new ArrayList<>();
            TakumiEntityCore.getEntityList().forEach(iTakumiEntity -> {
                if (iTakumiEntity.takumiRank() == EnumTakumiRank.HIGH) {
                    entities.add(iTakumiEntity);
                }
            });
            if (!entities.isEmpty()) {
                entities.removeIf(iTakumiEntity -> iTakumiEntity instanceof EntityAnnivCreeper);
                try {
                    entity = (Entity) entities.get(worldIn.rand.nextInt(entities.size())).getClass().getConstructor(
                            World.class).newInstance(worldIn);
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        if (entity != null) {
            entity.setPosition(pos.getX(), pos.getY(), pos.getZ());
            worldIn.setBlockToAir(pos);
            if (worldIn.getBlockState(pos.down()).getBlockHardness(worldIn, pos.down()) > 0) {
                worldIn.setBlockToAir(pos.down());
            }
            if (!worldIn.isRemote) {
                worldIn.createExplosion(new EntityAlterDummy(worldIn), pos.getX() + 0.5, pos.getY() + 0.5,
                        pos.getZ() + 0.5, 3f, true);
                worldIn.loadedEntityList.removeIf(entity1 -> entity1 instanceof EntityAlterDummy);
                if (worldIn.spawnEntity(entity)) {
                    return true;
                }
            }
        }
        return flg;
    }

    private void spawnWither(World worldIn, BlockPos pos) {
        BlockPattern blockpattern = this.getWitherPattern();
        BlockPattern.PatternHelper blockpattern$patternhelper = blockpattern.match(worldIn, pos);
        for (int j = 0; j < blockpattern.getPalmLength(); ++j) {
            for (int k = 0; k < blockpattern.getThumbLength(); ++k) {
                BlockWorldState blockworldstate1 = blockpattern$patternhelper.translateOffset(j, k, 0);
                worldIn.setBlockState(blockworldstate1.getPos(), Blocks.AIR.getDefaultState(), 2);
            }
        }

        BlockPos blockpos = blockpattern$patternhelper.translateOffset(1, 0, 0).getPos();
        EntityWitherCreeper witherCreeper = new EntityWitherCreeper(worldIn);
        BlockPos blockpos1 = blockpattern$patternhelper.translateOffset(1, 2, 0).getPos();
        witherCreeper.setLocationAndAngles((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.55D, (double) blockpos1.getZ() + 0.5D, blockpattern$patternhelper.getForwards().getAxis() == EnumFacing.Axis.X ? 0.0F : 90.0F, 0.0F);
        witherCreeper.renderYawOffset = blockpattern$patternhelper.getForwards().getAxis() == EnumFacing.Axis.X ? 0.0F : 90.0F;
        witherCreeper.ignite();

        worldIn.spawnEntity(witherCreeper);

        for (int l = 0; l < 120; ++l) {
            worldIn.spawnParticle(EnumParticleTypes.SNOWBALL, (double) blockpos.getX() + worldIn.rand.nextDouble(), (double) (blockpos.getY() - 2) + worldIn.rand.nextDouble() * 3.9D, (double) blockpos.getZ() + worldIn.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
        }

        for (int i1 = 0; i1 < blockpattern.getPalmLength(); ++i1) {
            for (int j1 = 0; j1 < blockpattern.getThumbLength(); ++j1) {
                BlockWorldState blockworldstate2 = blockpattern$patternhelper.translateOffset(i1, j1, 0);
                worldIn.notifyNeighborsRespectDebug(blockworldstate2.getPos(), Blocks.AIR, false);
            }
        }
    }

    private boolean canSpawnWither(World worldIn, BlockPos pos) {
        BlockPattern blockpattern = this.getWitherPattern();
        BlockPattern.PatternHelper blockpattern$patternhelper = blockpattern.match(worldIn, pos);
        return blockpattern$patternhelper != null;
    }

    protected BlockPattern getWitherPattern() {
        if (this.witherPattern == null) {
            this.witherPattern = FactoryBlockPattern.start().aisle("aaa", "###", "~#~")
                    .where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.SOUL_SAND)))
                    .where('a', BlockWorldState.hasState(BlockStateMatcher.forBlock(TakumiBlockCore.CREEPER_ALTAR)))
                    .where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).build();
        }

        return this.witherPattern;
    }
}
