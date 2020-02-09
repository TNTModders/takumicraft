package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.SoundType;

public class BlockTakumiHopper extends BlockHopper {
    public BlockTakumiHopper() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "creeperhopper");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("creeperhopper");
        this.setHardness(3f);
        this.setResistance(10000000f);
        this.setSoundType(SoundType.METAL);
    }
}
