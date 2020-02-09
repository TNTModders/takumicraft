package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockPressurePlateWeighted;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockTakumiPressurePlateWeighted extends BlockPressurePlateWeighted {
    public BlockTakumiPressurePlateWeighted() {
        super(Material.IRON,30);
        this.setRegistryName(TakumiCraftCore.MODID, "creeperplate_iron");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("creeperplate_iron");
        this.setHardness(0.5f);
        this.setResistance(10000000f);
        this.setSoundType(SoundType.WOOD);
    }
}
