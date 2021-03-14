package com.tntmodders.takumi.potion;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumParticleTypes;

public class PotionNightmare extends Potion {
    public PotionNightmare() {
        super(true, 0x440044);
        this.setRegistryName(TakumiCraftCore.MODID, "nightmare");
        this.setPotionName("nightmare");
    }

    @Override
    public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier) {
        super.performEffect(entityLivingBaseIn, amplifier);
        if (entityLivingBaseIn instanceof EntityPlayer && entityLivingBaseIn.world.isRemote) {
            entityLivingBaseIn.world.spawnParticle(EnumParticleTypes.MOB_APPEARANCE, entityLivingBaseIn.posX, entityLivingBaseIn.posY, entityLivingBaseIn.posZ, 0, 0, 0);
            if (entityLivingBaseIn.ticksExisted % 20 == 0) {
                entityLivingBaseIn.playSound(SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE, 0.5f, 1f);
            }
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return duration > 10;
    }
}
