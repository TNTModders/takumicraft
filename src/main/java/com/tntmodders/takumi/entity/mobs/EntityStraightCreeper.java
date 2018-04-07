package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.world.World;

public class EntityStraightCreeper extends EntityTakumiAbstractCreeper {

    public EntityStraightCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
        try {
            if (this.getAttackTarget() != null) {
                double dx = this.posX - this.getAttackTarget().posX;
                double dz = this.posZ - this.getAttackTarget().posZ;
                if (dx < dz) {
                    for (int x = (int) this.getAttackTarget().posX - 7; x < this.getAttackTarget().posX + 8; x++) {
                        int z = (int) this.getAttackTarget().posZ;
                        int y = (int) this.getAttackTarget().posY;
                        this.world.createExplosion(this, x, y, z, 0.75F * height, true);
                    }
                } else {
                    for (int z = (int) this.getAttackTarget().posZ - 7; z < this.getAttackTarget().posZ + 8; z++) {
                        int x = (int) this.getAttackTarget().posX;
                        int y = (int) this.getAttackTarget().posY;
                        this.world.createExplosion(this, x, y, z, 0.75F * height, true);
                    }
                }
            }
        } catch (Exception e) {
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, (this.getPowered() ? 6 : 3), true);
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
        return 0xff0000;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "straightcreeper";
    }

    @Override
    public int getRegisterID() {
        return 49;
    }

    @Override
    public int getPrimaryColor() {
        return 0x222222;
    }
}
