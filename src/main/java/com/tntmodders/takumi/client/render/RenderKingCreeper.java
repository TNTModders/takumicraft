package com.tntmodders.takumi.client.render;

import com.tntmodders.asm.TakumiASMNameMap;
import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.entity.mobs.boss.EntityKingCreeper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import java.lang.reflect.Field;

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
                GlStateManager.pushMatrix();
                float f = kingcreeper.getCreeperFlashIntensity(partialTicks);
                f = MathHelper.clamp(f, 0.0F, 1.0F) * 5;
                try {
                    Field field = TakumiASMNameMap.getField(EntityCreeper.class, "timeSinceIgnited");
                    field.setAccessible(true);
                    int time = field.getInt(kingcreeper);
                    if (time > 44) {
                        GlStateManager.translate(0,1,0);
                        GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                        GlStateManager.rotate(360 / 6 * (time - 44), 0.0f, 0.0f, 1.0f);
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
        }

        @Override
        public boolean shouldCombineTextures() {
            return false;
        }
    }
}
