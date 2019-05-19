package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.item.material.TakumiArmorMaterial;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

public class ItemMakeup extends ItemArmor {
    public ItemMakeup() {
        super(TakumiArmorMaterial.MAKEUP, 6, EntityEquipmentSlot.HEAD);
        this.setRegistryName(TakumiCraftCore.MODID, "makeup");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("makeup");
    }
}
