package com.tntmodders.asm;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.monster.EntityCreeper;

import java.lang.reflect.Field;

public class TakumiASMHooks {
    public static void TakumiExplodeHook(EntityCreeper creeper) {
        try {
            if (creeper instanceof EntityTakumiAbstractCreeper) {
                int i = ((EntityTakumiAbstractCreeper) creeper).getExplosionPower();
                Field field = TakumiASMNameMap.getField(EntityCreeper.class, "explosionRadius");
                field.setAccessible(true);
                field.set(creeper, i);
                ((EntityTakumiAbstractCreeper) creeper).takumiExplode();
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
