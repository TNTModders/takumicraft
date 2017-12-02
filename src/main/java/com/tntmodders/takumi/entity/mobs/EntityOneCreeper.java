package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

public class EntityOneCreeper extends EntityTakumiAbstractCreeper {
    public EntityOneCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.GRASS_M;
    }

    @Override
    public int getExplosionPower() {
        return 5;
    }

    @Override
    public int getSecondaryColor() {
        return 0x0055aa;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "onecreeper";
    }

    @Override
    public int getRegisterID() {
        return 235;
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        for (Entity entity : event.getAffectedEntities()) {
            if (entity instanceof EntityLivingBase && !(entity instanceof EntityPlayer && ((EntityPlayer) entity).isCreative())) {
                ((EntityLivingBase) entity).setHealth(1);
            }
        }
        event.getAffectedEntities().clear();
        return true;
    }
}
