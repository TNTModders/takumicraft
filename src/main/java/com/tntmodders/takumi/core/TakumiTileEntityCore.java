package com.tntmodders.takumi.core;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.client.TakumiModelCore;
import com.tntmodders.takumi.tileentity.TileEntityAcidBlock;
import com.tntmodders.takumi.tileentity.TileEntityTakumiBlock;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TakumiTileEntityCore {
    
    public static void register() {
        GameRegistry.registerTileEntity(TileEntityAcidBlock.class, TakumiCraftCore.MODID + ":acidblock");
        GameRegistry.registerTileEntity(TileEntityTakumiBlock.class, TakumiCraftCore.MODID + ":takumiblock");
        if (FMLCommonHandler.instance().getSide().isClient()) {
            TakumiModelCore.registerTileRender();
        }
    }
}
