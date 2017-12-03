package com.tntmodders.takumi.core;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.client.TakumiModelCore;
import com.tntmodders.takumi.tileentity.TileEntityAcidBlock;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TakumiTileEntityCore {
    public static void register() {
        GameRegistry.registerTileEntity(TileEntityAcidBlock.class, TakumiCraftCore.MODID + ":acidblock");
        if (FMLCommonHandler.instance().getSide().isClient()) {
            TakumiModelCore.registerTileRender();
        }
    }
}
