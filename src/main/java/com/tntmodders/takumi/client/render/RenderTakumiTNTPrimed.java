package com.tntmodders.takumi.client.render;

import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.entity.item.EntityTakumiTNTPrimed;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderTakumiTNTPrimed <T extends EntityTakumiTNTPrimed> extends Render <T> {
    
    public RenderTakumiTNTPrimed(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }
    
    @Override
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher(); GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y + 0.5F, (float) z);
        
        if (entity.getFuse() - partialTicks + 1.0F < 10.0F) {
            float f = 1.0F - (entity.getFuse() - partialTicks + 1.0F) / 10.0F; f = MathHelper.clamp(f, 0.0F, 1.0F); f = f * f; f = f * f;
            float f1 = 1.0F + f * 0.3F; GlStateManager.scale(f1, f1, f1);
        }
    
        float f2 = (1.0F - (entity.getFuse() - partialTicks + 1.0F) / 100.0F) * 0.8F; this.bindEntityTexture(entity);
        GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F); GlStateManager.translate(-0.5F, -0.5F, 0.5F);
        blockrendererdispatcher.renderBlockBrightness(TakumiBlockCore.TAKUMI_TNT.getDefaultState(), entity.getBrightness());
        GlStateManager.translate(0.0F, 0.0F, 1.0F);
        
        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial(); GlStateManager.enableOutlineMode(this.getTeamColor(entity));
            blockrendererdispatcher.renderBlockBrightness(TakumiBlockCore.TAKUMI_TNT.getDefaultState(), 1.0F); GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        } else if (entity.getFuse() / 5 % 2 == 0) {
            GlStateManager.disableTexture2D(); GlStateManager.disableLighting(); GlStateManager.enableBlend();
            GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.DST_ALPHA); GlStateManager.color(1.0F, 1.0F, 1.0F, f2);
            GlStateManager.doPolygonOffset(-3.0F, -3.0F); GlStateManager.enablePolygonOffset();
            blockrendererdispatcher.renderBlockBrightness(TakumiBlockCore.TAKUMI_TNT.getDefaultState(), 1.0F);
            GlStateManager.doPolygonOffset(0.0F, 0.0F); GlStateManager.disablePolygonOffset(); GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableBlend(); GlStateManager.enableLighting(); GlStateManager.enableTexture2D();
        }
    
        GlStateManager.popMatrix(); super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}
