package com.tntmodders.takumi.block;

import com.tntmodders.takumi.core.TakumiConfigCore;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockTakumiCreeperBomb extends BlockAbstractTakumiBomb {

    public BlockTakumiCreeperBomb() {
        super("creeperbomb", 0.1f, Material.TNT, MapColor.GREEN);
        this.setLightLevel(1f);
    }

    @Override
    float getPower() {
        return 5f;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return !TakumiConfigCore.inEventServer && super.canPlaceBlockAt(worldIn, pos);
    }
}
