package com.tntmodders.takumi.potion;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.potion.Potion;

public class PotionCreepered extends Potion {

    public PotionCreepered() {
        super(true, 0x00aa00);
        this.setRegistryName(TakumiCraftCore.MODID, "creepered");
        this.setPotionName("creepered");
    }
}
