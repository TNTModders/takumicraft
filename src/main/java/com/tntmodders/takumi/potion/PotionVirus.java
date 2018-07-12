package com.tntmodders.takumi.potion;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.potion.Potion;

public class PotionVirus extends Potion {

    public PotionVirus() {
        super(true, 0x001100);
        this.setRegistryName(TakumiCraftCore.MODID, "creepervirus");
        this.setPotionName("creepervirus");
    }
}
