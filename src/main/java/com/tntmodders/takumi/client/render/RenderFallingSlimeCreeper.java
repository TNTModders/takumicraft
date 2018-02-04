package com.tntmodders.takumi.client.render;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.entity.mobs.EntityFallingSlimeCreeper;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderFallingSlimeCreeper <T extends EntityFallingSlimeCreeper> extends RenderTakumiCreeper <T> {
    
    private static final ResourceLocation LOCATION = new ResourceLocation("textures/entity/creeper/creeper.png");
    
    public RenderFallingSlimeCreeper(RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.addLayer(new LayerSlimeArmor <>(this));
    }
    
    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        return LOCATION;
    }
    
    private static class LayerSlimeArmor <T extends EntityFallingSlimeCreeper> implements LayerRenderer <T> {
        
        private static final ResourceLocation location = new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/fallingslimecreeper.png");
        private final RenderFallingSlimeCreeper render;
        private final ModelCreeper model = new ModelCreeper(1.0f);
        
        public LayerSlimeArmor(RenderFallingSlimeCreeper fallingSlimeCreeper) {
            this.render = fallingSlimeCreeper;
        }
        
        @Override
        public void doRenderLayer(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float
                netHeadYaw, float headPitch, float scale) {
            if (!entitylivingbaseIn.isInvisible()) {
                GlStateManager.pushMatrix();
                this.render.bindTexture(location);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.enableNormalize();
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
                this.model.setModelAttributes(this.render.getMainModel());
                this.model.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
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
}
