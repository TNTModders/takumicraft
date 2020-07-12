package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockPackedIce;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;

public class BlockTakumiIce extends BlockPackedIce {
    public BlockTakumiIce() {
        super();
        String s = "creeperice";
        this.setRegistryName(TakumiCraftCore.MODID, s);
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName(s);
        this.setHardness(1.0f);
        this.setResistance(10000000f);
        this.setDefaultSlipperiness(0.985f);
        this.setSoundType(SoundType.GLASS);
        this.setLightLevel(0.667f);
    }

    @Override
    public float getSlipperiness(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable Entity entity) {
        return entity instanceof EntityLivingBase ? 1.075f : entity instanceof EntityItem ? 1.0f : super.getSlipperiness(state, world, pos, entity);
    }
}
