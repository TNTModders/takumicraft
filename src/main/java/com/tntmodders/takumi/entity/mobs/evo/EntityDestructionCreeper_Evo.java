package com.tntmodders.takumi.entity.mobs.evo;

import com.tntmodders.takumi.entity.mobs.EntityDestructionCreeper;
import net.minecraft.world.World;

public class EntityDestructionCreeper_Evo extends EntityDestructionCreeper {
    public EntityDestructionCreeper_Evo(World worldIn) {
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
        return 292;
    }

    @Override
    public boolean isEvo() {
        return true;
    }
}
