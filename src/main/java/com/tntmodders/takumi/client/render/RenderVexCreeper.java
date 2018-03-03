package com.tntmodders.takumi.client.render;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.client.render.layer.LayerTakumiCharge;
import com.tntmodders.takumi.entity.mobs.EntityVexCreeper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RenderVexCreeper<T extends EntityVexCreeper> extends RenderLiving<T> implements ITakumiRender {

    private static final ResourceLocation VEX_TEXTURE =
            new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/vexcreeper.png");
    private static final ResourceLocation VEX_CHARGING_TEXTURE =
            new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/vexcreeper_charging" + ".png");
    private int modelVersion;

    public RenderVexCreeper(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelVexCreeper(), 0.3f);
        this.addLayer(new LayerTakumiCharge(this));
        this.modelVersion = ((ModelVexCreeper) this.mainModel).getModelVersion();
    }

    @Override
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        int i = ((ModelVexCreeper) this.mainModel).getModelVersion();

        if (i != this.modelVersion) {
            this.mainModel = new ModelVexCreeper();
            this.modelVersion = i;
        }

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
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
        GlStateManager.scale(0.4F, 0.4F, 0.4F);
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
        return new ModelVexCreeper();
    }

    @SideOnly(Side.CLIENT)
    public static class ModelVexCreeper extends ModelBiped {

        protected ModelRenderer leftWing;
        protected ModelRenderer rightWing;

        public ModelVexCreeper() {
            this(0.0F);
        }

        public ModelVexCreeper(float p_i47224_1_) {
            super(p_i47224_1_, 0.0F, 64, 64);
            this.bipedLeftLeg.showModel = false;
            this.bipedHeadwear.showModel = false;
            this.bipedRightLeg = new ModelRenderer(this, 32, 0);
            this.bipedRightLeg.addBox(-1.0F, -1.0F, -2.0F, 6, 10, 4, 0.0F);
            this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
            this.rightWing = new ModelRenderer(this, 0, 32);
            this.rightWing.addBox(-20.0F, 0.0F, 0.0F, 20, 12, 1);
            this.leftWing = new ModelRenderer(this, 0, 32);
            this.leftWing.mirror = true;
            this.leftWing.addBox(0.0F, 0.0F, 0.0F, 20, 12, 1);
        }

        /**
         * Sets the models various rotation angles then renders the model.
         */
        @Override
        public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
                float headPitch, float scale) {
            super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            this.rightWing.render(scale);
            this.leftWing.render(scale);
        }

        /**
         * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
         * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
         * "far" arms and legs can swing at most.
         */
        @Override
        public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
                float headPitch, float scaleFactor, Entity entityIn) {
            super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor,
                    entityIn);
            EntityVexCreeper entityvex = (EntityVexCreeper) entityIn;

            if (entityvex.isCharging()) {
                if (entityvex.getPrimaryHand() == EnumHandSide.RIGHT) {
                    this.bipedRightArm.rotateAngleX = 3.7699115F;
                } else {
                    this.bipedLeftArm.rotateAngleX = 3.7699115F;
                }
            }

            this.bipedRightLeg.rotateAngleX += (float) Math.PI / 5F;
            this.rightWing.rotationPointZ = 2.0F;
            this.leftWing.rotationPointZ = 2.0F;
            this.rightWing.rotationPointY = 1.0F;
            this.leftWing.rotationPointY = 1.0F;
            this.rightWing.rotateAngleY = 0.47123894F + MathHelper.cos(ageInTicks * 0.8F) * (float) Math.PI * 0.05F;
            this.leftWing.rotateAngleY = -this.rightWing.rotateAngleY;
            this.leftWing.rotateAngleZ = -0.47123894F;
            this.leftWing.rotateAngleX = 0.47123894F;
            this.rightWing.rotateAngleX = 0.47123894F;
            this.rightWing.rotateAngleZ = 0.47123894F;
        }

        public int getModelVersion() {
            return 23;
        }
    }
}
