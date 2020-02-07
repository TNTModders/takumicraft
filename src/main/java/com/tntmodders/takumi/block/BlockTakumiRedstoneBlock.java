package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockCompressedPowered;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockTakumiRedstoneBlock extends BlockCompressedPowered {
    public BlockTakumiRedstoneBlock() {
        super(Material.ROCK, MapColor.GREEN);
        this.setRegistryName(TakumiCraftCore.MODID, "creeperredstoneblock");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("creeperredstoneblock");
        this.setHardness(5f);
        this.setResistance(10000000f);
        this.setSoundType(SoundType.METAL);
        this.setHarvestLevel("pickaxe", 2);
    }
}
