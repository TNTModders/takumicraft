package com.tntmodders.takumi.entity.mobs.evo;

import net.minecraft.world.World;

public class EntityEvolutionCreeper_Evo extends com.tntmodders.takumi.entity.mobs.EntityEvolutionCreeper {
    public EntityEvolutionCreeper_Evo(World worldIn) {
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
        return 300;
    }

    @Override
    public boolean isEvo() {
        return true;
    }
}
