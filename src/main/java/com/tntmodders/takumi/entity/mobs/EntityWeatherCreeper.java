package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.world.World;

public class EntityWeatherCreeper extends EntityTakumiAbstractCreeper {
    public EntityWeatherCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
        try {
            this.world.getWorldInfo().setRaining(true);
            this.world.getWorldInfo().setRainTime(12000);
            this.world.getWorldInfo().setThundering(true);
            this.world.getWorldInfo().setThunderTime(12000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.WIND_M;
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
        return "weathercreeper";
    }

    @Override
    public int getRegisterID() {
        return 36;
    }

    @Override
    public int getPrimaryColor() {
        return 0x000099;
    }
}
