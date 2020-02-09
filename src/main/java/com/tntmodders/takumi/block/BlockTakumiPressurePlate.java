package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockTakumiPressurePlate extends BlockPressurePlate {
    public BlockTakumiPressurePlate(Material materialIn, Sensitivity sensitivityIn, String s) {
        super(materialIn, sensitivityIn);
        this.setRegistryName(TakumiCraftCore.MODID, s);
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName(s);
        this.setHardness(0.5f);
        this.setResistance(10000000f);
        this.setSoundType(materialIn == Material.WOOD ? SoundType.WOOD : SoundType.STONE);
    }
}
