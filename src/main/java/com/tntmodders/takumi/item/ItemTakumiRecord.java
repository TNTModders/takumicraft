package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.item.ItemRecord;
import net.minecraft.util.SoundEvent;

public class ItemTakumiRecord extends ItemRecord {
    public ItemTakumiRecord(String s, SoundEvent soundIn) {
        super(s, soundIn);
        this.setRegistryName(TakumiCraftCore.MODID, s);
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName(s);
    }
}
