package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCreeperBombDummy extends Item {

    public ItemCreeperBombDummy() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "creeperbombdummy");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("creeperbomb");
    }

    @Override
    public String getUnlocalizedName() {
        return "tile.creeperbomb";
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "tile.creeperbomb";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }
}
