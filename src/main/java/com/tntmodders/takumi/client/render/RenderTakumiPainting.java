package com.tntmodders.takumi.client.render;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.item.ItemTakumiPainting;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPainting;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.util.ResourceLocation;

public class RenderTakumiPainting extends RenderPainting{
    private static final ResourceLocation KRISTOFFER_PAINTING_TEXTURE = new ResourceLocation("textures/painting/paintings_kristoffer_zetterstrand.png");
    private static final ResourceLocation TAKUMI_PAINTING_TEXTURE = new ResourceLocation(TakumiCraftCore.MODID,"textures/entity/creeperpainting.png");


    public RenderTakumiPainting(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityPainting entity) {
        return ItemTakumiPainting.isPaintingAntiExplosion(entity) ? TAKUMI_PAINTING_TEXTURE : KRISTOFFER_PAINTING_TEXTURE;
    }
}