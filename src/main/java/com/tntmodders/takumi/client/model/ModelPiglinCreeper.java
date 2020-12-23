package com.tntmodders.takumi.client.model;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelPiglinCreeper extends ModelPlayer {
    public final ModelRenderer field_239115_a_;
    public final ModelRenderer field_239116_b_;

    public ModelPiglinCreeper(float p_i232336_1_, int p_i232336_2_, int p_i232336_3_) {
        super(p_i232336_1_, false);
        this.textureWidth = p_i232336_2_;
        this.textureHeight = p_i232336_3_;
        this.bipedBody = new ModelRenderer(this, 16, 16);
        this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, p_i232336_1_);
        this.bipedHead = new ModelRenderer(this);
        this.bipedHead.setTextureOffset(0, 0).addBox(-5.0F, -8.0F, -4.0F, 10, 8, 8, p_i232336_1_);
        this.bipedHead.setTextureOffset(31, 1).addBox(-2.0F, -4.0F, -5.0F, 4, 4, 1, p_i232336_1_);
        this.bipedHead.setTextureOffset(2, 4).addBox(2.0F, -2.0F, -5.0F, 1, 2, 1, p_i232336_1_);
        this.bipedHead.setTextureOffset(2, 0).addBox(-3.0F, -2.0F, -5.0F, 1, 2, 1, p_i232336_1_);
        this.field_239115_a_ = new ModelRenderer(this);
        this.field_239115_a_.setRotationPoint(4.5F, -6.0F, 0.0F);
        this.field_239115_a_.setTextureOffset(51, 6).addBox(0.0F, 0.0F, -2.0F, 1, 5, 4, p_i232336_1_);
        this.bipedHead.addChild(this.field_239115_a_);
        this.field_239116_b_ = new ModelRenderer(this);
        this.field_239116_b_.setRotationPoint(-4.5F, -6.0F, 0.0F);
        this.field_239116_b_.setTextureOffset(39, 6).addBox(-1.0F, 0.0F, -2.0F, 1, 5, 4, p_i232336_1_);
        this.bipedHead.addChild(this.field_239116_b_);
        this.bipedHeadwear = new ModelRenderer(this);
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.bipedBody.render(scale);
        this.bipedHead.render(scale);
        this.bipedHeadwear.render(scale);
        this.bipedLeftArm.render(scale);
        this.bipedLeftLeg.render(scale);
        this.bipedRightArm.render(scale);
        this.bipedRightLeg.render(scale);
    }

    /**
     * Sets this entity's model rotation angles
     */
    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        float f = ((float) Math.PI / 6F);
        float f1 = ageInTicks * 0.1F + limbSwing * 0.5F;
        float f2 = 0.08F + limbSwingAmount * 0.4F;
        this.field_239115_a_.rotateAngleZ = (-(float) Math.PI / 6F) - MathHelper.cos(f1 * 1.2F) * f2;
        this.field_239116_b_.rotateAngleZ = ((float) Math.PI / 6F) + MathHelper.cos(f1) * f2;
    }
}
