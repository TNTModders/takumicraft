package com.tntmodders.takumi.client.render;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.entity.mobs.boss.EntityAngelCreeper;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class RenderAngelCreeper<T extends EntityAngelCreeper> extends RenderTakumiCreeper<T> {
    public static final ResourceLocation ANGEL = new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/angelcreeper.png");
    public static final ResourceLocation DEMON = new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/demoncreeper.png");
    public static final ResourceLocation RARE = new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/rarecreeper.png");
    public static final ResourceLocation ODD = new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/oddcreeper.png");
    public static final ResourceLocation BOLT = new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/boltcreeper.png");
    public static final ResourceLocation OFALEN = new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/ofalencreeper.png");
    public static final ResourceLocation TNT = new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/tntcreeper.png");

    public RenderAngelCreeper(RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.addLayer(new LayerAngelCreeperDeath());
    }

    @Override
    protected void applyRotations(T entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) {
        if (entityLiving.deathTime > 0) {
            float f2 = ((float) entityLiving.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
            f2 = MathHelper.sqrt(f2);

            if (f2 > 1.0F) {
                f2 = 1.0F;
            }

            GlStateManager.rotate(f2 * this.getDeathMaxRotation(entityLiving), 0.0F, 0.0F, 1.0F);
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        int id = entity.getAttackID();
        switch (id) {
            case 1:
                return DEMON;
            case 2:
                return RARE;
            case 3:
                return ODD;
            case 4:
                return BOLT;
            case 5:
                return OFALEN;
            case 6:
                return TNT;
            default:
                return ANGEL;
        }
    }

    @SideOnly(Side.CLIENT)
    public class LayerAngelCreeperDeath implements LayerRenderer<EntityAngelCreeper> {
        @Override
        public void doRenderLayer(EntityAngelCreeper entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            if (entitylivingbaseIn.deathTicks > 0) {
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();
                //RenderHelper.disableStandardItemLighting();
                float f = ((float) entitylivingbaseIn.deathTicks + partialTicks) / 200.0F;
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
                    bufferbuilder.pos(0.0D, 0.0D, 0.0D).color(255, 255, 255, (int) (255.0F * (1.0F - f1))).endVertex();
                    bufferbuilder.pos(-0.866D * (double) f3, f2, -0.5F * f3).color(255, 0, 255, 0).endVertex();
                    bufferbuilder.pos(0.866D * (double) f3, f2, -0.5F * f3).color(255, 0, 255, 0).endVertex();
                    bufferbuilder.pos(0.0D, f2, 1.0F * f3).color(255, 0, 255, 0).endVertex();
                    bufferbuilder.pos(-0.866D * (double) f3, f2, -0.5F * f3).color(255, 0, 255, 0).endVertex();
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
        public boolean shouldCombineTextures() {
            return false;
        }
    }
}
