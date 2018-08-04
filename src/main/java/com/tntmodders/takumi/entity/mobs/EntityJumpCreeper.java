package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.core.TakumiPotionCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

public class EntityJumpCreeper extends EntityTakumiAbstractCreeper {

    public EntityJumpCreeper(World worldIn) {
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
        return 4;
    }

    @Override
    public int getSecondaryColor() {
        return 0xffff00;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "jumpcreeper";
    }

    @Override
    public int getRegisterID() {
        return 260;
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        event.getAffectedEntities().forEach(entity -> {
            if (entity instanceof EntityLivingBase) {
                ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(TakumiPotionCore.EXP_JUMP, 400));
            }
        });
        return true;
    }

    @Override
    public int getPrimaryColor() {
        return 0x00ff00;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.getCreeperState() <= 0 && this.onGround) {
            this.jump();
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return source != DamageSource.FALL && super.attackEntityFrom(source, amount);
    }
}
