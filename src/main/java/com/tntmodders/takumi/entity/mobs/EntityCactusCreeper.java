package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

public class EntityCactusCreeper extends EntityTakumiAbstractCreeper {

    public EntityCactusCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        event.getAffectedBlocks().forEach(pos -> {
            if (!this.world.isAirBlock(pos)) {
                this.world.setBlockState(pos, Blocks.SAND.getDefaultState());
            }
        });
        event.getAffectedBlocks().forEach(pos -> {
            if (this.world.isAirBlock(pos) && Blocks.CACTUS.canPlaceBlockAt(this.world, pos)) {
                this.world.setBlockState(pos, Blocks.CACTUS.getDefaultState());
            }
        });
        event.getAffectedBlocks().clear();
        return true;
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        super.collideWithEntity(entityIn);
        if (!this.world.isRemote) {
            entityIn.attackEntityFrom(DamageSource.CACTUS, 0.5f);
        }
    }

    @Override
    public void takumiExplode() {
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.GRASS;
    }

    @Override
    public int getExplosionPower() {
        return 5;
    }

    @Override
    public int getSecondaryColor() {
        return 12303206;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "cactuscreeper";
    }

    @Override
    public int getRegisterID() {
        return 62;
    }

    @Override
    public int getPrimaryColor() {
        return 0x006600;
    }
}
