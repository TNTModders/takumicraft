package com.tntmodders.takumi.entity.item;

import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.MoverType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityTakumiLauncher extends EntityLiving {
    public EntityTakumiLauncher(World worldIn) {
        super(worldIn);
        this.setNoGravity(true);
        this.setSize(1f, 1f);
        this.setInvisible(true);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.setInvisible(true);
        if (this.getPassengers().isEmpty()) {
            this.setDead();
        } else {
            this.move(MoverType.SELF, 0, 1, 0);
            if (!this.world.isRemote) {
                TakumiUtils.takumiCreateExplosion(this.world, this, this.posX, this.posY, this.posZ, 3f, false, true);
            }
            if (this.world.getBlockState(this.getPosition().up()).getBlockHardness(this.world,
                    this.getPosition().up()) < 0 || this.posY > 256) {
                this.setDead();
            }
        }
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
}
