package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.core.TakumiEnchantmentCore;
import com.tntmodders.takumi.entity.mobs.EntityHuskCreeper;
import net.minecraft.block.BlockCake;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.Random;

public class BlockMeatBomb extends BlockCake implements ITakumiSPBomb {

    public BlockMeatBomb() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "creepermeatbomb");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("creepermeatbomb");
        this.setResistance(0f);
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
            world.createExplosion(null, x + 0.5, y + 0.5, z + 0.5, 5f, true);
            for (int i = 0; i < 3; i++) {
                EntityHuskCreeper huskCreeper = new EntityHuskCreeper(world);
                huskCreeper.setPosition(x + 0.5, y + 0.5, z + 0.5);
                world.spawnEntity(huskCreeper);
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            return this.eatCake(worldIn, pos, state, playerIn);
        } else {
            ItemStack itemstack = playerIn.getHeldItem(hand);
            return this.eatCake(worldIn, pos, state, playerIn) || itemstack.isEmpty();
        }
    }

    private boolean eatCake(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (!player.canEat(false)) {
            return false;
        } else {
            player.addStat(StatList.CAKE_SLICES_EATEN);
            player.getFoodStats().addStats(10, 0.75F);
            int i = state.getValue(BITES);

            if (i < 6) {
                worldIn.setBlockState(pos, state.withProperty(BITES, i + 1), 3);
            } else {
                worldIn.setBlockToAir(pos);
                this.explode(worldIn, pos.getX(), pos.getY(), pos.getZ());
            }

            return true;
        }
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(TakumiBlockCore.TAKUMI_BOMB_MEAT);
    }
}
