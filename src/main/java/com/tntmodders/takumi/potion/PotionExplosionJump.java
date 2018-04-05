package com.tntmodders.takumi.potion;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.potion.Potion;

public class PotionExplosionJump extends Potion {

    public PotionExplosionJump() {
        super(false, 0x55ff00);
        this.setRegistryName(TakumiCraftCore.MODID, "explosionjump");
        this.setPotionName("explosionjump");
    }
}
