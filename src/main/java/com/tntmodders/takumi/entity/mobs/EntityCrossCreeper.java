package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.world.World;

public class EntityCrossCreeper extends EntityTakumiAbstractCreeper {

    public EntityCrossCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
        for (int tx = -5; tx <= 5; tx++) {
            this.world.createExplosion(this, this.posX + tx, this.posY, this.posZ, 0.75F * height, true);
        }
        for (int tz = -5; tz <= 5; tz++) {
            this.world.createExplosion(this, this.posX, this.posY, this.posZ + tz, 0.75F * height, true);
        }
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
        return 0x0000ff;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "crosscreeper";
    }

    @Override
    public int getRegisterID() {
        return 50;
    }

    @Override
    public int getPrimaryColor() {
        return 0x222222;
    }
}
