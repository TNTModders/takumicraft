package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.SoundType;

public class BlockTakumiDispenser extends BlockDispenser {
    public BlockTakumiDispenser() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "creeperdispenser");
        this.setUnlocalizedName("creeperdispenser");
        this.setHardness(3.5f);
        this.setResistance(10000000f);
        this.setSoundType(SoundType.STONE);
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
    }
}
