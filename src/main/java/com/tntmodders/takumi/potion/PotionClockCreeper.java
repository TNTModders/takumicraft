package com.tntmodders.takumi.potion;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class PotionClockCreeper extends Potion {

    public PotionClockCreeper() {
        super(true, 0x7777ff);
        this.setRegistryName(TakumiCraftCore.MODID, "clockcreeper");
        this.setPotionName("clockcreeper");
    }

    @Override
    public boolean shouldRender(PotionEffect effect) {
        return false;
    }

    @Override
    public boolean shouldRenderInvText(PotionEffect effect) {
        return false;
    }

    @Override
    public boolean shouldRenderHUD(PotionEffect effect) {
        return false;
    }
}
