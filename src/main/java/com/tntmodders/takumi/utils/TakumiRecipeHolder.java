package com.tntmodders.takumi.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class TakumiRecipeHolder {
    public static final Map<ItemStack, List<ResourceLocation>> MAP = new ItemStackHashMap();

    public void register() {
        if (FMLCommonHandler.instance().getSide().isClient()) {
            this.getResource("assets/takumicraft/recipes/");
        }
    }

    public void getResource(String path) {
        ClassLoader loader = TakumiCraftCore.class.getClassLoader();
        URL url = loader.getResource(path);
        //TakumiCraftCore.LOGGER.info(url);
        if (url.getProtocol().equals("jar")) {
            String[] strings = url.getPath().split(":");
            String leadPath = strings[strings.length - 1].split("!")[0];
            File f = new File(leadPath);
            JarFile jarFile;
            try {
                jarFile = new JarFile(f);
                Enumeration<JarEntry> enumeration = jarFile.entries();
                while (enumeration.hasMoreElements()) {
                    JarEntry entry = enumeration.nextElement();
                    String s = entry.getName();
                    if (s != null && s.startsWith(path) && s.endsWith(".json")) {
                        InputStream stream = null;
                        try {
                            stream = loader.getResourceAsStream(s);
                            this.readStream(stream, s);
                            stream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            TakumiCraftCore.LOGGER.info("takumicraft : recipe registered on jar " + MAP.toString());
        } else {
            List<File> list = TakumiUtils.getListFile(path);
            if (list.size() > 0) {
                for (File recipe : list) {
                    TakumiCraftCore.LOGGER.info(recipe);
                    InputStream stream = null;
                    try {
                        stream = new FileInputStream(recipe);
                        this.readStream(stream, recipe.getName());
                        stream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            TakumiCraftCore.LOGGER.info("takumicraft : recipe registered on source " + MAP.toString());
        }
    }

    private void readStream(InputStream stream, String name) {
        JsonReader reader = new JsonReader(new InputStreamReader(stream));
        JsonObject jsonObject = new Gson().fromJson(reader, JsonObject.class);
        ResourceLocation location = new ResourceLocation(TakumiCraftCore.MODID, name.replaceAll("assets/takumicraft/recipes/", "").replaceAll(".json", ""));

        if (jsonObject.getAsJsonObject("key").has("Q")) {
            Item item = Item.getByNameOrId(jsonObject.getAsJsonObject("key").getAsJsonObject("Q").get("item").getAsString());
            int i = 0;
            if (jsonObject.getAsJsonObject("key").getAsJsonObject("Q").has("data")) {
                i = jsonObject.getAsJsonObject("key").getAsJsonObject("Q").get("data").getAsInt();
            }
            ItemStack stack = new ItemStack(item, 1, i);
            List<ResourceLocation> locations = MAP.containsKey(stack) ? MAP.get(stack) : new ArrayList<ResourceLocation>();
            locations.add(location);
            MAP.put(stack, locations);
        }
    }

    public static class ItemStackHashMap<K extends ItemStack, V extends List<ResourceLocation>> extends HashMap<K, V> {

        public V get(Object key) {
            if (key instanceof ItemStack && this.containsKey(key)) {
                for (Map.Entry<K, V> entry : this.entrySet()) {
                    if (entry.getKey().getItem() == ((ItemStack) key).getItem() && entry.getKey().getMetadata() == ((ItemStack) key).getMetadata()) {
                        return entry.getValue();
                    }
                }
            }
            return null;
        }

        @Override
        public boolean containsKey(Object key) {
            if (key instanceof ItemStack) {
                ItemStack itemStack = ((ItemStack) key);
                for (ItemStack stack : this.keySet()) {
                    if (stack.getItem() == itemStack.getItem() && stack.getMetadata() == itemStack.getMetadata()) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
