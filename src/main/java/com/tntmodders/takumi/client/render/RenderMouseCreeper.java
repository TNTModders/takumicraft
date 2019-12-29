package com.tntmodders.takumi.client.render;

import com.tntmodders.takumi.entity.mobs.EntityMouseCreeper;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;

public class RenderMouseCreeper<T extends EntityMouseCreeper> extends RenderTakumiCreeper {

    public RenderMouseCreeper(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelMouseCreeper());
    }

    public static class ModelMouseCreeper extends ModelCreeper {
        private ModelRenderer rightEar;
        private ModelRenderer leftEar;

        public ModelMouseCreeper() {
            super();
            this.rightEar = new ModelRenderer(this, 32, 0);
            this.rightEar.addBox(-9.0F, -11F, -3.0F, 7, 7, 2, 0);
            this.rightEar.setRotationPoint(0.0F, 6.0F, 0.0F);
            this.leftEar = new ModelRenderer(this, 32, 0);
            this.leftEar.addBox(2F, -11F, -3.0F, 7, 7, 2, 0);
            this.leftEar.setRotationPoint(0.0F, 6.0F, 0.0F);
        }

        @Override
        public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            this.rightEar.render(scale);
            this.leftEar.render(scale);
        }

        @Override
        public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
            super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
            this.rightEar.rotateAngleY = netHeadYaw * 0.017453292F;
            this.rightEar.rotateAngleX = headPitch * 0.017453292F;
            this.leftEar.rotateAngleY = netHeadYaw * 0.017453292F;
            this.leftEar.rotateAngleX = headPitch * 0.017453292F;
        }

    }
}
