package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockTakumiDirt extends BlockDirt {

    public BlockTakumiDirt() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "takumidirt");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("takumidirt");
        this.setHarvestLevel("shovel", 2);
        this.setHardness(1f);
        this.setResistance(10000000f);
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this, 1, DirtType.DIRT.getMetadata()));
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(this, 1);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return 0;
    }
}
