package com.tntmodders.takumi.entity.mobs;

import net.minecraft.world.World;

public class EntityMiniSpiderCreeper extends EntitySpiderCreeper {
    public EntityMiniSpiderCreeper(World worldIn) {
        super(worldIn);
        this.setSize(1.4F / 4, 0.9F / 4);
        this.setHealth(4f);
    }

    @Override
    public float getEyeHeight() {
        return super.getEyeHeight() / 4;
    }

    @Override
    public boolean isCustomSpawn() {
        return true;
    }

    @Override
    public String getRegisterName() {
        return "minispidercreeper";
    }

    @Override
    public int getRegisterID() {
        return 24;
    }
}
