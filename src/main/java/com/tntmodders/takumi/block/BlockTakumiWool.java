package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockColored;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemCloth;

public class BlockTakumiWool extends BlockColored implements ITakumiItemBlock {

    public BlockTakumiWool() {
        super(Material.CLOTH);
        this.setRegistryName(TakumiCraftCore.MODID, "creeperwool");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("creeperwool");
        this.setHardness(0.8f);
        this.setResistance(10000000f);
        this.setSoundType(SoundType.CLOTH);
    }

    @Override
    public ItemBlock getItem() {
        return new ItemCloth(this);
    }
}
