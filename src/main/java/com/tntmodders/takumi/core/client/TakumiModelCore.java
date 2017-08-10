package com.tntmodders.takumi.core.client;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.entity.ITakumiEntity;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;

@SideOnly(Side.CLIENT)
public class TakumiModelCore {
    public static void register() {
        Class clazz = TakumiItemCore.class;
        for (Field field : clazz.getFields()) {
            try {
                if (field.get(TakumiItemCore.INSTANCE) instanceof Item) {
                    Item item = ((Item) field.get(TakumiItemCore.INSTANCE));
                    String s = item.getUnlocalizedName().substring(5);
                    if (item.getHasSubtypes()) {
                        NonNullList<ItemStack> stacks = NonNullList.create();
                        item.getSubItems(TakumiCraftCore.TAB_CREEPER, stacks);
                        for(int i = 0; i < stacks.size(); i++){
                            ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(new ResourceLocation(TakumiCraftCore.MODID, s + "_" + i), "inventory"));
                            TakumiCraftCore.LOGGER.info("Registered item model with metadata" + i + " : " + s + "_" + i);
                        }
                    } else {
                        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ResourceLocation(TakumiCraftCore.MODID, s), "inventory"));
                        TakumiCraftCore.LOGGER.info("Registered item model: " + s);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        for (Item item : TakumiItemCore.itemBlocks) {
            String s = item.getUnlocalizedName().substring(5);
            if (item.getHasSubtypes()) {
                NonNullList<ItemStack> stacks = NonNullList.create();
                item.getSubItems(TakumiCraftCore.TAB_CREEPER, stacks);
                for(int i = 0; i < stacks.size(); i++){
                    ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(new ResourceLocation(TakumiCraftCore.MODID, s + "_" + i), "inventory"));
                    TakumiCraftCore.LOGGER.info("Registered block model with metadata" + i + " : " + s + "_" + i);
                }
            } else {
                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ResourceLocation(TakumiCraftCore.MODID, s), "inventory"));
                TakumiCraftCore.LOGGER.info("Registered block model: " + s);
            }
        }
    }

    public static void registerEntityRender(Class clazz, ITakumiEntity entity){
        RenderingRegistry.registerEntityRenderingHandler(clazz, new TakumiRenderFactory() {
            @Override
            public Render createRenderFor(RenderManager manager) {
                return entity.getRender(manager);
            }
        });
    }
}
