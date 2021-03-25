package com.tntmodders.takumi.client.render;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.client.render.layer.LayerTakumiCharge;
import com.tntmodders.takumi.entity.mobs.EntityFishingCreeper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSilverfish;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderFishingCreeper<T extends EntityFishingCreeper> extends RenderLiving<T> implements ITakumiRender {

    public RenderFishingCreeper(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelSalmon(), 0.5f);
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
    protected void preRenderCallback(T entitylivingbaseIn, float partialTickTime) {
        GlStateManager.scale(entitylivingbaseIn.getSizeAmp(), entitylivingbaseIn.getSizeAmp(),
                entitylivingbaseIn.getSizeAmp());
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
        return new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/fishingcreeper.png");
    }

    @Override
    public ModelBase getPoweredModel() {
        return new ModelSilverfish();
    }

    public static class ModelSalmon extends ModelBase {
        private final ModelRenderer bodyFront;
        private final ModelRenderer bodyRear;
        private final ModelRenderer head;
        private final ModelRenderer finRight;
        private final ModelRenderer finLeft;

        public ModelSalmon() {
            this.textureWidth = 32;
            this.textureHeight = 32;
            int i = 20;
            this.bodyFront = new ModelRenderer(this, 0, 0);
            this.bodyFront.addBox(-1.5F, -2.5F, 0, 3, 5, 8);
            this.bodyFront.setRotationPoint(0, 20, 0);
            this.bodyRear = new ModelRenderer(this, 0, 13);
            this.bodyRear.addBox(-1.5F, -2.5F, 0, 3, 5, 8);
            this.bodyRear.setRotationPoint(0, 20, 8);
            this.head = new ModelRenderer(this, 22, 0);
            this.head.addBox(-1, -2, -3, 2, 4, 3);
            this.head.setRotationPoint(0, 20, 0);
            ModelRenderer modelrenderer = new ModelRenderer(this, 20, 10);
            modelrenderer.addBox(0, -2.5F, 0, 0, 5, 6);
            modelrenderer.setRotationPoint(0, 0, 8);
            this.bodyRear.addChild(modelrenderer);
            ModelRenderer modelrenderer1 = new ModelRenderer(this, 2, 1);
            modelrenderer1.addBox(0, 0, 0, 0, 2, 3);
            modelrenderer1.setRotationPoint(0, -4.5F, 5);
            this.bodyFront.addChild(modelrenderer1);
            ModelRenderer modelrenderer2 = new ModelRenderer(this, 0, 2);
            modelrenderer2.addBox(0, 0, 0, 0, 2, 4);
            modelrenderer2.setRotationPoint(0, -4.5F, -1);
            this.bodyRear.addChild(modelrenderer2);
            this.finRight = new ModelRenderer(this, -4, 0);
            this.finRight.addBox(-2, 0, 0, 2, 0, 2);
            this.finRight.setRotationPoint(-1.5F, 21.5F, 0);
            this.finRight.rotateAngleZ = (-(float) Math.PI / 4F);
            this.finLeft = new ModelRenderer(this, 0, 0);
            this.finLeft.addBox(0, 0, 0, 2, 0, 2);
            this.finLeft.setRotationPoint(1.5F, 21.5F, 0);
            this.finLeft.rotateAngleZ = ((float) Math.PI / 4F);
        }

        @Override
        public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
            float f = 1.0F;
            float f1 = 1.0F;
            if (!entityIn.isInWater()) {
                f = 1.3F;
                f1 = 1.7F;
            }

            this.bodyRear.rotateAngleY = -f * 0.25F * MathHelper.sin(f1 * 0.6F * ageInTicks);
        }

        @Override
        public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            this.bodyFront.render(scale);
            this.bodyRear.render(scale);
            this.head.render(scale);
            this.finLeft.render(scale);
            this.finRight.render(scale);
        }
    }
}
