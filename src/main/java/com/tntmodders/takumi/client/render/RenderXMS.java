package com.tntmodders.takumi.client.render;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.entity.item.EntityXMS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
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

    private static final ResourceLocation YMS_TEXTURES =
            new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/xms.png");

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
        GlStateManager.scale(5F, 5F, 5F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        if (!entitylivingbaseIn.onGround) {
            float fa = entitylivingbaseIn.isAttackMode ? 2.5f : 1f;
            GlStateManager.translate(0, -0.35, 0);
            GlStateManager.rotate(entitylivingbaseIn.rotationPitch / fa, 1, 0, 0);
            GlStateManager.translate(0, 0.35, 0);
        } else {
            GlStateManager.rotate(0, 1, 0, 0);
        }
    }

    private static class ModelXMS extends ModelBase {
        static final ResourceLocation LOCATION =
                new ResourceLocation(TakumiCraftCore.MODID, "textures/models/armor/xms_helmet.png");
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
        //キャノピー
        ModelRenderer canopy;
        //操縦手
        ModelRenderer crew_head;
        ModelRenderer crew_body;
        ModelRenderer crew_left_hand;
        ModelRenderer crew_right_hand;
        ModelRenderer crew_head_helmet;

        public ModelXMS() {
            int y = 21;
            this.body = new ModelRenderer(this, 0, 0);
            this.body.setTextureSize(64, 64);
            this.body.addBox(-2.0F, 0F, -9.0F, 4, 3, 18);
            this.canopy = new ModelRenderer(this, 0, y + 14);
            this.canopy.setTextureSize(64, 64);
            this.canopy.addBox(-2.0f, -3, -3, 4, 3, 6);
            this.left_wing_one = new ModelRenderer(this, 0, y);
            this.left_wing_one.setTextureSize(64, 64);
            this.left_wing_one.addBox(0f, 0f, 4f, 18, 1, 4);
            this.left_engine_one = new ModelRenderer(this, 0, y + 5);
            this.left_engine_one.setTextureSize(64, 64);
            this.left_engine_one.addBox(2f, 1f, 3f, 4, 3, 6);
            this.right_wing_one = new ModelRenderer(this, 0, y);
            this.right_wing_one.setTextureSize(64, 64);
            this.right_wing_one.addBox(0f, -1f, 4f, 18, 1, 4);
            this.right_engine_one = new ModelRenderer(this, 0, y + 5);
            this.right_engine_one.setTextureSize(64, 64);
            this.right_engine_one.addBox(-6f, 1f, 3f, 4, 3, 6);
            this.right_wing_one.rotateAngleZ = (float) Math.PI;
            this.left_wing_two = new ModelRenderer(this, 0, y);
            this.left_wing_two.setTextureSize(64, 64);
            this.left_wing_two.addBox(0f, 0f, 4f, 18, 1, 4);
            this.left_engine_two = new ModelRenderer(this, 0, y + 5);
            this.left_engine_two.setTextureSize(64, 64);
            this.left_engine_two.addBox(2f, -2f, 3f, 4, 3, 6);
            this.right_wing_two = new ModelRenderer(this, 0, y);
            this.right_wing_two.setTextureSize(64, 64);
            this.right_wing_two.addBox(0f, -1f, 4f, 18, 1, 4);
            this.right_wing_two.rotateAngleZ = (float) Math.PI;
            this.right_engine_two = new ModelRenderer(this, 0, y + 5);
            this.right_engine_two.setTextureSize(64, 64);
            this.right_engine_two.addBox(-6f, -2f, 3f, 4, 3, 6);

            this.crew_head = new ModelRenderer(this, 0, 0);
            this.crew_head.setTextureSize(64, 64);
            this.crew_head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8);
            this.crew_head_helmet = new ModelRenderer(this, 0, 0);
            this.crew_head_helmet.setTextureSize(64, 32);
            this.crew_head_helmet.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5f);
            this.crew_body = new ModelRenderer(this, 16, 16);
            this.crew_body.setTextureSize(64, 64);
            this.crew_body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4);
            this.crew_left_hand = new ModelRenderer(this, 32, 48);
            this.crew_left_hand.setTextureSize(64, 64);
            this.crew_left_hand.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4);
            this.crew_left_hand.setRotationPoint(5.0F, 2.5F, 0.0F);
            this.crew_right_hand = new ModelRenderer(this, 40, 16);
            this.crew_right_hand.setTextureSize(64, 64);
            this.crew_right_hand.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4);
            this.crew_right_hand.setRotationPoint(-5.0F, 2.5F, 0.0F);
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
            this.canopy.render(scale);

            if (entityIn.getControllingPassenger() instanceof AbstractClientPlayer &&
                    Minecraft.getMinecraft().getRenderManager().options.thirdPersonView != 0) {
                GlStateManager.scale(0.25, 0.25, 0.25);
                GlStateManager.translate(0, -0.2, 0);
                Minecraft.getMinecraft().renderEngine.bindTexture(
                        ((AbstractClientPlayer) entityIn.getControllingPassenger()).getLocationSkin());
                this.crew_body.render(scale);
                this.crew_head.render(scale);
                this.crew_left_hand.render(scale);
                this.crew_right_hand.render(scale);
                Minecraft.getMinecraft().renderEngine.bindTexture(LOCATION);
                this.crew_head_helmet.render(scale);
                GlStateManager.translate(0, 0.2, 0);
                GlStateManager.scale(4, 4, 4);
            }
            GlStateManager.popMatrix();
        }

        public void crewRenderer(Entity entity, float scale) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(-1, -1, -1);
            GlStateManager.scale(1, 1, 1);
            GlStateManager.popMatrix();
        }

    }
}
