package com.tntmodders.takumi.core;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.client.TakumiClientCore;
import com.tntmodders.takumi.tileentity.*;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TakumiTileEntityCore {

    public static void register() {
        GameRegistry.registerTileEntity(TileEntityAcidBlock.class, TakumiCraftCore.MODID + ":acidblock");
        GameRegistry.registerTileEntity(TileEntityTakumiSuperPowered.class, TakumiCraftCore.MODID + ":takumiblock");
        GameRegistry.registerTileEntity(TileEntityMonsterBomb.class, TakumiCraftCore.MODID + ":monsterbomb");
        GameRegistry.registerTileEntity(TileEntityTakumiCreepered.class, TakumiCraftCore.MODID + ":takumicreepered");
        GameRegistry.registerTileEntity(TileEntityDarkBoard.class, TakumiCraftCore.MODID + ":takumidarkboard");
        GameRegistry.registerTileEntity(TileEntityDarkCore.class, TakumiCraftCore.MODID + ":takumidarkcore");
        GameRegistry.registerTileEntity(TileEntityVault.class, TakumiCraftCore.MODID + ":creepervault");
        GameRegistry.registerTileEntity(TileEntityTakumiBed.class, TakumiCraftCore.MODID + ":creeperbed");
        GameRegistry.registerTileEntity(TileEntityTakumiPiston.class, TakumiCraftCore.MODID + ":creeperpiston");
        GameRegistry.registerTileEntity(TileEntityTakumiShulkerBox.class, TakumiCraftCore.MODID + ":creepershulkerbox");
        GameRegistry.registerTileEntity(TileEntityTakumiSign.class, TakumiCraftCore.MODID + ":creepersign");
        GameRegistry.registerTileEntity(TileEntityTakumiForceField.class, TakumiCraftCore.MODID + ":takumiforcefield");
        if (FMLCommonHandler.instance().getSide().isClient()) {
            TakumiClientCore.registerTileRender();
        }
    }
}
