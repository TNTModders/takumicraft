package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.world.World;

public class EntityDaimonjiCreeper extends EntityTakumiAbstractCreeper {

    public EntityDaimonjiCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            for (int tz = 1; tz <= 3; tz++) {
                this.world.createExplosion(this, this.posX, this.posY, this.posZ + tz, this.getPowered() ? 1f : 0.6f, true);
            }
            for (int tx = -4; tx <= 4; tx++) {
                this.world.createExplosion(this, this.posX + tx, this.posY, this.posZ, this.getPowered() ? 1f : 0.6f, true);
            }
            for (int t = 1; t <= 5; t++) {
                this.world.createExplosion(this, this.posX - t, this.posY, this.posZ - t, this.getPowered() ? 1f : 0.6f, true);
            }
            for (int u = 1; u <= 5; u++) {
                this.world.createExplosion(this, this.posX + u, this.posY, this.posZ - u, this.getPowered() ? 1f : 0.6f, true);
            }
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
        return 0x00ff00;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "daimonjicreeper";
    }

    @Override
    public int getRegisterID() {
        return 82;
    }

    @Override
    public int getPrimaryColor() {
        return 0x222222;
    }
}
