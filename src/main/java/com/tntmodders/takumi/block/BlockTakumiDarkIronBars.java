package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockPane;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockTakumiDarkIronBars extends BlockPane {
    public BlockTakumiDarkIronBars() {
        super(Material.IRON, true);
        this.setSoundType(SoundType.METAL);
        this.setRegistryName(TakumiCraftCore.MODID, "darkironbars");
        this.setUnlocalizedName("darkironbars");
        this.setBlockUnbreakable();
        this.setResistance(10000000f);
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
    }
}
