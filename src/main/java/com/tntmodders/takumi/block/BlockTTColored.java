package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockEmptyDrops;
import net.minecraft.block.material.Material;

public class BlockTTColored extends BlockEmptyDrops implements IBlockTT {
    public BlockTTColored(String color) {
        super(Material.ROCK);
        this.setRegistryName(TakumiCraftCore.MODID, "ttcolored_"+color);
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("ttcolored_"+color);
        this.setBlockUnbreakable();
        this.setResistance(10000000f);
        this.setLightLevel(0.75f);
    }
}
