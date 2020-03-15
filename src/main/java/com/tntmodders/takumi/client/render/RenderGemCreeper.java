package com.tntmodders.takumi.client.render;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.client.render.layer.LayerTakumiCharge;
import com.tntmodders.takumi.entity.mobs.boss.EntityGemCreeper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;

public class RenderGemCreeper<T extends EntityGemCreeper> extends RenderLiving<T> implements ITakumiRender {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/gemcreeper.png");

    public RenderGemCreeper(RenderManager rendermanagerIn) {
        super(rendermanagerIn, new ModelWitherCreeper(0), 1f);
        this.addLayer(new LayerTakumiCharge(this));
    }

    @Override
    public ModelBase getPoweredModel() {
        return new ModelWitherCreeper(0.5f);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        return TEXTURE;
    }

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

    @Override
    protected void preRenderCallback(T entitylivingbaseIn, float partialTickTime) {
        GlStateManager.scale(2.375, 2.5, 2.5);
        GlStateManager.translate(0, 0.5, 0);
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);
        if (!entitylivingbaseIn.isBook) {
            GlStateManager.matrixMode(5888);
            GlStateManager.enableBlend();
            GlStateManager.color(0.5F, 0.5F, 0.5F, 0.8f);
            GlStateManager.disableLighting();
        }
    }

    public static class ModelWitherCreeper extends ModelBase {
        private final ModelRenderer[] upperBodyParts;
        private final ModelRenderer[] heads;

        public ModelWitherCreeper(float p_i46302_1_) {
            this.textureHeight = 100;
            this.textureWidth = 100;
            this.upperBodyParts = new ModelRenderer[5];
            this.upperBodyParts[0] = new ModelRenderer(this, 0, 0);
            this.upperBodyParts[0].addBox(-8, 10, -8, 16, 2, 16);
            this.upperBodyParts[1] = new ModelRenderer(this, 0, 0);
            this.upperBodyParts[1].addBox(-10, 8, -10, 20, 2, 20);
            this.upperBodyParts[2] = new ModelRenderer(this, 0, 40);
            this.upperBodyParts[2].addBox(-12, -8, -12, 24, 16, 24);
            this.upperBodyParts[3] = new ModelRenderer(this, 0, 0);
            this.upperBodyParts[3].addBox(-10, -10, -10, 20, 2, 20);
            this.upperBodyParts[4] = new ModelRenderer(this, 0, 0);
            this.upperBodyParts[4].addBox(-8, -12, -8, 16, 2, 16);

            this.heads = new ModelRenderer[2];
            this.heads[0] = new ModelRenderer(this, 0, 80);
            this.heads[0].addBox(-16, 0, 0, 8, 8, 8);
            this.heads[0].setRotationPoint(-12, 2, -4);
            this.heads[0].rotateAngleX = 45;
            this.heads[0].rotateAngleY = -45;
            //this.heads[0].rotateAngleZ = 45;
            this.heads[1] = new ModelRenderer(this, 0, 80);
            this.heads[1].addBox(8, 0, 0, 8, 8, 8);
            this.heads[1].setRotationPoint(12, 2, -4);
            this.heads[1].rotateAngleX = 45;
            this.heads[1].rotateAngleY = 45;
            //this.heads[1].rotateAngleZ = 45;
        }

        /**
         * Sets the models various rotation angles then renders the model.
         */
        @Override
        public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);

            for (ModelRenderer modelrenderer : this.heads) {
                modelrenderer.render(scale);
            }
            for (ModelRenderer modelrenderer1 : this.upperBodyParts) {
                modelrenderer1.render(scale);
            }
        }
    }
}
