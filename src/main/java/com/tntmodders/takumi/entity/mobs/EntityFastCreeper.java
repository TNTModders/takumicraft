package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.asm.TakumiASMNameMap;
import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.item.Item;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

public class EntityFastCreeper extends EntityTakumiAbstractCreeper {

    public EntityFastCreeper(World worldIn) {
        super(worldIn);
        try {
            Field field = TakumiASMNameMap.getField(EntityCreeper.class, "fuseTime");
            field.setAccessible(true);
            field.set(this, 15);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    protected Item getDropItem() {
        return TakumiItemCore.TAKUMI_TYPE_SWORD_FIRE;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.5D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(100);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1000);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1);
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
        return EnumTakumiType.WIND;
    }

    @Override
    public int getExplosionPower() {
        return 10;
    }

    @Override
    public int getSecondaryColor() {
        return 11184810;
    }

    @Override
    public int getPrimaryColor() {
        return 0x223399;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "fastcreeper";
    }

    @Override
    public int getRegisterID() {
        return 282;
    }
}
