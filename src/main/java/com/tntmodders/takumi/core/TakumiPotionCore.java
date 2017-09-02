package com.tntmodders.takumi.core;

import com.tntmodders.takumi.potion.TakumiPotionSubsidence;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraftforge.registries.IForgeRegistry;

public class TakumiPotionCore {
    public static final TakumiPotionSubsidence SUBSIDENCE = new TakumiPotionSubsidence();

    public static void register(IForgeRegistry<Potion> event) {
        event.register(SUBSIDENCE);
    }

    public static void registerPotionType(IForgeRegistry<PotionType> event) {
        PotionType type = new PotionType(SUBSIDENCE.getRegistryName().getResourcePath()
                , new PotionEffect(SUBSIDENCE, 400)).setRegistryName(SUBSIDENCE.getRegistryName());
        event.register(type);
    }
}
