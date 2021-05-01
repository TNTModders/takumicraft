package com.tntmodders.takumi.enchantment;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.item.ItemBattleArmor;
import com.tntmodders.takumi.item.ItemERArmor;
import com.tntmodders.takumi.item.ItemMagicArmor;
import com.tntmodders.takumi.item.ItemTakumiArmor;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public class EnchantmentTakumiExplosionProtection extends Enchantment {

    public EnchantmentTakumiExplosionProtection() {
        super(Rarity.VERY_RARE, EnumEnchantmentType.ARMOR,
                new EntityEquipmentSlot[]{EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS,
                        EntityEquipmentSlot.FEET});
        this.setRegistryName(TakumiCraftCore.MODID, "takumi_explosion_protection");
        this.setName("takumi_explosion_protection");
    }

    @Override
    public int calcModifierDamage(int level, DamageSource source) {
        return source.isExplosion() ? 10000000 : super.calcModifierDamage(level, source);
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 500;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return 500;
    }

    @Override
    public boolean canApply(ItemStack stack) {
        return (stack.getItem() instanceof ItemTakumiArmor || stack.getItem() instanceof ItemMagicArmor || stack.getItem() instanceof ItemERArmor || stack.getItem() instanceof ItemBattleArmor)
                && super.canApply(stack);
    }


}
