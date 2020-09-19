package com.tntmodders.takumi.client.render;

import com.tntmodders.takumi.client.model.ModelChestCreeper;
import com.tntmodders.takumi.entity.item.EntityTakumiRandomChest;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderTakumiRandomChest<T extends EntityTakumiRandomChest> extends Render<T> {
    private static final ResourceLocation TEXTURE_NORMAL = new ResourceLocation("textures/entity/chest/normal.png");
    private final ModelBase model = new ModelChestCreeper();

    public RenderTakumiRandomChest(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        return TEXTURE_NORMAL;
    }

    @Override
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(180, 0, 0, 1);
        GlStateManager.rotate(180, 0, 1, 0);
        GlStateManager.translate(-0.5, -1.5, -0.5);

        this.bindTexture(this.getEntityTexture(entity));
        this.model.render(entity, 0, 0, 0, 0, 0, 0.0625f);
        GlStateManager.popMatrix();
    }
}
