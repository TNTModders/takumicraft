package com.tntmodders.takumi.core;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.block.BlockTakumiCreeperBomb;
import com.tntmodders.takumi.block.BlockTakumiGunOre;
import com.tntmodders.takumi.block.BlockTakumiHotSpring;
import net.minecraft.block.Block;
import net.minecraftforge.registries.IForgeRegistry;

import java.lang.reflect.Field;

public class TakumiBlockCore {
    public static final TakumiBlockCore INSTANCE = new TakumiBlockCore();

    public static final Block CREEPER_BOMB = new BlockTakumiCreeperBomb();
    public static final Block GUNORE = new BlockTakumiGunOre();
    public static final Block HOT_SPRING = new BlockTakumiHotSpring();

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
