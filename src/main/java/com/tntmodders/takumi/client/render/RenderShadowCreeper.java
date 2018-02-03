package com.tntmodders.takumi.client.render;

import com.tntmodders.takumi.entity.mobs.EntityShadowCreeper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class RenderShadowCreeper <T extends EntityShadowCreeper> extends RenderTakumiCreeper <T> {
    
    public RenderShadowCreeper(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelCreeper());
        this.addLayer(new LayerShadow(this));
    }
    
    @Override
    public ResourceLocation getEntityTexture(T entity) {
        return super.getEntityTexture(entity);
    }
    
    private static class LayerShadow <T extends EntityShadowCreeper> implements LayerRenderer <T> {
        
        private final RenderShadowCreeper creeperRenderer;
        private final ModelBase creeperModel;
        private final Random random = new Random();
        
        public LayerShadow(RenderShadowCreeper creeperRendererIn) {
            this.creeperRenderer = creeperRendererIn;
            this.creeperModel = new ModelCreeper();
        }
        
        @Override
        public void doRenderLayer(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float
                netHeadYaw, float headPitch, float scale) {
            if (!entitylivingbaseIn.isInvisible() && entitylivingbaseIn.getTimeSinceIgnited() > 0) {
                int t = entitylivingbaseIn.getTimeSinceIgnited();
                for (Vec3d vec3d : entitylivingbaseIn.shadowList) {
                    GlStateManager.pushMatrix();
                    boolean flag = entitylivingbaseIn.isInvisible();
                    GlStateManager.depthMask(!flag);
                    this.creeperRenderer.bindTexture(creeperRenderer.getEntityTexture(entitylivingbaseIn));
                    GlStateManager.matrixMode(5890);
                    GlStateManager.loadIdentity();
                    GlStateManager.matrixMode(5888);
                    GlStateManager.enableBlend();
                    GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
                    GlStateManager.disableLighting();
                    GlStateManager.translate(vec3d.x, vec3d.y, vec3d.z);
                    GlStateManager.blendFunc(SourceFactor.ONE, DestFactor.ONE);
                    this.creeperModel.setModelAttributes(this.creeperRenderer.getMainModel());
                    Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
                    this.creeperModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                    Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
                    GlStateManager.matrixMode(5890);
                    GlStateManager.loadIdentity();
                    GlStateManager.matrixMode(5888);
                    GlStateManager.enableLighting();
                    GlStateManager.disableBlend();
                    GlStateManager.depthMask(flag);
                    GlStateManager.popMatrix();
                }
            }
        }
        
        @Override
        public boolean shouldCombineTextures() {
            return false;
        }
    }
}
