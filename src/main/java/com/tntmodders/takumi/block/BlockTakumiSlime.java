package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockSlime;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockTakumiSlime extends BlockSlime {

    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.125D, 0.125D, 0.125D, 0.875D, 0.875D, 0.875D);

    public BlockTakumiSlime() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "creeperslimeblock");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("creeperslimeblock");
        this.setSoundType(SoundType.SLIME);
        this.setResistance(10000000f);
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return AABB;
    }

    @Override
    public void onLanded(World worldIn, Entity entityIn) {
        entityIn.fallDistance = 0;
        if (entityIn.isSneaking()) {
            super.onLanded(worldIn, entityIn);
        } else if (entityIn.motionY < 0.0D) {
            entityIn.motionY = -entityIn.motionY * 2;
            if (entityIn.motionY > 10) {
                entityIn.motionY = 10;
            }
            if (!(entityIn instanceof EntityLivingBase)) {
                entityIn.motionY *= 0.8D;
            } else if (entityIn.motionY > 1) {
                worldIn.createExplosion(entityIn, entityIn.posX, entityIn.posY - 0.5, entityIn.posZ, 0f, false);
            }
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
    }
}
