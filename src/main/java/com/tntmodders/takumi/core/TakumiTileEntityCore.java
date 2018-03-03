package com.tntmodders.takumi.core;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.client.TakumiClientCore;
import com.tntmodders.takumi.tileentity.TileEntityAcidBlock;
import com.tntmodders.takumi.tileentity.TileEntityMonsterBomb;
import com.tntmodders.takumi.tileentity.TileEntityTakumiBlock;
import com.tntmodders.takumi.tileentity.TileEntityTakumiCreepered;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TakumiTileEntityCore {

    public static void register() {
        GameRegistry.registerTileEntity(TileEntityAcidBlock.class, TakumiCraftCore.MODID + ":acidblock");
        GameRegistry.registerTileEntity(TileEntityTakumiBlock.class, TakumiCraftCore.MODID + ":takumiblock");
        GameRegistry.registerTileEntity(TileEntityMonsterBomb.class, TakumiCraftCore.MODID + ":monsterbomb");
        GameRegistry.registerTileEntity(TileEntityTakumiCreepered.class, TakumiCraftCore.MODID + ":takumicreepered");
        if (FMLCommonHandler.instance().getSide().isClient()) {
            TakumiClientCore.registerTileRender();
        }
    }
}
