package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiBlockCore;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockTakumiRedstoneLight extends Block {

    public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);

    public BlockTakumiRedstoneLight() {
        super(Material.REDSTONE_LIGHT);
        this.setDefaultState(this.getDefaultState().withProperty(POWER, 0));
        this.setRegistryName(TakumiCraftCore.MODID, "creeperredstonelamp");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("creeperredstonelamp");
        this.setHardness(0.3f);
        this.setResistance(10000000f);
        this.setSoundType(SoundType.GLASS);
    }

    @Override
    public int getLightValue(IBlockState state) {
        return state.getValue(POWER) == 0 ? 0 : 15;
    }


    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(POWER, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(POWER);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, POWER);
    }

    /**
     * Called after the block is set in the Chunk data, but before the Tile Entity is set
     */
    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
                worldIn.setBlockState(pos, TakumiBlockCore.CREEPER_REDSTONE_LAMP.getDefaultState().withProperty(POWER, this.calcMaxRedstonePower(worldIn, pos)), 2);
        }
    }

    /**
     * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
     * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
     * block, etc.
     */
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!worldIn.isRemote) {
                worldIn.setBlockState(pos, TakumiBlockCore.CREEPER_REDSTONE_LAMP.getDefaultState().withProperty(POWER, this.calcMaxRedstonePower(worldIn, pos)), 2);
        }
    }

    private int calcMaxRedstonePower(World worldIn, BlockPos pos) {
        int power = 0;
        for (EnumFacing facing : EnumFacing.values()) {
            int i = worldIn.getRedstonePower(pos.offset(facing), facing);
            if (i > power) {
                power = i;
            }
        }
        return power;
    }
}
