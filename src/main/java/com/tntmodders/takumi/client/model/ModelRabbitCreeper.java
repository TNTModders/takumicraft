package com.tntmodders.takumi.client.model;

import com.tntmodders.takumi.entity.mobs.EntityRabbitCreeper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelRabbitCreeper extends ModelBase {

    private final ModelRenderer rabbitLeftFoot;
    private final ModelRenderer rabbitRightFoot;
    private final ModelRenderer rabbitLeftThigh;
    private final ModelRenderer rabbitRightThigh;
    private final ModelRenderer rabbitBody;
    private final ModelRenderer rabbitLeftArm;
    private final ModelRenderer rabbitRightArm;
    private final ModelRenderer rabbitHead;
    private final ModelRenderer rabbitRightEar;
    private final ModelRenderer rabbitLeftEar;
    private final ModelRenderer rabbitTail;
    private final ModelRenderer rabbitNose;
    private float jumpRotation;

    public ModelRabbitCreeper() {
        this.setTextureOffset("head.main", 0, 0);
        this.setTextureOffset("head.nose", 0, 24);
        this.setTextureOffset("head.ear1", 0, 10);
        this.setTextureOffset("head.ear2", 6, 10);
        this.rabbitLeftFoot = new ModelRenderer(this, 26, 24);
        this.rabbitLeftFoot.addBox(-1.0F, 5.5F, -3.7F, 2, 1, 7);
        this.rabbitLeftFoot.setRotationPoint(3.0F, 17.5F, 3.7F);
        this.rabbitLeftFoot.mirror = true;
        this.setRotationOffset(this.rabbitLeftFoot, 0.0F, 0.0F);
        this.rabbitRightFoot = new ModelRenderer(this, 8, 24);
        this.rabbitRightFoot.addBox(-1.0F, 5.5F, -3.7F, 2, 1, 7);
        this.rabbitRightFoot.setRotationPoint(-3.0F, 17.5F, 3.7F);
        this.rabbitRightFoot.mirror = true;
        this.setRotationOffset(this.rabbitRightFoot, 0.0F, 0.0F);
        this.rabbitLeftThigh = new ModelRenderer(this, 30, 15);
        this.rabbitLeftThigh.addBox(-1.0F, 0.0F, 0.0F, 2, 4, 5);
        this.rabbitLeftThigh.setRotationPoint(3.0F, 17.5F, 3.7F);
        this.rabbitLeftThigh.mirror = true;
        this.setRotationOffset(this.rabbitLeftThigh, -0.34906584F, 0.0F);
        this.rabbitRightThigh = new ModelRenderer(this, 16, 15);
        this.rabbitRightThigh.addBox(-1.0F, 0.0F, 0.0F, 2, 4, 5);
        this.rabbitRightThigh.setRotationPoint(-3.0F, 17.5F, 3.7F);
        this.rabbitRightThigh.mirror = true;
        this.setRotationOffset(this.rabbitRightThigh, -0.34906584F, 0.0F);
        this.rabbitBody = new ModelRenderer(this, 0, 0);
        this.rabbitBody.addBox(-3.0F, -2.0F, -10.0F, 6, 5, 10);
        this.rabbitBody.setRotationPoint(0.0F, 19.0F, 8.0F);
        this.rabbitBody.mirror = true;
        this.setRotationOffset(this.rabbitBody, -0.34906584F, 0.0F);
        this.rabbitLeftArm = new ModelRenderer(this, 8, 15);
        this.rabbitLeftArm.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2);
        this.rabbitLeftArm.setRotationPoint(3.0F, 17.0F, -1.0F);
        this.rabbitLeftArm.mirror = true;
        this.setRotationOffset(this.rabbitLeftArm, -0.17453292F, 0.0F);
        this.rabbitRightArm = new ModelRenderer(this, 0, 15);
        this.rabbitRightArm.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2);
        this.rabbitRightArm.setRotationPoint(-3.0F, 17.0F, -1.0F);
        this.rabbitRightArm.mirror = true;
        this.setRotationOffset(this.rabbitRightArm, -0.17453292F, 0.0F);
        this.rabbitHead = new ModelRenderer(this, 32, 0);
        this.rabbitHead.addBox(-2.5F, -4.0F, -5.0F, 5, 4, 5);
        this.rabbitHead.setRotationPoint(0.0F, 16.0F, -1.0F);
        this.rabbitHead.mirror = true;
        this.setRotationOffset(this.rabbitHead, 0.0F, 0.0F);
        this.rabbitRightEar = new ModelRenderer(this, 52, 0);
        this.rabbitRightEar.addBox(-2.5F, -9.0F, -1.0F, 2, 5, 1);
        this.rabbitRightEar.setRotationPoint(0.0F, 16.0F, -1.0F);
        this.rabbitRightEar.mirror = true;
        this.setRotationOffset(this.rabbitRightEar, 0.0F, -0.2617994F);
        this.rabbitLeftEar = new ModelRenderer(this, 58, 0);
        this.rabbitLeftEar.addBox(0.5F, -9.0F, -1.0F, 2, 5, 1);
        this.rabbitLeftEar.setRotationPoint(0.0F, 16.0F, -1.0F);
        this.rabbitLeftEar.mirror = true;
        this.setRotationOffset(this.rabbitLeftEar, 0.0F, 0.2617994F);
        this.rabbitTail = new ModelRenderer(this, 52, 6);
        this.rabbitTail.addBox(-1.5F, -1.5F, 0.0F, 3, 3, 2);
        this.rabbitTail.setRotationPoint(0.0F, 20.0F, 7.0F);
        this.rabbitTail.mirror = true;
        this.setRotationOffset(this.rabbitTail, -0.3490659F, 0.0F);
        this.rabbitNose = new ModelRenderer(this, 32, 9);
        this.rabbitNose.addBox(-0.5F, -2.5F, -5.5F, 1, 1, 1);
        this.rabbitNose.setRotationPoint(0.0F, 16.0F, -1.0F);
        this.rabbitNose.mirror = true;
        this.setRotationOffset(this.rabbitNose, 0.0F, 0.0F);
    }

    private void setRotationOffset(ModelRenderer p_setRotationOffset_1_, float p_setRotationOffset_2_,
            float p_setRotationOffset_3_) {
        p_setRotationOffset_1_.rotateAngleX = p_setRotationOffset_2_;
        p_setRotationOffset_1_.rotateAngleY = p_setRotationOffset_3_;
        p_setRotationOffset_1_.rotateAngleZ = 0.0F;
    }

    @Override
    public void render(Entity p_render_1_, float p_render_2_, float p_render_3_, float p_render_4_, float p_render_5_,
            float p_render_6_, float p_render_7_) {
        this.setRotationAngles(p_render_2_, p_render_3_, p_render_4_, p_render_5_, p_render_6_, p_render_7_,
                p_render_1_);
        if (this.isChild) {
            float lvt_8_1_ = 1.5F;
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.56666666F, 0.56666666F, 0.56666666F);
            GlStateManager.translate(0.0F, 22.0F * p_render_7_, 2.0F * p_render_7_);
            this.rabbitHead.render(p_render_7_);
            this.rabbitLeftEar.render(p_render_7_);
            this.rabbitRightEar.render(p_render_7_);
            this.rabbitNose.render(p_render_7_);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.4F, 0.4F, 0.4F);
            GlStateManager.translate(0.0F, 36.0F * p_render_7_, 0.0F);
            this.rabbitLeftFoot.render(p_render_7_);
            this.rabbitRightFoot.render(p_render_7_);
            this.rabbitLeftThigh.render(p_render_7_);
            this.rabbitRightThigh.render(p_render_7_);
            this.rabbitBody.render(p_render_7_);
            this.rabbitLeftArm.render(p_render_7_);
            this.rabbitRightArm.render(p_render_7_);
            this.rabbitTail.render(p_render_7_);
            GlStateManager.popMatrix();
        } else {
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.6F, 0.6F, 0.6F);
            GlStateManager.translate(0.0F, 16.0F * p_render_7_, 0.0F);
            this.rabbitLeftFoot.render(p_render_7_);
            this.rabbitRightFoot.render(p_render_7_);
            this.rabbitLeftThigh.render(p_render_7_);
            this.rabbitRightThigh.render(p_render_7_);
            this.rabbitBody.render(p_render_7_);
            this.rabbitLeftArm.render(p_render_7_);
            this.rabbitRightArm.render(p_render_7_);
            this.rabbitHead.render(p_render_7_);
            this.rabbitRightEar.render(p_render_7_);
            this.rabbitLeftEar.render(p_render_7_);
            this.rabbitTail.render(p_render_7_);
            this.rabbitNose.render(p_render_7_);
            GlStateManager.popMatrix();
        }

    }

    @Override
    public void setRotationAngles(float p_setRotationAngles_1_, float p_setRotationAngles_2_,
            float p_setRotationAngles_3_, float p_setRotationAngles_4_, float p_setRotationAngles_5_,
            float p_setRotationAngles_6_, Entity p_setRotationAngles_7_) {
        float lvt_8_1_ = p_setRotationAngles_3_ - (float) p_setRotationAngles_7_.ticksExisted;
        EntityRabbitCreeper lvt_9_1_ = (EntityRabbitCreeper) p_setRotationAngles_7_;
        this.rabbitNose.rotateAngleX = p_setRotationAngles_5_ * 0.017453292F;
        this.rabbitHead.rotateAngleX = p_setRotationAngles_5_ * 0.017453292F;
        this.rabbitRightEar.rotateAngleX = p_setRotationAngles_5_ * 0.017453292F;
        this.rabbitLeftEar.rotateAngleX = p_setRotationAngles_5_ * 0.017453292F;
        this.rabbitNose.rotateAngleY = p_setRotationAngles_4_ * 0.017453292F;
        this.rabbitHead.rotateAngleY = p_setRotationAngles_4_ * 0.017453292F;
        this.rabbitRightEar.rotateAngleY = this.rabbitNose.rotateAngleY - 0.2617994F;
        this.rabbitLeftEar.rotateAngleY = this.rabbitNose.rotateAngleY + 0.2617994F;
        this.jumpRotation = MathHelper.sin(lvt_9_1_.setJumpCompletion(lvt_8_1_) * 3.1415927F);
        this.rabbitLeftThigh.rotateAngleX = (this.jumpRotation * 50.0F - 21.0F) * 0.017453292F;
        this.rabbitRightThigh.rotateAngleX = (this.jumpRotation * 50.0F - 21.0F) * 0.017453292F;
        this.rabbitLeftFoot.rotateAngleX = this.jumpRotation * 50.0F * 0.017453292F;
        this.rabbitRightFoot.rotateAngleX = this.jumpRotation * 50.0F * 0.017453292F;
        this.rabbitLeftArm.rotateAngleX = (this.jumpRotation * -40.0F - 11.0F) * 0.017453292F;
        this.rabbitRightArm.rotateAngleX = (this.jumpRotation * -40.0F - 11.0F) * 0.017453292F;
    }

    @Override
    public void setLivingAnimations(EntityLivingBase p_setLivingAnimations_1_, float p_setLivingAnimations_2_,
            float p_setLivingAnimations_3_, float p_setLivingAnimations_4_) {
        super.setLivingAnimations(p_setLivingAnimations_1_, p_setLivingAnimations_2_, p_setLivingAnimations_3_,
                p_setLivingAnimations_4_);
        this.jumpRotation = MathHelper
                .sin(((EntityRabbitCreeper) p_setLivingAnimations_1_).setJumpCompletion(p_setLivingAnimations_4_) *
                        3.1415927F);
    }
}
