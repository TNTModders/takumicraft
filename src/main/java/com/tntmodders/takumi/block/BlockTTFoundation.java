package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockEmptyDrops;
import net.minecraft.block.material.Material;

public class BlockTTFoundation extends BlockEmptyDrops {
    public BlockTTFoundation() {
        super(Material.ROCK);
        this.setRegistryName(TakumiCraftCore.MODID, "ttfoundation");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("ttfoundation");
        this.setBlockUnbreakable();
        this.setResistance(10000000f);
    }
}
