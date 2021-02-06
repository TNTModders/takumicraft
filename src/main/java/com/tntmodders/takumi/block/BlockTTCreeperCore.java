package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockEmptyDrops;
import net.minecraft.block.material.Material;

public class BlockTTCreeperCore extends BlockEmptyDrops implements IBlockTT{
    public BlockTTCreeperCore() {
        super(Material.ROCK);
        this.setRegistryName(TakumiCraftCore.MODID, "ttcreepercore");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("ttcreepercore");
        this.setBlockUnbreakable();
        this.setResistance(10000000f);
    }
}
