package com.tntmodders.takumi.core;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.world.provider.TakumiWorldProvider;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

public class TakumiWorldCore {

    public static final DimensionType TAKUMI_WORLD = DimensionType
            .register("takumiworld", "_" + TakumiCraftCore.MODID, DimensionManager.getNextFreeDimId(),
                    TakumiWorldProvider.class, true);

    public static void register() {
        DimensionManager.registerDimension(TAKUMI_WORLD.getId(), TAKUMI_WORLD);
    }
}
