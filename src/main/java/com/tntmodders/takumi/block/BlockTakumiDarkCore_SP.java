package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockTakumiDarkCore_SP extends Block {
    public BlockTakumiDarkCore_SP() {
        super(Material.ROCK);
        this.setRegistryName(TakumiCraftCore.MODID, "darkcore_sp");
        this.setUnlocalizedName("darkcore_sp");
        this.setBlockUnbreakable();
        this.setResistance(10000000f);
        this.setLightLevel(0.2f);
        this.setLightOpacity(0);
    }

    @Override
    public boolean isFireSource(World world, BlockPos pos, EnumFacing side) {
        return true;
    }
}
