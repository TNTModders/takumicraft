package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockTorch;

public class BlockTakumiTorch extends BlockTorch {
    public BlockTakumiTorch() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "creepertorch");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("creepertorch");
        this.setResistance(10000000f);
        this.setLightLevel(1);
    }
}
