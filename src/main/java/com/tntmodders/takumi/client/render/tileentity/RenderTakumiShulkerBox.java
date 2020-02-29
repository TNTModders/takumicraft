package com.tntmodders.takumi.client.render.tileentity;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelShulker;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityShulkerBoxRenderer;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderTakumiShulkerBox extends TileEntityShulkerBoxRenderer {
    private final ModelShulker model;
    private static final ResourceLocation TEXTURE = new ResourceLocation(TakumiCraftCore.MODID, "textures/blocks/creepershulkerbox.png");
    public static RenderTakumiShulkerBox instance;

    public RenderTakumiShulkerBox(ModelShulker modelIn) {
        super(modelIn);
        this.model = modelIn;
        instance = this;
    }

    public void renderAsItem() {
        GL11.glPushMatrix();
        GL11.glTranslatef(0.5F, 1.5F, 0.5F);
        GL11.glScalef(1, -1, -1);
        GL11.glRotatef(35, 0, 1, 0);
        this.render(new TileEntityShulkerBox(), 0, 0, 0, 0, 0, 0);
        GL11.glPopMatrix();
    }

    @Override
    public void render(TileEntityShulkerBox te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        EnumFacing enumfacing = EnumFacing.UP;

        if (te.hasWorld()) {
            IBlockState iblockstate = this.getWorld().getBlockState(te.getPos());

            if (iblockstate.getBlock() instanceof BlockShulkerBox) {
                enumfacing = iblockstate.getValue(BlockShulkerBox.FACING);
            }
        }

        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        GlStateManager.disableCull();

        if (destroyStage >= 0) {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 4.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        } else {
            this.bindTexture(TEXTURE);
        }

        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();

        if (destroyStage < 0) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
        }

        GlStateManager.translate((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        GlStateManager.scale(1.0F, -1.0F, -1.0F);
        GlStateManager.translate(0.0F, 1.0F, 0.0F);
        float f = 0.9995F;
        GlStateManager.scale(0.9995F, 0.9995F, 0.9995F);
        GlStateManager.translate(0.0F, -1.0F, 0.0F);

        switch (enumfacing) {
            case DOWN:
                GlStateManager.translate(0.0F, 2.0F, 0.0F);
                GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
            case UP:
            default:
                break;
            case NORTH:
                GlStateManager.translate(0.0F, 1.0F, 1.0F);
                GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
                break;
            case SOUTH:
                GlStateManager.translate(0.0F, 1.0F, -1.0F);
                GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                break;
            case WEST:
                GlStateManager.translate(-1.0F, 1.0F, 0.0F);
                GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
                break;
            case EAST:
                GlStateManager.translate(1.0F, 1.0F, 0.0F);
                GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
        }

        this.model.base.render(0.0625F);
        GlStateManager.translate(0.0F, -te.getProgress(partialTicks) * 0.5F, 0.0F);
        GlStateManager.rotate(270.0F * te.getProgress(partialTicks), 0.0F, 1.0F, 0.0F);
        this.model.lid.render(0.0625F);
        GlStateManager.enableCull();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }
}
