package com.tntmodders.takumi.item.material;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;

public class TakumiArmorMaterial {

    public static final ArmorMaterial CREEPER =
            EnumHelper.addArmorMaterial("creeper", "takumicraft:creeper", 50, new int[]{0, 0, 0, 0}, 0,
                    SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 2.0f);

    public static final ArmorMaterial MAGIC =
            EnumHelper.addArmorMaterial("magic", "takumicraft:magic", 300, new int[]{6, 12, 16, 6}, 40,
                    SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 5.0f);

    public static final ArmorMaterial BATTLE =
            EnumHelper.addArmorMaterial("battle", "takumicraft:battle", 100, new int[]{3, 6, 8, 3}, 40,
                    SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.0f);

    public static final ArmorMaterial BATTLE_POWERED =
            EnumHelper.addArmorMaterial("battle_powered", "takumicraft:battle", 300, new int[]{8, 14, 18, 8}, 40,
                    SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 20.0f);

    public static final ArmorMaterial ERA =
            EnumHelper.addArmorMaterial("era", "takumicraft:era", 10, new int[]{15,15,15,15}, 0,
                    SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 10.0f);

    public static final ArmorMaterial MAKEUP =
            EnumHelper.addArmorMaterial("makeup", "takumicraft:makeup", 100, new int[]{1, 1, 1, 1}, 0,
                    SoundEvents.ENTITY_CREEPER_PRIMED, 0.0f);
}
