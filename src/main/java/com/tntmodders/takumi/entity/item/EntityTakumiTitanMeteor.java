package com.tntmodders.takumi.entity.item;

import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityTakumiTitanMeteor extends AbstractEntityTakumiGrenade {
    public EntityTakumiTitanMeteor(World worldIn) {
        super(worldIn);
        this.setSize(1f, 1f);
    }

    public EntityTakumiTitanMeteor(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        this.setSize(1f, 1f);
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
        this.count++;
        if (!this.world.isRemote) {
            if (this.count <= this.getCount()) {
                for (int x = -75; x <= 75; x++) {
                    for (int y = -25; y <= 25; y++) {
                        for (int z = -75; z <= 75; z++) {
                            if (x * x + 3 * y * y + z * z <= 75 * 75 && this.rand.nextInt(2500) == 0) {
                                TakumiUtils.takumiCreateExplosion(world, this, result.hitVec.x + x, result.hitVec.y + y, result.hitVec.z + z, this.getPower(), true, this.getDestroy());
                            }
                        }
                    }
                }
            } else {
                this.setDead();
            }
        }
    }


    @Override
    public void onUpdate() {
        super.onUpdate();
        this.setGlowing(true);
    }

    @Override
    public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn) {
        return blockStateIn.getBlockHardness(worldIn, pos) > 2000 ? 2000 : 0.25f;
    }
}
