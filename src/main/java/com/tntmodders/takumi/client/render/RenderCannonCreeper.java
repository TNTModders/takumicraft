package com.tntmodders.takumi.client.render;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.client.render.layer.LayerTakumiCharge;
import com.tntmodders.takumi.entity.mobs.EntityCannonCreeper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderCannonCreeper<T extends EntityCannonCreeper> extends RenderLiving<T> implements ITakumiRender {

    public RenderCannonCreeper(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelCannon(), 0.5f);
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
        return new ModelCannon();
    }

    public static class ModelCannon extends ModelBase {
        //variables init:
        public ModelRenderer barrel;
        public ModelRenderer body;

        public float stat = -4F;

        public ModelCannon() {
            this(0.0F);
        }

        public ModelCannon(float p_i46366_1_) {
            //constructor:
            barrel = new ModelRenderer(this, 0, 0);
            //barrel.
            barrel.setTextureOffset(0, 0);
            barrel.addBox(4F + stat, -12F, -4F + stat, 8, 8, 16);
            barrel.setRotationPoint(0F, 16F, -4F);
            barrel.rotateAngleX = -0.6F;

            body = new ModelRenderer(this, 0, 40);
            body.setTextureOffset(0, 24);
            body.addBox(0F + stat, 16F, 0F + stat, 16, 8, 16);
        }

        /**
         * Sets the models various rotation angles then renders the model.
         */
        @Override
        public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_,
                float p_78088_6_, float p_78088_7_) {
            this.render(p_78088_7_);
        }

        public void render(float f) {
            barrel.render(f);
            body.render(f);
        }
    }
}
