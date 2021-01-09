package com.tntmodders.takumi.potion;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.potion.Potion;

public class PotionCorruption extends Potion {
    public PotionCorruption() {
        super(true, 0x002200);
        this.setRegistryName(TakumiCraftCore.MODID, "corruption");
        this.setPotionName("corruption");
    }
}
