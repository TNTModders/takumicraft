package com.tntmodders.takumi.client.render.tileentity;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.client.model.ModelBoard;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.tileentity.TileEntityDarkBoard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderDarkBoard<T extends TileEntityDarkBoard> extends TileEntitySpecialRenderer<T> {
    static final ResourceLocation LOCATION =
            new ResourceLocation(TakumiCraftCore.MODID, "textures/blocks/darkboard.png");
    static final ResourceLocation LOCATION_ON =
            new ResourceLocation(TakumiCraftCore.MODID, "textures/blocks/darkboard_on.png");
    final ModelBoard modelBase = new ModelBoard();

    @Override
    public void render(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        float tick = Minecraft.getMinecraft().player.ticksExisted / 2;
        GlStateManager.pushMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.disableCull();
        GlStateManager.translate(x + 0.5, y + 1, z + 0.5);
        GlStateManager.rotate(180, 1, 0, 0);
        GlStateManager.rotate(tick, 0, 1, 0);
        GlStateManager.translate(-0.375, 0.125 + Math.sin(tick / Math.PI) * 0.1, -0.375);
        GlStateManager.scale(0.2, 0.2, 0.2);
        GlStateManager.enableAlpha();
        this.bindTexture(te.getBlockType() == TakumiBlockCore.DARKBOARD_ON ? LOCATION_ON : LOCATION);
        this.modelBase.render();
        GlStateManager.popMatrix();
    }
}
