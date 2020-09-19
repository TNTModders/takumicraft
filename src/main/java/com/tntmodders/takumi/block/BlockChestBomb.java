package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiEnchantmentCore;
import com.tntmodders.takumi.entity.item.EntityTakumiParachute;
import com.tntmodders.takumi.entity.item.EntityTakumiRandomChest;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockChestBomb extends Block implements ITakumiSPBomb {

    public BlockChestBomb() {
        super(Material.WOOD);
        this.setRegistryName(TakumiCraftCore.MODID, "creeperchestbomb");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("creeperchestbomb");
        this.setSoundType(SoundType.WOOD);
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
            for (int dy = Math.min(world.getHeight(x, z) + 100, 256); dy <= 256; dy++) {
                if (!world.collidesWithAnyBlock(new AxisAlignedBB(x - 2, dy - 2, z - 2, x + 2, dy + 2, z + 2))) {
                    EntityTakumiRandomChest chest = new EntityTakumiRandomChest(world);
                    chest.setPosition(x + 0.5, dy, z + 0.5);
                    chest.spawnedByBomb = true;
                    world.spawnEntity(chest);
                    EntityTakumiParachute parachute = new EntityTakumiParachute(world);
                    parachute.setPosition(x + 0.5, dy, z + 0.5);
                    world.spawnEntity(parachute);
                    chest.startRiding(parachute, true);
                    break;
                }
            }
            world.createExplosion(null, x + 0.5, y + 0.5, z + 0.5, 5f, true);
        } catch (Exception ignored) {
        }
    }
}
