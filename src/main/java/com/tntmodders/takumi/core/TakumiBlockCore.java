package com.tntmodders.takumi.core;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.block.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.registries.IForgeRegistry;

import java.lang.reflect.Field;

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
    public static final Block CREEPER_GLASS_PANE = new BlockTakumiGlassPane();
    public static final Block CREEPER_STAINED_GLASS = new BlockTakumiStainedGlass();
    public static final BlockTakumiStainedGlassPane CREEPER_STAINED_GLASS_PANE = new BlockTakumiStainedGlassPane();

    public static void register(IForgeRegistry<Block> registry) {
        Class clazz = TakumiBlockCore.class;
        for (Field field : clazz.getFields()) {
            try {
                if (field.get(TakumiBlockCore.INSTANCE) instanceof Block) {
                    Block block = ((Block) field.get(TakumiBlockCore.INSTANCE));
                    registry.register(block);
                    TakumiCraftCore.LOGGER.info("Registered Block : " + block.getUnlocalizedName());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
