package com.tntmodders.takumi.core;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.block.ITakumiMetaBlock;
import com.tntmodders.takumi.item.*;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TakumiItemCore {
    public static final TakumiItemCore INSTANCE = new TakumiItemCore();
    public static final Item TAKUMI_SHIELD = new ItemTakumiShield();
    public static final Item TAKUMI_BOOK = new ItemTakumiBook();
    public static final Item TAKUMI_BUCKET = new ItemTakumiBucket(Blocks.AIR);
    public static final Item TAKUMI_SPRING_BUCKET = new ItemTakumiBucket(TakumiBlockCore.HOT_SPRING);
    public static final Item TAKUMI_BOW = new ItemTakumiBow();
    public static final ItemTakumiArrow TAKUMI_ARROW_HA = new ItemTakumiArrow("ha", 2, 1, false);
    public static final Item TAKUMI_SWORD = new ItemTakumiSword();
    public static final Item TAKUMI_BOLT_STONE = new ItemTakumiBoltStone();
    public static List<Item> itemBlocks = new ArrayList<>();

    public static void register(IForgeRegistry<Item> registry) {
        Class clazz = TakumiItemCore.class;
        for (Field field : clazz.getFields()) {
            try {
                if (field.get(TakumiItemCore.INSTANCE) instanceof Item) {
                    Item item = ((Item) field.get(TakumiItemCore.INSTANCE));
                    registry.register(item);
                    OreDictionary.registerOre(item.getRegistryName().getResourcePath(), item);
                    TakumiCraftCore.LOGGER.info("Registered Item : " + item.getUnlocalizedName());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        clazz = TakumiBlockCore.class;
        for (Field field : clazz.getFields()) {
            try {
                if (field.get(TakumiItemCore.INSTANCE) instanceof Block) {
                    Block block = ((Block) field.get(TakumiBlockCore.INSTANCE));
                    Item item = new ItemBlock(block);
                    if (block instanceof ITakumiMetaBlock) {
                        item = ((ITakumiMetaBlock) block).getItem();
                    }
                    item = item.setRegistryName(block.getRegistryName());
                    registry.register(item);
                    TakumiItemCore.itemBlocks.add(item);
                    TakumiCraftCore.LOGGER.info("Registered Item : " + block.getUnlocalizedName());
                    OreDictionary.registerOre(item.getRegistryName().getResourcePath(), item);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        Items.SPAWN_EGG.setCreativeTab(TakumiCraftCore.TAB_EGGS);
    }
}
