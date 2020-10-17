package com.tntmodders.takumi.client.render;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.entity.item.EntityTakumiCannon;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderTakumiCannon extends Render<EntityTakumiCannon> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/takumicannon.png");
    private final ModelBase model = new ModelTakumiCannon();

    public RenderTakumiCannon(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EntityTakumiCannon entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.rotate(180, 1, 0, 0);
        GlStateManager.translate(0, -1, 0);
        this.renderManager.renderEngine.bindTexture(TEXTURE);
        this.model.render(entity, 0, 0, 0, entityYaw, entity.rotationPitch, 0.0625f);
        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityTakumiCannon entity) {
        return null;
    }

    public class ModelTakumiCannon extends ModelBase {
        private final ModelRenderer base;
        private final ModelRenderer cannon;

        public ModelTakumiCannon() {
            this.base = new ModelRenderer(this, 0, 0);
            this.base.setTextureSize(256, 256);
            this.base.addBox(-24, 0, -24, 48, 16, 48);
            this.cannon = new ModelRenderer(this, 0, 64);
            this.cannon.setTextureSize(256, 256);
            this.cannon.addBox(-12, -24, -12, 24, 24, 81);
            this.cannon.setRotationPoint(0, 16, 0);
        }

        @Override
        public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            float f = ((EntityTakumiCannon) entityIn).getFacing().getHorizontalAngle();
            GlStateManager.rotate(f, 0, 1, 0);
            if (entityIn.getControllingPassenger() != null) {
                this.cannon.rotateAngleX = -((float) Math.toRadians(entityIn.getControllingPassenger().rotationPitch));
                if (this.cannon.rotateAngleX < 0) {
                    this.cannon.rotateAngleX = 0;
                }
            }
            this.base.render(scale);
            this.cannon.render(scale);
        }
    }
}
