package com.tntmodders.takumi.potion;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.potion.Potion;

public class PotionAntiExplosion extends Potion {
    public PotionAntiExplosion() {
        super(false, 0x005500);
        this.setRegistryName(TakumiCraftCore.MODID, "antiexplosion");
        this.setPotionName("antiexplosion");
    }
}
