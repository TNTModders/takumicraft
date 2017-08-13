package com.tntmodders.tutorial;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AluminiumRecipeHolder {
    public final Map<ItemStack, List<ResourceLocation>> map = new ItemStackHashMap();

    public void register() {
       /* if (FMLCommonHandler.instance().getSide().isClient()) {
            File file = FMLCommonHandler.instance().findContainerFor(AluminiumMod.aluminiumInstance).getSource();
            String s = file.toURI().getPath();
            if (s.endsWith("jar")) {
                s = "jar:file:/" + s + "!";
            }
            s = s + "assets/aluminiummod/recipes/";
            File list = new File(s);
            if (list.listFiles().length > 0) {
                for (File recipe : list.listFiles()) {
                    InputStream stream = null;
                    try {
                        stream = recipe.toURI().toURL().openStream();
                        JsonReader reader = new JsonReader(new InputStreamReader(stream));
                        JsonObject jsonObject = new Gson().fromJson(reader, JsonObject.class);
                        String filename = recipe.getName();
                        ResourceLocation location = new ResourceLocation("aluminiummod", filename.substring(0, filename.length() - 5));

                        if (jsonObject.has("key") && jsonObject.getAsJsonObject("key").has("#")) {
                            String name = jsonObject.getAsJsonObject("key").getAsJsonObject("#").get("item").getAsString();
                            Item item = Item.getByNameOrId(name);
                            int i = 0;
                            if (jsonObject.getAsJsonObject("key").getAsJsonObject("#").has("data")) {
                                i = jsonObject.getAsJsonObject("key").getAsJsonObject("#").get("data").getAsInt();
                            }
                            ItemStack stack = new ItemStack(item, 1, i);
                            List<ResourceLocation> locations = MAP.containsKey(stack) ? MAP.get(stack) : new ArrayList<ResourceLocation>();
                            locations.add(location);
                            MAP.put(stack, locations);
                        } else if (jsonObject.has("ingredients") && jsonObject.getAsJsonArray("ingredients").get(0).getAsJsonObject().has("item")) {
                            String name = jsonObject.getAsJsonArray("ingredients").get(0).getAsJsonObject().get("item").getAsString();
                            Item item = Item.getByNameOrId(name);
                            int i = 0;
                            if (jsonObject.getAsJsonArray("ingredients").get(0).getAsJsonObject().has("data")) {
                                i = jsonObject.getAsJsonArray("ingredients").get(0).getAsJsonObject().get("data").getAsInt();
                            }
                            ItemStack stack = new ItemStack(item, 1, i);
                            List<ResourceLocation> locations = MAP.containsKey(stack) ? MAP.get(stack) : new ArrayList<ResourceLocation>();
                            locations.add(location);
                            MAP.put(stack, locations);
                        }
                        stream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }*/
    }

    public class ItemStackHashMap<K extends ItemStack, V extends List<ResourceLocation>> extends HashMap<K, V> {

        public V get(Object key) {
            if (key instanceof ItemStack && this.containsKey(key)) {
                for (Entry<K, V> entry : this.entrySet()) {
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
