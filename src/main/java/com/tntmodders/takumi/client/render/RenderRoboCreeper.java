package com.tntmodders.takumi.client.render;

import com.tntmodders.takumi.client.render.layer.LayerTakumiRazer;
import com.tntmodders.takumi.entity.mobs.EntityRoboCreeper;
import net.minecraft.client.renderer.entity.RenderManager;

public class RenderRoboCreeper<T extends EntityRoboCreeper> extends RenderTakumiCreeper<T> {
    public RenderRoboCreeper(RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.addLayer(new LayerTakumiRazer());
    }
}
