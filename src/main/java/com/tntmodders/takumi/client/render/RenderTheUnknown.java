package com.tntmodders.takumi.client.render;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.entity.EntityUnknownLay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelIllager;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySpellcasterIllager;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class RenderTheUnknown extends RenderLiving<EntityMob> {
    private static final ResourceLocation LOCATION = new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/theunknown.png");

    public RenderTheUnknown(RenderManager p_i47207_1_) {
        super(p_i47207_1_, new ModelIllager(0.0F, 0.0F, 64, 64), 0.5F);
        this.addLayer(new LayerHeldItem(this) {
            @Override
            public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
                if (((EntitySpellcasterIllager) entitylivingbaseIn).isSpellcasting()) {
                    super.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
                }
            }

            @Override
            protected void translateToHand(EnumHandSide p_191361_1_) {
                ((ModelIllager) this.livingEntityRenderer.getMainModel()).getArm(p_191361_1_).postRender(0.0625F);
            }
        });
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation getEntityTexture(EntityMob entity) {
        return LOCATION;
    }

    /**
     * Allows the render to do state modifications necessary before the model is rendered.
     */
    @Override
    protected void preRenderCallback(EntityMob entitylivingbaseIn, float partialTickTime) {
        GlStateManager.scale(0.9375F, 0.9375F, 0.9375F);
    }

    @Override
    public void doRender(EntityMob entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        float f = entity.ticksExisted + partialTicks;
        GlStateManager.translate(f * 0.01F, f * 0.01F, 0.0F);
        GlStateManager.scale(2, 2, 2);
        GlStateManager.matrixMode(5888);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
    }

    public static class RenderUnknownLay<T extends EntityUnknownLay> extends Render<T> {
        private final ModelUnknownLay modelBase = new ModelUnknownLay();

        public RenderUnknownLay(RenderManager renderManager) {
            super(renderManager);
        }

        @Nullable
        @Override
        protected ResourceLocation getEntityTexture(T entity) {
            return LOCATION;
        }

        @Override
        public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
            GlStateManager.pushMatrix();
            this.bindTexture(LOCATION);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            float f = entity.ticksExisted + partialTicks;
            GlStateManager.translate(f * 0.01F, f * 0.01F, 0.0F);
            GlStateManager.scale(2, 2, 2);
            GlStateManager.matrixMode(5888);
            if (entity instanceof EntityUnknownLay.EntityUnknownLayEx) {
                float f1 = f < 30 ? 10 : 40 - f;
                GlStateManager.translate(x - 2.5, y, z - 2.5);
                GlStateManager.scale(f1 * 5 / 10, f1 * 5 / 10, f1 * 5 / 10);
            } else {
                float f1 = f > 30 ? 30 : f;
                GlStateManager.scale(f1 / 30, f1 / 30, f1 / 30);
                GlStateManager.translate(x - 0.5, y, z - 0.5);
                GlStateManager.scale(f1 / 30, f1 / 30, f1 / 30);
            }
            Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
            this.modelBase.render();
            Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.popMatrix();
            if (entity.ticksExisted > 30) {
                GlStateManager.pushMatrix();
                this.bindTexture(LOCATION);
                GlStateManager.alphaFunc(516, 0.1F);
                GlStateManager.disableFog();
                TileEntityBeaconRenderer.renderBeamSegment(x - 0.5, y, z - 0.5, partialTicks, 1, entity.world.getTotalWorldTime(), 0, entity.ticksExisted * 2 - 60, new float[]{0f, 0f, 0f});
                GlStateManager.enableFog();
                GlStateManager.popMatrix();
            }
        }

        private class ModelUnknownLay extends ModelBase {

            private final ModelRenderer box;

            public ModelUnknownLay() {
                this.box = new ModelRenderer(this, 0, 0);
                this.box.setTextureSize(16, 16);
                this.box.addBox(0, 0, 0, 16, 16, 16);
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
}
