package com.tntmodders.takumi.client.render.layer;

import com.tntmodders.takumi.client.render.ITakumiRender;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;


public class LayerTakumiCharge implements LayerRenderer <EntityTakumiAbstractCreeper> {
    
    private static final ResourceLocation LIGHTNING_TEXTURE = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    private final RenderLiving creeperRenderer;
    private final ModelBase creeperModel;
    
    public LayerTakumiCharge(RenderLiving creeperRendererIn) {
        this.creeperRenderer = creeperRendererIn;
        if (creeperRenderer.getMainModel() instanceof ModelCreeper) {
            this.creeperModel = new ModelCreeper(2.0f);
        } else if (creeperRenderer instanceof ITakumiRender) {
            this.creeperModel = ((ITakumiRender) creeperRenderer).getPoweredModel();
        } else {
            this.creeperModel = creeperRenderer.getMainModel();
        }
    }
    
    @Override
    public void doRenderLayer(EntityTakumiAbstractCreeper entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float
            ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (entitylivingbaseIn.getPowered() && !entitylivingbaseIn.isInvisible()) {
            boolean flag = entitylivingbaseIn.isInvisible();
            GlStateManager.depthMask(!flag);
            this.creeperRenderer.bindTexture(entitylivingbaseIn.getArmor());
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            float f = entitylivingbaseIn.ticksExisted + partialTicks;
            GlStateManager.translate(f * 0.01F, f * 0.01F, 0.0F);
            GlStateManager.matrixMode(5888);
            GlStateManager.enableBlend();
            GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F); GlStateManager.disableLighting();
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
        }
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}