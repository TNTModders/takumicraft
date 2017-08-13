package com.tntmodders.takumi.core;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.client.TakumiModelCore;
import com.tntmodders.takumi.entity.ITakumiEntity;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TakumiEntityCore {
    private static List<Biome> biomes = new ArrayList<>();
    public static List<ITakumiEntity> entityList = new ArrayList<>();

    public static void register() {
        for (Field fileld : Biomes.class.getDeclaredFields()) {
            try {
                TakumiEntityCore.biomes.add(((Biome) fileld.get(null)));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        TakumiEntityCore.biomes.remove(Biomes.HELL);
        TakumiEntityCore.biomes.remove(Biomes.VOID);

        List<File> files = TakumiUtils.getListFile("com/tntmodders/takumi/entity/mobs/");
        TakumiCraftCore.LOGGER.info(files);
        ArrayList<EntityHolder> entityHolders = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            try {
                ClassLoader loader = TakumiCraftCore.class.getClassLoader();
                //TakumiCraftCore.LOGGER.info(TakumiEntityCore.class.getName() + " : " + "com.tntmodders.takumi.entity.mobs." + file.getName().replaceAll(".class", ""));
                Class clazz = loader.loadClass("com.tntmodders.takumi.entity.mobs." + file.getName().replaceAll(".class", ""));
                //TakumiCraftCore.LOGGER.info(TakumiEntityCore.class.getName() + " : " + clazz);
                ITakumiEntity entity = ((ITakumiEntity) clazz.getConstructor(World.class).newInstance(Minecraft.getMinecraft().world));
                //TakumiCraftCore.LOGGER.info(entity);
                entityHolders.add(new EntityHolder(clazz, entity));
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
        entityHolders.sort(new EntityComparator());
        TakumiCraftCore.LOGGER.info(entityHolders);
        for (EntityHolder holder : entityHolders) {
            Class clazz = holder.clazz;
            ITakumiEntity entity = holder.entity;
            ResourceLocation location = new ResourceLocation(TakumiCraftCore.MODID, entity.getRegisterName());
            EntityRegistry.registerModEntity(location, clazz, location.getResourcePath(), entity.getRegisterID(), TakumiCraftCore.TakumiInstance, 64, 2, true,
                    entity.getPrimaryColor(), entity.getSecondaryColor());
            if (!entity.isCustomSpawn() && entity.takumiRank().getSpawnWeight() != 0) {
                EntityRegistry.addSpawn(clazz, entity.takumiRank().getSpawnWeight(), 1, 20, EnumCreatureType.MONSTER, biomes.toArray(new Biome[0]));
            }
            if (FMLCommonHandler.instance().getSide().isClient()) {
                TakumiModelCore.registerEntityRender(clazz, entity);
            }
            TakumiEntityCore.entityList.add(entity);
            TakumiCraftCore.LOGGER.info("Registered entity on ID " + entity.getRegisterID() + " : " + location.getResourcePath() + " , " + entity.takumiRank().name() + " and " + entity.takumiType().name());

            File packFile = FMLCommonHandler.instance().findContainerFor(TakumiCraftCore.TakumiInstance).getSource();
            File oldFile = null;
            for (File f : TakumiUtils.getListFile("assets/takumicraft/advancements/")) {
                if (f.getName().contains("slay_.json")) {
                    oldFile = f;
                    break;
                }
            }
            if (oldFile != null) {
                ClassLoader loader = TakumiCraftCore.class.getClassLoader();
                URL url = loader.getResource("assets/takumicraft/advancements/");
                if (!url.getProtocol().equals("jar")) {
                    String[] strings = {oldFile.getAbsolutePath().replaceAll(".json", ""),
                            oldFile.getAbsolutePath().split("out")[0] + "src" + oldFile.getAbsolutePath().split("out")[1].replaceAll("production", "main")
                                    .replaceAll("forge1.12", "resources").replaceAll(".json", "")};
                    for (String sPath : strings) {
                        String sResource = sPath + entity.getRegisterName() + ".json";
                        File file = new File(sResource);
                        //TakumiCraftCore.LOGGER.info(TakumiEntityCore.class.getName() + " : " + sResource + " : " + file.toString());
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        FileReader h_fr = null;
                        String buf = "";

                        String h_s;
                        try {
                            h_fr = new FileReader(oldFile);
                            BufferedReader h_br = new BufferedReader(h_fr);
                            while (true) {
                                h_s = h_br.readLine();
                                if (h_s == null) {
                                    break;
                                }

                                h_s = h_s.replaceAll("minecraft:creeper", "takumicraft:" + entity.getRegisterName());
                                h_s = h_s.replaceAll("creeper_hoge", entity.getRegisterName());
                                buf = buf + h_s;
                            }
                            h_fr.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            FileWriter writer = new FileWriter(file);
                            writer.write(buf);
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }


    static class EntityComparator implements Comparator<EntityHolder> {
        @Override
        public int compare(EntityHolder o1, EntityHolder o2) {
            return o1.entity.getRegisterID() < o2.entity.getRegisterID() ? -1 : 1;
        }
    }

    static class EntityHolder {
        final Class clazz;
        final ITakumiEntity entity;

        EntityHolder(Class cls, ITakumiEntity ent) {
            this.clazz = cls;
            this.entity = ent;
        }
    }
}
