package com.tntmodders.takumi.core;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.client.TakumiModelCore;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TakumiFluidCore {
    
    public static final Fluid HOT_SPRING = new Fluid("takumihotspring", new ResourceLocation(TakumiCraftCore.MODID, "blocks/hotspring_still"),
                                                     new ResourceLocation(TakumiCraftCore.MODID, "blocks/hotspring_flow"));
    
    public static void register() {
        FluidRegistry.registerFluid(HOT_SPRING);
        if (FMLCommonHandler.instance().getSide().isClient()) {
            TakumiModelCore.registerFluid(TakumiBlockCore.HOT_SPRING, "hotspring");
        }
    }
}
