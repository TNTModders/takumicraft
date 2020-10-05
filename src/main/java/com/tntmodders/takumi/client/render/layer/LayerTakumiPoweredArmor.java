package com.tntmodders.takumi.client.render.layer;

import com.google.common.collect.Lists;
import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.event.TakumiClientEvents;
import com.tntmodders.takumi.item.ItemBattleArmor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class LayerTakumiPoweredArmor implements LayerRenderer {
    private static final ResourceLocation ICE_TEXTURE = new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/frozen_entity.png");
    private final RenderLivingBase render;
    protected ModelBiped modelLeggings;
    protected ModelBiped modelArmor;

    public LayerTakumiPoweredArmor(RenderLivingBase renderLivingIn) {
        this.render = renderLivingIn;
        this.modelLeggings = new ModelBiped(0.5F);
        this.modelArmor = new ModelBiped(1.0F);
    }

    @Override
    public void doRenderLayer(EntityLivingBase entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (Lists.newArrayList(entityLivingBaseIn.getArmorInventoryList()).stream().allMatch(
                itemStack -> itemStack.getItem() instanceof ItemBattleArmor &&
                        ((ItemBattleArmor) itemStack.getItem()).isPowered)) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(TakumiClientEvents.ModelSaber.SABER_TEXTURE);
            GlStateManager.pushMatrix();
            GlStateManager.depthMask(true);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            float tick = Minecraft.getMinecraft().player.ticksExisted * 2;
            GL11.glTranslated(tick * 0.01F, tick * 0.01F, 0.0F);
            GlStateManager.matrixMode(5888);
            GlStateManager.enableBlend();
            GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
            GlStateManager.disableLighting();
            int i = 15728880;
            int j = i % 65536;
            int k = i / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            this.modelArmor.setModelAttributes(this.render.getMainModel());
            this.modelArmor.setLivingAnimations(entityLivingBaseIn, limbSwing, limbSwingAmount, partialTicks);
            this.modelArmor.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, entityLivingBaseIn.rotationPitch, scale, entityLivingBaseIn);
            this.modelArmor.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

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
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
