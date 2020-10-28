package com.tntmodders.takumi.client.render;

import com.tntmodders.asm.TakumiASMNameMap;
import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.entity.mobs.boss.EntityKingCreeper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import java.lang.reflect.Field;
import java.util.Random;

public class RenderKingCreeper extends RenderTakumiCreeper {
    public RenderKingCreeper(RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.addLayer(new LayerKingSword());
    }

    public class LayerKingSword<E extends EntityKingCreeper> implements LayerRenderer<E> {
        private final ItemStack sword = new ItemStack(TakumiItemCore.TAKUMI_SWORD);

        @Override
        public void doRenderLayer(E kingcreeper, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            if (kingcreeper.getAttackID() == 7) {
                if (kingcreeper.getCreeperState() > 0) {
                    GlStateManager.pushMatrix();
                    float f = kingcreeper.getCreeperFlashIntensity(partialTicks);
                    f = MathHelper.clamp(f, 0.0F, 1.0F) * 5;
                    try {
                        Field field = TakumiASMNameMap.getField(EntityCreeper.class, "timeSinceIgnited");
                        field.setAccessible(true);
                        int time = field.getInt(kingcreeper);
                        if (time > kingcreeper.maxFuseTime - 6) {
                            GlStateManager.translate(0, 1, 0);
                            GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                            GlStateManager.rotate(360 / 6 * (time - 44 + partialTicks), 0.0f, 0.0f, 1.0f);
                        } else {
                            GlStateManager.translate(0, -0.5 - f / 2.5, 0);
                            GlStateManager.rotate(-135.0f, 0.0f, 0.0f, 1.0f);
                        }
                    } catch (Exception e) {
                    }
                    GlStateManager.scale(f, f, f);
                    Minecraft.getMinecraft().getItemRenderer().renderItem(kingcreeper, sword, ItemCameraTransforms.TransformType.NONE);
                    GlStateManager.popMatrix();
                }
            } else if (kingcreeper.getAttackID() == 99) {
                if (kingcreeper.getCreeperState() > 0) {
                    try {
                        Field field = TakumiASMNameMap.getField(EntityCreeper.class, "timeSinceIgnited");
                        field.setAccessible(true);
                        int time = field.getInt(kingcreeper);
                        if (time > 0) {
                            Tessellator tessellator = Tessellator.getInstance();
                            BufferBuilder bufferbuilder = tessellator.getBuffer();
                            //RenderHelper.disableStandardItemLighting();
                            float f = (partialTicks + time) / 100.0F;
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
                            GlStateManager.translate(0, 0.6, 0);
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
                                bufferbuilder.pos(-0.866D * (double) f3, f2, -0.5F * f3).color(0, 255, 0, 0).endVertex();
                                bufferbuilder.pos(0.866D * (double) f3, f2, -0.5F * f3).color(0, 255, 0, 0).endVertex();
                                bufferbuilder.pos(0.0D, f2, 1.0F * f3).color(0, 255, 0, 0).endVertex();
                                bufferbuilder.pos(-0.866D * (double) f3, f2, -0.5F * f3).color(0, 255, 0, 0).endVertex();
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
                    } catch (Exception e) {
                    }
                }
            }
        }

        @Override
        public boolean shouldCombineTextures() {
            return false;
        }
    }
}
