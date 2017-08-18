package com.tntmodders.takumi.core;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TakumiFluidCore {
    public static final Fluid HOT_SPRING = new Fluid("takumihotspring", new ResourceLocation(TakumiCraftCore.MODID, "blocks/hotspring_still"),
            new ResourceLocation(TakumiCraftCore.MODID, "blocks/hotspring_flow"));

    public static void register() {
        FluidRegistry.registerFluid(HOT_SPRING);
        if (FMLCommonHandler.instance().getSide().isClient()) {
            ModelLoader.setCustomStateMapper(TakumiBlockCore.HOT_SPRING, new StateMapperBase() {
                @Override
                protected ModelResourceLocation getModelResourceLocation(IBlockState p_178132_1_) {
                    return new ModelResourceLocation(new ResourceLocation(TakumiCraftCore.MODID, "hotspring"), "fluid");
                }
            });
        }
    }
}
