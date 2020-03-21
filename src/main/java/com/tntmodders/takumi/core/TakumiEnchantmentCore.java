package com.tntmodders.takumi.core;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.enchantment.EnchantmentTakumiAntiPowered;
import com.tntmodders.takumi.enchantment.EnchantmentTakumiElytra;
import com.tntmodders.takumi.enchantment.EnchantmentTakumiExplosionProtection;
import com.tntmodders.takumi.enchantment.EnchantmentTakumiMineSweeper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistry;

import java.lang.reflect.Field;

public class TakumiEnchantmentCore {

    public static final Enchantment ANTI_POWERED = new EnchantmentTakumiAntiPowered();
    public static final Enchantment EXPLOSION_PROTECTION = new EnchantmentTakumiExplosionProtection();
    public static final Enchantment MINESWEEPER = new EnchantmentTakumiMineSweeper();
    public static final Enchantment TYPE_MAGIC =
            new Enchantment(Enchantment.Rarity.VERY_RARE, EnumEnchantmentType.WEAPON,
                    new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND}) {
                @Override
                protected boolean canApplyTogether(Enchantment ench) {
                    return ench != TYPE_DEST && super.canApplyTogether(ench);
                }

                @Override
                public boolean canApplyAtEnchantingTable(ItemStack stack) {
                    return false;
                }
            }.setRegistryName(TakumiCraftCore.MODID, "takumi_type_magic").setName("takumi_type_magic");
    public static final Enchantment TYPE_DEST =
            new Enchantment(Enchantment.Rarity.VERY_RARE, EnumEnchantmentType.WEAPON,
                    new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND}) {
                @Override
                protected boolean canApplyTogether(Enchantment ench) {
                    return ench != TYPE_MAGIC && super.canApplyTogether(ench);
                }

                @Override
                public boolean canApplyAtEnchantingTable(ItemStack stack) {
                    return false;
                }
            }.setRegistryName(TakumiCraftCore.MODID, "takumi_type_dest").setName("takumi_type_dest");
    public static final Enchantment ROCKET_ELYTRA = new EnchantmentTakumiElytra();
    public static final Enchantment ITEM_PROTECTION = new Enchantment(Enchantment.Rarity.VERY_RARE, EnumEnchantmentType.ALL, EntityEquipmentSlot.values()) {
        @Override
        public boolean canApplyAtEnchantingTable(ItemStack stack) {
            return false;
        }
    }.setRegistryName(TakumiCraftCore.MODID, "takumi_item_protection").setName("takumi_item_protection");

    public static void register(IForgeRegistry<Enchantment> registry) {
        Class clazz = TakumiEnchantmentCore.class;
        for (Field field : clazz.getFields()) {
            try {
                if (field.get(TakumiItemCore.INSTANCE) instanceof Enchantment) {
                    Enchantment enchantment = (Enchantment) field.get(TakumiItemCore.INSTANCE);
                    registry.register(enchantment);
                    TakumiCraftCore.LOGGER.info("Registered Enchantment : " + enchantment.getName());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
