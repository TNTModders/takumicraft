package com.tntmodders.takumi.core;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.block.ITakumiItemBlock;
import com.tntmodders.takumi.entity.ITakumiEntity.EnumTakumiType;
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
    public static final Item TAKUMI_WATER_BUCKET = new ItemTakumiBucket(TakumiBlockCore.TAKUMI_WATER);
    public static final Item TAKUMI_BOW = new ItemTakumiBow();
    public static final ItemTakumiArrow TAKUMI_ARROW_HA = new ItemTakumiArrow("ha", 2, 1, false);
    public static final ItemTakumiArrow TAKUMI_ARROW_RETSU = new ItemTakumiArrow("retsu", 4, 1, false);
    public static final ItemTakumiArrow TAKUMI_ARROW_SAI = new ItemTakumiArrow("sai", 3, 1, true);
    public static final ItemTakumiArrow TAKUMI_ARROW_SAN = new ItemTakumiArrow("san", 4, 1, false);
    public static final ItemTakumiArrow TAKUMI_ARROW_KAN = new ItemTakumiArrow("kan", 4, 5, true);
    public static final ItemTakumiArrow TAKUMI_ARROW_BAKU = new ItemTakumiArrow("baku", 10, 5, true);
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
    public static final Item TAKUMI_CHOCO_BALL = new ItemTakumiChocolateBall();
    public static final Item KING_CORE = new ItemKingCore();
    public static final Item MAGIC_STONE = new ItemTakumiMagicStone();
    public static final Item TAKUMI_OFALEN = new ItemTakumiOfalen();
    public static final Item CREEPER_CORE = new ItemCreeperCore();
    public static final Item MAGIC_CREEPER_HELMET = new ItemMagicArmor(EntityEquipmentSlot.HEAD);
    public static final Item MAGIC_CREEPER_CHEST = new ItemMagicArmor(EntityEquipmentSlot.CHEST);
    public static final Item MAGIC_CREEPER_LEGS = new ItemMagicArmor(EntityEquipmentSlot.LEGS);
    public static final Item MAGIC_CREEPER_BOOTS = new ItemMagicArmor(EntityEquipmentSlot.FEET);
    public static final Item MAGIC_BOW = new ItemMagicBow();
    public static final Item ATTACK_BLOCK = new ItemAttackBlock();
    public static final Item TAKUMI_XMS = new ItemXMS();
    public static final Item TAKUMI_YMS = new ItemYMS();
    public static final Item TAKUMI_TYPE_CORE = new ItemTypeCore();
    public static final Item TAKUMI_TYPE_SWORD_FIRE = new ItemTypeSword(EnumTakumiType.FIRE);
    public static final Item TAKUMI_TYPE_SWORD_GRASS = new ItemTypeSword(EnumTakumiType.GRASS);
    public static final Item TAKUMI_TYPE_SWORD_WATER = new ItemTypeSword(EnumTakumiType.WATER);
    public static final Item TAKUMI_TYPE_SWORD_WIND = new ItemTypeSword(EnumTakumiType.WIND);
    public static final Item TAKUMI_TYPE_SWORD_GROUND = new ItemTypeSword(EnumTakumiType.GROUND);
    public static final Item TAKUMI_TYPE_SWORD_NORMAL = new ItemTypeSword(EnumTakumiType.NORMAL);
    public static final Item TAKUMI_TYPE_CORE_DEST = new ItemTypeCoreSP(false);
    public static final Item TAKUMI_TYPE_CORE_MAGIC = new ItemTypeCoreSP(true);
    public static final Item TAKUMI_BOWGUN = new ItemTakumiBowGun();
    public static final Item BATTLE_SHIELD = new ItemBattleShield(false);
    public static final Item BATTLE_SHIELD_POWERED = new ItemBattleShield(true);
    public static final Item BATTLE_CREEPER_HELMET = new ItemBattleArmor(false, EntityEquipmentSlot.HEAD);
    public static final Item BATTLE_CREEPER_CHEST = new ItemBattleArmor(false, EntityEquipmentSlot.CHEST);
    public static final Item BATTLE_CREEPER_LEGS = new ItemBattleArmor(false, EntityEquipmentSlot.LEGS);
    public static final Item BATTLE_CREEPER_BOOTS = new ItemBattleArmor(false, EntityEquipmentSlot.FEET);
    public static final Item BATTLE_CREEPER_HELMET_POWERED = new ItemBattleArmor(true, EntityEquipmentSlot.HEAD);
    public static final Item BATTLE_CREEPER_CHEST_POWERED = new ItemBattleArmor(true, EntityEquipmentSlot.CHEST);
    public static final Item BATTLE_CREEPER_LEGS_POWERED = new ItemBattleArmor(true, EntityEquipmentSlot.LEGS);
    public static final Item BATTLE_CREEPER_BOOTS_POWERED = new ItemBattleArmor(true, EntityEquipmentSlot.FEET);
    public static final Item ENERGY_CORE = new ItemEnergyCore();
    public static final Item TAKUMI_MLRS = new ItemMLRS();
    public static final Item MAKEUP = new ItemMakeup();
    public static final Item TESTER = new ItemTester();
    public static final Item THROW_GRENEDE = new ItemTakumiThrowGrenede();
    public static final Item EXP_PRO_PRI = new ItemExpProcPri();
    public static final Item EVO_CORE = new ItemEvoCore();
    public static final Item PARALYSIS_CORE = new ItemParalysisCore();
    public static final Item TAKUMI_MINECART = new ItemTakumiMinecart();
    //public static final Item TAKUMI_BOAT = new ItemTakumiBoat();
    public static final Item TOSSCREEPER_BOMB = new ItemTossCreeperBomb();
    public static final Item TAKUMI_PAINTING = new ItemTakumiPainting();
    public static final Item CREEPER_SIGN = new ItemTakumiSign();

    public static List<Item> itemBlocks = new ArrayList<>();

    public static void register(IForgeRegistry<Item> registry) {
        Class<TakumiItemCore> clazz = TakumiItemCore.class;
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

        Class<TakumiBlockCore> clazz2 = TakumiBlockCore.class;
        for (Field field : clazz2.getFields()) {
            try {
                if (field.get(INSTANCE) instanceof Block) {
                    Block block = (Block) field.get(TakumiBlockCore.INSTANCE);
                    Item item = new ItemBlock(block);
                    if (block instanceof ITakumiItemBlock && ((ITakumiItemBlock) block).getItem() != null) {
                        item = ((ITakumiItemBlock) block).getItem();
                    }
                    if (item != null) {
                        item = item.setRegistryName(block.getRegistryName());
                        registry.register(item);
                        itemBlocks.add(item);
                        TakumiCraftCore.LOGGER.info("Registered Item : " + block.getUnlocalizedName());
                        OreDictionary.registerOre(item.getRegistryName().getResourcePath(), item);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        Items.SPAWN_EGG.setCreativeTab(TakumiCraftCore.TAB_EGGS);
    }
}
