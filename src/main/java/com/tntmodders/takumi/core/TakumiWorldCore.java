package com.tntmodders.takumi.core;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.world.gen.TakumiMapGenDarkShrine;
import com.tntmodders.takumi.world.gen.structure.StructureTakumiDarkShrinePieces;
import com.tntmodders.takumi.world.provider.TakumiWorldProvider;
import net.minecraft.world.DimensionType;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.DimensionManager;

public class TakumiWorldCore {

    public static final DimensionType TAKUMI_WORLD = DimensionType
            .register("takumiworld", "_" + TakumiCraftCore.MODID, DimensionManager.getNextFreeDimId(),
                    TakumiWorldProvider.class, true);

    public static void register() {
        DimensionManager.registerDimension(TAKUMI_WORLD.getId(), TAKUMI_WORLD);
    }

    public static void registerMapGen() {
        MapGenStructureIO.registerStructure(TakumiMapGenDarkShrine.Start.class, "TakumiDarkShrine");
        StructureTakumiDarkShrinePieces.registerTakumiDarkShrinePieces();
/*        MapGenStructureIO.registerStructure(StructureDarkShrineStart.class,"darkshrine");
        MapGenStructureIO.registerStructureComponent(ComponentDarkShrine.class,"darkshrinebuildings");*/
    }
}
