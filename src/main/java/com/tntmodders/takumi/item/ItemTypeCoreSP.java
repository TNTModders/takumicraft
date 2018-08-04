package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTypeCoreSP extends Item {

    public ItemTypeCoreSP(boolean isMagic) {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "typecoresp_" + (isMagic ? "magic" : "dest"));
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("typecoresp_" + (isMagic ? "magic" : "dest"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.UNCOMMON;
    }
}
