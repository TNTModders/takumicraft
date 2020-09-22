package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

public class EntityPoisonCreeper extends EntityTakumiAbstractCreeper {

    private int timer;

    public EntityPoisonCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        if (!this.world.isRemote) {
            event.getAffectedEntities().forEach(entity -> {
                if (entity instanceof EntityLivingBase && !(entity instanceof EntityCreeper)) {
                    ((EntityLivingBase) entity).addPotionEffect(
                            new PotionEffect(MobEffects.POISON, 600, 30));
                }
            });
        }
        event.getAffectedEntities().clear();
        return true;
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
        return EnumTakumiType.WIND;
    }

    @Override
    public int getExplosionPower() {
        return 2;
    }

    @Override
    public int getSecondaryColor() {
        return 0xff00ff;
    }

    @Override
    public int getPrimaryColor() {
        return 0x7777ff;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "poisoncreeper";
    }

    @Override
    public int getRegisterID() {
        return 88;
    }
}
