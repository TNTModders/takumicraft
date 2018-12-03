package com.tntmodders.takumi.client.model.sp;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelPlayerTHM extends ModelBase {
    public ModelRenderer head;
    public ModelRenderer creeperArmor;
    public ModelRenderer body;
    public ModelRenderer leg1;
    public ModelRenderer leg2;
    public ModelRenderer leg3;
    public ModelRenderer leg4;
    public ModelRenderer bodyw;
    public ModelRenderer leg1w;
    public ModelRenderer leg2w;
    public ModelRenderer leg3w;
    public ModelRenderer leg4w;

    public ModelPlayerTHM() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        int i = 6;
        this.head = new ModelRenderer(this, 0, 0);
        this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0);
        this.head.setRotationPoint(0.0F, 6.0F, 0.0F);
        this.creeperArmor = new ModelRenderer(this, 32, 0);
        this.creeperArmor.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0 + 0.5F);
        this.creeperArmor.setRotationPoint(0.0F, 6.0F, 0.0F);
        this.body = new ModelRenderer(this, 16, 16);
        this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0);
        this.body.setRotationPoint(0.0F, 6.0F, 0.0F);
        this.bodyw = new ModelRenderer(this, 16, 32);
        this.bodyw.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.25f);
        this.bodyw.setRotationPoint(0.0F, 6.0F, 0.0F);
        this.leg1 = new ModelRenderer(this, 0, 16);
        this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0);
        this.leg1.setRotationPoint(-2.0F, 18.0F, 4.0F);
        this.leg1w = new ModelRenderer(this, 0, 32);
        this.leg1w.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.25f);
        this.leg1w.setRotationPoint(-2.0F, 18.0F, 4.0F);
        this.leg2 = new ModelRenderer(this, 16, 48);
        this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0);
        this.leg2.setRotationPoint(2.0F, 18.0F, 4.0F);
        this.leg2w = new ModelRenderer(this, 0, 48);
        this.leg2w.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.25f);
        this.leg2w.setRotationPoint(2.0F, 18.0F, 4.0F);
        this.leg3 = new ModelRenderer(this, 0, 16);
        this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0);
        this.leg3.setRotationPoint(-2.0F, 18.0F, -4.0F);
        this.leg3w = new ModelRenderer(this, 0, 32);
        this.leg3w.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.25f);
        this.leg3w.setRotationPoint(-2.0F, 18.0F, -4.0F);
        this.leg4 = new ModelRenderer(this, 16, 48);
        this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0);
        this.leg4.setRotationPoint(2.0F, 18.0F, -4.0F);
        this.leg4w = new ModelRenderer(this, 0, 48);
        this.leg4w.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.25f);
        this.leg4w.setRotationPoint(2.0F, 18.0F, -4.0F);
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
            float headPitch, float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        this.head.render(scale);
        this.body.render(scale);
        this.bodyw.render(scale);
        this.leg1.render(scale);
        this.leg2.render(scale);
        this.leg3.render(scale);
        this.leg4.render(scale);
        this.leg1w.render(scale);
        this.leg2w.render(scale);
        this.leg3w.render(scale);
        this.leg4w.render(scale);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
            float headPitch, float scaleFactor, Entity entityIn) {
        this.head.rotateAngleY = netHeadYaw * 0.017453292F;
        this.head.rotateAngleX = headPitch * 0.017453292F;
        this.leg1.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leg2.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.leg3.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.leg4.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;

        copyModelAngles(this.body, this.bodyw);
        copyModelAngles(this.leg1, leg1w);
        copyModelAngles(this.leg2, leg2w);
        copyModelAngles(this.leg3, leg3w);
        copyModelAngles(this.leg4, leg4w);
    }
}
