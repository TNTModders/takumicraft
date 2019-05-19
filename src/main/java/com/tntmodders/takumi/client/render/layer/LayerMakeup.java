package com.tntmodders.takumi.client.render.layer;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiItemCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;

public class LayerMakeup<E extends EntityLivingBase> implements LayerRenderer<E> {

    final RenderLivingBase renderTakumiCreeper;
    final ModelCreeper mainModel = new ModelCreeper();
    final ResourceLocation location_newyear =
            new ResourceLocation(TakumiCraftCore.MODID, "textures/models/armor/makeup_layer_1.png");

    public LayerMakeup(RenderLivingBase creeper) {
        this.renderTakumiCreeper = creeper;
    }

    @Override
    public void doRenderLayer(E entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks,
            float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (entitylivingbaseIn.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == TakumiItemCore.MAKEUP) {
            this.renderTakumiCreeper.bindTexture(location_newyear);
            Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
            mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
