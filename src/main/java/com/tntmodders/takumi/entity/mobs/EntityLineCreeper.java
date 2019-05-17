package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.world.World;

public class EntityLineCreeper extends EntityTakumiAbstractCreeper {

    public EntityLineCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            for (int y = -20; y <= 20; y++) {
                for (int i = -20; i <= 20; i++) {
                    this.world.createExplosion(this,
                            this.posX + Math.cos(Math.toRadians(this.rotationYawHead + 90)) * i, this.posY + y,
                            this.posZ + Math.sin(Math.toRadians(this.rotationYawHead + 90)) * i,
                            this.getPowered() ? 3.5f : 1.75f, true);
                }
            }
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
        return 3;
    }

    @Override
    public int getSecondaryColor() {
        return 0xff5500;
    }

    @Override
    public int getPrimaryColor() {
        return 0x00ff88;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "linecreeper";
    }

    @Override
    public int getRegisterID() {
        return 284;
    }
}
