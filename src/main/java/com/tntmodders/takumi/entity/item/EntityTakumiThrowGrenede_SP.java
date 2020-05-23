package com.tntmodders.takumi.entity.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityTakumiThrowGrenede_SP extends EntityTakumiThrowGrenede{
    public EntityTakumiThrowGrenede_SP(World worldIn) {
        super(worldIn);
    }

    public EntityTakumiThrowGrenede_SP(World worldIn, EntityLivingBase throwerIn) {
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
