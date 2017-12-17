package com.tntmodders.takumi.client.render.layer;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.client.render.RenderSpiderCreeper;
import com.tntmodders.takumi.entity.mobs.EntitySpiderCreeper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;


public class LayerSpiderCreeperEyes <T extends EntitySpiderCreeper> implements LayerRenderer <T> {
    
    private static final ResourceLocation SPIDER_EYES = new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/spidercreeper_eyes.png");
    private final RenderSpiderCreeper <T> spiderRenderer;
    
    public LayerSpiderCreeperEyes(RenderSpiderCreeper <T> spiderRendererIn) {
        this.spiderRenderer = spiderRendererIn;
    }
    
    @Override
    public void doRenderLayer(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw,
            float headPitch, float scale) {
        this.spiderRenderer.bindTexture(SPIDER_EYES);
        GlStateManager.enableBlend(); GlStateManager.disableAlpha(); GlStateManager.blendFunc(SourceFactor.ONE, DestFactor.ONE);
        
        if (entitylivingbaseIn.isInvisible()) {
            GlStateManager.depthMask(false);
        } else {
            GlStateManager.depthMask(true);
        }
        
        int i = 61680;
        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        this.spiderRenderer.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        i = entitylivingbaseIn.getBrightnessForRender();
        j = i % 65536;
        k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
        this.spiderRenderer.setLightmap(entitylivingbaseIn);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}