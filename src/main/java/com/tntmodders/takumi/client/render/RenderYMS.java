package com.tntmodders.takumi.client.render;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.entity.item.EntityYMS;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

public class RenderYMS<T extends EntityYMS> extends RenderLiving<T> {

    private static final ResourceLocation YMS_TEXTURES =
            new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/yms.png");

    public RenderYMS(RenderManager rendermanagerIn) {
        super(rendermanagerIn, new ModelYMS(), 0.5f);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        return YMS_TEXTURES;
    }

    @Override
    protected void applyRotations(T entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) {
        if (entityLiving.getControllingPassenger() instanceof EntityPlayer) {
            rotationYaw = ((EntityPlayer) entityLiving.getControllingPassenger()).rotationYawHead;
        }
        super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
    }

    @Override
    protected void preRenderCallback(T par1EntityLiving, float par2) {
        GL11.glScalef(2F, 2F, 2F);
        GL11.glRotatef(180f, 1, 0, 0);
        GL11.glTranslatef(0, 1.5f, 0);
        GL11.glRotatef(par1EntityLiving.rotationPitch - 90, 0, 1, 0);
    }

    private static class ModelYMS extends ModelBase {
        public ModelRenderer box;
        public ModelRenderer box0;
        public ModelRenderer box1;
        public ModelRenderer box2;
        public ModelRenderer box3;
        public ModelRenderer box4;
        public ModelRenderer box5;
        public ModelRenderer box6;

        public ModelYMS() {
            this.textureHeight = 252;
            this.textureWidth = 252;

            box = new ModelRenderer(this, 0, 0);
            box.addBox(0F, 0F, 0F, 16, 8, 20);
            box.setRotationPoint(0F, 4F, -10F);

            box0 = new ModelRenderer(this, 0, 28);
            box0.addBox(0F, 0F, 0F, 12, 10, 12);
            box0.setRotationPoint(2F, 6F, -6F);

            box1 = new ModelRenderer(this, 0, 50);
            box1.addBox(0F, 0F, 0F, 12, 4, 12);
            box1.setRotationPoint(-12F, 6F, -6F);

            box2 = new ModelRenderer(this, 0, 66);
            box2.addBox(0F, 0F, 0F, 16, 8, 14);
            box2.setRotationPoint(-24F, 4F, -7F);

            box3 = new ModelRenderer(this, 0, 88);
            box3.addBox(0F, 0F, 0F, 10, 6, 40);
            box3.setRotationPoint(-22F, 5F, -20F);

            box4 = new ModelRenderer(this, 0, 134);
            box4.addBox(0F, 0F, 0F, 16, 8, 16);
            box4.setRotationPoint(16F, 3F, -8F);

            box5 = new ModelRenderer(this, 0, 158);
            box5.addBox(0F, 0F, 0F, 40, 8, 12);
            box5.setRotationPoint(-42F, 2F, -30F);

            box6 = new ModelRenderer(this, 0, 158);
            box6.addBox(0F, 0F, 0F, 40, 8, 12);
            box6.setRotationPoint(-42F, 2F, 18F);
        }

        @Override
        public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
            super.render(entity, f, f1, f2, f3, f4, f5);
            this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
            box.render(f5);
            box0.render(f5);
            box1.render(f5);
            box2.render(f5);
            box3.render(f5);
            box4.render(f5);
            box5.render(f5);
            box6.render(f5);
        }

        @Override
        public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
            super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        }

        private void setRotation(ModelRenderer model, float x, float y, float z) {
            model.rotateAngleX = x;
            model.rotateAngleY = y;
            model.rotateAngleZ = z;
        }

    }
}
