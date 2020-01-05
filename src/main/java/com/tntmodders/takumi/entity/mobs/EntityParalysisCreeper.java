package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.world.World;

public class EntityParalysisCreeper extends EntityTakumiAbstractCreeper {

    public EntityParalysisCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {

    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.HIGH;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL_MD;
    }

    @Override
    public int getExplosionPower() {
        return 6;
    }

    @Override
    public int getSecondaryColor() {
        return 0xff0f0f;
    }

    @Override
    public int getPrimaryColor() {
        return 0x220000;
    }

    @Override
    public boolean isCustomSpawn() {
        return true;
    }

    @Override
    public String getRegisterName() {
        return "paralysiscreeper";
    }

    @Override
    public int getRegisterID() {
        return 410;
    }
}
