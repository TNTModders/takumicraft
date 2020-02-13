package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.world.World;

public class EntityTossCreeper extends EntityTakumiAbstractCreeper {

    public EntityTossCreeper(World worldIn) {
        super(worldIn);
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
        return EnumTakumiType.FIRE_D;
    }

    @Override
    public int getExplosionPower() {
        return 3;
    }

    @Override
    public int getSecondaryColor() {
        return 0xff9900;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "tosscreeper";
    }

    @Override
    public int getRegisterID() {
        return 0;
    }
}
