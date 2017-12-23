package com.tntmodders.takumi.client.render;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.client.render.layer.LayerTakumiCharge;
import com.tntmodders.takumi.entity.mobs.EntityLlamaCreeper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelLlama;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderLlamaCreeper <T extends EntityLlamaCreeper> extends RenderLiving <T> implements ITakumiRender {
    
    public RenderLlamaCreeper(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelLlamaCreeper(0), 0.5f);
        this.addLayer(new LayerTakumiCharge(this));
    }
    
    /**
     * Gets an RGBA int color multiplier to apply.
     */
    @Override
    protected int getColorMultiplier(T entitylivingbaseIn, float lightBrightness, float partialTickTime) {
        float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
        
        if ((int) (f * 10.0F) % 2 == 0) {
            return 0;
        }
        int i = (int) (f * 0.2F * 255.0F);
        i = MathHelper.clamp(i, 0, 255);
        return i << 24 | 822083583;
    }
    
    /**
     * Allows the render to do state modifications necessary before the model is rendered.
     */
    @Override
    protected void preRenderCallback(T entitylivingbaseIn, float partialTickTime) {
        float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
        float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        f = f * f;
        f = f * f;
        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        GlStateManager.scale(f2, f3, f2);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        return new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/" + entity.getRegisterName() + ".png");
    }
    
    @Override
    public ModelBase getPoweredModel() {
        return new ModelLlamaCreeper(2.0f);
    }
    
    private static class ModelLlamaCreeper extends ModelLlama {
        
        public ModelLlamaCreeper(float p_i47226_1_) {
            super(p_i47226_1_);
        }
        
        @Override
        public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float
                scale) {
            this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
            this.head.render(scale);
            this.body.render(scale);
            this.leg1.render(scale);
            this.leg2.render(scale);
            this.leg3.render(scale);
            this.leg4.render(scale);
        }
    }
}
