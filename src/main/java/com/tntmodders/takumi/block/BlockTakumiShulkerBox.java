package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.tileentity.TileEntityTakumiShulkerBox;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class BlockTakumiShulkerBox extends BlockShulkerBox implements ITakumiItemBlock {
    public BlockTakumiShulkerBox() {
        super(EnumDyeColor.GREEN);
        this.setRegistryName(TakumiCraftCore.MODID, "creepershulkerbox");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("creepershulkerbox");
        this.setSoundType(SoundType.STONE);
        this.setHardness(2.0f);
        this.setResistance(10000000f);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityTakumiShulkerBox();
    }

    @Override
    public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager) {
        return true;
    }

    @Override
    public boolean addHitEffects(IBlockState state, World worldObj, RayTraceResult target, ParticleManager manager) {
        return true;
    }

    @Override
    public boolean addLandingEffects(IBlockState state, WorldServer worldObj, BlockPos blockPosition, IBlockState iblockstate, EntityLivingBase entity, int numberOfParticles) {
        return true;
    }

    @Override
    public boolean addRunningEffects(IBlockState state, World world, BlockPos pos, Entity entity) {
        return true;
    }

    @Override
    public ItemBlock getItem() {
        return ((ItemBlock) new ItemBlock(this).setMaxStackSize(1));
    }
}
