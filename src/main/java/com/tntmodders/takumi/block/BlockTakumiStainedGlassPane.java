package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.SoundType;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemCloth;

public class BlockTakumiStainedGlassPane extends BlockStainedGlassPane implements ITakumiItemBlock {

    public BlockTakumiStainedGlassPane() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "creeperstainedglasspane");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("creeperstainedglasspane");
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
