package com.tntmodders.takumi.core;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.block.*;
import com.tntmodders.takumi.entity.mobs.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraftforge.registries.IForgeRegistry;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class TakumiBlockCore {
    
    public static final TakumiBlockCore INSTANCE = new TakumiBlockCore();
    
    public static final Block CREEPER_BOMB = new BlockTakumiCreeperBomb();
    public static final Block GUNORE = new BlockTakumiGunOre();
    public static final Block HOT_SPRING = new BlockTakumiHotSpring();
    public static final Block CREEPER_BRICK = new BlockTakumiAntiExplosion(Material.SAND, "creeperbrick", 2.5f, "pickaxe");
    public static final Block CREEPER_IRON = new BlockTakumiAntiExplosion(Material.IRON, "creeperiron", 5f, "pickaxe");
    public static final Block DUMMY_GUNORE = new BlockTakumiDummyGunOre();
    public static final Block CREEPER_LOG = new BlockTakumiLog();
    public static final Block CREEPER_LEAVES = new BlockTakumiLeaf();
    public static final Block CREEPER_GLASS = new BlockTakumiGlass();
    public static final Block CREEPER_STAINED_GLASS = new BlockTakumiStainedGlass();
    public static final Block CREEPER_GLASS_PANE = new BlockTakumiGlassPane();
    public static final BlockTakumiStainedGlassPane CREEPER_STAINED_GLASS_PANE = new BlockTakumiStainedGlassPane();
    public static final Block CREEPER_WOOL = new BlockTakumiWool();
    public static final Block CREEPER_SANDSTAR_LOW = new BlockTakumiSandStarLow();
    public static final Block ACID_BLOCK = new BlockTakumiAcid();
    public static final Block CREEPER_ALTAR = new BlockTakumiAltar();
    public static final Block TAKUMI_TNT = new BlockTakumiTNT();
    public static final BlockTakumiMonsterBomb TAKUMI_BOMB_FIREWORK = new BlockTakumiMonsterBomb(EntityFireworksCreeper.class, "fireworkscreeper");
    //public static final BlockTakumiMonsterBomb TAKUMI_BOMB_KING = new BlockTakumiMonsterBomb(EntityKingCreeper.class, "kingcreeper");
    public static final BlockTakumiMonsterBomb TAKUMI_BOMB_BOLT = new BlockTakumiMonsterBomb(EntityBoltCreeper.class, "boltcreeper");
    public static final BlockTakumiMonsterBomb TAKUMI_BOMB_OFALEN = new BlockTakumiMonsterBomb(EntityOfalenCreeper.class, "ofalencreeper");
    public static final BlockTakumiMonsterBomb TAKUMI_BOMB_TNT = new BlockTakumiMonsterBomb(EntityTNTCreeper.class, "tntcreeper");
    public static final BlockTakumiMonsterBomb TAKUMI_BOMB_DEST = new BlockTakumiMonsterBomb(EntityDestructionCreeper.class, "destructioncreeper");
    public static final BlockTakumiMonsterBomb TAKUMI_BOMB_CREATIVE = new BlockTakumiMonsterBomb(EntityCreativeCreeper.class, "creativecreeper");
    public static final BlockTakumiMonsterBomb TAKUMI_BOMB_ART = new BlockTakumiMonsterBomb(EntityArtCreeper.class, "artcreeper");
    public static final BlockTakumiMonsterBomb TAKUMI_BOMB_REWRITE = new BlockTakumiMonsterBomb(EntityRewriteCreeper.class, "rewritecreeper");
    public static final BlockTakumiMonsterBomb TAKUMI_BOMB_YUKARI = new BlockTakumiMonsterBomb(EntityYukariCreeper.class, "yukaricreeper");
    public static final BlockTakumiMonsterBomb TAKUMI_BOMB_FALL = new BlockTakumiMonsterBomb(EntityFallCreeper.class, "fallcreeper");
    public static final BlockTakumiMonsterBomb TAKUMI_BOMB_CALL = new BlockTakumiMonsterBomb(EntityCallCreeper.class, "callcreeper");
    public static final BlockTakumiMonsterBomb TAKUMI_BOMB_COLOR = new BlockTakumiMonsterBomb(EntityColorCreeper.class, "colorcreeper");
    public static final BlockTakumiMonsterBomb TAKUMI_BOMB_PIERCE = new BlockTakumiMonsterBomb(EntityPierceCreeper.class, "piercecreeper");
    public static final Block TAKUMI_BLOCK = new BlockTakumiBlock();
    public static final Block TAKUMI_CREEPERED = new BlockTakumiCreepered();
    public static final Block TAKUMI_PORTAL_FRAME = new BlockTakumiPortalFrame();
    public static final BlockTakumiPortal TAKUMI_PORTAL = new BlockTakumiPortal();
    public static final Block FALLING_BOMB = new BlockFallingBomb();
    public static final Block TAKUMI_STONE = new BlockTakumiStone();
    public static final Block TAKUMI_DIRT = new BlockTakumiDirt();
    public static final Block TAKUMI_GRASS = new BlockTakumiGrass();
    
    public static final Map <Class <? extends EntityCreeper>, BlockTakumiMonsterBomb> BOMB_MAP = new HashMap <>();
    
    public static void register(IForgeRegistry <Block> registry) {
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
