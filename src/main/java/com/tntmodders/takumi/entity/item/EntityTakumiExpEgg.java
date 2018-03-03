package com.tntmodders.takumi.entity.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.world.World;

public class EntityTakumiExpEgg extends AbstractEntityTakumiGrenade {

    public EntityTakumiExpEgg(World worldIn) {
        super(worldIn);
    }

    public EntityTakumiExpEgg(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public int getPower() {
        int i = 3;
        if (this.getThrower() != null && this.getThrower() instanceof EntityCreeper &&
                ((EntityCreeper) this.getThrower()).getPowered()) {
            i = i * 2;
        }
        return i;
    }

    @Override
    public boolean getDestroy() {
        return true;
    }
}
