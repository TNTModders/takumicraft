package com.tntmodders.takumi.enchantment;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.item.ItemTakumiMineSweeperTool;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class EnchantmentTakumiMineSweeper extends Enchantment {

    public EnchantmentTakumiMineSweeper() {
        super(Rarity.RARE, EnumEnchantmentType.DIGGER, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
        this.setRegistryName(TakumiCraftCore.MODID, "takumi_minesweeper");
        this.setName("takumi_minesweeper");
    }

    @Override
    public boolean canApply(ItemStack stack) {
        return stack.getItem() instanceof ItemTakumiMineSweeperTool && super.canApply(stack);
    }
}
