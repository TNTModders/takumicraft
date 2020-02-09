package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockLever;
import net.minecraft.block.SoundType;

public class BlockTakumiLever extends BlockLever {
    public BlockTakumiLever() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "creeperlever");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("creeperlever");
        this.setHardness(0.5f);
        this.setResistance(10000000f);
        this.setSoundType(SoundType.WOOD);
    }
}
