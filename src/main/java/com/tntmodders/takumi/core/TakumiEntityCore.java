package com.tntmodders.takumi.core;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.client.TakumiModelCore;
import com.tntmodders.takumi.entity.ITakumiEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TakumiEntityCore {
    private static List<Biome> biomes = new ArrayList<>();

    public static void register() {
        for (Field fileld : Biomes.class.getDeclaredFields()) {
            try {
                TakumiEntityCore.biomes.add(((Biome) fileld.get(null)));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        File packFile = FMLCommonHandler.instance().findContainerFor(TakumiCraftCore.TakumiInstance).getSource();
        String s = packFile.toURI().getPath();
        if (s.endsWith("jar")) {
            s = s + "/";
        }
        String spack = s + ("com.tntmodders.takumi.entity.mobs".replace(".", "/"));
        List<File> files = Arrays.asList(new File(spack).listFiles());
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            String className = "com.tntmodders.takumi.entity.mobs." + file.getName().substring(0, file.getName().indexOf(".class"));
            try {
                Class clazz = Class.forName(className);
                ITakumiEntity entity = ((ITakumiEntity) clazz.getConstructor(World.class).newInstance(Minecraft.getMinecraft().world));
                ResourceLocation location = new ResourceLocation(TakumiCraftCore.MODID, entity.getRegisterName());
                EntityRegistry.registerModEntity(location, clazz, location.getResourcePath(), i, TakumiCraftCore.TakumiInstance, 64, 2, true,
                        entity.getPrimaryColor(), entity.getSecondaryColor());
                if (!entity.isCustomSpawn() && entity.takumiRank().getSpawnWeight() != 0) {
                    EntityRegistry.addSpawn(clazz, entity.takumiRank().getSpawnWeight(), 1, 20, EnumCreatureType.MONSTER, biomes.toArray(new Biome[0]));
                }
                if (FMLCommonHandler.instance().getSide().isClient()) {
                    TakumiModelCore.registerEntityRender(clazz, entity);
                }
                TakumiCraftCore.LOGGER.info("Registered entity : " + location.getResourcePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
