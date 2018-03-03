package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemCloth;

public class BlockTakumiStainedGlass extends BlockStainedGlass implements ITakumiItemBlock {

    public BlockTakumiStainedGlass() {
        super(Material.GLASS);
        this.setRegistryName(TakumiCraftCore.MODID, "creeperstainedglass");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("creeperstainedglass");
        this.setHardness(0.45f);
        this.setResistance(10000000f);
        this.setSoundType(SoundType.GLASS);
        this.setLightLevel(0.4f);
    }

    @Override
    public ItemBlock getItem() {
        return new ItemCloth(this);
    }
}
