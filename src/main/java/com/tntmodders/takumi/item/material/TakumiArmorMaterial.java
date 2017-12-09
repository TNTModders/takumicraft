package com.tntmodders.takumi.item.material;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;

public class TakumiArmorMaterial {
    
    public static final ArmorMaterial CREEPER = EnumHelper.addArmorMaterial("creeper", "takumicraft:creeper", 10, new int[]{0, 0, 0,
            0}, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 10.0f);
}
