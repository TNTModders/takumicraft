package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.item.Item;

public class ItemTakumiMagicStone extends Item {

    public ItemTakumiMagicStone() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "magicstone");
        this.setUnlocalizedName("magicstone");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
    }
}
