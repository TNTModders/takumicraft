package com.tntmodders.takumi.client.render.layer;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.client.model.ModelSheepCreeper1;
import com.tntmodders.takumi.client.render.RenderSheepCreeper;
import com.tntmodders.takumi.entity.mobs.EntitySheepCreeper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;


public class LayerSheepCreeperWool implements LayerRenderer<EntitySheepCreeper> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/sheepcreeper_fur.png");
    private final RenderSheepCreeper sheepRenderer;
    private final ModelSheepCreeper1 sheepModel = new ModelSheepCreeper1();

    public LayerSheepCreeperWool(RenderSheepCreeper sheepRendererIn) {
        this.sheepRenderer = sheepRendererIn;
    }

    @Override
    public void doRenderLayer(EntitySheepCreeper entitylivingbaseIn, float limbSwing, float limbSwingAmount,
            float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!entitylivingbaseIn.getSheared() && !entitylivingbaseIn.isInvisible()) {
            this.sheepRenderer.bindTexture(TEXTURE);

            if (entitylivingbaseIn.getRainbow()) {
                int i1 = 25;
                int i = entitylivingbaseIn.ticksExisted / 25 + entitylivingbaseIn.getEntityId();
                int j = EnumDyeColor.values().length;
                int k = i % j;
                int l = (i + 1) % j;
                float f = ((float) (entitylivingbaseIn.ticksExisted % 25) + partialTicks) / 25.0F;
                float[] afloat1 = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(k));
                float[] afloat2 = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(l));
                GlStateManager.color(afloat1[0] * (1.0F - f) + afloat2[0] * f, afloat1[1] * (1.0F - f) + afloat2[1] * f,
                        afloat1[2] * (1.0F - f) + afloat2[2] * f);
            } else {
                float[] afloat = EntitySheep.getDyeRgb(entitylivingbaseIn.getFleeceColor());
                GlStateManager.color(afloat[0], afloat[1], afloat[2]);
            }

            this.sheepModel.setModelAttributes(this.sheepRenderer.getMainModel());
            this.sheepModel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
            this.sheepModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch,
                    scale);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}