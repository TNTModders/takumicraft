package com.tntmodders.takumi.client.render.layer;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiPotionCore;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class LayerFrozenEffect implements LayerRenderer {
    private static final ResourceLocation ICE_TEXTURE = new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/frozen_entity.png");
    private final RenderLivingBase render;
    private final ModelBase model;

    public LayerFrozenEffect(RenderLivingBase renderLivingIn) {
        this.render = renderLivingIn;
        this.model = renderLivingIn.getMainModel();
    }

    @Override
    public void doRenderLayer(EntityLivingBase entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (entityLivingBaseIn.isPotionActive(TakumiPotionCore.FROZEN) && entityLivingBaseIn.getActivePotionEffect(TakumiPotionCore.FROZEN).getDuration() > 0) {
            GlStateManager.pushMatrix();
            GlStateManager.color(1F, 1F, 1F, 1.0F);
            this.render.bindTexture(ICE_TEXTURE);
            GlStateManager.enableNormalize();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.model.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, entityLivingBaseIn.rotationPitch, scale, entityLivingBaseIn);
            this.model.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GlStateManager.disableBlend();
            GlStateManager.disableNormalize();
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
