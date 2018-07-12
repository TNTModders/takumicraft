package com.tntmodders.takumi.core;

import com.tntmodders.takumi.potion.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionType;
import net.minecraftforge.registries.IForgeRegistry;

public class TakumiPotionCore {

    public static final PotionSubsidence SUBSIDENCE = new PotionSubsidence();
    public static final Potion INVERSION = new PotionInversion();
    public static final Potion CREEPERED = new PotionCreepered();
    public static final Potion EXP_JUMP = new PotionExplosionJump();
    public static final Potion EP = new PotionEP();

    public static void register(IForgeRegistry<Potion> event) {
        event.register(SUBSIDENCE);
        event.register(INVERSION);
        event.register(CREEPERED);
        event.register(EXP_JUMP);
    }

    public static void registerPotionType(IForgeRegistry<PotionType> event) {
    }
}
