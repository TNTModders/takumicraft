package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.text.translation.I18n;

public class ItemTakumiShield extends ItemShield {
    public ItemTakumiShield() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "takumishield");
        //this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("takumishield");
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (stack.getSubCompound("BlockEntityTag") != null) {
            EnumDyeColor enumdyecolor = TileEntityBanner.getColor(stack);
            return I18n.translateToLocal("takumicraft.item.shield.name") + "[" + I18n.translateToLocal(enumdyecolor.getUnlocalizedName()) + "]";
        } else {
            return I18n.translateToLocal("takumicraft.item.shield.name");
        }
    }
}
