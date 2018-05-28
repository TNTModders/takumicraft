package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.asm.TakumiASMNameMap;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.lang.reflect.Field;

public class EntityNoobCreeper extends EntityTakumiAbstractCreeper {

    public EntityNoobCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.getCreeperState() > 0) {
            this.addPotionEffect(new PotionEffect(MobEffects.SPEED, 5, 5));
            if (this.getAttackTarget() != null) {
                this.getLookHelper().setLookPositionWithEntity(this.getAttackTarget(), 1f, 1f);
            }
            int i = 5;
            this.moveHelper.setMoveTo(this.posX + this.getLookVec().x * i, this.posY + this.getLookVec().y * i,
                    this.posZ + this.getLookVec().z * i, 1.5);
            if (this.onGround) {
                this.jump();
            }
        }
        int i = 0;
        try {
            Field field = TakumiASMNameMap.getField(EntityCreeper.class, "timeSinceIgnited");
            field.setAccessible(true);
            i = ((int) field.get(this));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if (i > 25) {
            this.setCreeperState(-1);
        }
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        if (!this.world.isRemote) {
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, (this.getPowered() ? 5f : 3f), true);
        }
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
        return 3;
    }

    @Override
    public int getSecondaryColor() {
        return 0x22ff00;
    }

    @Override
    public int getPrimaryColor() {
        return 0x00aa22;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "noobcreeper";
    }

    @Override
    public int getRegisterID() {
        return 55;
    }
}
