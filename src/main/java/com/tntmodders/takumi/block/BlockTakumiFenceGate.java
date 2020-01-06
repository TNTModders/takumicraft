package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;

public class BlockTakumiFenceGate extends BlockFenceGate {

    public BlockTakumiFenceGate(Material material, String s, float hardness, String tool) {
        super(BlockPlanks.EnumType.OAK);
        this.setRegistryName(TakumiCraftCore.MODID, s);
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName(s);
        this.setHardness(hardness);
        this.setResistance(10000000f);
        if (tool != null) {
            this.setHarvestLevel(tool, 2);
        }
    }
}
