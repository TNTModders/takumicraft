package com.tntmodders.takumi.client.render.layer;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.mobs.EntityRoboCreeper;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import java.util.Random;

public class LayerTakumiRazer implements LayerRenderer<EntityTakumiAbstractCreeper> {

    public LayerTakumiRazer() {
    }

    @Override
    public void doRenderLayer(EntityTakumiAbstractCreeper entitylivingbaseIn, float limbSwing, float limbSwingAmount,
            float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (entitylivingbaseIn instanceof EntityRoboCreeper &&
                ((EntityRoboCreeper) entitylivingbaseIn).getPos() != null) {
            for (int t = 0; t < 20; t++) {
                long boltVertex = entitylivingbaseIn.getRNG().nextLong();
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();
                GlStateManager.disableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
                GlStateManager.scale(0.25, 0.03125, 0.25);
                GlStateManager.rotate(-90, 1, 0, 0);
                double[] adouble = new double[8];
                double[] adouble1 = new double[8];
                double d0 = 0.0D;
                double d1 = 0.0D;
                Random random = new Random(boltVertex);

                for (int i = 7; i >= 0; --i) {
                    adouble[i] = d0;
                    adouble1[i] = d1;
                    d0 += (double) (random.nextInt(11) - 5);
                    d1 += (double) (random.nextInt(11) - 5);
                }

                for (int k1 = 0; k1 < 4; ++k1) {
                    Random random1 = new Random(boltVertex);

                    for (int j = 0; j < 3; ++j) {
                        int k = 7;
                        int l = 0;

                        if (j > 0) {
                            k = 7 - j;
                        }

                        if (j > 0) {
                            l = k - 2;
                        }

                        double d2 = adouble[k] - d0;
                        double d3 = adouble1[k] - d1;

                        for (int i1 = k; i1 >= l; --i1) {
                            double d4 = d2;
                            double d5 = d3;

                            if (j == 0) {
                                d2 += (double) (random1.nextInt(11) - 5);
                                d3 += (double) (random1.nextInt(11) - 5);
                            } else {
                                d2 += (double) (random1.nextInt(31) - 15);
                                d3 += (double) (random1.nextInt(31) - 15);
                            }

                            bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
                            float f = 0.5F;
                            float f1 = 0.45F;
                            float f2 = 0.45F;
                            float f3 = 0.5F;
                            double d6 = 0.1D + (double) k1 * 0.2D;

                            if (j == 0) {
                                d6 *= (double) i1 * 0.1D + 1.0D;
                            }

                            double d7 = 0.1D + (double) k1 * 0.2D;

                            if (j == 0) {
                                d7 *= (double) (i1 - 1) * 0.1D + 1.0D;
                            }

                            for (int j1 = 0; j1 < 5; ++j1) {
                                double d8 = 0.5D - d6;
                                double d9 = 0.5D - d6;

                                if (j1 == 1 || j1 == 2) {
                                    d8 += d6 * 2.0D;
                                }

                                if (j1 == 2 || j1 == 3) {
                                    d9 += d6 * 2.0D;
                                }

                                double d10 = 0.5D - d7;
                                double d11 = 0.5D - d7;

                                if (j1 == 1 || j1 == 2) {
                                    d10 += d7 * 2.0D;
                                }

                                if (j1 == 2 || j1 == 3) {
                                    d11 += d7 * 2.0D;
                                }

                                bufferbuilder.pos(d10 + d2, (double) (i1 * 16), d11 + d3).color(1F, 0F, 0F,
                                        0.3F).endVertex();
                                bufferbuilder.pos(d8 + d4, (double) ((i1 + 1) * 16), d9 + d5).color(0.8F, 0F, 0F,
                                        0.3F).endVertex();
                            }

                            tessellator.draw();
                        }
                    }
                }
                GlStateManager.rotate(90, 1, 0, 0);
                GlStateManager.scale(4, 32, 4);
                GlStateManager.disableBlend();
                GlStateManager.enableLighting();
                GlStateManager.enableTexture2D();
            }
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
