package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.SoundType;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemCloth;

public class BlockTakumiCarpet extends BlockCarpet implements ITakumiItemBlock {

    public BlockTakumiCarpet() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "creepercarpet");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("creepercarpet");
        this.setHardness(0.8f);
        this.setResistance(10000000f);
        this.setSoundType(SoundType.CLOTH);
    }

    @Override
    public ItemBlock getItem() {
        return new ItemCloth(this);
    }
}
