package com.tntmodders.takumi.client.render;

import com.tntmodders.takumi.entity.mobs.EntitySmokeCreeper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;

public class RenderSmokeCreeper<T extends EntitySmokeCreeper> extends RenderTakumiCreeper<T> {
    public RenderSmokeCreeper(RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.addLayer(new LayerSmokeCreeper(this));
    }

    public class LayerSmokeCreeper implements LayerRenderer {

        private final RenderSmokeCreeper render;

        public LayerSmokeCreeper(RenderSmokeCreeper renderSmokeCreeper) {
            this.render = renderSmokeCreeper;
        }

        @Override
        public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            if (entitylivingbaseIn instanceof EntitySmokeCreeper && ((EntitySmokeCreeper) entitylivingbaseIn).isGlowingSP) {
                GlStateManager.pushMatrix();
                GlStateManager.enableColorMaterial();
                GlStateManager.enableOutlineMode(0x113311);
                this.render.mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                GlStateManager.disableOutlineMode();
                GlStateManager.disableColorMaterial();
                GlStateManager.popMatrix();
            }
        }

        @Override
        public boolean shouldCombineTextures() {
            return false;
        }
    }
}
