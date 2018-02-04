package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiEnchantmentCore;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockFallingBomb extends BlockFalling {
    
    public BlockFallingBomb() {
        super(Material.GROUND);
        this.setRegistryName(TakumiCraftCore.MODID, "fallingbomb");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("fallingbomb");
        this.setResistance(0);
    }
    
    @Override
    public void onEndFalling(World worldIn, BlockPos pos, IBlockState p_176502_3_, IBlockState p_176502_4_) {
        super.onEndFalling(worldIn, pos, p_176502_3_, p_176502_4_);
        if (!worldIn.isRemote) {
            worldIn.createExplosion(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 2f, true);
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getDustColor(IBlockState state) {
        return 0x004400;
    }
    
    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return MapColor.CYAN_STAINED_HARDENED_CLAY;
    }
    
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }
    
    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (!worldIn.isRemote && !(player.getHeldItemMainhand() != null && EnchantmentHelper.getEnchantments(player.getHeldItemMainhand())
                .containsKey(TakumiEnchantmentCore.MINESWEEPER) && (player.getHeldItemMainhand().getStrVsBlock(state) > 1.0f || this.getHarvestTool
                (state) == null))) {
            this.explode(worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
    }
    
    public void explode(World world, int x, int y, int z) {
        world.createExplosion(null, x + 0.5, y + 0.5, z + 0.5, 2, true);
    }
}
