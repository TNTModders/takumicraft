package com.tntmodders.takumi.client.render.tileentity;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.tileentity.TileEntityTakumiForceField;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderTakumiForceField<T extends TileEntityTakumiForceField> extends TileEntitySpecialRenderer<T> {
    private static final ResourceLocation LOCATION = new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/big_creeper_armor.png");
    ModelCreeper creeper = new ModelCreeper();

    @Override
    public void render(T te, double dx, double dy, double dz, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(true);
        this.bindTexture(LOCATION);
        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        float f = te.getTicksExisted() + partialTicks;
        GlStateManager.translate(f * 0.01F, f * 0.01F, 0.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.enableBlend();
        GlStateManager.color(0.8F * ((float) Math.sin(Math.toRadians(f))), 0.8F * ((float) Math.sin(Math.toRadians(f + 120))), 0.8F * ((float) Math.sin(Math.toRadians(f - 120))), 1.0F);
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        GlStateManager.translate(dx + 0.5, dy + 1.55, dz + 0.5);
        GlStateManager.rotate(180, 1, 0, 0);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        this.creeper.render(null, 0, 0, 0, 0, 0, 0.065f);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();


    }
}
