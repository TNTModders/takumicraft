package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockRail;

public class BlockTakumiRail extends BlockRail {
    public BlockTakumiRail() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "creeperrail");
        this.setUnlocalizedName("creeperrail");
        this.setHardness(0.7f);
        this.setResistance(10000000f);
        this.setHarvestLevel("pickaxe", 2);
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
    }
}
