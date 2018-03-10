package com.tntmodders.takumi.core;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

import java.lang.reflect.Field;

public class TakumiLootTableCore {
    private static final TakumiLootTableCore INSTANCE = new TakumiLootTableCore();

    public static final ResourceLocation TAKUMI_DARK_CHEST =
            new ResourceLocation(TakumiCraftCore.MODID, "chests/darkchest");

    public static void register() {
        Class<TakumiLootTableCore> clazz = TakumiLootTableCore.class;
        for (Field field : clazz.getFields()) {
            try {
                if (field.get(INSTANCE) instanceof ResourceLocation) {
                    ResourceLocation resourceLocation = (ResourceLocation) field.get(INSTANCE);
                    LootTableList.register(resourceLocation);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
