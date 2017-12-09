package com.tntmodders.takumi.core.client;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class TakumiRenderFactory implements IRenderFactory <Entity> {
    
    public TakumiRenderFactory() {
    }
    
    @Override
    public Render createRenderFor(RenderManager manager) {
        return null;
    }
    
}
