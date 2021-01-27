package com.tntmodders.takumi.client.render.tileentity;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.client.model.ModelBoard;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.tileentity.TileEntityTTTESR;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

@SideOnly(Side.CLIENT)
public class RenderTTTESR<T extends TileEntityTTTESR> extends TileEntitySpecialRenderer<T> {

    static final ResourceLocation LOCATION =
            new ResourceLocation(TakumiCraftCore.MODID, "textures/blocks/darkboard.png");
    private static final ResourceLocation TEXTURE_ARMOR =
            new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/dragon_armor.png");
    private final ArrayList<ModelTakumiBlock> modelBarriar = new ArrayList<>();
    ModelBoard modelBoard = new ModelBoard();

    public RenderTTTESR() {
        for (int i = 0; i < 64; i++) {
            modelBarriar.add(new ModelTakumiBlock(i));
        }
    }

    @Override
    public void render(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (te.getBlockType() == TakumiBlockCore.TT_BARRIAR) {
            GlStateManager.pushMatrix();
            GlStateManager.depthMask(true);
            this.bindTexture(TEXTURE_ARMOR);
            GlStateManager.matrixMode(GL11.GL_TEXTURE);
            GlStateManager.loadIdentity();
            float f = Minecraft.getMinecraft().world.getWorldTime();
            GlStateManager.translate(f * 0.01F, f * 0.01F, 0.0F);
            GlStateManager.matrixMode(GL11.GL_MODELVIEW);
            GlStateManager.enableBlend();
            float f4 = 0.5F;
            GlStateManager.color(f4, f4, f4, 1.0F);
            GlStateManager.enableLighting();
            GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE);
            GlStateManager.matrixMode(GL11.GL_TEXTURE);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(GL11.GL_MODELVIEW);

            GlStateManager.translate((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
            Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE_ARMOR);
            modelBarriar.get(Minecraft.getMinecraft().player.ticksExisted % 64).render();
            GlStateManager.disableLighting();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        } else if (te.getBlockType() == TakumiBlockCore.TT_CURSE) {
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
            this.bindTexture(LOCATION);
            this.modelBoard.render();
            GlStateManager.popMatrix();
        }
    }

    private class ModelTakumiBlock extends ModelBase {

        private final ModelRenderer box;

        public ModelTakumiBlock(int i) {
            this.box = new ModelRenderer(this, i, i);
            this.box.addBox(-8, 7, -8, 16, 1, 16);
        }

        public void render() {
            this.box.render(0.0625F);
        }

        @Override
        public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
                           float headPitch, float scale) {
        }
    }
}
