package com.tntmodders.takumi.core;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class TakumiFluidCore {

    public static final Fluid HOT_SPRING =
            new Fluid("takumihotspring", new ResourceLocation(TakumiCraftCore.MODID, "blocks/takumihotspring"),
                    new ResourceLocation(TakumiCraftCore.MODID, "blocks/takumihotspring")).setLuminosity(
                    15).setTemperature(500).setDensity(2000).setViscosity(1500).setRarity(EnumRarity.UNCOMMON);

    public static final Fluid TAKUMI_WATER =
            new Fluid("takumiwater", new ResourceLocation(TakumiCraftCore.MODID, "blocks/water_still"),
                    new ResourceLocation(TakumiCraftCore.MODID, "blocks/water_flow")).setLuminosity(6).setTemperature(
                    20).setDensity(1000).setViscosity(100);

    public static void register() {
        FluidRegistry.registerFluid(HOT_SPRING);
        FluidRegistry.registerFluid(TAKUMI_WATER);
    }
}
