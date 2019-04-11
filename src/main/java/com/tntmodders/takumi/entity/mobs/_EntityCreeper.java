package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.world.World;

public class _EntityCreeper extends EntityTakumiAbstractCreeper {

    public _EntityCreeper(World worldIn) {
        super(worldIn);
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
        return EnumTakumiType.NORMAL;
    }

    @Override
    public int getExplosionPower() {
        return 3;
    }

    @Override
    public int getSecondaryColor() {
        return 0x00ff00;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "";
    }

    @Override
    public int getRegisterID() {
        return 0;
    }
}
