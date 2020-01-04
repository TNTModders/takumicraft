package com.tntmodders.takumi.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class TakumiRecipeHolder {

    public void register() {
        if (FMLCommonHandler.instance().getSide().isClient()) {
            this.getResource("assets/takumicraft/recipes/");
        }
    }

    public void getResource(String path) {
        ClassLoader loader = TakumiCraftCore.class.getClassLoader();
        URL url = loader.getResource(path);
        //TakumiCraftCore.LOGGER.info(url);
        if (!Objects.equals(url.getProtocol(), "jar")) {
            List<File> list = TakumiUtils.getListFile(path);
            if (list.size() > 0) {
                for (File recipe : list) {
                    //TakumiCraftCore.LOGGER.info(recipe);
                    InputStream stream;
                    File packFile =
                            FMLCommonHandler.instance().findContainerFor(TakumiCraftCore.TakumiInstance).getSource();
                    File oldFile = null;
                    String assetS = "assets/takumicraft/advancements/recipes/";
                    for (File f : TakumiUtils.getListFile(assetS)) {
                        if (f.getName().contains("recipe_.json")) {
                            oldFile = f;
                            break;
                        }
                    }
                    if (oldFile != null) {
                        //ClassLoader loader = TakumiCraftCore.class.getClassLoader();
                        //URL url = loader.getResource(assetS);
                        if (!Objects.equals(url.getProtocol(), "jar")) {
                            String[] strings = {oldFile.getAbsolutePath().replaceAll(".json", ""),
                                    oldFile.getAbsolutePath().split("out")[0] + "src" +
                                            oldFile.getAbsolutePath().split("out")[1].replaceAll("production",
                                                    "main").replaceAll("minecraft", "resources").replaceAll(".json",
                                                    "")};
                            for (String sPath : strings) {
                                String sResource =
                                        sPath + recipe.getName().replaceAll("assets/takumicraft/recipes/", "");
                                File file = new File(sResource);
                                try {
                                    file.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                FileReader h_fr;
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
                                        h_s = h_s.replaceAll("takumicraft:skull", "takumicraft:" +
                                                recipe.getName().replaceAll("assets/takumicraft/recipes/",
                                                        "").replaceAll("" + ".json", ""));
                                        try {
                                            stream = new FileInputStream(recipe);
                                            h_s = h_s.replaceAll("minecraft:gunpowder", this.getItem(stream,
                                                    recipe.getName().replaceAll("assets/takumicraft/recipes/",
                                                            "").replaceAll("" + ".json", "")));
                                            stream.close();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
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
        }
    }

    private String getItem(InputStream stream, String name) {
        JsonReader reader = new JsonReader(new InputStreamReader(stream));
        JsonObject jsonObject = new Gson().fromJson(reader, JsonObject.class);
        ResourceLocation location = new ResourceLocation(TakumiCraftCore.MODID,
                name.replaceAll("assets/takumicraft/recipes/", "").replaceAll("" + ".json", ""));
        if (jsonObject.getAsJsonObject("key").has("Q")) {
            return jsonObject.getAsJsonObject("key").getAsJsonObject("Q").get("item").getAsString();
        }
        return "";
    }
}
