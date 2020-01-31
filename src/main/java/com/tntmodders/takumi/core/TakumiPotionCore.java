package com.tntmodders.takumi.core;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.potion.*;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraftforge.registries.IForgeRegistry;

public class TakumiPotionCore {

    public static final PotionSubsidence SUBSIDENCE = new PotionSubsidence();
    public static final Potion INVERSION = new PotionInversion();
    public static final Potion CREEPERED = new PotionCreepered();
    public static final Potion EXP_JUMP = new PotionExplosionJump();
    public static final Potion EP = new PotionEP();
    public static final Potion VIRUS = new PotionVirus();
    public static final Potion CLOCK = new PotionClockCreeper();
    public static final Potion ANTI_EXPLOSION = new PotionAntiExplosion();
    public static final Potion ANTI_SWELLING = new PotionAntiSwelling();

    public static final PotionType EXPLOSION = new PotionType(new PotionEffect(ANTI_EXPLOSION, 3600))
            .setRegistryName(TakumiCraftCore.MODID, "antiexplosion");
    public static final PotionType SWELLING = new PotionType(new PotionEffect(ANTI_SWELLING, 600))
            .setRegistryName(TakumiCraftCore.MODID, "antiswelling");
    public static final PotionType CREEPER = new PotionType().setRegistryName(TakumiCraftCore.MODID, "creeper");

    public static void register(IForgeRegistry<Potion> event) {
        event.register(SUBSIDENCE);
        event.register(INVERSION);
        event.register(CREEPERED);
        event.register(EXP_JUMP);
        event.register(EP);
        event.register(VIRUS);
        event.register(CLOCK);
        event.register(ANTI_EXPLOSION);
        event.register(ANTI_SWELLING);
    }

    public static void registerPotionType(IForgeRegistry<PotionType> event) {
        event.register(CREEPER);
        event.register(EXPLOSION);
        event.register(SWELLING);

        PotionHelper.addMix(PotionTypes.THICK, Ingredient.fromStacks(new ItemStack(TakumiItemCore.TAKUMI_TYPE_CORE, 1, 5)), CREEPER);
        PotionHelper.addMix(CREEPER, TakumiItemCore.TAKUMI_TYPE_CORE_DEST, EXPLOSION);
        PotionHelper.addMix(CREEPER, TakumiItemCore.TAKUMI_TYPE_CORE_MAGIC, SWELLING);
    }
}
