package com.tntmodders.takumi.entity.item;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityTakumiBoat extends EntityBoat {
    private float lastYd;
    public EntityTakumiBoat(World worldIn) {
        super(worldIn);
        this.isImmuneToFire = true;
    }

    public EntityTakumiBoat(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        this.isImmuneToFire = true;
    }

    @Override
    public float getWaterLevelAbove() {
        AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
        int i = MathHelper.floor(axisalignedbb.minX);
        int j = MathHelper.ceil(axisalignedbb.maxX);
        int k = MathHelper.floor(axisalignedbb.maxY);
        int l = MathHelper.ceil(axisalignedbb.maxY);
        int i1 = MathHelper.floor(axisalignedbb.minZ);
        int j1 = MathHelper.ceil(axisalignedbb.maxZ);
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

        try {
            label108:

            for (int k1 = k; k1 < l; ++k1) {
                float f = 0.0F;
                int l1 = i;

                while (true) {
                    if (l1 >= j) {
                        if (f < 1.0F) {
                            float f2 = (float) blockpos$pooledmutableblockpos.getY() + f;
                            return f2;
                        }

                        break;
                    }

                    for (int i2 = i1; i2 < j1; ++i2) {
                        blockpos$pooledmutableblockpos.setPos(l1, k1, i2);
                        IBlockState iblockstate = this.world.getBlockState(blockpos$pooledmutableblockpos);

                        if (/*iblockstate.getMaterial() == Material.WATER*/ iblockstate.getMaterial().isLiquid()) {
                            f = Math.max(f, BlockLiquid.getBlockLiquidHeight(iblockstate, this.world, blockpos$pooledmutableblockpos));
                        }

                        if (f >= 1.0F) {
                            continue label108;
                        }
                    }

                    ++l1;
                }
            }

            float f1 = (float) (l + 1);
            return f1;
        } finally {
            blockpos$pooledmutableblockpos.release();
        }
    }
}
