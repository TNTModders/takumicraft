package com.tntmodders.takumi.client.render;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.client.render.layer.LayerTakumiCharge;
import com.tntmodders.takumi.entity.mobs.boss.EntityForestCreeper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderForestCreeper<T extends EntityForestCreeper> extends RenderLiving<T> implements ITakumiRender {

    public RenderForestCreeper(RenderManager renderManagerIn) {
        this(renderManagerIn, new ModelForestCreeper());
    }

    public RenderForestCreeper(RenderManager renderManagerIn, ModelBase model) {
        super(renderManagerIn, model, 0.5F);
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
        if (entitylivingbaseIn.getSizeAmp() != 1) {
            double d = entitylivingbaseIn.getSizeAmp();
            GlStateManager.scale(d, d, d);
        }
        float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
        float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        f = f * f;
        f = f * f;
        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        GlStateManager.scale(f2, f3, f2);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        return new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/" + entity.getRegisterName() + ".png");
    }


    @Override
    public ModelBase getPoweredModel() {
        return new ModelForestCreeper();
    }

    public static class ModelForestCreeper extends ModelBase {

        ModelRenderer box1;
        ModelRenderer box2;
        ModelRenderer box3;
        ModelRenderer box4;
        ModelRenderer leaf1;
        ModelRenderer leaf2;
        ModelRenderer leaf3;
        ModelRenderer leaf4;
        ModelRenderer leaf5;
        ModelRenderer leaf6;

        public ModelForestCreeper() {
            super();
            int x = 256;
            int y = 256;
            this.box1 = new ModelRenderer(this, 0, 0);
            this.box1.setTextureSize(x, y);
            this.box1.addBox(-8, 8, -8, 16, 16, 16, 0);
            this.box2 = new ModelRenderer(this, 0, 0);
            this.box2.setTextureSize(x, y);
            this.box2.addBox(-8, -8, -8, 16, 16, 16, 0);
            this.leaf1 = new ModelRenderer(this, 0, 64);
            this.leaf1.setTextureSize(x, y);
            this.leaf1.addBox(-24, -24, -24, 48, 16, 48, 0);
            this.box3 = new ModelRenderer(this, 0, 0);
            this.box3.setTextureSize(x, y);
            this.box3.addBox(-8, -24, -8, 16, 16, 16, 0);
            this.leaf2 = new ModelRenderer(this, 0, 32);
            this.leaf2.setTextureSize(x, y);
            this.leaf2.addBox(-24, -40, -8, 16, 16, 16, 0);
            this.leaf3 = new ModelRenderer(this, 0, 32);
            this.leaf3.setTextureSize(x, y);
            this.leaf3.addBox(-8, -40, -24, 16, 16, 16, 0);
            this.leaf4 = new ModelRenderer(this, 0, 32);
            this.leaf4.setTextureSize(x, y);
            this.leaf4.addBox(8, -40, -8, 16, 16, 16, 0);
            this.leaf5 = new ModelRenderer(this, 0, 32);
            this.leaf5.setTextureSize(x, y);
            this.leaf5.addBox(-8, -40, 8, 16, 16, 16, 0);
            this.box4 = new ModelRenderer(this, 0, 0);
            this.box4.setTextureSize(x, y);
            this.box4.addBox(-8, -40, -8, 16, 16, 16, 0);
            this.leaf6 = new ModelRenderer(this, 0, 32);
            this.leaf6.setTextureSize(x, y);
            this.leaf6.addBox(-8, -56, -8, 16, 16, 16, 0);
        }

        @Override
        public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
                float headPitch, float scale) {
            this.box1.rotateAngleX = 0.0f;
            this.box1.rotateAngleY = 0.0f;
            this.box1.rotateAngleZ = 0.0f;
            this.box1.setRotationPoint(0.0f, 0.0f, 0.0f);
            this.box1.render(scale);
            this.box2.rotateAngleX = 0.0f;
            this.box2.rotateAngleY = 0.0f;
            this.box2.rotateAngleZ = 0.0f;
            this.box2.setRotationPoint(0.0f, 0.0f, 0.0f);
            this.box2.render(scale);
            this.leaf1.rotateAngleX = 0.0f;
            this.leaf1.rotateAngleY = 0.0f;
            this.leaf1.rotateAngleZ = 0.0f;
            this.leaf1.setRotationPoint(0.0f, 0.0f, 0.0f);
            this.leaf1.render(scale);
            this.box3.rotateAngleX = 0.0f;
            this.box3.rotateAngleY = 0.0f;
            this.box3.rotateAngleZ = 0.0f;
            this.box3.setRotationPoint(0.0f, 0.0f, 0.0f);
            this.leaf2.rotateAngleX = 0.0f;
            this.leaf2.rotateAngleY = 0.0f;
            this.leaf2.rotateAngleZ = 0.0f;
            this.leaf2.setRotationPoint(0.0f, 0.0f, 0.0f);
            this.leaf2.render(scale);
            this.leaf3.rotateAngleX = 0.0f;
            this.leaf3.rotateAngleY = 0.0f;
            this.leaf3.rotateAngleZ = 0.0f;
            this.leaf3.setRotationPoint(0.0f, 0.0f, 0.0f);
            this.leaf3.render(scale);
            this.leaf4.rotateAngleX = 0.0f;
            this.leaf4.rotateAngleY = 0.0f;
            this.leaf4.rotateAngleZ = 0.0f;
            this.leaf4.setRotationPoint(0.0f, 0.0f, 0.0f);
            this.leaf4.render(scale);
            this.leaf5.rotateAngleX = 0.0f;
            this.leaf5.rotateAngleY = 0.0f;
            this.leaf5.rotateAngleZ = 0.0f;
            this.leaf5.setRotationPoint(0.0f, 0.0f, 0.0f);
            this.leaf5.render(scale);
            this.box4.rotateAngleX = 0.0f;
            this.box4.rotateAngleY = 0.0f;
            this.box4.rotateAngleZ = 0.0f;
            this.box4.setRotationPoint(0.0f, 0.0f, 0.0f);
            this.box4.render(scale);
            this.leaf6.rotateAngleX = 0.0f;
            this.leaf6.rotateAngleY = 0.0f;
            this.leaf6.rotateAngleZ = 0.0f;
            this.leaf6.setRotationPoint(0.0f, 0.0f, 0.0f);
            this.leaf6.render(scale);
        }

        @Override
        public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
                float headPitch, float scaleFactor, Entity entityIn) {
            this.box1.rotateAngleX = 0.0f;
            this.box1.rotateAngleY = 0.0f;
            this.box1.rotateAngleZ = 0.0f;
            this.box1.setRotationPoint(0.0f, 0.0f, 0.0f);
            this.box2.rotateAngleX = 0.0f;
            this.box2.rotateAngleY = 0.0f;
            this.box2.rotateAngleZ = 0.0f;
            this.box2.setRotationPoint(0.0f, 0.0f, 0.0f);
            this.box3.rotateAngleX = 0.0f;
            this.box3.rotateAngleY = 0.0f;
            this.box3.rotateAngleZ = 0.0f;
            this.box3.setRotationPoint(0.0f, 0.0f, 0.0f);
            this.leaf1.rotateAngleX = 0.0f;
            this.leaf1.rotateAngleY = 0.0f;
            this.leaf1.rotateAngleZ = 0.0f;
            this.leaf1.setRotationPoint(0.0f, 0.0f, 0.0f);
            this.box4.rotateAngleX = 0.0f;
            this.box4.rotateAngleY = 0.0f;
            this.box4.rotateAngleZ = 0.0f;
            this.box4.setRotationPoint(0.0f, 0.0f, 0.0f);
            this.leaf2.rotateAngleX = 0.0f;
            this.leaf2.rotateAngleY = 0.0f;
            this.leaf2.rotateAngleZ = 0.0f;
            this.leaf2.setRotationPoint(0.0f, 0.0f, 0.0f);
            this.leaf3.rotateAngleX = 0.0f;
            this.leaf3.rotateAngleY = 0.0f;
            this.leaf3.rotateAngleZ = 0.0f;
            this.leaf3.setRotationPoint(0.0f, 0.0f, 0.0f);
            this.leaf4.rotateAngleX = 0.0f;
            this.leaf4.rotateAngleY = 0.0f;
            this.leaf4.rotateAngleZ = 0.0f;
            this.leaf4.setRotationPoint(0.0f, 0.0f, 0.0f);
            this.leaf5.rotateAngleX = 0.0f;
            this.leaf5.rotateAngleY = 0.0f;
            this.leaf5.rotateAngleZ = 0.0f;
            this.leaf5.setRotationPoint(0.0f, 0.0f, 0.0f);
            this.leaf6.rotateAngleX = 0.0f;
            this.leaf6.rotateAngleY = 0.0f;
            this.leaf6.rotateAngleZ = 0.0f;
            this.leaf6.setRotationPoint(0.0f, 0.0f, 0.0f);
        }
    }
}
