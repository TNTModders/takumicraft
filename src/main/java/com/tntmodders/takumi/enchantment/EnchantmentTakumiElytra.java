package com.tntmodders.takumi.enchantment;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class EnchantmentTakumiElytra extends Enchantment {

    public EnchantmentTakumiElytra() {
        super(Rarity.COMMON, EnumEnchantmentType.WEARABLE, new EntityEquipmentSlot[]{EntityEquipmentSlot.CHEST});
        this.setRegistryName(TakumiCraftCore.MODID, "takumi_elytra");
        this.setName("takumi_elytra");
    }

    @Override
    public boolean canApply(ItemStack stack) {
        return stack.getItem() == Items.ELYTRA && super.canApply(stack);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return false;
    }
}
