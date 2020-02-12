package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockTakumiTrapDoor extends BlockTrapDoor {
    public BlockTakumiTrapDoor(Material material, String s) {
        super(material);
        this.setRegistryName(TakumiCraftCore.MODID, s);
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName(s);
        this.setHardness(5.0f);
        this.setResistance(10000000f);
        this.setSoundType(SoundType.WOOD);
    }
}
