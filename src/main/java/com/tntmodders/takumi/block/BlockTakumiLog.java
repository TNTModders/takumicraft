package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockTakumiLog extends BlockLog {
    public static final PropertyEnum<EnumAxis> LOG_AXIS = PropertyEnum.create("axis", BlockTakumiLog.EnumAxis.class);

    public BlockTakumiLog() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "takumilog");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("takumilog");
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockTakumiLog.LOG_AXIS, BlockTakumiLog.EnumAxis.Y));
    }

    @Override
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
        if (!worldIn.isRemote) {
            this.explode(worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (!worldIn.isRemote && player.getHeldItem(EnumHand.MAIN_HAND).getItem() != Items.DIAMOND_PICKAXE) {
            this.explode(worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
    }

    public void explode(World world, int x, int y, int z) {
        world.createExplosion(null, x + 0.5, y + 0.5, z + 0.5, getPower(), true);
    }

    float getPower() {
        return 1.5f;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(BlockTakumiLog.LOG_AXIS, BlockTakumiLog.EnumAxis.values()[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BlockTakumiLog.LOG_AXIS).ordinal();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BlockTakumiLog.LOG_AXIS);
    }
}
