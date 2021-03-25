package com.tntmodders.takumi.client.render;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.client.render.layer.LayerTakumiCharge;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.mobs.EntityVergerCreeper;
import com.tntmodders.takumi.entity.mobs.EntityYukariCreeper;
import com.tntmodders.takumi.entity.mobs.boss.EntityKingCreeper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import java.util.Calendar;

public class RenderVergerCreeper<T extends EntityVergerCreeper> extends RenderLiving<T> {

    public RenderVergerCreeper(RenderManager renderManagerIn) {
        this(renderManagerIn, new ModelCreeper());
    }

    public RenderVergerCreeper(RenderManager renderManagerIn, ModelBase model) {
        super(renderManagerIn, model, 0.5F);
        this.addLayer(new LayerTakumiCharge(this));
        this.addLayer(new LayerSeasonCreepers<>(this));
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

    @Override
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (!entity.isBook) {
            GlStateManager.pushMatrix();
            GlStateManager.matrixMode(5888);
            GlStateManager.enableBlend();
            GlStateManager.color(0.5F, 0.5F, 0.5F, 0.4f);
            GlStateManager.disableLighting();
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        if (!entity.isBook) {
            GlStateManager.enableLighting();
            GlStateManager.color(1, 1, 1, 1);
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        return new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/" + entity.getRegisterName() + ".png");
    }

    private class LayerSeasonCreepers<E extends EntityTakumiAbstractCreeper> implements LayerRenderer<E> {

        final RenderVergerCreeper renderTakumiCreeper;
        final ResourceLocation location_newyear =
                new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/seasons/newyear.png");
        final ResourceLocation location_newyear_yukari =
                new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/seasons/newyear_yukari.png");
        final ResourceLocation location_newyear_king =
                new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/seasons/newyear_king.png");
        final ResourceLocation location_xmas =
                new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/seasons/xmas_yukari.png");


        public LayerSeasonCreepers(RenderVergerCreeper creeper) {
            this.renderTakumiCreeper = creeper;
        }

        @Override
        public void doRenderLayer(E entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks,
                                  float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            Calendar calendar = entitylivingbaseIn.world.getCurrentDate();
            int month = calendar.get(Calendar.MONTH) + 1;
            int date = calendar.get(Calendar.DATE);
            if (month == 12 && (date == 24 || date == 25) && entitylivingbaseIn instanceof EntityYukariCreeper) {
                this.renderTakumiCreeper.bindTexture(location_xmas);
                Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
                mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch,
                        scale);
                Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
            } else if (month == 1 && date < 8) {
                this.renderTakumiCreeper.bindTexture(
                        entitylivingbaseIn instanceof EntityYukariCreeper ? location_newyear_yukari :
                                entitylivingbaseIn instanceof EntityKingCreeper ? location_newyear_king :
                                        location_newyear);
                Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
                mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch,
                        scale);
                Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
            }
        }

        @Override
        public boolean shouldCombineTextures() {
            return false;
        }
    }
}
