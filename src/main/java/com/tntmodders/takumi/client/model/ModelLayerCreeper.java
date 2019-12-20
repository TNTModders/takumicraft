package com.tntmodders.takumi.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelLayerCreeper extends ModelBase {

    ModelRenderer box;

    public ModelLayerCreeper() {
        super();
        this.box = new ModelRenderer(this, 0, 0);
        this.box.setTextureSize(16, 16);
        this.box.addBox(-8, 8, -8, 16, 1, 16, 0);
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
            float headPitch, float scale) {
        this.box.rotateAngleX = 0.0f;
        this.box.rotateAngleY = 0.0f;
        this.box.rotateAngleZ = 0.0f;
        this.box.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.box.render(scale);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
            float headPitch, float scaleFactor, Entity entityIn) {
        this.box.rotateAngleX = 0.0f;
        this.box.rotateAngleY = 0.0f;
        this.box.rotateAngleZ = 0.0f;
        this.box.setRotationPoint(0.0f, 0.0f, 0.0f);
    }
}
