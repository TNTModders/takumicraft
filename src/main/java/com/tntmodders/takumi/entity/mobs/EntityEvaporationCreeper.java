package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

import java.util.ArrayList;
import java.util.List;

public class EntityEvaporationCreeper extends EntityTakumiAbstractCreeper {

    public EntityEvaporationCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public int getPrimaryColor() {
        return 0xddddff;
    }

    @Override
    public void takumiExplode() {
        this.buildsand((int) this.posX, (int) this.posY, (int) this.posZ, this.getPowered() ? 10 : 5, this.world);
    }

    protected void buildsand(int ox, int oy, int oz, int height, World par1World) {
        int blockX, blockY, blockZ;
        int searchRange = height * 2;
        for (int x = -1 * searchRange; x < searchRange; ++x) {
            blockX = x + ox;
            for (int y = (int) (-0.5 * searchRange); y < (int) (0.5 * searchRange); ++y) {
                blockY = y + oy;
                for (int z = -1 * searchRange; z < searchRange; ++z) {
                    blockZ = z + oz;

                    BlockPos pos = new BlockPos(blockX, blockY, blockZ);
                    if (par1World.getBlockState(pos).getMaterial().isLiquid()) {
                        if (!this.world.isRemote) {
                            par1World.createExplosion(this, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1f, false);
                            TakumiUtils.setBlockStateProtected(world, pos, Blocks.AIR.getDefaultState());
                        } else {
                            par1World.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, pos.getX() + this.rand.nextDouble(),
                                    pos.getY() + this.rand.nextDouble(), pos.getZ() + this.rand.nextDouble(), 0, 0, 0);
                            par1World.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS,
                                    0.5f, 0.7f, true);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        List<Entity> entityList = new ArrayList<>();
        event.getAffectedEntities().forEach(entity -> {
            if (entity.isInWater() || entity.isInLava()) {
                entity.attackEntityFrom(DamageSource.causeMobDamage(this).setExplosion(), 7f);
            } else {
                entityList.add(entity);
            }
        });
        event.getAffectedEntities().removeAll(entityList);
        return true;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.world.isRemote && this.ticksExisted % 20 == 0 && this.isInWater()) {
            this.attackEntityFrom(DamageSource.DROWN, 1f);
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.WIND_D;
    }

    @Override
    public int getExplosionPower() {
        return 2;
    }

    @Override
    public int getSecondaryColor() {
        return 0x000033;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "evaporationcreeper";
    }

    @Override
    public int getRegisterID() {
        return 90;
    }
}
