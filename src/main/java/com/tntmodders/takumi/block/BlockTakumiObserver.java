package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockObserver;

public class BlockTakumiObserver extends BlockObserver {
    public BlockTakumiObserver() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "creeperobserver");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("creeperobserver");
        this.setHardness(3f);
        this.setResistance(10000000f);
    }
}
