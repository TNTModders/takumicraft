package com.tntmodders.takumi.client.render;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.mobs.EntityIllusionerCreeper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderTransCreeper_2<T extends EntityTakumiAbstractCreeper> extends RenderLiving<T> {

    public RenderTransCreeper_2(RenderManager renderManagerIn) {
        this(renderManagerIn, new ModelCreeper());
    }

    public RenderTransCreeper_2(RenderManager renderManagerIn, ModelBase model) {
        super(renderManagerIn, model, 0.5F);
        this.addLayer(new LayerTransCharge(this));
        this.addLayer(new LayerCrazy(this));
    }

    /**
     * Gets an RGBA int color multiplier to apply.
     */
    @Override
    protected int getColorMultiplier(T entitylivingbaseIn, float lightBrightness, float partialTickTime) {
        float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);

        if ((int) (f * 10.0F) % 2 == 0) {
            return 0;
        }
        int i = (int) (f * 0.2F * 255.0F);
        i = MathHelper.clamp(i, 0, 255);
        return i << 24 | 822083583;
    }

    /**
     * Allows the render to do state modifications necessary before the model is rendered.
     */
    @Override
    protected void preRenderCallback(T entitylivingbaseIn, float partialTickTime) {
        //GlStateManager.rotate(180, 1, 0, 0);
        //GlStateManager.translate(0, 1.65, 0);
        if (entitylivingbaseIn.getSizeAmp() != 1) {
            double d = entitylivingbaseIn.getSizeAmp();
            GlStateManager.scale(d, d, d);
        }
        float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
        float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        f = f * f;
        f = f * f;
        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        GlStateManager.scale(f2, f3, f2);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    public ResourceLocation getEntityTexture(T entity) {
        return new ResourceLocation("textures/entity/creeper/creeper.png");
    }

    private static class LayerCrazy implements LayerRenderer<EntityTakumiAbstractCreeper> {

        private final ModelBase creeperModel;
        private final RenderTransCreeper_2 creeperRenderer;

        public LayerCrazy(RenderTransCreeper_2 creeperRendererIn) {
            this.creeperRenderer = creeperRendererIn;
            this.creeperModel = new ModelCreeper();
        }

        @Override
        public void doRenderLayer(EntityTakumiAbstractCreeper entitylivingbaseIn, float limbSwing,
                float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch,
                float scale) {
            for (int i = 0; i < 12; i++) {
                int degree = i * 30;
                GlStateManager.pushMatrix();
                GlStateManager.scale(0.75, 0.75, 0.75);
                GlStateManager.translate(0, 0.75, 0);
                GlStateManager.rotate(entitylivingbaseIn.ticksExisted * 100 + degree, 1, 0, 0);
                this.creeperRenderer.bindTexture(this.creeperRenderer.getEntityTexture(entitylivingbaseIn));
                this.creeperModel.setModelAttributes(this.creeperRenderer.getMainModel());
                Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
                this.creeperModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw,
                        headPitch, scale);
                GlStateManager.popMatrix();
            }
        }

        @Override
        public boolean shouldCombineTextures() {
            return false;
        }
    }

    private static class LayerTransCharge implements LayerRenderer<EntityTakumiAbstractCreeper> {

        private static final ResourceLocation LIGHTNING_TEXTURE =
                new ResourceLocation("textures/entity/creeper/creeper_armor.png");
        private final RenderLiving creeperRenderer;
        private final ModelBase creeperModel;

        public LayerTransCharge(RenderLiving creeperRendererIn) {
            this.creeperRenderer = creeperRendererIn;
            if (creeperRenderer.getMainModel() instanceof ModelCreeper) {
                this.creeperModel = new ModelCreeper(2.0f);
            } else if (creeperRenderer instanceof ITakumiRender) {
                this.creeperModel = ((ITakumiRender) creeperRenderer).getPoweredModel();
            } else {
                this.creeperModel = creeperRenderer.getMainModel();
            }
        }

        @Override
        public void doRenderLayer(EntityTakumiAbstractCreeper entitylivingbaseIn, float limbSwing,
                float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch,
                float scale) {
            if (/*entitylivingbaseIn.getPowered() &&*/
                    (!entitylivingbaseIn.isInvisible() || entitylivingbaseIn instanceof EntityIllusionerCreeper)) {
                GlStateManager.pushMatrix();
                boolean flag = entitylivingbaseIn.isInvisible();
                GlStateManager.depthMask(!flag);
                this.creeperRenderer.bindTexture(entitylivingbaseIn.getArmor());
                GlStateManager.matrixMode(5890);
                GlStateManager.loadIdentity();
                float f = entitylivingbaseIn.ticksExisted + partialTicks;
                GlStateManager.translate(f * 0.01F, f * 0.01F, 0.0F);
                GlStateManager.matrixMode(5888);
                GlStateManager.enableBlend();
                GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
                GlStateManager.disableLighting();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
                this.creeperModel.setModelAttributes(this.creeperRenderer.getMainModel());
                Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
                this.creeperModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw,
                        headPitch, scale);
                Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
                GlStateManager.matrixMode(5890);
                GlStateManager.loadIdentity();
                GlStateManager.matrixMode(5888);
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
                        GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                GlStateManager.enableLighting();
                GlStateManager.disableBlend();
                GlStateManager.depthMask(flag);
                GlStateManager.popMatrix();
            }
        }

        @Override
        public boolean shouldCombineTextures() {
            return false;
        }
    }
}
