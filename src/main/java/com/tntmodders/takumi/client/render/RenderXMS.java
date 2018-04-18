package com.tntmodders.takumi.client.render;

import com.tntmodders.takumi.entity.item.EntityXMS;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderXMS<T extends EntityXMS> extends RenderLiving<T> {

    private static final ResourceLocation YMS_TEXTURES = new ResourceLocation("ttextures/entity/ghast/ghast.png");

    public RenderXMS(RenderManager rendermanagerIn) {
        super(rendermanagerIn, new ModelXMS(), 0.5f);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        return YMS_TEXTURES;
    }

    @Override
    protected void applyRotations(T entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) {
        if (entityLiving.getControllingPassenger() instanceof EntityPlayer) {
            rotationYaw = ((EntityPlayer) entityLiving.getControllingPassenger()).rotationYawHead;
        }
        super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
    }

    @Override
    protected void preRenderCallback(T entitylivingbaseIn, float partialTickTime) {
        float f = 1.0F;
        float f1 = 4.5F;
        float f2 = 4.5F;
        GlStateManager.scale(4.5F, 4.5F, 4.5F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private static class ModelXMS extends ModelBase {
        ModelRenderer body;
        //下の翼
        ModelRenderer left_wing_one;
        ModelRenderer right_wing_one;
        ModelRenderer left_engine_one;
        ModelRenderer right_engine_one;
        //上の翼
        ModelRenderer left_wing_two;
        ModelRenderer right_wing_two;
        ModelRenderer left_engine_two;
        ModelRenderer right_engine_two;

        public ModelXMS() {
            this.body = new ModelRenderer(this, 0, 0);
            this.body.addBox(-2.0F, 0F, -9.0F, 4, 3, 18);
            this.left_wing_one = new ModelRenderer(this, 0, 0);
            this.left_wing_one.addBox(0f, 0f, 4f, 18, 1, 4);
            this.left_engine_one = new ModelRenderer(this, 0, 0);
            this.left_engine_one.addBox(2f, 1f, 3f, 4, 3, 6);
            this.right_wing_one = new ModelRenderer(this, 0, 0);
            this.right_wing_one.addBox(0f, -1f, 4f, 18, 1, 4);
            this.right_engine_one = new ModelRenderer(this, 0, 0);
            this.right_engine_one.addBox(-6f, 1f, 3f, 4, 3, 6);
            this.right_wing_one.rotateAngleZ = (float) Math.PI;
            this.left_wing_two = new ModelRenderer(this, 0, 0);
            this.left_wing_two.addBox(0f, 0f, 4f, 18, 1, 4);
            this.left_engine_two = new ModelRenderer(this, 0, 0);
            this.left_engine_two.addBox(2f, -2f, 3f, 4, 3, 6);
            this.right_wing_two = new ModelRenderer(this, 0, 0);
            this.right_wing_two.addBox(0f, -1f, 4f, 18, 1, 4);
            this.right_wing_two.rotateAngleZ = (float) Math.PI;
            this.right_engine_two = new ModelRenderer(this, 0, 0);
            this.right_engine_two.addBox(-6f, -2f, 3f, 4, 3, 6);
        }

        @Override
        public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
                float headPitch, float scale) {
            EntityXMS yms = ((EntityXMS) entityIn);
            int tick = yms.attackModeTick > 10 ? 10 : yms.attackModeTick;
            this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 1.25F, 0.0F);
            this.body.render(scale);
            this.left_wing_one.rotateAngleZ = ((float) (Math.PI / 12 * (0.1 * tick)));
            this.left_wing_one.render(scale);
            this.left_engine_one.render(scale);
            this.right_wing_one.rotateAngleZ = ((float) (Math.PI - Math.PI / 12 * (0.1 * tick)));
            this.right_wing_one.render(scale);
            this.right_engine_one.render(scale);
            this.left_wing_two.rotateAngleZ = -((float) (Math.PI / 12 * (0.1 * tick)));
            this.left_wing_two.render(scale);
            this.left_engine_two.render(scale);
            this.right_wing_two.rotateAngleZ = ((float) (Math.PI + Math.PI / 12 * (0.1 * tick)));
            this.right_wing_two.render(scale);
            this.right_engine_two.render(scale);
            GlStateManager.popMatrix();
        }

    }
}
