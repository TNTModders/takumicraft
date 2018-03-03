package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTakumiGlass extends BlockGlass {

    public BlockTakumiGlass() {
        super(Material.GLASS, false);
        this.setRegistryName(TakumiCraftCore.MODID, "creeperglass");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("creeperglass");
        this.setHardness(0.45f);
        this.setResistance(10000000f);
        this.setSoundType(SoundType.GLASS);
        this.setLightLevel(0.4f);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }
}
