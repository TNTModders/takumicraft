package com.tntmodders.takumi.client.render;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiConfigCore;
import com.tntmodders.takumi.entity.mobs.EntityCeruleanCreeper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderCeruleanCreeper <T extends EntityCeruleanCreeper> extends RenderTakumiCreeper <T> {
    
    public RenderCeruleanCreeper(RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.addLayer(new RenderCeruleanEye <>(this));
    }
    
    @Override
    protected void renderModel(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch,
            float scaleFactor) {
        boolean flag = entitylivingbaseIn.isInvisible();
        if (TakumiConfigCore.isTransparentCeruleanCreeper) {
            GlStateManager.depthMask(!flag);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            float f = entitylivingbaseIn.ticksExisted + ageInTicks;
            GlStateManager.translate(f * 0.01F, f * 0.01F, 0.0F);
            GlStateManager.matrixMode(5888);
            GlStateManager.enableBlend();
            GlStateManager.color(1F, 1F, 1F, 0.1025F);
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.SRC_COLOR);
            Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        }
        super.renderModel(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        if (TakumiConfigCore.isTransparentCeruleanCreeper) {
            Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(flag);
        }
    }
    
    private class RenderCeruleanEye <E extends EntityCeruleanCreeper> implements LayerRenderer <E> {
        
        final RenderCeruleanCreeper renderCeruleanCreeper;
        final ResourceLocation location = new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/ceruleancreeper_eye.png");
        
        public RenderCeruleanEye(RenderCeruleanCreeper creeper) {
            this.renderCeruleanCreeper = creeper;
        }
        
        @Override
        public void doRenderLayer(E entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float
                netHeadYaw, float headPitch, float scale) {
            this.renderCeruleanCreeper.bindTexture(location);
            Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
            mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        }
        
        @Override
        public boolean shouldCombineTextures() {
            return false;
        }
    }
}
