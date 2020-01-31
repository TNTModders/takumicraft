package com.tntmodders.takumi.potion;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.potion.Potion;

public class PotionAntiSwelling extends Potion {
    public PotionAntiSwelling() {
        super(false, 0x00ff00);
        this.setRegistryName(TakumiCraftCore.MODID, "antiswelling");
        this.setPotionName("antiswelling");
    }
}
