package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.tileentity.TileEntityDarkBoard;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockTakumiDarkBoard extends BlockContainer {
    public BlockTakumiDarkBoard() {
        super(Material.ROCK);
        this.setRegistryName(TakumiCraftCore.MODID, "darkboard");
        this.setUnlocalizedName("darkboard");
        this.setBlockUnbreakable();
        this.setResistance(10000000f);
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setLightLevel(1f);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityDarkBoard();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
            EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityDarkBoard) {
            TakumiCraftCore.LOGGER.info(((TileEntityDarkBoard) worldIn.getTileEntity(pos)).name);
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        if (worldIn.getTileEntity(pos) instanceof TileEntityDarkBoard) {
            Random random = new Random();
            random.setSeed(System.currentTimeMillis() + pos.getX() + pos.getZ());
            if (((TileEntityDarkBoard) worldIn.getTileEntity(pos)).name == null ||
                    ((TileEntityDarkBoard) worldIn.getTileEntity(pos)).name.isEmpty()) {
                Class clazz = ((Class) TakumiBlockCore.BOMB_MAP.keySet().toArray()[random
                        .nextInt(TakumiBlockCore.BOMB_MAP.size())]);
                try {
                    EntityTakumiAbstractCreeper creeper =
                            (EntityTakumiAbstractCreeper) clazz.getConstructor(World.class).newInstance(worldIn);
                    ((TileEntityDarkBoard) worldIn.getTileEntity(pos)).name = creeper.getRegisterName();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            TakumiCraftCore.LOGGER.info(((TileEntityDarkBoard) worldIn.getTileEntity(pos)).name);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos,
            EnumFacing side) {
        return true;
    }


    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
}
