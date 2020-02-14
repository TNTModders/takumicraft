package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;

public class EntityShrinkCreeper extends EntityTakumiAbstractCreeper {

    public EntityShrinkCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.5);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (isShrinked()) {
            this.setSize(0.6f / 2f, 1.7f / 2f);
        } else {
            this.setSize(0.6f, 1.7f);
        }
    }

    @Override
    public void takumiExplode() {
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.WIND;
    }

    @Override
    public int getExplosionPower() {
        return 5;
    }

    @Override
    public int getSecondaryColor() {
        return 0xaaff99;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "shrinkcreeper";
    }

    @Override
    public int getRegisterID() {
        return 85;
    }

    @Override
    public double getSizeAmp() {
        return isShrinked() ? 0.5 : 1;
    }


    private boolean isShrinked() {
        return this.getAttackTarget() != null || this.getCreeperState() > 0 || this.hasIgnited();
    }

}
