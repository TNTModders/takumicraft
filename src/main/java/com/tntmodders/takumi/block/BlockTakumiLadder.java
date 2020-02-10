package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.SoundType;

public class BlockTakumiLadder extends BlockLadder {

    public BlockTakumiLadder() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "creeperladder");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("creeperladder");
        this.setHardness(0.4f);
        this.setResistance(10000000f);
        this.setSoundType(SoundType.LADDER);
    }
}
