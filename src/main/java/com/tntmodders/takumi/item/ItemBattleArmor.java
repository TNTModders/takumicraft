package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiEnchantmentCore;
import com.tntmodders.takumi.item.material.TakumiArmorMaterial;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBattleArmor extends ItemArmor {
    public final boolean isPowered;

    public ItemBattleArmor(boolean flg, EntityEquipmentSlot equipmentSlotIn) {
        super(flg ? TakumiArmorMaterial.BATTLE_POWERED : TakumiArmorMaterial.BATTLE, 5, equipmentSlotIn);
        this.isPowered = flg;
        String s =
                flg ? "battlearmor_powered_" + equipmentSlotIn.getName() : "battlearmor_" + equipmentSlotIn.getName();
        this.setRegistryName(TakumiCraftCore.MODID, s);
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName(s);
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean isHeld) {
        if (this.isPowered && !itemStack.isItemEnchanted()) {
            itemStack.addEnchantment(TakumiEnchantmentCore.EXPLOSION_PROTECTION, 10);
        }
    }

    @Override
    public void onCreated(ItemStack itemStack, World p_77622_2_, EntityPlayer p_77622_3_) {
        if (this.isPowered && !itemStack.isItemEnchanted()) {
            itemStack.addEnchantment(TakumiEnchantmentCore.EXPLOSION_PROTECTION, 10);
        }
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return this.isPowered ? EnumRarity.RARE : EnumRarity.UNCOMMON;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            ItemStack itemStack = new ItemStack(this, 1);
            if (this.isPowered) {
                itemStack.addEnchantment(TakumiEnchantmentCore.EXPLOSION_PROTECTION, 10);
            }
            items.add(itemStack);
        }
    }
}
