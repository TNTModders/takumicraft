package com.tntmodders.takumi.entity.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityTakumiThrowGrenede extends AbstractEntityTakumiGrenade {
    public EntityTakumiThrowGrenede(World worldIn) {
        super(worldIn);
    }

    public EntityTakumiThrowGrenede(World worldIn, EntityLivingBase throwerIn) {
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
        return false;
    }
}
