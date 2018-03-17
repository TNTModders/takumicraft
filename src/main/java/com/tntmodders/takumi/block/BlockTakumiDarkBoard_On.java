package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.tileentity.TileEntityDarkBoard;
import com.tntmodders.takumi.tileentity.TileEntityDarkCore;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockTakumiDarkBoard_On extends BlockContainer {
    public BlockTakumiDarkBoard_On() {
        super(Material.ROCK);
        this.setRegistryName(TakumiCraftCore.MODID, "darkboard_on");
        this.setUnlocalizedName("darkboard_on");
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
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (world.isRemote) {
            world.loadedTileEntityList.forEach(tileEntity -> {
                if (tileEntity instanceof TileEntityDarkCore) {
                    BlockPos end = tileEntity.getPos();
                    double x = pos.getX() + 0.5;
                    double endX = end.getX() + 0.5;
                    double y = pos.getY() + 0.5;
                    double endY = end.getY() + 0.5;
                    double z = pos.getZ() + 0.5;
                    double endZ = end.getZ() + 0.5;
                    int count = 0;
                    for (int i = 0; i < 250; i++) {
                        world.spawnAlwaysVisibleParticle(EnumParticleTypes.PORTAL.getParticleID(),
                                x + world.rand.nextDouble() * 0.2 - 0.1 + (endX - x) / 250 * i,
                                y + world.rand.nextDouble() * 0.2 - 0.1 + (endY - y) / 250 * i,
                                z + world.rand.nextDouble() * 0.2 - 0.1 + (endZ - z) / 250 * i, -(endX - x) / 250 * i,
                                -(endY - y) / 250 * i, -(endZ - z) / 250 * i);
                    }
                }
            });
        }
    }

    @SideOnly(Side.CLIENT)
    private void renderBeam(World world, BlockPos pos, BlockPos end) {

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
