package com.tntmodders.tutorial;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemAluminium extends Item {

    public ItemAluminium() {
        super();
        //レジストリに保存する名称を登録する。大文字禁止。
        this.setRegistryName("aluminiummod","aluminium");
        //クリエイティブタブを設定する。
        this.setCreativeTab(CreativeTabs.MATERIALS);
        //翻訳名を登録する。大文字非推奨。
        this.setUnlocalizedName("aluminium");
    }
}
