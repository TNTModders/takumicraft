package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

public class EntityKeepCreeper extends EntityTakumiAbstractCreeper {

    public EntityKeepCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.75D);
    }

    @Override
    public void takumiExplode() {
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        event.getAffectedEntities().forEach(entity -> {
            if (entity instanceof EntityPlayer) {
                ((EntityPlayer) entity).setLastAttackedEntity(this);
                entity.attackEntityFrom(DamageSource.causeMobDamage(this).setExplosion().setDamageIsAbsolute(), ((EntityPlayer) entity).getMaxHealth());
            }
        });
        return true;
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL;
    }

    @Override
    public int getExplosionPower() {
        return 4;
    }

    @Override
    public int getSecondaryColor() {
        return 0x5500ff;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "keepcreeper";
    }

    @Override
    public int getRegisterID() {
        return 84;
    }
}
