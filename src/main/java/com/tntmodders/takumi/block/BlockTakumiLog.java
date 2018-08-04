package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiEnchantmentCore;
import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
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
        this.setDefaultState(this.blockState.getBaseState().withProperty(LOG_AXIS, BlockTakumiLog.EnumAxis.Y));
        this.setHarvestLevel("axe", 1);
    }

    @Override
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
        if (!worldIn.isRemote) {
            this.explode(worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (!worldIn.isRemote && !(player.getHeldItemMainhand() != null &&
                EnchantmentHelper.getEnchantments(player.getHeldItemMainhand()).containsKey(
                        TakumiEnchantmentCore.MINESWEEPER) &&
                (player.getHeldItemMainhand().getStrVsBlock(state) > 1.0f || this.getHarvestTool(state) == null))) {
            this.explode(worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
    }

    @Override
    public boolean canDropFromExplosion(Explosion explosionIn) {
        return false;
    }

    public void explode(World world, int x, int y, int z) {
        world.createExplosion(null, x + 0.5, y + 0.5, z + 0.5, getPower(), true);
    }

    float getPower() {
        return 1.5f;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(LOG_AXIS, BlockTakumiLog.EnumAxis.values()[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(LOG_AXIS).ordinal();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, LOG_AXIS);
    }
}
