package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockColored;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemCloth;

public class BlockTakumiClay extends BlockColored implements ITakumiItemBlock {
    
    public BlockTakumiClay() {
        super(Material.CLOTH);
        this.setRegistryName(TakumiCraftCore.MODID, "creeperclay");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("creeperclay");
        this.setHardness(2f);
        this.setHarvestLevel("pickaxe", 1);
        this.setResistance(10000000f);
        this.setSoundType(SoundType.GROUND);
    }
    
    @Override
    public ItemBlock getItem() {
        return new ItemCloth(this);
    }
}
