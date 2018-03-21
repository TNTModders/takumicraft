package com.tntmodders.takumi.client.render;


import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.client.model.ModelIllager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySpellcasterIllager;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderDarkVillager extends RenderLiving<EntityMob> {
    private static final ResourceLocation EVOKER_ILLAGER = new ResourceLocation
            (TakumiCraftCore.MODID,"textures/entity/darkvillager.png");

    public RenderDarkVillager(RenderManager p_i47207_1_) {
        super(p_i47207_1_, new ModelIllager(0.0F, 0.0F, 64, 64), 0.5F);
        this.addLayer(new LayerHeldItem(this) {
            @Override
            public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount,
                    float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
                if (((EntitySpellcasterIllager) entitylivingbaseIn).isSpellcasting()) {
                    super.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks,
                            netHeadYaw, headPitch, scale);
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
        return EVOKER_ILLAGER;
    }

    /**
     * Allows the render to do state modifications necessary before the model is rendered.
     */
    @Override
    protected void preRenderCallback(EntityMob entitylivingbaseIn, float partialTickTime) {
        float f = 0.9375F;
        GlStateManager.scale(0.9375F, 0.9375F, 0.9375F);
    }
}