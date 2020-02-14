package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

public class EntitySlipCreeper extends EntityTakumiAbstractCreeper {

    public EntitySlipCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
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
        return 0x222200;
    }

    @Override
    public int getPrimaryColor() {
        return 0x885522;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "slipcreeper";
    }

    @Override
    public int getRegisterID() {
        return 302;
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        event.getAffectedEntities().forEach(entity -> {
            if(entity instanceof EntityLivingBase){
                entity.setFire(10);
                ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.POISON,200));
                ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.WITHER,200));
                ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.HUNGER,200,127));
            }
        });
        event.getAffectedEntities().clear();
        return true;
    }
}
