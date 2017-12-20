package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.tileentity.TileEntityTakumiCreepered;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockTakumiCreepered extends BlockContainer {
    
    public BlockTakumiCreepered() {
        super(Material.BARRIER);
        this.setRegistryName(TakumiCraftCore.MODID, "takumicreepered");
        //this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("takumicreepered");
        this.setLightLevel(1f);
        this.setLightOpacity(255);
        this.setHardness(5f);
    }
    
    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return true;
    }
    
    @Override
    public int quantityDropped(Random random) {
        return 0;
    }
    
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }
    
    @Override
    public boolean canDropFromExplosion(Explosion explosionIn) {
        return false;
    }
    
    @Override
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
        worldIn.playSound(null, pos, SoundEvents.ENTITY_CREEPER_PRIMED, SoundCategory.AMBIENT, 1.0f, 0.5f);
        return true;
    }
    
    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityTakumiCreepered();
    }
}
