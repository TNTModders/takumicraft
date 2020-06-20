package com.tntmodders.takumi.item.material;

import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.util.EnumHelper;

public class TakumiToolMaterial {

    public static final ToolMaterial TAKUMI_MATERIAL = EnumHelper.addToolMaterial("anti_powered", 6, 101, 250, 0, 1);
    public static final ToolMaterial LIGHTSABER_MATERIAL =
            EnumHelper.addToolMaterial("takumi_lightsaber", 6, 50, 500, 26, 0);

}
