package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockLadder;

public class BlockTakumiLadder extends BlockLadder {

    public BlockTakumiLadder() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "creeperladder");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("creeperladder");
        this.setResistance(10000000f);
    }
}
