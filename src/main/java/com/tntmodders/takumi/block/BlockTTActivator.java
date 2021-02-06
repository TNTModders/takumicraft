package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockEmptyDrops;
import net.minecraft.block.material.Material;

public class BlockTTActivator extends BlockEmptyDrops implements IBlockTT{
    public BlockTTActivator() {
        super(Material.ROCK);
        this.setRegistryName(TakumiCraftCore.MODID, "ttactivator");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("ttactivator");
        this.setBlockUnbreakable();
        this.setResistance(10000000f);
    }
}
