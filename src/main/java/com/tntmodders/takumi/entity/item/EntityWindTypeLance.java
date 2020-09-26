package com.tntmodders.takumi.entity.item;

import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityWindTypeLance extends EntityTippedArrow {
    public EntityWindTypeLance(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.8F);
    }

    public EntityWindTypeLance(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }

    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {
        if (!(raytraceResultIn.entityHit instanceof EntityPlayer)) {
            this.dismount();
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.setInvisible(true);
        if (!this.world.isRemote) {
            TakumiUtils.takumiCreateExplosion(this.world, this, this.posX, this.posY, this.posZ, 2f, false, false, 2);
            if (this.world.collidesWithAnyBlock(this.getEntityBoundingBox().grow(2)) || this.world.getWorldBorder().getClosestDistance(this) < 2.5 * 2.5) {
                this.dismount();
            }
        }
    }

    private void dismount() {
        Entity entity = this.getRidingEntity();
        if (entity != null) {
            entity.setPosition(this.prevPosX, this.prevPosY, this.prevPosZ);
        }
        this.dismountRidingEntity();
        this.setDead();
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn) {
        return blockStateIn.getBlockHardness(worldIn, pos) < 0 ? 1000000f : 0.1f;
    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }

    @Override
    public boolean shouldRiderSit() {
        return false;
    }

    @Override
    public boolean shouldDismountInWater(Entity rider) {
        return false;
    }

    @Override
    public double getMountedYOffset() {
        return 0;
    }
}
