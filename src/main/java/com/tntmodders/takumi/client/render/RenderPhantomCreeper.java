package com.tntmodders.takumi.client.render;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.client.render.layer.LayerTakumiCharge;
import com.tntmodders.takumi.entity.mobs.EntityPhantomCreeper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderPhantomCreeper<T extends EntityPhantomCreeper> extends RenderLiving<T> implements ITakumiRender {
    public RenderPhantomCreeper(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelPhantom(), 0.5f);
        this.addLayer(new LayerPhantomCreeper(this));
        this.addLayer(new LayerTakumiCharge(this));
    }

    /**
     * Gets an RGBA int color multiplier to apply.
     */
    @Override
    protected int getColorMultiplier(T entitylivingbaseIn, float lightBrightness, float partialTickTime) {
        float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);

        if ((int) (f * 10.0F) % 2 == 0) {
            return 0;
        } else {
            int i = (int) (f * 0.2F * 255.0F);
            i = MathHelper.clamp(i, 0, 255);
            return i << 24 | 822083583;
        }
    }

    @Override
    protected void preRenderCallback(T entitylivingbaseIn, float partialTickTime) {
        float f = 1.0F + 0.15F * 2;
        GlStateManager.scale(f, f, f);
        GlStateManager.translate(0.0F, 1.3125F, 0.1875F);
    }

    @Override
    protected void applyRotations(T entityLiving, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks);
        GlStateManager.rotate(entityLiving.rotationPitch, 1.0F, 0.0F, 0.0F);
    }

    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        return new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/" + entity.getRegisterName() + ".png");
    }

    @Override
    public ModelBase getPoweredModel() {
        return new ModelPhantom();
    }

    public static class ModelPhantom extends ModelBase {
        private final ModelRenderer field_203070_a;
        private final ModelRenderer field_203071_b;
        private final ModelRenderer field_203072_c;
        private final ModelRenderer field_203073_d;
        private final ModelRenderer field_203074_e;
        private final ModelRenderer field_203075_f;
        private final ModelRenderer field_204233_g;
        private final ModelRenderer field_204234_h;

        public ModelPhantom() {
            this.textureWidth = 64;
            this.textureHeight = 64;
            this.field_203070_a = new ModelRenderer(this, 0, 8);
            this.field_203070_a.addBox(-3.0F, -2.0F, -8.0F, 5, 3, 9);
            this.field_204233_g = new ModelRenderer(this, 3, 20);
            this.field_204233_g.addBox(-2.0F, 0.0F, 0.0F, 3, 2, 6);
            this.field_204233_g.setRotationPoint(0.0F, -2.0F, 1.0F);
            this.field_203070_a.addChild(this.field_204233_g);
            this.field_204234_h = new ModelRenderer(this, 4, 29);
            this.field_204234_h.addBox(-1.0F, 0.0F, 0.0F, 1, 1, 6);
            this.field_204234_h.setRotationPoint(0.0F, 0.5F, 6.0F);
            this.field_204233_g.addChild(this.field_204234_h);
            this.field_203071_b = new ModelRenderer(this, 23, 12);
            this.field_203071_b.addBox(0.0F, 0.0F, 0.0F, 6, 2, 9);
            this.field_203071_b.setRotationPoint(2.0F, -2.0F, -8.0F);
            this.field_203072_c = new ModelRenderer(this, 16, 24);
            this.field_203072_c.addBox(0.0F, 0.0F, 0.0F, 13, 1, 9);
            this.field_203072_c.setRotationPoint(6.0F, 0.0F, 0.0F);
            this.field_203071_b.addChild(this.field_203072_c);
            this.field_203073_d = new ModelRenderer(this, 23, 12);
            this.field_203073_d.mirror = true;
            this.field_203073_d.addBox(-6.0F, 0.0F, 0.0F, 6, 2, 9);
            this.field_203073_d.setRotationPoint(-3.0F, -2.0F, -8.0F);
            this.field_203074_e = new ModelRenderer(this, 16, 24);
            this.field_203074_e.mirror = true;
            this.field_203074_e.addBox(-13.0F, 0.0F, 0.0F, 13, 1, 9);
            this.field_203074_e.setRotationPoint(-6.0F, 0.0F, 0.0F);
            this.field_203073_d.addChild(this.field_203074_e);
            this.field_203071_b.rotateAngleZ = 0.1F;
            this.field_203072_c.rotateAngleZ = 0.1F;
            this.field_203073_d.rotateAngleZ = -0.1F;
            this.field_203074_e.rotateAngleZ = -0.1F;
            this.field_203070_a.rotateAngleX = -0.1F;
            this.field_203075_f = new ModelRenderer(this, 0, 0);
            this.field_203075_f.addBox(-4.0F, -2.0F, -5.0F, 7, 3, 5);
            this.field_203075_f.setRotationPoint(0.0F, 1.0F, -7.0F);
            this.field_203075_f.rotateAngleX = 0.2F;
            this.field_203070_a.addChild(this.field_203075_f);
            this.field_203070_a.addChild(this.field_203071_b);
            this.field_203070_a.addChild(this.field_203073_d);
        }

        @Override
        public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
            this.field_203070_a.render(scale);
        }

        @Override
        public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
            float f = ((float) (entityIn.getEntityId() * 3) + ageInTicks) * 0.13F;
            float f1 = 16.0F;
            this.field_203071_b.rotateAngleZ = MathHelper.cos(f) * 16.0F * ((float) Math.PI / 180F);
            this.field_203072_c.rotateAngleZ = MathHelper.cos(f) * 16.0F * ((float) Math.PI / 180F);
            this.field_203073_d.rotateAngleZ = -this.field_203071_b.rotateAngleZ;
            this.field_203074_e.rotateAngleZ = -this.field_203072_c.rotateAngleZ;
            this.field_204233_g.rotateAngleX = -(5.0F + MathHelper.cos(f * 2.0F) * 5.0F) * ((float) Math.PI / 180F);
            this.field_204234_h.rotateAngleX = -(5.0F + MathHelper.cos(f * 2.0F) * 5.0F) * ((float) Math.PI / 180F);
        }
    }

    public static class LayerPhantomCreeper implements LayerRenderer<EntityPhantomCreeper> {

        private static final ResourceLocation field_204248_a = new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/phantomcreeper_eyes.png");
        private final RenderLiving creeperRenderer;

        public LayerPhantomCreeper(RenderLiving creeperRendererIn) {
            this.creeperRenderer = creeperRendererIn;
        }

        @Override
        public void doRenderLayer(EntityPhantomCreeper entitylivingbaseIn, float limbSwing, float limbSwingAmount,
                                  float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            this.creeperRenderer.bindTexture(field_204248_a);
            GlStateManager.enableBlend();
            GlStateManager.disableAlpha();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(!entitylivingbaseIn.isInvisible());
            int i = 61680;
            int j = 61680;
            int k = 0;
            GlStateManager.enableLighting();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            EntityRenderer gamerenderer = Minecraft.getMinecraft().entityRenderer;
            gamerenderer.setupFogColor(true);
            this.creeperRenderer.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            gamerenderer.setupFogColor(false);
            this.creeperRenderer.setLightmap(entitylivingbaseIn);
            GlStateManager.depthMask(true);
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
        }

        @Override
        public boolean shouldCombineTextures() {
            return false;
        }
    }
}
