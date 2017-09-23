package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

public class EntityFrostCreeper extends EntityTakumiAbstractCreeper {
    public EntityFrostCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void onLivingUpdate() {
        if (this.world.isRemote) {
            for (int i = 0; i < 2; ++i) {
                this.world.spawnParticle(EnumParticleTypes.SNOW_SHOVEL, this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width, this.posY + this.rand.nextDouble() * (double) this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width, 0.0D, 0.0D, 0.0D);
            }
        }
        super.onLivingUpdate();
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        for (BlockPos pos : event.getAffectedBlocks()) {
            event.getWorld().setBlockState(pos, Blocks.WATER.getDefaultState());
        }
        for (BlockPos pos : event.getAffectedBlocks()) {
            for (int i = 0; i < 6; i++) {
                BlockPos newPos = pos.offset(EnumFacing.VALUES[i]);
                if (event.getWorld().getBlockState(newPos).getMaterial() != Material.WATER && event.getWorld().getBlockState(newPos).getBlockHardness(world, newPos) != -1 &&
                        (event.getWorld().getBlockState(newPos).getBlock().getExplosionResistance(world, newPos, event.getExplosion().getExplosivePlacedBy(), event.getExplosion())
                                < Blocks.OBSIDIAN.getDefaultState().getBlock().getExplosionResistance(world, newPos, event.getExplosion().getExplosivePlacedBy(), event.getExplosion())
                                || event.getWorld().isAirBlock(newPos))) {
                    event.getWorld().setBlockState(newPos, Blocks.PACKED_ICE.getDefaultState());
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
        return EnumTakumiType.WATER_D;
    }

    @Override
    public int getExplosionPower() {
        return 3;
    }

    @Override
    public int getSecondaryColor() {
        return 5592575;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "frostcreeper";
    }

    @Override
    public int getRegisterID() {
        return 206;
    }
}
