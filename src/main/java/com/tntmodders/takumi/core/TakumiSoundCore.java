package com.tntmodders.takumi.core;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class TakumiSoundCore {
    private static final ResourceLocation record_explosion_name = new ResourceLocation(TakumiCraftCore.MODID, "record_explosion");
    public static final SoundEvent RECORD_EXPLOSION = new SoundEvent(record_explosion_name).setRegistryName(record_explosion_name);

    public static void register(IForgeRegistry<SoundEvent> registry) {
        registry.register(RECORD_EXPLOSION);
    }
}
