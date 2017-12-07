package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiEnchantmentCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockTakumiCreeperBomb extends Block {
    
    public BlockTakumiCreeperBomb() {
        super(Material.TNT);
        this.setRegistryName(TakumiCraftCore.MODID, "creeperbomb");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("creeperbomb");
        this.setResistance(0f);
        this.setHardness(0.5f);
        this.setLightLevel(1f);
    }
    
    @Override
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
        if (!worldIn.isRemote) {
            this.explode(worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
    }
    
    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (!worldIn.isRemote && !(player.getHeldItemMainhand() != null && EnchantmentHelper
                .getEnchantments(player.getHeldItemMainhand()).containsKey(TakumiEnchantmentCore
                        .MINESWEEPER) && (player.getHeldItemMainhand().getStrVsBlock(state) > 1.0f || this
                .getHarvestTool(state) == null))) {
            this.explode(worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
    }
    
    public void explode(World world, int x, int y, int z) {
        world.createExplosion(null, x + 0.5, y + 0.5, z + 0.5, getPower(), true);
    }
    
    float getPower() {
        return 5f;
    }
}
