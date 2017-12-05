package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityBoltCreeper extends EntityTakumiAbstractCreeper {
    public EntityBoltCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void onDeath(DamageSource source) {
        if (!this.world.isRemote) {
            this.dropItem(TakumiItemCore.TAKUMI_BOLT_STONE, 1);
        }
        super.onDeath(source);
    }

    @Override
    public void onStruckByLightning(EntityLightningBolt lightningBolt) {
        TakumiUtils.takumiSetPowered(this, true);
    }

    @Override
    public void takumiExplode() {
        for (int i = 0; i < 10 * (this.getPowered() ? 3 : 1); i++) {
            BlockPos pos = this.getPosition().add(MathHelper.nextDouble(this.rand, -5 * (this.getPowered() ? 2 : 1), 5 * (this.getPowered() ? 2 : 1)),
                    MathHelper.nextDouble(this.rand, -5 * (this.getPowered() ? 2 : 1), 5 * (this.getPowered() ? 2 : 1)),
                    MathHelper.nextDouble(this.rand, -5 * (this.getPowered() ? 2 : 1), 5 * (this.getPowered() ? 2 : 1)));
            EntityLightningBolt bolt = new EntityLightningBolt(this.world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, false);
            this.world.addWeatherEffect(bolt);
            this.world.spawnEntity(bolt);
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.HIGH;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.WIND_MD;
    }

    @Override
    public int getExplosionPower() {
        return 7;
    }

    @Override
    public int getSecondaryColor() {
        return 0x8888ff;
    }

    @Override
    public boolean isCustomSpawn() {
        return true;
    }

    @Override
    public String getRegisterName() {
        return "boltcreeper";
    }

    @Override
    public int getRegisterID() {
        return 401;
    }

    @Override
    public void setDead() {
        if (!this.world.isRemote && !this.getPowered() && this.getHealth() > 0) {
            EntityBoltCreeper creeper = new EntityBoltCreeper(this.world);
            creeper.copyLocationAndAnglesFrom(this);
            creeper.onStruckByLightning(null);
            this.world.spawnEntity(creeper);
        }
        super.setDead();
    }

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        if (!damageSrc.isExplosion() && !damageSrc.isFireDamage() && damageSrc != DamageSource.LIGHTNING_BOLT) {
            super.damageEntity(damageSrc, damageAmount);
        }
    }
}
