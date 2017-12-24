package com.tntmodders.takumi.client.render;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.client.model.ModelIllagerCreeper;
import com.tntmodders.takumi.client.render.layer.LayerTakumiCharge;
import com.tntmodders.takumi.entity.mobs.EntityAbstractSpellCreeper;
import com.tntmodders.takumi.entity.mobs.EntityEvokerCreeper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderEvokerCreeper <T extends EntityEvokerCreeper> extends RenderLiving <T> implements ITakumiRender {
    
    public RenderEvokerCreeper(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelIllagerCreeper(0.0F, 0.0F, 64, 64), 0.5f);
        this.addLayer(new LayerTakumiCharge(this));
        this.addLayer(new LayerHeldItem(this) {
            @Override
            public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float
                    ageInTicks, float netHeadYaw, float headPitch, float scale) {
                if (((EntityAbstractSpellCreeper) entitylivingbaseIn).isSpellcasting()) {
                    super.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
                }
            }
            
            @Override
            protected void translateToHand(EnumHandSide p_191361_1_) {
                ((ModelIllagerCreeper) this.livingEntityRenderer.getMainModel()).getArm(p_191361_1_).postRender(0.0625F);
            }
        });
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
        GlStateManager.scale(0.9375F, 0.9375F, 0.9375F);
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
        return new ModelIllagerCreeper(2.0F, 0.0F, 64, 64);
    }
}
