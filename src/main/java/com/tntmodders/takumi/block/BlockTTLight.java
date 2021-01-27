package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockEmptyDrops;
import net.minecraft.block.material.Material;

public class BlockTTLight extends BlockEmptyDrops {
    public BlockTTLight() {
        super(Material.ROCK);
        this.setRegistryName(TakumiCraftCore.MODID, "ttlight");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("ttlight");
        this.setBlockUnbreakable();
        this.setResistance(10000000f);
        this.setLightLevel(1f);
    }
}
