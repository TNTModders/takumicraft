package com.tntmodders.takumi.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockTakumiCreeperBomb extends BlockAbstractTakumiBomb {

    public BlockTakumiCreeperBomb() {
        super("creeperbomb", 0.1f, Material.TNT, MapColor.GREEN);
        this.setLightLevel(1f);
    }

    @Override
    float getPower() {
        return 5f;
    }
}
