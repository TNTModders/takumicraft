package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.world.World;

public class EntityRoundCreeper extends EntityTakumiAbstractCreeper {

    public EntityRoundCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
        float r = this.getPowered() ? 4 : 2.5f;
        int z;
        for (int x = -40; x <= 40; x++) {
            z = (int) Math.sqrt(40 * 40 - x * x);
            this.world
                    .createExplosion(this, this.posX + x, this.world.getHeight(this.getPosition().add(x, 0, z)).getY(),
                            this.posZ + z, r, true);
            this.world
                    .createExplosion(this, this.posX + x, this.world.getHeight(this.getPosition().add(x, 0, -z)).getY(),
                            this.posZ - z, r, true);
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.GRASS;
    }

    @Override
    public int getExplosionPower() {
        return 3;
    }

    @Override
    public int getSecondaryColor() {
        return 0x66ff22;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "roundcreeper";
    }

    @Override
    public int getRegisterID() {
        return 243;
    }
}
