package com.tntmodders.takumi.client.render;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.entity.item.EntityBigCreeperDummy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.nio.FloatBuffer;
import java.util.Random;

public class RenderBigCreeperDummy<T extends EntityBigCreeperDummy> extends Render<T> {
    private static final Random RANDOM = new Random(31100L);
    private static final FloatBuffer MODELVIEW = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer PROJECTION = GLAllocation.createDirectFloatBuffer(16);
    private static final ResourceLocation LIGHTNING_TEXTURE =
            new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    private static final ResourceLocation CREEPER_TEXTURES =
            new ResourceLocation("textures/entity/creeper/creeper.png");
    private static final ResourceLocation CREEPER_TEXTURES_DMG =
            new ResourceLocation(TakumiCraftCore.MODID,"textures/entity/creeper/bigcreeper_dmg.png");
    private static final ResourceLocation END_SKY_TEXTURE = new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/bigcreeper_overlay_1.png");
    private static final ResourceLocation END_PORTAL_TEXTURE = new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/bigcreeper_overlay_2.png");
    private final ModelBase model = new ModelCreeper();
    private final FloatBuffer buffer = GLAllocation.createDirectFloatBuffer(16);


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
        if (entity.isDamaged()) {
            GlStateManager.disableLighting();
            RANDOM.setSeed(31100L);
            GlStateManager.getFloat(2982, MODELVIEW);
            GlStateManager.getFloat(2983, PROJECTION);
            double d0 = x * x + y * y + z * z;
            int i = this.getPasses(d0);
            float f = 1;
            boolean flag = false;

            for (int j = 0; j < i; ++j) {
                GlStateManager.pushMatrix();
                float f1 = 2.0F / (float) (18 - j);

                if (j == 0) {
                    this.bindTexture(END_SKY_TEXTURE);
                    f1 = 0.15F;
                    GlStateManager.enableBlend();
                    GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                }

                if (j >= 1) {
                    this.bindTexture(END_PORTAL_TEXTURE);
                    flag = true;
                    Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
                }

                if (j == 1) {
                    GlStateManager.enableBlend();
                    GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
                }

                GlStateManager.texGen(GlStateManager.TexGen.S, 9216);
                GlStateManager.texGen(GlStateManager.TexGen.T, 9216);
                GlStateManager.texGen(GlStateManager.TexGen.R, 9216);
                GlStateManager.texGen(GlStateManager.TexGen.S, 9474, this.getBuffer(1.0F, 0.0F, 0.0F, 0.0F));
                GlStateManager.texGen(GlStateManager.TexGen.T, 9474, this.getBuffer(0.0F, 1.0F, 0.0F, 0.0F));
                GlStateManager.texGen(GlStateManager.TexGen.R, 9474, this.getBuffer(0.0F, 0.0F, 1.0F, 0.0F));
                GlStateManager.enableTexGenCoord(GlStateManager.TexGen.S);
                GlStateManager.enableTexGenCoord(GlStateManager.TexGen.T);
                GlStateManager.enableTexGenCoord(GlStateManager.TexGen.R);
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(5890);
                GlStateManager.pushMatrix();
                GlStateManager.loadIdentity();
                GlStateManager.translate(0.5F, 0.5F, 0.0F);
                GlStateManager.scale(0.5F, 0.5F, 1.0F);

                float f2 = (float) (j + 1);
                GlStateManager.translate(17.0F / f2, (2.0F + f2 / 1.5F) * ((float) Minecraft.getSystemTime() % 800000.0F / 800000.0F), 0.0F);
                GlStateManager.rotate((f2 * f2 * 4321.0F + f2 * 9.0F) * 2.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.scale(4.5F - f2 / 4.0F, 4.5F - f2 / 4.0F, 1.0F);

                GlStateManager.multMatrix(PROJECTION);
                GlStateManager.multMatrix(MODELVIEW);
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
                float f3 = (RANDOM.nextFloat() * 0.5F + 0.1F) * f1;
                float f4 = (RANDOM.nextFloat() * 0.5F + 0.4F) * f1;
                float f5 = (RANDOM.nextFloat() * 0.5F + 0.5F) * f1;


                bufferbuilder.pos(x - 1.0D, y, z + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.pos(x + 1.0D, y, z + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.pos(x + 1.0D, y + 2.0D, z + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.pos(x - 1.0D, y + 2.0D, z + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.pos(x - 1.0D, y + 2.0D, z - 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.pos(x + 1.0D, y + 2.0D, z - 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.pos(x + 1.0D, y, z - 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.pos(x - 1.0D, y, z - 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.pos(x + 1.0D, y + 2.0D, z - 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.pos(x + 1.0D, y + 2.0D, z + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.pos(x + 1.0D, y, z + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.pos(x + 1.0D, y, z - 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.pos(x - 1.0D, y, z - 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.pos(x - 1.0D, y, z + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.pos(x - 1.0D, y + 2.0D, z + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.pos(x - 1.0D, y + 2.0D, z - 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.pos(x - 1.0D, y, z - 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.pos(x + 1.0D, y, z - 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.pos(x + 1.0D, y, z + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.pos(x - 1.0D, y, z + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.pos(x - 1.0D, y + 2.0D, z + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.pos(x + 1.0D, y + 2.0D, z + 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.pos(x + 1.0D, y + 2.0D, z - 1.0D).color(f3, f4, f5, 1.0F).endVertex();
                bufferbuilder.pos(x - 1.0D, y + 2.0D, z - 1.0D).color(f3, f4, f5, 1.0F).endVertex();

                tessellator.draw();
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(5888);
                this.bindTexture(END_SKY_TEXTURE);
            }

            GlStateManager.disableBlend();
            GlStateManager.disableTexGenCoord(GlStateManager.TexGen.S);
            GlStateManager.disableTexGenCoord(GlStateManager.TexGen.T);
            GlStateManager.disableTexGenCoord(GlStateManager.TexGen.R);
            GlStateManager.enableLighting();

            if (flag) {
                Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
            }
        }
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
                bufferbuilder.pos(-0.866D * (double) f3, f2, -0.5F * f3).color(64, 255, 64, 255).endVertex();
                bufferbuilder.pos(0.866D * (double) f3, f2, -0.5F * f3).color(64, 255, 64, 255).endVertex();
                bufferbuilder.pos(0.0D, f2, 1.0F * f3).color(64, 255, 64, 255).endVertex();
                bufferbuilder.pos(-0.866D * (double) f3, f2, -0.5F * f3).color(64, 255, 64, 255).endVertex();
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

    private FloatBuffer getBuffer(float p_147525_1_, float p_147525_2_, float p_147525_3_, float p_147525_4_) {
        this.buffer.clear();
        this.buffer.put(p_147525_1_).put(p_147525_2_).put(p_147525_3_).put(p_147525_4_);
        this.buffer.flip();
        return this.buffer;
    }

    protected int getPasses(double p_191286_1_) {
        int i;

        if (p_191286_1_ > 36864.0D) {
            i = 1;
        } else if (p_191286_1_ > 25600.0D) {
            i = 3;
        } else if (p_191286_1_ > 16384.0D) {
            i = 5;
        } else if (p_191286_1_ > 9216.0D) {
            i = 7;
        } else if (p_191286_1_ > 4096.0D) {
            i = 9;
        } else if (p_191286_1_ > 1024.0D) {
            i = 11;
        } else if (p_191286_1_ > 576.0D) {
            i = 13;
        } else if (p_191286_1_ > 256.0D) {
            i = 14;
        } else {
            i = 15;
        }

        return i;
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
