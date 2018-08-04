package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemEnergyCore extends Item {
    public static final String[] NAMES = {"normal", "red", "blue", "yellow"};

    public ItemEnergyCore() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "energycore");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("energycore");
        this.setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int i = stack.getMetadata();
        return super.getUnlocalizedName() + "." + NAMES[i];
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.UNCOMMON;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            for (int i = 0; i < 4; ++i) {
                items.add(new ItemStack(this, 1, i));
            }
        }
    }
}
