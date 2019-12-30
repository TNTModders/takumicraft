package com.tntmodders.takumi.entity.mobs.evo;

import com.tntmodders.takumi.entity.mobs.EntityDarkCreeper;
import net.minecraft.world.World;

public class EntityDarkCreeper_Evo extends EntityDarkCreeper {
    public EntityDarkCreeper_Evo(World worldIn) {
        super(worldIn);
    }

    @Override
    public int getExplosionPower() {
        return super.getExplosionPower() * 3;
    }

    @Override
    public boolean isCustomSpawn() {
        return true;
    }

    @Override
    public String getRegisterName() {
        return super.getRegisterName() + "_evo";
    }

    @Override
    public int getRegisterID() {
        return 294;
    }

    @Override
    public boolean isEvo() {
        return true;
    }
}
