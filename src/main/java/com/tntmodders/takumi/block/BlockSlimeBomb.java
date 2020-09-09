package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiEnchantmentCore;
import com.tntmodders.takumi.entity.mobs.EntitySlimeCreeper;
import net.minecraft.block.BlockSlime;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockSlimeBomb extends BlockSlime {

    public BlockSlimeBomb() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "creeperslimebomb");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("creeperslimebomb");
        this.setSoundType(SoundType.SLIME);
        this.setResistance(0f);
    }

    @Override
    public void onLanded(World worldIn, Entity entityIn) {
        entityIn.fallDistance = 0;
        if (entityIn.isSneaking()) {
            super.onLanded(worldIn, entityIn);
        } else if (entityIn.motionY < 0) {
            entityIn.motionY = -entityIn.motionY * 6;
            if (entityIn.motionY > 2.225) {
                entityIn.motionY = 2.225;
            }
            if (!(entityIn instanceof EntityLivingBase)) {
                entityIn.motionY *= 0.8D;
            } else if (entityIn.motionY > 1) {
                worldIn.createExplosion(entityIn, entityIn.posX, entityIn.posY - 0.5, entityIn.posZ, 0f, false);
            }
        }
    }

    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        entityIn.fall(fallDistance, 0.0F);
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
            for (int i = 0; i < 4; i++) {
                EntitySlimeCreeper slimeCreeper = new EntitySlimeCreeper(world);
                slimeCreeper.setSlimeSize(2, true);
                slimeCreeper.setPosition(x + 0.5, y + 0.5, z + 0.5);
                world.spawnEntity(slimeCreeper);
            }
            world.createExplosion(null, x + 0.5, y + 0.5, z + 0.5, 3f, true);
        } catch (Exception ignored) {
        }
    }
}
