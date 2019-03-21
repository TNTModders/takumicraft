package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityWhiteEyesCreeper extends EntityTakumiAbstractCreeper {

    public EntityWhiteEyesCreeper(World worldIn) {
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
        return 0;
    }

    @Override
    public int getSecondaryColor() {
        return 0xffffff;
    }

    @Override
    public int getPrimaryColor() {
        return 0xffffff;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "whiteeyescreeper";
    }

    @Override
    public int getRegisterID() {
        return 75;
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return null;
    }
}
