package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

public class EntitySandstormCreeper extends EntityTakumiAbstractCreeper {

    public EntitySandstormCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void onLivingUpdate() {
        if (this.world.isRemote) {
            this.world.spawnParticle(EnumParticleTypes.CRIT,
                    this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width,
                    this.posY + this.rand.nextDouble() * (double) this.height,
                    this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width, 0.0D, 0.0D, 0.0D);
        }
        super.onLivingUpdate();
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        for (BlockPos pos : event.getAffectedBlocks()) {
            event.getWorld().setBlockState(pos, Blocks.SAND.getDefaultState());
        }
        for (BlockPos pos : event.getAffectedBlocks()) {
            for (int i = 0; i < 6; i++) {
                BlockPos newPos = pos.offset(EnumFacing.VALUES[i]);
                if (event.getWorld().getBlockState(newPos).getBlock() != Blocks.SAND &&
                        event.getWorld().getBlockState(newPos).getBlockHardness(world, newPos) != -1 &&
                        (event.getWorld().getBlockState(newPos).getBlock()
                              .getExplosionResistance(world, newPos, event.getExplosion().getExplosivePlacedBy(),
                                      event.getExplosion()) < Blocks.OBSIDIAN.getDefaultState().getBlock()
                                                                             .getExplosionResistance(world, newPos,
                                                                                     event.getExplosion()
                                                                                          .getExplosivePlacedBy(),
                                                                                     event.getExplosion()) ||
                                event.getWorld().isAirBlock(newPos))) {
                    event.getWorld().setBlockState(newPos, TakumiBlockCore.GUNORE.getDefaultState());
                }
            }
        }
        event.getAffectedBlocks().removeAll(event.getAffectedBlocks());
        return true;
    }

    @Override
    public int getPrimaryColor() {
        return 65280;
    }

    @Override
    public void takumiExplode() {
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.GROUND_D;
    }

    @Override
    public int getExplosionPower() {
        return 3;
    }

    @Override
    public int getSecondaryColor() {
        return 6749952;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "sandstormcreeper";
    }

    @Override
    public int getRegisterID() {
        return 208;
    }
}
