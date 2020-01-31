package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockRailDetector;

public class BlockTakumiRailDetector extends BlockRailDetector {
    public BlockTakumiRailDetector() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "creeperrail_detector");
        this.setUnlocalizedName("creeperrail_detector");
        this.setResistance(10000000f);
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
    }
}
