package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockRailPowered;

public class BlockTakumiRailPowered extends BlockRailPowered {
    public BlockTakumiRailPowered(boolean activator) {
        super(activator);
        String name = activator ? "creeperrail_activator" : "creeperrail_powered";
        this.setRegistryName(TakumiCraftCore.MODID, name);
        this.setUnlocalizedName(name);
        this.setHardness(0.7f);
        this.setResistance(10000000f);
        this.setHarvestLevel("pickaxe", 2);
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
    }
}
