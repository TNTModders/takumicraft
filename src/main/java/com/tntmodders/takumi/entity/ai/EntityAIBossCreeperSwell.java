package com.tntmodders.takumi.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAICreeperSwell;
import net.minecraft.entity.monster.EntityCreeper;

public class EntityAIBossCreeperSwell extends EntityAICreeperSwell {
    private EntityCreeper swellingCreeper;

    public EntityAIBossCreeperSwell(EntityCreeper entitycreeperIn) {
        super(entitycreeperIn);
        this.swellingCreeper = entitycreeperIn;
    }

    @Override
    public boolean shouldExecute() {
        EntityLivingBase entitylivingbase = this.swellingCreeper.getAttackTarget();
        return this.swellingCreeper.getCreeperState() > 0 ||
                entitylivingbase != null && this.swellingCreeper.getDistanceSqToEntity(entitylivingbase) < 25.0D;
    }
}
