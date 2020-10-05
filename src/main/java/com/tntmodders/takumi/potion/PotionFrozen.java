package com.tntmodders.takumi.potion;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiPacketCore;
import com.tntmodders.takumi.core.TakumiPotionCore;
import com.tntmodders.takumi.network.MessageFrozenEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class PotionFrozen extends Potion {
    public PotionFrozen() {
        super(true, 0x000044);
        this.setRegistryName(TakumiCraftCore.MODID, "frozen");
        this.setPotionName("frozen");
        this.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "7107DE5E-7CE8-4030-940E-514C1F160891", -10D, 0);
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

    @Override
    public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier) {
        super.performEffect(entityLivingBaseIn, amplifier);
        if (!entityLivingBaseIn.world.isRemote && (entityLivingBaseIn.getActivePotionEffect(TakumiPotionCore.FROZEN).getDuration() == 1 || entityLivingBaseIn.isBurning())) {
            entityLivingBaseIn.removePotionEffect(TakumiPotionCore.FROZEN);
            PotionEffect effect = new PotionEffect(TakumiPotionCore.FROZEN, 200, 0, true, false);
            TakumiPacketCore.INSTANCE.sendToAllTracking(new MessageFrozenEffect(entityLivingBaseIn.getEntityId(), effect, true), entityLivingBaseIn);
            entityLivingBaseIn.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1f, 1f);
        }
    }
}
