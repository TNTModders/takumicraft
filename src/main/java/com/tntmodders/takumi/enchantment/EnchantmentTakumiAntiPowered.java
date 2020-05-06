package com.tntmodders.takumi.enchantment;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiItemCore;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Arrays;

public class EnchantmentTakumiAntiPowered extends Enchantment {
    private static final Item[] APPLYABLE = {
            /*Items.DIAMOND_SWORD,*/
            TakumiItemCore.TAKUMI_SWORD,
            TakumiItemCore.BATTLE_SHIELD_POWERED,
            TakumiItemCore.TAKUMI_TYPE_SWORD_FIRE,
            TakumiItemCore.TAKUMI_TYPE_SWORD_NORMAL,
            TakumiItemCore.TAKUMI_TYPE_SWORD_WATER,
            TakumiItemCore.TAKUMI_TYPE_SWORD_GRASS,
            TakumiItemCore.TAKUMI_TYPE_SWORD_GROUND,
            TakumiItemCore.TAKUMI_TYPE_SWORD_WIND
    };

    public EnchantmentTakumiAntiPowered() {
        super(Rarity.RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
        this.setRegistryName(TakumiCraftCore.MODID, "takumi_anti_powered");
        this.setName("takumi_anti_powered");
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return Arrays.asList(APPLYABLE).contains(stack.getItem()) && super.canApplyAtEnchantingTable(stack);
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 20;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return 30;
    }

}
