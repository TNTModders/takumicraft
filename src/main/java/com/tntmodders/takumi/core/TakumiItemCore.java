package com.tntmodders.takumi.core;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.block.ITakumiItemBlock;
import com.tntmodders.takumi.item.*;
import com.tntmodders.takumi.item.ItemTakumiMineSweeperTool.EnumTakumiTool;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
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
    public static final Item TAKUMI_CREEPER_HELMET = new ItemTakumiArmor(EntityEquipmentSlot.HEAD);
    public static final Item TAKUMI_CREEPER_CHEST = new ItemTakumiArmor(EntityEquipmentSlot.CHEST);
    public static final Item TAKUMI_CREEPER_LEGS = new ItemTakumiArmor(EntityEquipmentSlot.LEGS);
    public static final Item TAKUMI_CREEPER_BOOTS = new ItemTakumiArmor(EntityEquipmentSlot.FEET);
    public static final Item TAKUMI_MINE_PICKAXE = new ItemTakumiMineSweeperTool(EnumTakumiTool.PICKAXE);
    public static final Item TAKUMI_MINE_SHOVEL = new ItemTakumiMineSweeperTool(EnumTakumiTool.SHOVEL);
    public static final Item TAKUMI_MINE_AXE = new ItemTakumiMineSweeperTool(EnumTakumiTool.AXE);
    public static final Item TAKUMI_PORTAL_KIT = new ItemTakumiPortalKit();
    
    public static List <Item> itemBlocks = new ArrayList <>();
    
    public static void register(IForgeRegistry <Item> registry) {
        Class <TakumiItemCore> clazz = TakumiItemCore.class;
        for (Field field : clazz.getFields()) {
            try {
                if (field.get(INSTANCE) instanceof Item) {
                    Item item = (Item) field.get(INSTANCE);
                    registry.register(item);
                    OreDictionary.registerOre(item.getRegistryName().getResourcePath(), item);
                    TakumiCraftCore.LOGGER.info("Registered Item : " + item.getUnlocalizedName());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        
        Class <TakumiBlockCore> clazz2 = TakumiBlockCore.class;
        for (Field field : clazz2.getFields()) {
            try {
                if (field.get(INSTANCE) instanceof Block) {
                    Block block = (Block) field.get(TakumiBlockCore.INSTANCE);
                    Item item = new ItemBlock(block);
                    if (block instanceof ITakumiItemBlock) {
                        item = ((ITakumiItemBlock) block).getItem();
                    }
                    item = item.setRegistryName(block.getRegistryName());
                    registry.register(item);
                    itemBlocks.add(item);
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
