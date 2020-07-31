package com.tntmodders.takumi.client.render;

import com.tntmodders.takumi.entity.item.EntityBigCreeperDummy;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Random;

public class RenderBigCreeperDummy<T extends EntityBigCreeperDummy> extends Render<T> {

    private static final ResourceLocation LIGHTNING_TEXTURE =
            new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    private static final ResourceLocation CREEPER_TEXTURES =
            new ResourceLocation("textures/entity/creeper/creeper.png");
    private final ModelBase model = new ModelCreeper();


    public RenderBigCreeperDummy(RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.shadowSize = 0F;
    }


    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        return LIGHTNING_TEXTURE;
    }

    @Override
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        this.bindTexture(CREEPER_TEXTURES);
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(180, 0, 0, 1);
        GlStateManager.scale(100, 100, 100);
        GlStateManager.translate(0, -1.3, 0);
        this.model.render(entity, 0, 0, 0, entity.ticksExisted, 0, 0.065f);
        GlStateManager.popMatrix();

        if (entity.getCharging()) {
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            //RenderHelper.disableStandardItemLighting();
            float f = ((float) entity.chargingTicks + partialTicks) / 200.0F;
            float f1 = 0.0F;

            if (f > 0.8F) {
                f1 = (f - 0.8F) / 0.2F;
            }

            Random random = new Random(432L);
            GlStateManager.disableTexture2D();
            GlStateManager.shadeModel(7425);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            GlStateManager.disableAlpha();
            GlStateManager.enableCull();
            GlStateManager.depthMask(false);
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            GlStateManager.scale(50, 50, 50);
            GlStateManager.translate(0, 1, 0);
            for (int i = 0; (float) i < (f + f * f) / 2.0F * 60.0F; ++i) {
                GlStateManager.rotate(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(random.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.rotate(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(random.nextFloat() * 360.0F + f * 90.0F, 0.0F, 0.0F, 1.0F);
                float f2 = random.nextFloat() * 20.0F + 5.0F + f1 * 10.0F;
                float f3 = random.nextFloat() * 2.0F + 1.0F + f1 * 2.0F;
                bufferbuilder.begin(6, DefaultVertexFormats.POSITION_COLOR);
                bufferbuilder.pos(0.0D, 0.0D, 0.0D).color(0, 255, 0, (int) (255.0F * (1.0F - f1 / 1.25f))).endVertex();
                bufferbuilder.pos(-0.866D * (double) f3, f2, -0.5F * f3).color(64, 255, 64, 0).endVertex();
                bufferbuilder.pos(0.866D * (double) f3, f2, -0.5F * f3).color(64, 255, 64, 0).endVertex();
                bufferbuilder.pos(0.0D, f2, 1.0F * f3).color(64, 255, 64, 0).endVertex();
                bufferbuilder.pos(-0.866D * (double) f3, f2, -0.5F * f3).color(64, 255, 64, 0).endVertex();
                tessellator.draw();
            }

            GlStateManager.popMatrix();
            GlStateManager.depthMask(true);
            GlStateManager.disableCull();
            GlStateManager.disableBlend();
            GlStateManager.shadeModel(7424);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableTexture2D();
            GlStateManager.enableAlpha();
            RenderHelper.enableStandardItemLighting();
        }
    }

    @Override
    public boolean shouldRender(T livingEntity, ICamera camera, double camX, double camY, double camZ) {
        return true;
    }
}
/*
import com.tntmodders.takumi.entity.item.EntityAttackBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderAttackBlock<T extends EntityAttackBlock> extends RenderLiving<T> {
    private static final ResourceLocation LIGHTNING_TEXTURE =
            new ResourceLocation("textures/entity/creeper/creeper_armor.png");

    public RenderAttackBlock(RenderManager rendermanagerIn) {
        super(rendermanagerIn, new ModelCreeper(), 0f);
    }

    @Override
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        boolean flag = entity.isInvisible();
        GlStateManager.depthMask(!flag);
        this.bindTexture(LIGHTNING_TEXTURE);
        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        float f = (float) entity.ticksExisted + partialTicks;
        GlStateManager.translate(f * 0.01F, f * 0.01F, 0.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.enableBlend();
        float f1 = 0.5F;
        GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        this.getMainModel().setModelAttributes(this.getMainModel());
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(180, 1, 0, 0);
        GlStateManager.translate(0, -1.5, 0);
        this.getMainModel().render(entity, entity.limbSwing, entity.limbSwingAmount, entity.ticksExisted,
                entity.rotationYaw, entity.rotationPitch, 0.0625f);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(flag);
        GlStateManager.popMatrix();
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        return LIGHTNING_TEXTURE;
    }
}*/
