package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiBlockCore;
import net.minecraft.block.BlockEmptyDrops;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockTTCreeperCore extends BlockEmptyDrops implements IBlockTT {
    public BlockTTCreeperCore() {
        super(Material.ROCK);
        this.setRegistryName(TakumiCraftCore.MODID, "ttcreepercore");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("ttcreepercore");
        this.setBlockUnbreakable();
        this.setResistance(10000000f);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        worldIn.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 0f, false);
        worldIn.setBlockState(pos, TakumiBlockCore.TT_FOUNDATION.getDefaultState());
        return true;
    }
}
