package com.tntmodders.tutorial;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockAluminium extends Block {
    public BlockAluminium() {
        super(Material.IRON);
        this.setRegistryName("aluminiummod", "aluminium_block");
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        this.setUnlocalizedName("aluminium_block");
    }
}
