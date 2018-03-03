package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.client.render.RenderInversionCreeper;
import com.tntmodders.takumi.core.TakumiPotionCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

public class EntityInversionCreeper extends EntityTakumiAbstractCreeper {

    public EntityInversionCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        event.getAffectedEntities().forEach(entity -> {
            if (entity instanceof EntityLivingBase) {
                ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(TakumiPotionCore.INVERSION, 600));
                ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 590));
            }
        });
        return true;
    }

    @Override
    public int getPrimaryColor() {
        return 0x003300;
    }

    @Override
    public Object getRender(RenderManager manager) {
        return new RenderInversionCreeper<>(manager);
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
        return EnumTakumiType.NORMAL_M;
    }

    @Override
    public int getExplosionPower() {
        return 3;
    }

    @Override
    public int getSecondaryColor() {
        return 0x00aa00;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "inversioncreeper";
    }

    @Override
    public int getRegisterID() {
        return 46;
    }
}
