package com.tntmodders.tutorial;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemColorAluminium extends Item {

    public ItemColorAluminium() {
        super();
        this.setRegistryName("aluminiummod", "color_aluminium");
        this.setCreativeTab(CreativeTabs.MATERIALS);
        this.setUnlocalizedName("color_aluminium");
        //メタデータありのアイテムとして登録する。
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    //翻訳名を変更する。後ろに.nameが着くためその前まで。
    public String getUnlocalizedName(ItemStack stack) {
        int i = stack.getMetadata();
        return "item.color_aluminium." + i;
    }

    //クリエイティブタブに複数のアイテムを登録する。
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            for (int i = 0; i < 16; ++i) {
                ItemStack itemStack = new ItemStack(this, 1, i);
                itemStack.addEnchantment(Enchantments.SILK_TOUCH,1);
                itemStack.setCount(64);
                items.add(itemStack);
            }
        }
    }
}
