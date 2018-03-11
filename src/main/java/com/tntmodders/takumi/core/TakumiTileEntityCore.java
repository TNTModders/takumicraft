package com.tntmodders.takumi.core;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.client.TakumiClientCore;
import com.tntmodders.takumi.tileentity.*;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TakumiTileEntityCore {

    public static void register() {
        GameRegistry.registerTileEntity(TileEntityAcidBlock.class, TakumiCraftCore.MODID + ":acidblock");
        GameRegistry.registerTileEntity(TileEntityTakumiBlock.class, TakumiCraftCore.MODID + ":takumiblock");
        GameRegistry.registerTileEntity(TileEntityMonsterBomb.class, TakumiCraftCore.MODID + ":monsterbomb");
        GameRegistry.registerTileEntity(TileEntityTakumiCreepered.class, TakumiCraftCore.MODID + ":takumicreepered");
        GameRegistry.registerTileEntity(TileEntityDarkBoard.class,TakumiCraftCore.MODID+":takumidarkboard");
        if (FMLCommonHandler.instance().getSide().isClient()) {
            TakumiClientCore.registerTileRender();
        }
    }
}
