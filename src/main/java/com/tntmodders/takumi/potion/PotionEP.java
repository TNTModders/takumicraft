package com.tntmodders.takumi.potion;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.potion.Potion;

public class PotionEP extends Potion {

    public PotionEP() {
        super(true, 0x001100);
        this.setRegistryName(TakumiCraftCore.MODID, "ep");
        this.setPotionName("ep");
    }
}
