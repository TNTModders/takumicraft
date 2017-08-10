package com.tntmodders.asm;

import com.tntmodders.takumi.entity.EntityTakumiAbstranctCreeper;
import net.minecraft.entity.monster.EntityCreeper;

import java.lang.reflect.Field;

public class TakumiASMHooks {
    public static void TakumiExplodeHook(EntityCreeper creeper) {
        try {
            if (creeper instanceof EntityTakumiAbstranctCreeper) {
                int i = ((EntityTakumiAbstranctCreeper) creeper).getExplosionPower();
                Field field = EntityCreeper.class.getDeclaredField("explosionRadius");
                field.setAccessible(true);
                field.set(creeper, i);
                ((EntityTakumiAbstranctCreeper) creeper).takumiExplode();
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
