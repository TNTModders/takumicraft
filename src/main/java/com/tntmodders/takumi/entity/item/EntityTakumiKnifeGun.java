package com.tntmodders.takumi.entity.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityTakumiKnifeGun extends AbstractEntityTakumiGrenade {
    public EntityTakumiKnifeGun(World worldIn) {
        super(worldIn);
        this.setSize(0.5f, 0.5f);
    }

    public EntityTakumiKnifeGun(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        this.setSize(0.5f, 0.5f);
    }

    public EntityTakumiKnifeGun(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
        this.setSize(0.5f, 0.5f);
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public int getPower() {
        return 7;
    }

    @Override
    public boolean getDestroy() {
        return false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.ticksExisted > 200) {
            this.setDead();
        }
    }
}
