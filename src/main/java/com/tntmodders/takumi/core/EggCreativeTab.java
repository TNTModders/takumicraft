package com.tntmodders.takumi.core;

import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EggCreativeTab extends CreativeTabs {
    public EggCreativeTab() {
        super("eggs");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getTranslatedTabLabel() {
        return TakumiUtils.takumiTranslate("takumicraft.egg.name");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getTabIconItem() {
        ItemStack itemStack = new ItemStack(Items.SPAWN_EGG);
        ItemMonsterPlacer.applyEntityIdToItemStack(itemStack, new ResourceLocation("minecraft:creeper"));
        return itemStack;
    }
}
