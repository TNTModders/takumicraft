package com.tntmodders.takumi.core;

import com.tntmodders.takumi.potion.PotionCreepered;
import com.tntmodders.takumi.potion.PotionExplosionJump;
import com.tntmodders.takumi.potion.PotionInversion;
import com.tntmodders.takumi.potion.TakumiPotionSubsidence;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraftforge.registries.IForgeRegistry;

public class TakumiPotionCore {

    public static final TakumiPotionSubsidence SUBSIDENCE = new TakumiPotionSubsidence();
    public static final Potion INVERSION = new PotionInversion();
    public static final Potion CREEPERED = new PotionCreepered();
    public static final Potion EXP_JUMP = new PotionExplosionJump();

    public static void register(IForgeRegistry<Potion> event) {
        event.register(SUBSIDENCE);
        event.register(INVERSION);
        event.register(CREEPERED);
        event.register(EXP_JUMP);
    }

    public static void registerPotionType(IForgeRegistry<PotionType> event) {
        PotionType type = new PotionType(SUBSIDENCE.getRegistryName().getResourcePath(),
                new PotionEffect(SUBSIDENCE, 400)).setRegistryName(SUBSIDENCE.getRegistryName());
        event.register(type);
        type = new PotionType(INVERSION.getRegistryName().getResourcePath(),
                new PotionEffect(INVERSION, 400)).setRegistryName(INVERSION.getRegistryName());
        event.register(type);
        type = new PotionType(CREEPERED.getRegistryName().getResourcePath(),
                new PotionEffect(CREEPERED, 30)).setRegistryName(CREEPERED.getRegistryName());
        event.register(type);
        type = new PotionType(EXP_JUMP.getRegistryName().getResourcePath(),
                new PotionEffect(EXP_JUMP, 400)).setRegistryName(EXP_JUMP.getRegistryName());
        event.register(type);
    }
}
