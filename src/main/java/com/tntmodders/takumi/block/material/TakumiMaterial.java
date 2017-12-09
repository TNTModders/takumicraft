package com.tntmodders.takumi.block.material;


import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;

public class TakumiMaterial extends Material {
    
    public static final Material HOT_SPRING = new MaterialLiquid(MapColor.SNOW);
    
    public TakumiMaterial(MapColor color) {
        super(color);
    }
}
