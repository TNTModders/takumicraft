package com.tntmodders.takumi.entity.item;

import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.util.math.RayTraceResult;
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
                for (int x = -25; x <= 25; x++) {
                    for (int y = -25; y <= 25; y++) {
                        for (int z = -25; z <= 25; z++) {
                            if (x * x + y * y + z * z <= 25 * 25 && this.rand.nextInt(75) == 0) {
                                TakumiUtils.takumiCreateExplosion(world, this, this.posX + x, this.posY + y, this.posZ + z, this.getPower(), true, this.getDestroy());
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
}
