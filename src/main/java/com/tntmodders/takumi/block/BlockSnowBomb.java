package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiEnchantmentCore;
import com.tntmodders.takumi.tileentity.TileEntityTakumiSuperPowered;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPressurePlateWeighted;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSnowBomb extends BlockPressurePlateWeighted {

    public BlockSnowBomb() {
        super(Material.SNOW, 15);
        this.setRegistryName(TakumiCraftCore.MODID, "creepersnowbomb");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("creepersnowbomb");
        this.setResistance(0f);
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (!worldIn.isRemote && !(worldIn.getTileEntity(pos) instanceof TileEntityTakumiSuperPowered) &&
                ((entityIn instanceof EntityLivingBase && !entityIn.isSneaking()) || computeRedstoneStrength(worldIn, pos) > 3)) {
            this.explode(worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
    }

    @Override
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
        if (!pos.equals(new BlockPos(explosionIn.getPosition().addVector(-0.5, -0.5, -0.5))) || explosionIn.getExplosivePlacedBy() != null) {
            worldIn.setBlockToAir(pos);
            if (!worldIn.isRemote) {
                this.explode(worldIn, pos.getX(), pos.getY(), pos.getZ());
            }
        }
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (!worldIn.isRemote && !(player.getHeldItemMainhand() != null &&
                EnchantmentHelper.getEnchantments(player.getHeldItemMainhand()).containsKey(
                        TakumiEnchantmentCore.MINESWEEPER) &&
                (player.getHeldItemMainhand().getStrVsBlock(state) > 1.0f || this.getHarvestTool(state) == null))) {
            worldIn.setBlockToAir(pos);
            this.explode(worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
    }

    @Override
    public boolean canDropFromExplosion(Explosion explosionIn) {
        return false;
    }

    public void explode(World world, int x, int y, int z) {
        try {
            world.setBlockToAir(new BlockPos(x, y, z));
            world.createExplosion(null, x + 0.5, y + 0.5, z + 0.5, 3f, true);
        } catch (Exception ignored) {
        }
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
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
        Block block = iblockstate.getBlock();

        return block != this && super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }

    @Override
    protected void playClickOnSound(World worldIn, BlockPos color) {
        worldIn.playSound(null, color, SoundEvents.BLOCK_SNOW_STEP, SoundCategory.BLOCKS, 0.3F, 0.90000004F);
    }

    @Override
    protected void playClickOffSound(World worldIn, BlockPos pos) {
        worldIn.playSound(null, pos, SoundEvents.BLOCK_SNOW_STEP, SoundCategory.BLOCKS, 0.3F, 0.75F);
    }

}
