package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockDropper;
import net.minecraft.block.SoundType;

public class BlockTakumiDropper extends BlockDropper {
    public BlockTakumiDropper() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "creeperdropper");
        this.setUnlocalizedName("creeperdropper");
        this.setHardness(3.5f);
        this.setResistance(10000000f);
        this.setSoundType(SoundType.STONE);
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
    }
}
