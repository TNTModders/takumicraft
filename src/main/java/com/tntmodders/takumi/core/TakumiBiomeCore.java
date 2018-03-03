package com.tntmodders.takumi.core;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.world.biome.*;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.IForgeRegistry;

import java.lang.reflect.Field;

public class TakumiBiomeCore {

    public static final TakumiBiomeCore INSTANCE = new TakumiBiomeCore();

    public static final Biome TAKUMI_PLAINS = new BiomeTakumiPlains();
    public static final Biome TAKUMI_LAVA_MOUNTAINS = new BiomeTakumiMountains(true);
    public static final Biome TAKUMI_OCEAN = new BiomeTakumiOcean();
    public static final Biome TAKUMI_MOUNTAINS = new BiomeTakumiMountains();
    public static final Biome TAKUMI_HOTSPRING_MOUNTAINS = new BiomeTakumiHotSpringMountains();
    public static final Biome TAKUMI_FOREST = new BiomeTakumiForest();
    public static final Biome TAKUMI_OBJET = new BiomeTakumiObjet();
    public static final Biome TAKUMI_MESA = new BiomeTakumiMesa();

    public static void register(IForgeRegistry<Biome> registry) {
        Class clazz = TakumiBiomeCore.class;
        for (Field field : clazz.getFields()) {
            try {
                if (field.get(INSTANCE) instanceof Biome) {
                    Biome biome = (Biome) field.get(INSTANCE);
                    registry.register(biome);
                    TakumiCraftCore.LOGGER.info("Registered Biome : " + biome.getBiomeName());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
