package com.tntmodders.takumi.potion;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.potion.Potion;

public class PotionInversion extends Potion {
    
    public PotionInversion() {
        super(true, 0x00ff00);
        this.setRegistryName(TakumiCraftCore.MODID, "inversion");
        this.setPotionName("inversion");
    }
}
