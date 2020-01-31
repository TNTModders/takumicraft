package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockRail;

public class BlockTakumiRail extends BlockRail {
    public BlockTakumiRail() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "creeperrail");
        this.setUnlocalizedName("creeperrail");
        this.setResistance(10000000f);
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
    }
}
