package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockTakumiDarkBrick extends Block {
    public BlockTakumiDarkBrick() {
        super(Material.ROCK);
        this.setRegistryName(TakumiCraftCore.MODID, "darkbrick");
        this.setUnlocalizedName("darkbrick");
        this.setBlockUnbreakable();
        this.setResistance(10000000f);
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setLightLevel(0.2f);
        this.setLightOpacity(0);
    }

    @Override
    public boolean isFireSource(World world, BlockPos pos, EnumFacing side) {
        return true;
    }
}
