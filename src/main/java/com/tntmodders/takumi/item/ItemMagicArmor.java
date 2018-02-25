package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.item.material.TakumiArmorMaterial;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMagicArmor extends ItemArmor {
    
    public ItemMagicArmor(EntityEquipmentSlot equipmentSlotIn) {
        super(TakumiArmorMaterial.MAGIC, 5, equipmentSlotIn);
        this.setRegistryName(TakumiCraftCore.MODID, "magicarmor_" + equipmentSlotIn.getName());
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("magicarmor_" + equipmentSlotIn.getName());
    }
    
    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean isHeld) {
        if (!itemStack.isItemEnchanted()) {
            itemStack.addEnchantment(Enchantments.UNBREAKING, 10);
            itemStack.addEnchantment(Enchantments.MENDING, 10);
            itemStack.addEnchantment(Enchantments.VANISHING_CURSE, 10);
            itemStack.addEnchantment(Enchantments.BINDING_CURSE, 10);
        }
    }
    
    @Override
    public void onCreated(ItemStack itemStack, World p_77622_2_, EntityPlayer p_77622_3_) {
        if (!itemStack.isItemEnchanted()) {
            itemStack.addEnchantment(Enchantments.UNBREAKING, 10);
            itemStack.addEnchantment(Enchantments.MENDING, 10);
            itemStack.addEnchantment(Enchantments.VANISHING_CURSE, 10);
            itemStack.addEnchantment(Enchantments.BINDING_CURSE, 10);
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack itemStack) {
        return true;
    }
    
    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if (!itemStack.isItemEnchanted()) {
            itemStack.addEnchantment(Enchantments.UNBREAKING, 10);
            itemStack.addEnchantment(Enchantments.MENDING, 10);
            itemStack.addEnchantment(Enchantments.VANISHING_CURSE, 10);
            itemStack.addEnchantment(Enchantments.BINDING_CURSE, 10);
        }
    }
}
