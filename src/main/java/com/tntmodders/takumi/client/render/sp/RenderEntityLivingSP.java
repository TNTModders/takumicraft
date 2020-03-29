package com.tntmodders.takumi.client.render.sp;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.entity.mobs.EntityIllusionerCreeper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderEntityLivingSP<T extends EntityLivingBase> extends RenderLivingBase<T> {
    public RenderEntityLivingSP(RenderManager renderManagerIn) {
        this(renderManagerIn, new ModelCreeper());
    }

    public RenderEntityLivingSP(RenderManager renderManagerIn, ModelBase model) {
        super(renderManagerIn, model, 0.5F);
        this.addLayer(new LayerTakumiChargeSP(this));
    }


    /**
     * Gets an RGBA int color multiplier to apply.
     */
    @Override
    protected int getColorMultiplier(T entitylivingbaseIn, float lightBrightness, float partialTickTime) {
        if (entitylivingbaseIn instanceof EntityCreeper) {
            float f = ((EntityCreeper) entitylivingbaseIn).getCreeperFlashIntensity(partialTickTime);

            if ((int) (f * 10.0F) % 2 == 0) {
                return 0;
            }
            int i = (int) (f * 0.2F * 255.0F);
            i = MathHelper.clamp(i, 0, 255);
            return i << 24 | 822083583;
        }
        return super.getColorMultiplier(entitylivingbaseIn, lightBrightness, partialTickTime);
    }

    /**
     * Allows the render to do state modifications necessary before the model is rendered.
     */
    @Override
    protected void preRenderCallback(T entitylivingbaseIn, float partialTickTime) {
        if (entitylivingbaseIn instanceof EntityCreeper) {
            float f = ((EntityCreeper) entitylivingbaseIn).getCreeperFlashIntensity(partialTickTime);
            float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
            f = MathHelper.clamp(f, 0.0F, 1.0F);
            f = f * f;
            f = f * f;
            float f2 = (1.0F + f * 0.4F) * f1;
            float f3 = (1.0F + f * 0.1F) / f1;
            GlStateManager.scale(f2, f3, f2);
        }
    }

    @Override
    protected boolean canRenderName(T entity) {
        return false;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        return new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/creeper.png");
    }

    public class LayerTakumiChargeSP implements LayerRenderer<EntityLivingBase> {

        private final ResourceLocation texture =
                new ResourceLocation("textures/entity/creeper/creeper_armor.png");
        private final RenderLivingBase creeperRenderer;
        private final ModelBase creeperModel;

        public LayerTakumiChargeSP(RenderLivingBase creeperRendererIn) {
            this.creeperRenderer = creeperRendererIn;
            this.creeperModel = new ModelCreeper(2.0f);
        }

        @Override
        public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount,
                                  float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            if (entitylivingbaseIn instanceof EntityCreeper) {
                if (((EntityCreeper) entitylivingbaseIn).getPowered() &&
                        (!entitylivingbaseIn.isInvisible() || entitylivingbaseIn instanceof EntityIllusionerCreeper)) {
                    GlStateManager.pushMatrix();
                    boolean flag = entitylivingbaseIn.isInvisible();
                    GlStateManager.depthMask(!flag);
                    this.creeperRenderer.bindTexture(texture);
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
                    this.creeperModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch,
                            scale);
                    Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
                    GlStateManager.matrixMode(5890);
                    GlStateManager.loadIdentity();
                    GlStateManager.matrixMode(5888);
                    GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
                            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                    GlStateManager.enableLighting();
                    GlStateManager.disableBlend();
                    GlStateManager.depthMask(!flag);
                    GlStateManager.popMatrix();
                }
            }
        }

        @Override
        public boolean shouldCombineTextures() {
            return false;
        }
    }
}
