package com.tntmodders.takumi.core;

import com.tntmodders.takumi.potion.TakumiPotionSubsidence;
import net.minecraft.potion.Potion;
import net.minecraftforge.registries.IForgeRegistry;

public class TakumiPotionCore {
    public static final TakumiPotionSubsidence SUBSIDENCE = new TakumiPotionSubsidence();

    public static void register(IForgeRegistry<Potion> event) {
        event.register(SUBSIDENCE);
    }
}
