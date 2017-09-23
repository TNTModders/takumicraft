package com.tntmodders.takumi.enchantment;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class EnchantmentTakumiAntiPowered extends Enchantment {
    public EnchantmentTakumiAntiPowered() {
        super(Enchantment.Rarity.VERY_RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
        this.setRegistryName(TakumiCraftCore.MODID, "takumi_anti_powered");
        this.setName("takumi_anti_powered");
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return stack.getItem() == Items.DIAMOND_SWORD && super.canApplyAtEnchantingTable(stack);
    }
}
