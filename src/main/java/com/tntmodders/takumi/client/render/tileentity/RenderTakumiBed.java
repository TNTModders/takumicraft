package com.tntmodders.takumi.client.render.tileentity;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.tileentity.TileEntityTakumiBed;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBed;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityBedRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class RenderTakumiBed extends TileEntityBedRenderer {
    private ModelBed model = new ModelBed();
    private ModelTakumiBed overlayModel = new ModelTakumiBed(16, 16);
    private ModelTakumiBed poweredModel = new ModelTakumiBed(64, 32);
    private int version;
    private static final ResourceLocation BASE_TEXTURE = new ResourceLocation(TakumiCraftCore.MODID, "textures/blocks/creeperbed_model.png");
    private static final ResourceLocation LIGHTNING_TEXTURE =
            new ResourceLocation("textures/entity/creeper/creeper_armor.png");

    public RenderTakumiBed() {
        this.version = this.model.getModelVersion();
    }

    @Override
    public void render(TileEntityBed te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (this.version != this.model.getModelVersion()) {
            this.model = new ModelBed();
            this.version = this.model.getModelVersion();
        }

        boolean flag = te.getWorld() != null;
        boolean flag1 = !flag || te.isHeadPiece();
        int i = flag ? te.getBlockMetadata() & 3 : 0;

        if (destroyStage >= 0) {
            GlStateManager.pushMatrix();
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 4.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
            GlStateManager.popMatrix();
        } else {
            ResourceLocation resourcelocation = BASE_TEXTURE;

            if (resourcelocation != null) {
                this.bindTexture(resourcelocation);
            }
        }

        if (flag) {
            GlStateManager.pushMatrix();
            this.renderPiece(((TileEntityTakumiBed) te), flag1, x, y, z, i, alpha, destroyStage);
            GlStateManager.popMatrix();
        } else {
            GlStateManager.pushMatrix();
            this.renderPiece((TileEntityTakumiBed) te, true, x, y, z, i, alpha, destroyStage);
            this.renderPiece((TileEntityTakumiBed) te, false, x, y, z - 1.0D, i, alpha, destroyStage);
            GlStateManager.popMatrix();
        }

        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }

    private void renderPiece(TileEntityTakumiBed te, boolean p_193847_1_, double x, double y, double z, int p_193847_8_, float alpha, int stage) {
        this.model.preparePiece(p_193847_1_);
        GlStateManager.pushMatrix();
        float f = 0.0F;
        float f1 = 0.0F;
        float f2 = 0.0F;

        if (p_193847_8_ == EnumFacing.NORTH.getHorizontalIndex()) {
            f = 0.0F;
        } else if (p_193847_8_ == EnumFacing.SOUTH.getHorizontalIndex()) {
            f = 180.0F;
            f1 = 1.0F;
            f2 = 1.0F;
        } else if (p_193847_8_ == EnumFacing.WEST.getHorizontalIndex()) {
            f = -90.0F;
            f2 = 1.0F;
        } else if (p_193847_8_ == EnumFacing.EAST.getHorizontalIndex()) {
            f = 90.0F;
            f1 = 1.0F;
        }

        GlStateManager.translate((float) x + f1, (float) y + 0.5625F, (float) z + f2);
        GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(f, 0.0F, 0.0F, 1.0F);
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();
        this.model.render();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
        GlStateManager.popMatrix();

        if (stage < 0) {
            try {
                if (this.getOverlayTexture(te) != null) {
                    GlStateManager.pushMatrix();
                    this.bindTexture(this.getOverlayTexture(te));
                    GlStateManager.translate((float) x + f1, (float) y + 0.5625F, (float) z + f2);
                    GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                    GlStateManager.rotate(f, 0.0F, 0.0F, 1.0F);
                    GlStateManager.enableRescaleNormal();
                    this.overlayModel.preparePiece(p_193847_1_);
                    this.overlayModel.render(null, 0, 0, 0, 0, 0, 0);
                    GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
                    GlStateManager.popMatrix();
                } else {
                    GlStateManager.pushMatrix();
                    GlStateManager.depthMask(true);
                    this.bindTexture(LIGHTNING_TEXTURE);
                    GlStateManager.matrixMode(5890);
                    GlStateManager.loadIdentity();
                    float tick = Minecraft.getMinecraft().player.ticksExisted;
                    GlStateManager.translate(tick * 0.01F, tick * 0.01F, 0.0F);
                    GlStateManager.matrixMode(5888);
                    GlStateManager.enableBlend();
                    GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
                    GlStateManager.disableLighting();
                    GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
                    Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
                    GlStateManager.translate((float) x + f1, (float) y + 0.5625F, (float) z + f2);
                    GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                    GlStateManager.rotate(f, 0.0F, 0.0F, 1.0F);
                    this.poweredModel.preparePiece(p_193847_1_);
                    this.poweredModel.render(null, 0, 0, 0, 0, 0, 0);
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
            } catch (Exception ignored) {

            }
        }
    }

    private ResourceLocation getOverlayTexture(TileEntityTakumiBed te) {
        if (te.getTexture() != null) {
            try {
                Block block = Block.getBlockFromName(te.getTexture());
                IBlockState state = block.getStateFromMeta(te.getMeta());
                String path = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state).getIconName();
                if (!path.contains(":")) {
                    path = "minecraft:" + path;
                }
                ResourceLocation location = new ResourceLocation(path.split(":")[0], "textures/" + path.split(":")[1] + ".png");
                if (location.toString().contains("blocks")) {
                    return location;
                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
        } else {
            EnumFacing facing = te.getWorld().getBlockState(te.getPos()).getValue(BlockBed.FACING);
            if (te.getWorld().getBlockState(te.getPos()).getValue(BlockBed.PART) == BlockBed.EnumPartType.HEAD) {
                facing = facing.getOpposite();
            }
            if ((te.getWorld().getTileEntity(te.getPos().offset(facing))) instanceof TileEntityTakumiBed &&
                    ((TileEntityTakumiBed) te.getWorld().getTileEntity(te.getPos().offset(facing))).getTexture() != null) {
                return this.getOverlayTexture(((TileEntityTakumiBed) te.getWorld().getTileEntity(te.getPos().offset(facing))));
            }
        }
        return null;
    }

    public class ModelTakumiBed extends ModelBase {
        ModelRenderer legTop;
        ModelRenderer legLeft;
        ModelRenderer legRight;
        ModelRenderer legFront;
        ModelRenderer headTop;
        ModelRenderer headLeft;
        ModelRenderer headRight;

        public ModelTakumiBed(int width, int height) {
            this.textureHeight = height;
            this.textureWidth = width;
            this.legTop = new ModelRenderer(this, 0, 8);
            this.legTop.addBox(0, 0, -0.01f, 16, 16, 0, 0);
            this.legLeft = new ModelRenderer(this, -4, 4);
            this.legLeft.addBox(-0.01f, 0, 0, 0, 16, 4, 0);
            this.legRight = new ModelRenderer(this, 12, 4);
            this.legRight.addBox(16.01f, 0, 0, 0, 16, 4, 0);
            this.legFront = new ModelRenderer(this, 0, 8);
            this.legFront.addBox(0, 16, -0.01f, 16, 4, 0, 0);
            this.legFront.setRotationPoint(0, 16, -16);
            this.legFront.rotateAngleX = ((float) (0.5 * Math.PI));
            this.headTop = new ModelRenderer(this, 0, 0);
            this.headTop.addBox(0, 8, -0.01f, 16, 8, 0, 0);
            this.headLeft = new ModelRenderer(this, -4, -4);
            this.headLeft.addBox(-0.01f, 8, 0, 0, 8, 4, 0);
            this.headRight = new ModelRenderer(this, 12, -4);
            this.headRight.addBox(16.01f, 8, 0, 0, 8, 4, 0);
        }

        @Override
        public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            this.legTop.render(0.0625F);
            this.legLeft.render(0.0625F);
            this.legRight.render(0.0625F);
            this.legFront.render(0.0625F);
            this.headTop.render(0.0625F);
            this.headLeft.render(0.0625F);
            this.headRight.render(0.0625F);
        }

        public void preparePiece(boolean flg) {
            this.legTop.showModel = !flg;
            this.legLeft.showModel = !flg;
            this.legRight.showModel = !flg;
            this.legFront.showModel = !flg;
            this.headTop.showModel = flg;
            this.headLeft.showModel = flg;
            this.headRight.showModel = flg;
        }
    }
}
