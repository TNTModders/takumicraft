package com.tntmodders.takumi.client.render;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.entity.item.EntityTakumiParachute;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderTakumiParachute<T extends EntityTakumiParachute> extends Render<T> {
    private final ModelBase model = new ModelTakumiParachute();

    public RenderTakumiParachute(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        return new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/takumiparachute.png");
    }

    @Override
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(180, 0, 0, 1);
        GlStateManager.translate(0, -1.5, 0);
        GlStateManager.rotate(entity.rotationYaw - 90, 0, 1, 0);
        this.bindTexture(this.getEntityTexture(entity));
        this.model.render(entity, 0, 0, 0, 0, 0, 0.0625f);
        GlStateManager.popMatrix();
    }

    private class ModelTakumiParachute extends ModelBase {
        ModelRenderer body;
        ModelRenderer wire1;
        ModelRenderer wire2;
        ModelRenderer wire3;
        ModelRenderer wire4;

        public ModelTakumiParachute() {
            this.textureHeight = 28;
            this.textureWidth = 80;
            this.body = new ModelRenderer(this, 0, 0);
            this.body.addBox(-10.0f, 0f, -10.0f, 20, 8, 20);
            this.body.rotationPointY += 8.0F;

            this.wire1 = new ModelRenderer(this, 0, 0);
            this.wire1.addBox(-5f, 16.0f, -6.0f, 3, 12, 3);

            this.wire2 = new ModelRenderer(this, 0, 0);
            this.wire2.addBox(-5f, 16.0f, 4.0f, 3, 12, 3);

            this.wire3 = new ModelRenderer(this, 0, 0);
            this.wire3.addBox(2f, 16.0f, -6.0f, 3, 12, 3);

            this.wire4 = new ModelRenderer(this, 0, 0);
            this.wire4.addBox(2f, 16.0f, 4.0f, 3, 12, 3);
        }

        /**
         * Sets the models various rotation angles then renders the model.
         */
        @Override
        public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            this.body.render(0.065f);
            this.wire1.render(0.065f);
            this.wire2.render(0.065f);
            this.wire3.render(0.065f);
            this.wire4.render(0.065f);
        }
    }
}
