package com.tntmodders.takumi.core;

import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TakumiCreativeTab extends CreativeTabs {
    
    public TakumiCreativeTab() {
        super("Takumi Craft");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public String getTranslatedTabLabel() {
        return TakumiUtils.takumiTranslate("takumicraft.info.name");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getTabIconItem() {
        return new ItemStack(TakumiBlockCore.CREEPER_BOMB);
    }
}
