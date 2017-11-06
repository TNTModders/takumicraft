package com.tntmodders.takumi.client.render;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.client.render.layer.LayerHeldItemWitchCreeper;
import com.tntmodders.takumi.client.render.layer.LayerTakumiCharge;
import com.tntmodders.takumi.entity.mobs.EntityWitchCreeper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelWitch;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;


public class RenderWitchCreeper<T extends EntityWitchCreeper> extends RenderLiving<T> implements ITakumiRender {

    public RenderWitchCreeper(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelWitch(0.0f), 0.5F);
        this.addLayer(new LayerTakumiCharge(this));
        this.addLayer(new LayerHeldItemWitchCreeper(this));
    }

    @Override
    public ModelWitch getMainModel() {
        return (ModelWitch) this.mainModel;
    }

    @Override
    public void transformHeldFull3DItemLayer() {
        GlStateManager.translate(0.0F, 0.1875F, 0.0F);
    }

    @Override
    protected int getColorMultiplier(T entitylivingbaseIn, float lightBrightness, float partialTickTime) {
        float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);

        if ((int) (f * 10.0F) % 2 == 0) {
            return 0;
        } else {
            int i = (int) (f * 0.2F * 255.0F);
            i = MathHelper.clamp(i, 0, 255);
            return i << 24 | 822083583;
        }
    }

    @Override
    protected void preRenderCallback(T entitylivingbaseIn, float partialTickTime) {
        GlStateManager.scale(0.9375F, 0.9375F, 0.9375F);
        float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
        float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        f = f * f;
        f = f * f;
        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        GlStateManager.scale(f2, f3, f2);
    }

    @Override
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        ((ModelWitch) this.mainModel).holdingItem = !entity.getHeldItemMainhand().isEmpty();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        return new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/" + entity.getRegisterName() + ".png");
    }

    @Override
    public ModelBase getPoweredModel() {
        return new ModelWitch(2.0f);
    }
}
