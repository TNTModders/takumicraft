package com.tntmodders.takumi.core;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.enchantment.EnchantmentTakumiAntiPowered;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.registries.IForgeRegistry;

import java.lang.reflect.Field;

public class TakumiEnchantmentCore {
    public static final Enchantment ANTI_POWERED = new EnchantmentTakumiAntiPowered();

    public static void register(IForgeRegistry<Enchantment> registry) {
        Class clazz = TakumiEnchantmentCore.class;
        for (Field field : clazz.getFields()) {
            try {
                if (field.get(TakumiItemCore.INSTANCE) instanceof Enchantment) {
                    Enchantment enchantment = ((Enchantment) field.get(TakumiItemCore.INSTANCE));
                    registry.register(enchantment);
                    TakumiCraftCore.LOGGER.info("Registered Enchantment : " + enchantment.getName());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
