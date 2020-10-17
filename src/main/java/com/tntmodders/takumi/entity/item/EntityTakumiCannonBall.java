package com.tntmodders.takumi.entity.item;

import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityTakumiCannonBall extends AbstractEntityTakumiGrenade {
    public EntityTakumiCannonBall(World worldIn) {
        super(worldIn);
    }

    public EntityTakumiCannonBall(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public EntityTakumiCannonBall(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public int getPower() {
        return 10;
    }

    @Override
    public boolean getDestroy() {
        return true;
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (!(result.entityHit instanceof EntityTakumiCannon || result.entityHit instanceof EntityPlayer)) {
            if (!this.world.isRemote) {
                TakumiUtils.takumiCreateExplosion(world, this, this.posX, this.posY, this.posZ, this.getPower(), false,
                        this.getDestroy(), 5, false);
            }
            this.setDead();
        }
    }

    @Override
    public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn) {
        return blockStateIn.getBlockHardness(worldIn, pos) < 0 ? super.getExplosionResistance(explosionIn, worldIn, pos, blockStateIn)
                : super.getExplosionResistance(explosionIn, worldIn, pos, blockStateIn) / 10;
    }
}
