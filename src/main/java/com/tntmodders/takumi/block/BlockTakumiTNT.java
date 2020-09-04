package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.entity.item.EntityTakumiTNTPrimed;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockTakumiTNT extends BlockTNT {

    public BlockTakumiTNT() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "takumitnt");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("takumitnt");
    }

    @Override
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
        if (!worldIn.isRemote) {
            EntityTakumiTNTPrimed entitytntprimed =
                    new EntityTakumiTNTPrimed(worldIn, pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F,
                            explosionIn.getExplosivePlacedBy());
            entitytntprimed.setGriefing(true);
            entitytntprimed.setFuse(
                    (short) (worldIn.rand.nextInt(entitytntprimed.getFuse() / 4) + entitytntprimed.getFuse() / 8));
            worldIn.spawnEntity(entitytntprimed);
        }
    }

    @Override
    public void explode(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase igniter) {
        if (!worldIn.isRemote) {
            if (state.getValue(EXPLODE)) {
                EntityTakumiTNTPrimed entitytntprimed =
                        new EntityTakumiTNTPrimed(worldIn, pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F, igniter);
                entitytntprimed.setGriefing(true);
                worldIn.spawnEntity(entitytntprimed);
                worldIn.playSound(null, entitytntprimed.posX, entitytntprimed.posY, entitytntprimed.posZ,
                        SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }
    }
}
