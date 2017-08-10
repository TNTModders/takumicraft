package com.tntmodders.takumi.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TakumiRecipeHolder {
    public final Map<ItemStack, List<ResourceLocation>> map = new ItemStackHashMap();

    public void register() {
        if (FMLCommonHandler.instance().getSide().isClient()) {
            File file = FMLCommonHandler.instance().findContainerFor(TakumiCraftCore.TakumiInstance).getSource();
            String s = file.toURI().getPath();
            if (s.endsWith("jar")) {
                s = s + "/";
            }
            s = s + "assets/takumicraft/recipes/";
            TakumiCraftCore.LOGGER.info("TakumiCraft : searching recipes from " + s);
            File list = new File(s);
            if (list.listFiles().length > 0) {
                for (File recipe : list.listFiles()) {
                    InputStream stream = null;
                    try {
                        stream = recipe.toURI().toURL().openStream();
                        JsonReader reader = new JsonReader(new InputStreamReader(stream));
                        JsonObject jsonObject = new Gson().fromJson(reader, JsonObject.class);
                        String filename = recipe.getName();
                        ResourceLocation location = new ResourceLocation(TakumiCraftCore.MODID, filename.substring(0, filename.length() - 5));

                        if (jsonObject.getAsJsonObject("key").has("Q")) {
                            Item item = Item.getByNameOrId(jsonObject.getAsJsonObject("key").getAsJsonObject("Q").get("item").getAsString());
                            int i = 0;
                            if (jsonObject.getAsJsonObject("key").getAsJsonObject("Q").has("data")) {
                                i = jsonObject.getAsJsonObject("key").getAsJsonObject("Q").get("data").getAsInt();
                            }
                            ItemStack stack = new ItemStack(item, 1, i);
                            List<ResourceLocation> locations = map.containsKey(stack) ? map.get(stack) : new ArrayList<ResourceLocation>();
                            locations.add(location);
                            map.put(stack, locations);
                        }
                        stream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        TakumiCraftCore.LOGGER.info(map.toString());
    }

    public class ItemStackHashMap<K extends ItemStack, V extends List<ResourceLocation>> extends HashMap<K, V> {

        public V get(Object key) {
            if (key instanceof  ItemStack && this.containsKey(key)) {
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
