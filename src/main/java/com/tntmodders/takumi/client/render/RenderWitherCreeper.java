package com.tntmodders.takumi.client.render;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.client.render.layer.LayerTakumiCharge;
import com.tntmodders.takumi.entity.mobs.boss.EntityWitherCreeper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;

public class RenderWitherCreeper<T extends EntityWitherCreeper> extends RenderLiving<T> implements ITakumiRender {

    private static final ResourceLocation INVULNERABLE_WITHER_TEXTURES = new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/withercreeper_inv.png");
    private static final ResourceLocation WITHER_TEXTURES = new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/withercreeper.png");

    public RenderWitherCreeper(RenderManager rendermanagerIn) {
        super(rendermanagerIn, new ModelWitherCreeper(0), 1f);
        this.addLayer(new LayerWitherAura(this));
        this.addLayer(new LayerTakumiCharge(this));
    }

    @Override
    public ModelBase getPoweredModel() {
        return new ModelWitherCreeper(0.5f);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        int i = entity.getInvulTime();
        return i > 0 && (i > 80 || i / 5 % 2 != 1) ? INVULNERABLE_WITHER_TEXTURES : WITHER_TEXTURES;
    }

    @Override
    protected void preRenderCallback(T entitylivingbaseIn, float partialTickTime) {
        float f = 2.0F;
        int i = entitylivingbaseIn.getInvulTime();

        if (i > 0) {
            f -= ((float) i - partialTickTime) / 220.0F * 0.5F;
        }

        GlStateManager.scale(f, f, f);
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);
    }

    public static class ModelWitherCreeper extends ModelBase {
        private final ModelRenderer[] upperBodyParts;
        private final ModelRenderer[] heads;

        public ModelWitherCreeper(float p_i46302_1_) {
            this.textureWidth = 64;
            this.textureHeight = 64;
            this.upperBodyParts = new ModelRenderer[3];
            this.upperBodyParts[0] = new ModelRenderer(this, 0, 16);
            this.upperBodyParts[0].addBox(-10.0F, 3.9F, -0.5F, 20, 3, 3, p_i46302_1_);
            this.upperBodyParts[1] = (new ModelRenderer(this)).setTextureSize(this.textureWidth, this.textureHeight);
            this.upperBodyParts[1].setRotationPoint(-2.0F, 6.9F, -0.5F);
            this.upperBodyParts[1].setTextureOffset(0, 22).addBox(0.0F, 0.0F, 0.0F, 3, 10, 3, p_i46302_1_);
            this.upperBodyParts[1].setTextureOffset(24, 22).addBox(-4.0F, 1.5F, 0.5F, 11, 2, 2, p_i46302_1_);
            this.upperBodyParts[1].setTextureOffset(24, 22).addBox(-4.0F, 4.0F, 0.5F, 11, 2, 2, p_i46302_1_);
            this.upperBodyParts[1].setTextureOffset(24, 22).addBox(-4.0F, 6.5F, 0.5F, 11, 2, 2, p_i46302_1_);
            this.upperBodyParts[2] = new ModelRenderer(this, 12, 22);
            this.upperBodyParts[2].addBox(0.0F, 0.0F, 0.0F, 3, 6, 3, p_i46302_1_);
            this.heads = new ModelRenderer[3];
            this.heads[0] = new ModelRenderer(this, 0, 0);
            this.heads[0].addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8, p_i46302_1_);
            this.heads[1] = new ModelRenderer(this, 32, 0);
            this.heads[1].addBox(-4.0F, -4.0F, -4.0F, 6, 6, 6, p_i46302_1_);
            this.heads[1].rotationPointX = -8.0F;
            this.heads[1].rotationPointY = 4.0F;
            this.heads[2] = new ModelRenderer(this, 32, 0);
            this.heads[2].addBox(-4.0F, -4.0F, -4.0F, 6, 6, 6, p_i46302_1_);
            this.heads[2].rotationPointX = 10.0F;
            this.heads[2].rotationPointY = 4.0F;
        }

        /**
         * Sets the models various rotation angles then renders the model.
         */
        @Override
        public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);

            for (ModelRenderer modelrenderer : this.heads) {
                modelrenderer.render(scale);
            }

            for (ModelRenderer modelrenderer1 : this.upperBodyParts) {
                modelrenderer1.render(scale);
            }
        }

        /**
         * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
         * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
         * "far" arms and legs can swing at most.
         */
        @Override
        public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
            float f = MathHelper.cos(ageInTicks * 0.1F);
            this.upperBodyParts[1].rotateAngleX = (0.065F + 0.05F * f) * (float) Math.PI;
            this.upperBodyParts[2].setRotationPoint(-2.0F, 6.9F + MathHelper.cos(this.upperBodyParts[1].rotateAngleX) * 10.0F, -0.5F + MathHelper.sin(this.upperBodyParts[1].rotateAngleX) * 10.0F);
            this.upperBodyParts[2].rotateAngleX = (0.265F + 0.1F * f) * (float) Math.PI;
            this.heads[0].rotateAngleY = netHeadYaw * 0.017453292F;
            this.heads[0].rotateAngleX = headPitch * 0.017453292F;
        }

        /**
         * Used for easily adding entity-dependent animations. The second and third float params here are the same second
         * and third as in the setRotationAngles method.
         */
        @Override
        public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
            EntityWitherCreeper entitywither = (EntityWitherCreeper) entitylivingbaseIn;

            for (int i = 1; i < 3; ++i) {
                this.heads[i].rotateAngleY = (entitywither.getHeadYRotation(i - 1) - entitylivingbaseIn.renderYawOffset) * 0.017453292F;
                this.heads[i].rotateAngleX = entitywither.getHeadXRotation(i - 1) * 0.017453292F;
            }
        }
    }

    public static class LayerWitherAura implements LayerRenderer<EntityWitherCreeper> {
        private static final ResourceLocation WITHER_ARMOR = new ResourceLocation("textures/entity/wither/wither_armor.png");
        private final RenderWitherCreeper witherRenderer;
        private final ModelWitherCreeper witherModel = new ModelWitherCreeper(0.5F);

        public LayerWitherAura(RenderWitherCreeper witherRendererIn) {
            this.witherRenderer = witherRendererIn;
        }

        @Override
        public void doRenderLayer(EntityWitherCreeper entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            if (entitylivingbaseIn.isArmored()) {
                GlStateManager.depthMask(!entitylivingbaseIn.isInvisible());
                this.witherRenderer.bindTexture(WITHER_ARMOR);
                GlStateManager.matrixMode(5890);
                GlStateManager.loadIdentity();
                float f = (float) entitylivingbaseIn.ticksExisted + partialTicks;
                float f1 = MathHelper.cos(f * 0.02F) * 3.0F;
                float f2 = f * 0.01F;
                GlStateManager.translate(f1, f2, 0.0F);
                GlStateManager.matrixMode(5888);
                GlStateManager.enableBlend();
                float f3 = 0.5F;
                GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
                GlStateManager.disableLighting();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
                this.witherModel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
                this.witherModel.setModelAttributes(this.witherRenderer.getMainModel());
                Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
                this.witherModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
                GlStateManager.matrixMode(5890);
                GlStateManager.loadIdentity();
                GlStateManager.matrixMode(5888);
                GlStateManager.enableLighting();
                GlStateManager.disableBlend();
            }
        }

        @Override
        public boolean shouldCombineTextures() {
            return false;
        }
    }
}
