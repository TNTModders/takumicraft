package com.tntmodders.takumi.client.render.layer;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.client.model.ModelSkeletonCreeper;
import com.tntmodders.takumi.entity.mobs.EntitySkeletonCreeper;
import com.tntmodders.takumi.entity.mobs.EntityStrayCreeper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;


public class LayerStrayCreeperClothing implements LayerRenderer<EntitySkeletonCreeper> {

    private static final ResourceLocation STRAY_CLOTHES_TEXTURES =
            new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/straycreeper_overlay.png");
    private final RenderLivingBase<?> renderer;
    private final ModelSkeletonCreeper layerModel = new ModelSkeletonCreeper(0.25F, true);

    public LayerStrayCreeperClothing(RenderLivingBase<?> p_i47183_1_) {
        this.renderer = p_i47183_1_;
    }

    @Override
    public void doRenderLayer(EntitySkeletonCreeper entitylivingbaseIn, float limbSwing, float limbSwingAmount,
            float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (entitylivingbaseIn instanceof EntityStrayCreeper) {
            this.layerModel.setModelAttributes(this.renderer.getMainModel());
            this.layerModel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.renderer.bindTexture(STRAY_CLOTHES_TEXTURES);
            this.layerModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch,
                    scale);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}