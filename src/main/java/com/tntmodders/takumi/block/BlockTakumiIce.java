package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockPackedIce;
import net.minecraft.block.SoundType;

public class BlockTakumiIce extends BlockPackedIce {
    public BlockTakumiIce() {
        super();
        String s = "creeperice";
        this.setRegistryName(TakumiCraftCore.MODID, s);
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName(s);
        this.setHardness(1.0f);
        this.setResistance(10000000f);
        this.setDefaultSlipperiness(1.15f);
        this.setSoundType(SoundType.GLASS);
    }
}
