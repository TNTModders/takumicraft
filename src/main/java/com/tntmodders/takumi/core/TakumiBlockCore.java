package com.tntmodders.takumi.core;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.block.*;
import com.tntmodders.takumi.entity.mobs.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.material.Material;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.init.Items;
import net.minecraftforge.registries.IForgeRegistry;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class TakumiBlockCore {

    public static final TakumiBlockCore INSTANCE = new TakumiBlockCore();

    public static final Block CREEPER_BOMB = new BlockTakumiCreeperBomb();
    public static final Block GUNORE = new BlockTakumiGunOre();
    public static final Block HOT_SPRING = new BlockTakumiHotSpring();
    public static final Block TAKUMI_WATER = new BlockTakumiWater();
    public static final Block CREEPER_BRICK =
            new BlockTakumiAntiExplosion(Material.SAND, "creeperbrick", 2.5f, "pickaxe");
    public static final Block CREEPER_IRON = new BlockTakumiAntiExplosion(Material.IRON, "creeperiron", 5f, "pickaxe");
    public static final Block DUMMY_GUNORE = new BlockTakumiDummyGunOre();
    public static final Block CREEPER_LOG = new BlockTakumiLog();
    public static final Block CREEPER_LEAVES = new BlockTakumiLeaf();
    public static final Block CREEPER_GLASS = new BlockTakumiGlass();
    public static final Block CREEPER_STAINED_GLASS = new BlockTakumiStainedGlass();
    public static final Block CREEPER_GLASS_PANE = new BlockTakumiGlassPane();
    public static final BlockTakumiStainedGlassPane CREEPER_STAINED_GLASS_PANE = new BlockTakumiStainedGlassPane();
    public static final Block CREEPER_WOOL = new BlockTakumiWool();
    public static final Block CREEPER_IRON_STAIRS = new BlockTakumiAntiExplosionStairs(CREEPER_IRON.getDefaultState(),
            Material.IRON, "creeperiron_stairs", 5f, "pickaxe");
    public static final Block CREEPER_BRICK_STAIRS = new BlockTakumiAntiExplosionStairs(CREEPER_BRICK.getDefaultState(),
            Material.SAND, "creeperbrick_stairs", 2.5f, "pickaxe");
    public static final Block CREEPER_PLANKS = new BlockTakumiAntiExplosion(Material.WOOD, "creeperplanks", 1.0f, "axe");
    public static final Block CREEPER_PLANKS_STAIRS = new BlockTakumiAntiExplosionStairs(CREEPER_PLANKS.getDefaultState(),
            Material.WOOD, "creeperplanks_stairs", 1.0f, "axe");
    public static final Block CREEPER_BRICK_SLAB = new BlockTakumiAntiExplosionSlab(Material.SAND, "creeperbrick_slab", 2.5f, "pickaxe");
    public static final Block CREEPER_IRON_SLAB = new BlockTakumiAntiExplosionSlab(Material.IRON, "creeperiron_slab", 5f, "pickaxe");
    public static final Block CREEPER_PLANKS_SLAB = new BlockTakumiAntiExplosionSlab(Material.WOOD, "creeperplanks_slab", 1.0f, "axe");
    public static final Block CREEPER_CARPET = new BlockTakumiCarpet();
    public static final Block CREEPER_IRON_WALL = new BlockTakumiAntiExplosionWall(CREEPER_IRON, Material.IRON,
            "creeperiron_wall", 5f, "pickaxe");
    public static final Block CREEPER_BRICK_WALL = new BlockTakumiAntiExplosionWall(CREEPER_BRICK, Material.SAND,
            "creeperbrick_wall", 2.5f, "pickaxe");
    public static final Block CREEPER_FENCE = new BlockTakumiFence(Material.WOOD, "creeperfence", 1.0f, "axe");
    public static final Block CREEPER_FENCEGATE = new BlockTakumiFenceGate(Material.WOOD, "creeperfence_gate", 1.0f, "axe");
    public static final Block CREEPER_IRONBARS = new BlockTakumiIronBars(Material.IRON, "creeperironbars", 5f, "pickaxe");

    //Util Blocks
    public static final Block CREEPER_VAULT = new BlockTakumiVault();
    public static final Block CREEPER_TORCH = new BlockTakumiTorch();
    public static final Block CREEPER_ILLUMINATION = new BlockTakumiIllumination();
    public static final Block CREEPER_LANTERN = new BlockTakumiLantern();
    public static final Block CREEPER_LADDER = new BlockTakumiLadder();
    public static final Block CREEPER_BED = new BlockTakumiBed();
    public static final Block CREEPER_IRON_DOOR = new BlockTakumiDoor(Material.IRON, "creeperiron_door", 5f, "pickaxe");
    public static final Block CREEPER_PLANKS_DOOR = new BlockTakumiDoor(Material.WOOD, "creeperplanks_door", 1f, "axe");

    //Rails
    public static final Block CREEPER_RAIL = new BlockTakumiRail();
    public static final Block CREEPER_RAIL_POWERED = new BlockTakumiRailPowered(false);
    public static final Block CREEPER_RAIL_ACTIVATOR = new BlockTakumiRailPowered(true);
    public static final Block CREEPER_RAIL_DETECTOR = new BlockTakumiRailDetector();

    //Red(?)stones
    public static final Block CREEPER_REDSTONE_WIRE = new BlockTakumiRedstoneWire();
    public static final Block CREEPER_REDSTONE_BLOCK = new BlockTakumiRedstoneBlock();
    public static final Block CREEPER_REDSTONE_OSCILLATOR = new BlockTakumiRedstoneOscillator();
    public static final Block CREEPER_REDSTONE_LAMP = new BlockTakumiRedstoneLight();
    public static final Block CREEPER_REDSTONE_REPEATER = new BlockTakumiRedstoneRepeater(false);
    public static final Block CREEPER_REDSTONE_REPEATER_ON = new BlockTakumiRedstoneRepeater(true);
    public static final Block CREEPER_REDSTONE_TORCH = new BlockTakumiRedstoneTorch(true);
    public static final Block CREEPER_REDSTONE_TORCH_OFF = new BlockTakumiRedstoneTorch(false);
    public static final Block CREEPER_REDSTONE_COMPARATOR = new BlockTakumiRedstoneComparator(false);
    public static final Block CREEPER_REDSTONE_COMPARATOR_ON = new BlockTakumiRedstoneComparator(true);
    public static final Block CREEPER_OBSERVER = new BlockTakumiObserver();
    public static final Block CREEPER_LEVER = new BlockTakumiLever();
    public static final Block CREEPER_BUTTON_IRON = new BlockTakumiButton("creeperbutton_iron", false);
    public static final Block CREEPER_BUTTON_BRICK = new BlockTakumiButton("creeperbutton_brick", false);
    public static final Block CREEPER_BUTTON_PLANKS = new BlockTakumiButton("creeperbutton_planks", true);
    public static final Block CREEPER_PLATE_BRICK = new BlockTakumiPressurePlate(Material.ROCK, BlockPressurePlate.Sensitivity.MOBS, "creeperplate_brick");
    public static final Block CREEPER_PLATE_PLANKS = new BlockTakumiPressurePlate(Material.WOOD, BlockPressurePlate.Sensitivity.EVERYTHING, "creeperplate_planks");
    public static final Block CREEPER_PLATE_IRON = new BlockTakumiPressurePlateWeighted();
    public static final Block CREEPER_HOPPER = new BlockTakumiHopper();
    public static final Block CREEPER_PISTON = new BlockTakumiPiston(false);
    public static final Block CREEPER_STICKY_PISTON = new BlockTakumiPiston(true);
    public static final Block CREEPER_PISTON_HEAD = new BlockTakumiPistonExtension();
    public static final Block CREEPER_PISTON_EXTEND = new BlockTakumiPistonMoving();
    public static final Block CREEPER_DROPPER = new BlockTakumiDropper();
    public static final Block CREEPER_DISPENSER = new BlockTakumiDispenser();
    public static final Block CREEPER_DETECTOR = new BlockTakumiDetector(false,"creeperdetector");
    public static final Block CREEPER_DETECTOR_INV = new BlockTakumiDetector(true,"creeperdetector_inv");
    public static final Block CREEPER_TIME_DETECTOR = new BlockTakumiTimeDetector(false,"creepertimedetector");
    public static final Block CREEPER_TIME_DETECTOR_INV = new BlockTakumiTimeDetector(true,"creepertimedetector_inv");

    //Danger Blocks
    public static final Block CREEPER_SANDSTAR_LOW = new BlockTakumiSandStarLow();
    public static final Block ACID_BLOCK = new BlockTakumiAcid();
    public static final Block CREEPER_ALTAR = new BlockTakumiAltar();
    public static final Block TAKUMI_TNT = new BlockTakumiTNT();
    public static final BlockTakumiMonsterBomb TAKUMI_BOMB_FIREWORK =
            new BlockTakumiMonsterBomb(EntityFireworksCreeper.class, "fireworkscreeper");
    //public static final BlockTakumiMonsterBomb TAKUMI_BOMB_KING = new BlockTakumiMonsterBomb(EntityKingCreeper.class, "kingcreeper");
    public static final BlockTakumiMonsterBomb TAKUMI_BOMB_BOLT =
            new BlockTakumiMonsterBomb(EntityBoltCreeper.class, "boltcreeper");
    public static final BlockTakumiMonsterBomb TAKUMI_BOMB_OFALEN =
            new BlockTakumiMonsterBomb(EntityOfalenCreeper.class, "ofalencreeper");
    public static final BlockTakumiMonsterBomb TAKUMI_BOMB_TNT =
            new BlockTakumiMonsterBomb(EntityTNTCreeper.class, "tntcreeper");
    public static final BlockTakumiMonsterBomb TAKUMI_BOMB_DEST =
            new BlockTakumiMonsterBomb(EntityDestructionCreeper.class, "destructioncreeper");
    public static final BlockTakumiMonsterBomb TAKUMI_BOMB_CREATIVE =
            new BlockTakumiMonsterBomb(EntityCreativeCreeper.class, "creativecreeper");
    public static final BlockTakumiMonsterBomb TAKUMI_BOMB_ART =
            new BlockTakumiMonsterBomb(EntityArtCreeper.class, "artcreeper");
    public static final BlockTakumiMonsterBomb TAKUMI_BOMB_REWRITE =
            new BlockTakumiMonsterBomb(EntityRewriteCreeper.class, "rewritecreeper");
    public static final BlockTakumiMonsterBomb TAKUMI_BOMB_YUKARI =
            new BlockTakumiMonsterBomb(EntityYukariCreeper.class, "yukaricreeper");
    public static final BlockTakumiMonsterBomb TAKUMI_BOMB_FALL =
            new BlockTakumiMonsterBomb(EntityFallCreeper.class, "fallcreeper");
    public static final BlockTakumiMonsterBomb TAKUMI_BOMB_CALL =
            new BlockTakumiMonsterBomb(EntityCallCreeper.class, "callcreeper");
    public static final BlockTakumiMonsterBomb TAKUMI_BOMB_COLOR =
            new BlockTakumiMonsterBomb(EntityColorCreeper.class, "colorcreeper");
    public static final BlockTakumiMonsterBomb TAKUMI_BOMB_PIERCE =
            new BlockTakumiMonsterBomb(EntityPierceCreeper.class, "piercecreeper");
    public static final BlockTakumiMonsterBomb TAKUMI_BOMB_WEATHER =
            new BlockTakumiMonsterBomb(EntityWeatherCreeper.class, "weathercreeper");
    public static final BlockTakumiMonsterBomb TAKUMI_BOMB_RETURN =
            new BlockTakumiMonsterBomb(EntityReturnCreeper.class, "returncreeper");
    public static final BlockTakumiMonsterBomb TAKUMI_BOMB_SINOBI =
            new BlockTakumiMonsterBomb(EntitySinobiCreeper.class, "sinobicreeper");
    public static final BlockTakumiMonsterBomb TAKUMI_BOMB_INVISIBLE =
            new BlockTakumiMonsterBomb(EntityInvisibleCreeper.class, "invisiblecreeper");

    public static final Block TAKUMI_BLOCK = new BlockTakumiBlock();
    public static final Block TAKUMI_CREEPERED = new BlockTakumiCreepered();
    public static final Block TAKUMI_PORTAL_FRAME = new BlockTakumiPortalFrame();
    public static final BlockTakumiPortal TAKUMI_PORTAL = new BlockTakumiPortal();
    public static final Block FALLING_BOMB = new BlockFallingBomb();
    public static final Block TAKUMI_STONE = new BlockTakumiStone();
    public static final Block TAKUMI_DIRT = new BlockTakumiDirt();
    public static final Block TAKUMI_GRASS = new BlockTakumiGrass();
    public static final Block TAKUMI_ORE_DIAMOND = new BlockTakumiOres(Items.DIAMOND);
    public static final Block TAKUMI_ORE_GUNPOWDER = new BlockTakumiOres(Items.GUNPOWDER);
    public static final Block TAKUMI_ORE_EMERALD = new BlockTakumiOres(Items.EMERALD);
    public static final Block TAKUMI_ORE_COAL = new BlockTakumiOres(Items.COAL);
    public static final Block TAKUMI_ORE_IRON = new BlockTakumiOres(Items.IRON_INGOT);
    public static final Block TAKUMI_ORE_GOLD = new BlockTakumiOres(Items.GOLD_INGOT);
    public static final Block TAKUMI_ORE_REDSTONE = new BlockTakumiOres(Items.REDSTONE);
    public static final Block TAKUMI_ORE_LAPIS = new BlockTakumiOres(Items.DYE);
    public static final Block TAKUMI_ORE_QUARTZ = new BlockTakumiOres(Items.QUARTZ);
    public static final Block TAKUMI_ORE_PEARL = new BlockTakumiOres(Items.ENDER_PEARL);
    public static final Block TAKUMI_ORE_GLOW = new BlockTakumiOres(Items.GLOWSTONE_DUST);
    public static final Block TAKUMI_ORE_MAGIC = new BlockTakumiOres(TakumiItemCore.MAGIC_STONE);
    public static final Block TAKUMI_CLAY = new BlockTakumiClay();
    public static final Block DARKBRICK = new BlockTakumiDarkBrick();
    public static final Block DARKBOARD = new BlockTakumiDarkBoard();
    public static final Block DARKBOARD_ON = new BlockTakumiDarkBoard_On();
    public static final Block DARKIRON_BARS = new BlockTakumiDarkIronBars();
    public static final Block DARKCORE = new BlockTakumiDarkCore();
    public static final Block DARKCORE_ON = new BlockTakumiDarkCore_On();
    public static final Block DARKCORE_SP = new BlockTakumiDarkCore_SP();
    public static final Block ANVIL_CREEPER = new BlockAnvilCreeper();
    public static final Block TAKUMI_STONE_EP = new BlockTakumiStone_EP();
    public static final Block YUKARI_DUMMY = new BlockYukariDummy();


    public static final Map<Class<? extends EntityCreeper>, BlockTakumiMonsterBomb> BOMB_MAP = new HashMap<>();

    public static void register(IForgeRegistry<Block> registry) {
        Class clazz = TakumiBlockCore.class;
        for (Field field : clazz.getFields()) {
            try {
                if (field.get(INSTANCE) instanceof Block) {
                    Block block = (Block) field.get(INSTANCE);
                    registry.register(block);
                    TakumiCraftCore.LOGGER.info("Registered Block : " + block.getUnlocalizedName());
                    if (block instanceof BlockTakumiMonsterBomb) {
                        BOMB_MAP.put(((BlockTakumiMonsterBomb) block).getEntityClass(), (BlockTakumiMonsterBomb) block);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
