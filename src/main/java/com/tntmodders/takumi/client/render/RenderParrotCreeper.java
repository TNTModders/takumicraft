package com.tntmodders.takumi.client.render;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.client.model.ModelParrotCreeper;
import com.tntmodders.takumi.client.render.layer.LayerTakumiCharge;
import com.tntmodders.takumi.entity.mobs.EntityParrotCreeper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;


public class RenderParrotCreeper extends RenderLiving <EntityParrotCreeper> implements ITakumiRender {
    
    public static final ResourceLocation TEXTURE = new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/parrotcreeper.png");
    
    public RenderParrotCreeper(RenderManager p_i47375_1_) {
        super(p_i47375_1_, new ModelParrotCreeper(), 0.3F);
        this.addLayer(new LayerTakumiCharge(this));
    }
    
    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation getEntityTexture(EntityParrotCreeper entity) {
        return TEXTURE;
    }
    
    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    @Override
    public float handleRotationFloat(EntityParrotCreeper livingBase, float partialTicks) {
        return this.getCustomBob(livingBase, partialTicks);
    }
    
    /**
     * Gets an RGBA int color multiplier to apply.
     */
    @Override
    protected int getColorMultiplier(EntityParrotCreeper entitylivingbaseIn, float lightBrightness, float partialTickTime) {
        float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
    
        if ((int) (f * 10.0F) % 2 == 0) {
            return 0;
        } else {
            int i = (int) (f * 0.2F * 255.0F);
            i = MathHelper.clamp(i, 0, 255);
            return i << 24 | 822083583;
        }
    }
    
    /**
     * Allows the render to do state modifications necessary before the model is rendered.
     */
    @Override
    protected void preRenderCallback(EntityParrotCreeper entitylivingbaseIn, float partialTickTime) {
        float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
        float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        f = f * f;
        f = f * f;
        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        GlStateManager.scale(f2, f3, f2);
    }
    
    private float getCustomBob(EntityParrotCreeper parrot, float p_192861_2_) {
        float f = parrot.oFlap + (parrot.flap - parrot.oFlap) * p_192861_2_;
        float f1 = parrot.oFlapSpeed + (parrot.flapSpeed - parrot.oFlapSpeed) * p_192861_2_;
        return (MathHelper.sin(f) + 1.0F) * f1;
    }
    
    @Override
    public ModelBase getPoweredModel() {
        return new ModelParrotCreeper();
    }
}