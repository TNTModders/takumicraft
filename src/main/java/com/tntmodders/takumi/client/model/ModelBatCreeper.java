package com.tntmodders.takumi.client.model;

import com.tntmodders.takumi.entity.mobs.EntityBatCreeper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelBatCreeper extends ModelBase {
    
    private final ModelRenderer BatCreeperHead;
    /**
     * The body box of the BatCreeper model.
     */
    private final ModelRenderer BatCreeperBody;
    /**
     * The inner right wing box of the BatCreeper model.
     */
    private final ModelRenderer BatCreeperRightWing;
    /**
     * The inner left wing box of the BatCreeper model.
     */
    private final ModelRenderer BatCreeperLeftWing;
    /**
     * The outer right wing box of the BatCreeper model.
     */
    private final ModelRenderer BatCreeperOuterRightWing;
    /**
     * The outer left wing box of the BatCreeper model.
     */
    private final ModelRenderer BatCreeperOuterLeftWing;
    
    public ModelBatCreeper() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.BatCreeperHead = new ModelRenderer(this, 0, 0);
        this.BatCreeperHead.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6);
        ModelRenderer modelrenderer = new ModelRenderer(this, 24, 0);
        modelrenderer.addBox(-4.0F, -6.0F, -2.0F, 3, 4, 1);
        this.BatCreeperHead.addChild(modelrenderer);
        ModelRenderer modelrenderer1 = new ModelRenderer(this, 24, 0);
        modelrenderer1.mirror = true;
        modelrenderer1.addBox(1.0F, -6.0F, -2.0F, 3, 4, 1);
        this.BatCreeperHead.addChild(modelrenderer1);
        this.BatCreeperBody = new ModelRenderer(this, 0, 16);
        this.BatCreeperBody.addBox(-3.0F, 4.0F, -3.0F, 6, 12, 6);
        this.BatCreeperBody.setTextureOffset(0, 34).addBox(-5.0F, 16.0F, 0.0F, 10, 6, 1);
        this.BatCreeperRightWing = new ModelRenderer(this, 42, 0);
        this.BatCreeperRightWing.addBox(-12.0F, 1.0F, 1.5F, 10, 16, 1);
        this.BatCreeperOuterRightWing = new ModelRenderer(this, 24, 16);
        this.BatCreeperOuterRightWing.setRotationPoint(-12.0F, 1.0F, 1.5F);
        this.BatCreeperOuterRightWing.addBox(-8.0F, 1.0F, 0.0F, 8, 12, 1);
        this.BatCreeperLeftWing = new ModelRenderer(this, 42, 0);
        this.BatCreeperLeftWing.mirror = true;
        this.BatCreeperLeftWing.addBox(2.0F, 1.0F, 1.5F, 10, 16, 1);
        this.BatCreeperOuterLeftWing = new ModelRenderer(this, 24, 16);
        this.BatCreeperOuterLeftWing.mirror = true;
        this.BatCreeperOuterLeftWing.setRotationPoint(12.0F, 1.0F, 1.5F);
        this.BatCreeperOuterLeftWing.addBox(0.0F, 1.0F, 0.0F, 8, 12, 1);
        this.BatCreeperBody.addChild(this.BatCreeperRightWing);
        this.BatCreeperBody.addChild(this.BatCreeperLeftWing);
        this.BatCreeperRightWing.addChild(this.BatCreeperOuterRightWing);
        this.BatCreeperLeftWing.addChild(this.BatCreeperOuterLeftWing);
    }
    
    /**
     * Sets the models various rotation angles then renders the model.
     */
    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        this.BatCreeperHead.render(scale);
        this.BatCreeperBody.render(scale);
    }
    
    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor,
            Entity entityIn) {
        if (((EntityBatCreeper) entityIn).getIsBatHanging()) {
            this.BatCreeperHead.rotateAngleX = headPitch * 0.017453292F;
            this.BatCreeperHead.rotateAngleY = (float) Math.PI - netHeadYaw * 0.017453292F;
            this.BatCreeperHead.rotateAngleZ = (float) Math.PI;
            this.BatCreeperHead.setRotationPoint(0.0F, -2.0F, 0.0F);
            this.BatCreeperRightWing.setRotationPoint(-3.0F, 0.0F, 3.0F);
            this.BatCreeperLeftWing.setRotationPoint(3.0F, 0.0F, 3.0F);
            this.BatCreeperBody.rotateAngleX = (float) Math.PI;
            this.BatCreeperRightWing.rotateAngleX = -0.15707964F;
            this.BatCreeperRightWing.rotateAngleY = -((float) Math.PI * 2F / 5F);
            this.BatCreeperOuterRightWing.rotateAngleY = -1.7278761F;
            this.BatCreeperLeftWing.rotateAngleX = this.BatCreeperRightWing.rotateAngleX;
            this.BatCreeperLeftWing.rotateAngleY = -this.BatCreeperRightWing.rotateAngleY;
            this.BatCreeperOuterLeftWing.rotateAngleY = -this.BatCreeperOuterRightWing.rotateAngleY;
        } else {
            this.BatCreeperHead.rotateAngleX = headPitch * 0.017453292F;
            this.BatCreeperHead.rotateAngleY = netHeadYaw * 0.017453292F;
            this.BatCreeperHead.rotateAngleZ = 0.0F;
            this.BatCreeperHead.setRotationPoint(0.0F, 0.0F, 0.0F);
            this.BatCreeperRightWing.setRotationPoint(0.0F, 0.0F, 0.0F);
            this.BatCreeperLeftWing.setRotationPoint(0.0F, 0.0F, 0.0F);
            this.BatCreeperBody.rotateAngleX = (float) Math.PI / 4F + MathHelper.cos(ageInTicks * 0.1F) * 0.15F;
            this.BatCreeperBody.rotateAngleY = 0.0F;
            this.BatCreeperRightWing.rotateAngleY = MathHelper.cos(ageInTicks * 1.3F) * (float) Math.PI * 0.25F;
            this.BatCreeperLeftWing.rotateAngleY = -this.BatCreeperRightWing.rotateAngleY;
            this.BatCreeperOuterRightWing.rotateAngleY = this.BatCreeperRightWing.rotateAngleY * 0.5F;
            this.BatCreeperOuterLeftWing.rotateAngleY = -this.BatCreeperRightWing.rotateAngleY * 0.5F;
        }
    }
}