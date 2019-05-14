package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class EntityShockCreeper extends EntityTakumiAbstractCreeper {

    public EntityShockCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 10, 8));
        if (this.getCreeperState() > 0 && this.onGround) {
            this.jump();
        }
        if (this.getAttackTarget() != null &&
                this.getDistanceSq(this.getAttackTarget().posX, this.posY, this.getAttackTarget().posZ) < 16) {
            this.setCreeperState(1);
        }
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        if (!this.world.isRemote) {
            if (this.getCreeperState() > 0) {
                this.world.createExplosion(this, this.posX, this.posY, this.posZ, (this.getPowered() ? 7f : 3.5f),
                        true);

            } else if (distance < 3f) {
                this.world.createExplosion(this, this.posX, this.posY, this.posZ, (this.getPowered() ? 2.5f : 1.25f),
                        false);
            }
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
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
        return 0xf0ff0f;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "shockcreeper";
    }

    @Override
    public int getRegisterID() {
        return 283;
    }
}
