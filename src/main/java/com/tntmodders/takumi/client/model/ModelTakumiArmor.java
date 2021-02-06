package com.tntmodders.takumi.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

public class ModelTakumiArmor extends ModelBiped {

    public ModelRenderer leg1;
    public ModelRenderer leg2;
    public ModelRenderer leg1_;
    public ModelRenderer leg2_;
    public ModelRenderer leg3;
    public ModelRenderer leg4;


    public ModelTakumiArmor() {
        this(0.0F);
    }

    public ModelTakumiArmor(float modelSize) {
        this(modelSize, 0.0F, 64, 32);
    }

    public ModelTakumiArmor(float modelSize, float p_i1149_2_, int textureWidthIn, int textureHeightIn) {
        super(modelSize, p_i1149_2_, textureWidthIn, textureHeightIn);
        this.leftArmPose = ModelBiped.ArmPose.EMPTY;
        this.rightArmPose = ModelBiped.ArmPose.EMPTY;
        this.textureWidth = textureWidthIn;
        this.textureHeight = textureHeightIn;
        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize + 0.55f);
        this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedBody = new ModelRenderer(this, 16, 16);
        this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, modelSize + 0.35f);
        this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leg1 = new ModelRenderer(this, 0, 16);
        this.leg1.addBox(-2.0F, 6.0F, -3.0F, 4, 6, 4, modelSize + 0.55f);
        this.leg1.setRotationPoint(-2.0F, 12.0F, 3F);
        this.leg2 = new ModelRenderer(this, 0, 16);
        this.leg2.addBox(-2.0F, 6.0F, -3.0F, 4, 6, 4, modelSize + 0.55f);
        this.leg2.setRotationPoint(2.0F, 12.0F, 3F);
        this.leg1_ = new ModelRenderer(this, 0, 16);
        this.leg1_.addBox(-2.0F, 6.0F, -3.0F, 4, 6, 4, modelSize + 0.55f);
        this.leg1_.setRotationPoint(-2.0F, 12.0F, -3F);
        this.leg2_ = new ModelRenderer(this, 0, 16);
        this.leg2_.addBox(-2.0F, 6.0F, -3.0F, 4, 6, 4, modelSize + 0.55f);
        this.leg2_.setRotationPoint(2.0F, 12.0F, -3F);
        this.leg3 = new ModelRenderer(this, 0, 16);
        this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, modelSize + 0.35f);
        this.leg3.setRotationPoint(-2.0F, 12.0F, 0.0F);
        this.leg4 = new ModelRenderer(this, 0, 16);
        this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, modelSize + 0.35f);
        this.leg4.setRotationPoint(2.0F, 12.0F, 0.0F);
        this.bipedLeftLeg.isHidden = true;
        this.bipedRightLeg.isHidden = true;
        this.bipedHeadwear.isHidden = true;
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        boolean flag = entityIn instanceof EntityLivingBase && ((EntityLivingBase) entityIn).getTicksElytraFlying() > 4;
        this.bipedHead.rotateAngleY = netHeadYaw * 0.017453292F;
        if (flag) {
            this.bipedHead.rotateAngleX = -((float) Math.PI / 4F);
        } else {
            this.bipedHead.rotateAngleX = headPitch * 0.017453292F;
        }
        this.bipedBody.rotateAngleY = 0.0F;
        float f = 1.0F;

        if (flag) {
            f = (float) (entityIn.motionX * entityIn.motionX + entityIn.motionY * entityIn.motionY + entityIn.motionZ * entityIn.motionZ);
            f = f / 0.2F;
            f = f * f * f;
        }

        if (f < 1.0F) {
            f = 1.0F;
        }
        this.leg1.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / f;
        this.leg2.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount / f;
        this.leg1_.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / f;
        this.leg2_.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount / f;
        this.leg3.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / f;
        this.leg4.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount / f;
        if (this.isSneak) {
            this.bipedBody.rotateAngleX = 0.5F;
            this.leg1.rotationPointZ = 7.0F;
            this.leg2.rotationPointZ = 7.0F;
            this.leg1.rotationPointY = 9.0F;
            this.leg2.rotationPointY = 9.0F;
            this.leg1_.rotationPointZ = 1.0F;
            this.leg2_.rotationPointZ = 1.0F;
            this.leg1_.rotationPointY = 9.0F;
            this.leg2_.rotationPointY = 9.0F;
            this.leg3.rotationPointZ = 4.0F;
            this.leg4.rotationPointZ = 4.0F;
            this.leg3.rotationPointY = 9.0F;
            this.leg4.rotationPointY = 9.0F;
            this.bipedHead.rotationPointY = 1.0F;
        } else {
            this.bipedBody.rotateAngleX = 0.0F;
            // this.leg1.rotationPointZ = 0.1F;
            // this.leg2.rotationPointZ = 0.1F;
            this.leg1.rotationPointY = 12.0F;
            this.leg2.rotationPointY = 12.0F;
            //this.leg1_.rotationPointZ = 0.1F;
            //this.leg2_.rotationPointZ = 0.1F;
            this.leg1_.rotationPointY = 12.0F;
            this.leg2_.rotationPointY = 12.0F;
            this.leg3.rotationPointZ = 0.1F;
            this.leg4.rotationPointZ = 0.1F;
            this.leg3.rotationPointY = 12.0F;
            this.leg4.rotationPointY = 12.0F;
            this.bipedHead.rotationPointY = 0.0F;
        }
        if (this.isRiding) {
            this.leg1.rotationPointX=-2F;
            this.leg2.rotationPointX=2F;
            this.leg1.rotationPointZ = 0F;
            this.leg2.rotationPointZ = 0F;
            this.leg1.rotationPointY = 9.5F;
            this.leg2.rotationPointY =  9.5F;
            this.leg1_.rotationPointX=-2F;
            this.leg2_.rotationPointX=2F;
            this.leg1_.rotationPointZ = 0F;
            this.leg2_.rotationPointZ = 0F;
            this.leg1_.rotationPointY = 14.5F;
            this.leg2_.rotationPointY = 14.5F;
            this.leg1.rotateAngleX = -1.4137167F;
            this.leg1.rotateAngleY = ((float) Math.PI / 10F);
            this.leg1.rotateAngleZ = 0.07853982F;
            this.leg2.rotateAngleX = -1.4137167F;
            this.leg2.rotateAngleY = -((float) Math.PI / 10F);
            this.leg2.rotateAngleZ = -0.07853982F;
            this.leg1_.rotateAngleX = -1.4137167F;
            this.leg1_.rotateAngleY = ((float) Math.PI / 10F);
            this.leg1_.rotateAngleZ = 0.07853982F;
            this.leg2_.rotateAngleX = -1.4137167F;
            this.leg2_.rotateAngleY = -((float) Math.PI / 10F);
            this.leg2_.rotateAngleZ = -0.07853982F;
            this.leg3.rotateAngleX = -1.4137167F;
            this.leg3.rotateAngleY = ((float) Math.PI / 10F);
            this.leg3.rotateAngleZ = 0.07853982F;
            this.leg4.rotateAngleX = -1.4137167F;
            this.leg4.rotateAngleY = -((float) Math.PI / 10F);
            this.leg4.rotateAngleZ = -0.07853982F;
        }
    }

    public static class ModelTakumiArmor_Head extends ModelTakumiArmor {
        @Override
        public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            if (entityIn.isSneaking()) {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }
            this.bipedHead.render(scale);
            if (entityIn.isSneaking()) {
                GlStateManager.translate(0.0F, -0.2F, 0.0F);
            }
        }
    }

    public static class ModelTakumiArmor_Chest extends ModelTakumiArmor {
        @Override
        public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            if (entityIn.isSneaking()) {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }
            this.bipedBody.render(scale);
            if (entityIn.isSneaking()) {
                GlStateManager.translate(0.0F, -0.2F, 0.0F);
            }
        }
    }

    public static class ModelTakumiArmor_Legs extends ModelTakumiArmor {
        @Override
        public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            if (entityIn.isSneaking()) {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }
            this.leg3.render(scale);
            this.leg4.render(scale);
            if (entityIn.isSneaking()) {
                GlStateManager.translate(0.0F, -0.2F, 0.0F);
            }
        }
    }

    public static class ModelTakumiArmor_Feet extends ModelTakumiArmor {
        @Override
        public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            if (entityIn.isSneaking()) {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }
            this.leg1.render(scale);
            this.leg2.render(scale);
            this.leg1_.render(scale);
            this.leg2_.render(scale);
            if (entityIn.isSneaking()) {
                GlStateManager.translate(0.0F, -0.2F, 0.0F);
            }
        }
    }


}
