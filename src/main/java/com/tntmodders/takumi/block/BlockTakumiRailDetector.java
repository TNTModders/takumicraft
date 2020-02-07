package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockRailDetector;

public class BlockTakumiRailDetector extends BlockRailDetector {
    public BlockTakumiRailDetector() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "creeperrail_detector");
        this.setUnlocalizedName("creeperrail_detector");
        this.setHardness(0.7f);
        this.setResistance(10000000f);
        this.setHarvestLevel("pickaxe", 2);
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
    }
}
