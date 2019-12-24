package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

public class BlockTakumiAntiExplosionStairs extends BlockStairs {
    public BlockTakumiAntiExplosionStairs(IBlockState modelState, Material material, String s, float hardness, String tool) {
        super(modelState);
        this.setRegistryName(TakumiCraftCore.MODID, s);
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName(s);
        this.setHardness(hardness);
        this.setResistance(10000000f);
        if (tool != null) {
            this.setHarvestLevel(tool, 2);
        }
        this.setLightOpacity(0);
    }

    @Override
    public boolean getUseNeighborBrightness(IBlockState state) {
        return false;
    }
}
