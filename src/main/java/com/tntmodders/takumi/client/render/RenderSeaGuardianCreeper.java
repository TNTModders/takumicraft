package com.tntmodders.takumi.client.render;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.client.render.layer.LayerTakumiCharge;
import com.tntmodders.takumi.entity.mobs.EntitySeaGuardianCreeper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RenderSeaGuardianCreeper <T extends EntitySeaGuardianCreeper> extends RenderLiving <T> implements ITakumiRender {
    
    private static final ResourceLocation GUARDIAN_BEAM_TEXTURE = new ResourceLocation("textures/entity/guardian_beam.png");
    
    public RenderSeaGuardianCreeper(RenderManager rendermanagerIn) {
        super(rendermanagerIn, new ModelSeaGuardianCreeper(), 0.5f);
        this.addLayer(new LayerTakumiCharge(this));
    }
    
    /**
     * Gets an RGBA int color multiplier to apply.
     */
    @Override
    protected int getColorMultiplier(T entitylivingbaseIn, float lightBrightness, float partialTickTime) {
        float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
        
        if ((int) (f * 10.0F) % 2 == 0) {
            return 0;
        }
        int i = (int) (f * 0.2F * 255.0F);
        i = MathHelper.clamp(i, 0, 255);
        return i << 24 | 822083583;
    }
    
    /**
     * Allows the render to do state modifications necessary before the model is rendered.
     */
    @Override
    protected void preRenderCallback(T entitylivingbaseIn, float partialTickTime) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        
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
    protected ResourceLocation getEntityTexture(T entity) {
        return new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/" + entity.getRegisterName() + ".png");
    }
    
    @Override
    public boolean shouldRender(T livingEntity, ICamera camera, double camX, double camY, double camZ) {
        if (super.shouldRender(livingEntity, camera, camX, camY, camZ)) {
            return true;
        }
        if (livingEntity.hasTargetedEntity()) {
            EntityLivingBase entitylivingbase = livingEntity.getTargetedEntity();
            
            if (entitylivingbase != null) {
                Vec3d vec3d = this.getPosition(entitylivingbase, entitylivingbase.height * 0.5D, 1.0F);
                Vec3d vec3d1 = this.getPosition(livingEntity, livingEntity.getEyeHeight(), 1.0F);
                
                return camera.isBoundingBoxInFrustum(new AxisAlignedBB(vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y, vec3d.z));
            }
        }
        
        return false;
    }
    
    private Vec3d getPosition(EntityLivingBase entityLivingBaseIn, double p_177110_2_, float p_177110_4_) {
        double d0 = entityLivingBaseIn.lastTickPosX + (entityLivingBaseIn.posX - entityLivingBaseIn.lastTickPosX) * p_177110_4_;
        double d1 = p_177110_2_ + entityLivingBaseIn.lastTickPosY + (entityLivingBaseIn.posY - entityLivingBaseIn.lastTickPosY) * p_177110_4_;
        double d2 = entityLivingBaseIn.lastTickPosZ + (entityLivingBaseIn.posZ - entityLivingBaseIn.lastTickPosZ) * p_177110_4_;
        return new Vec3d(d0, d1, d2);
    }
    
    @Override
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        EntityLivingBase entitylivingbase = entity.getTargetedEntity();
        
        if (entitylivingbase != null) {
            float f = entity.getAttackAnimationScale(partialTicks);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            this.bindTexture(GUARDIAN_BEAM_TEXTURE);
            GlStateManager.glTexParameteri(3553, 10242, 10497);
            GlStateManager.glTexParameteri(3553, 10243, 10497);
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
            float f1 = 240.0F;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
            GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE, SourceFactor.ONE, DestFactor.ZERO);
            float f2 = entity.world.getTotalWorldTime() + partialTicks;
            float f3 = f2 * 0.5F % 1.0F;
            float f4 = entity.getEyeHeight();
            GlStateManager.pushMatrix();
            GlStateManager.translate((float) x, (float) y + f4, (float) z);
            Vec3d vec3d = this.getPosition(entitylivingbase, entitylivingbase.height * 0.5D, partialTicks);
            Vec3d vec3d1 = this.getPosition(entity, f4, partialTicks);
            Vec3d vec3d2 = vec3d.subtract(vec3d1);
            double d0 = vec3d2.lengthVector() + 1.0D;
            vec3d2 = vec3d2.normalize();
            float f5 = (float) Math.acos(vec3d2.y);
            float f6 = (float) Math.atan2(vec3d2.z, vec3d2.x);
            GlStateManager.rotate(((float) Math.PI / 2F + -f6) * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(f5 * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
            int i = 1;
            double d1 = (double) f2 * 0.05D * -1.5D;
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            float f7 = f * f;
            int j = 64 + (int) (f7 * 191.0F);
            int k = 32 + (int) (f7 * 191.0F);
            int l = 128 - (int) (f7 * 64.0F);
            double d2 = 0.2D;
            double d3 = 0.282D;
            double d4 = 0.0D + Math.cos(d1 + 2.356194490192345D) * 0.282D;
            double d5 = 0.0D + Math.sin(d1 + 2.356194490192345D) * 0.282D;
            double d6 = 0.0D + Math.cos(d1 + Math.PI / 4D) * 0.282D;
            double d7 = 0.0D + Math.sin(d1 + Math.PI / 4D) * 0.282D;
            double d8 = 0.0D + Math.cos(d1 + 3.9269908169872414D) * 0.282D;
            double d9 = 0.0D + Math.sin(d1 + 3.9269908169872414D) * 0.282D;
            double d10 = 0.0D + Math.cos(d1 + 5.497787143782138D) * 0.282D;
            double d11 = 0.0D + Math.sin(d1 + 5.497787143782138D) * 0.282D;
            double d12 = 0.0D + Math.cos(d1 + Math.PI) * 0.2D;
            double d13 = 0.0D + Math.sin(d1 + Math.PI) * 0.2D;
            double d14 = 0.0D + Math.cos(d1 + 0.0D) * 0.2D;
            double d15 = 0.0D + Math.sin(d1 + 0.0D) * 0.2D;
            double d16 = 0.0D + Math.cos(d1 + Math.PI / 2D) * 0.2D;
            double d17 = 0.0D + Math.sin(d1 + Math.PI / 2D) * 0.2D;
            double d18 = 0.0D + Math.cos(d1 + Math.PI * 3D / 2D) * 0.2D;
            double d19 = 0.0D + Math.sin(d1 + Math.PI * 3D / 2D) * 0.2D;
            double d20 = 0.0D;
            double d21 = 0.4999D;
            double d22 = (double) (-1.0F + f3);
            double d23 = d0 * 2.5D + d22;
            bufferbuilder.pos(d12, d0, d13).tex(0.4999D, d23).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d12, 0.0D, d13).tex(0.4999D, d22).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d14, 0.0D, d15).tex(0.0D, d22).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d14, d0, d15).tex(0.0D, d23).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d16, d0, d17).tex(0.4999D, d23).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d16, 0.0D, d17).tex(0.4999D, d22).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d18, 0.0D, d19).tex(0.0D, d22).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d18, d0, d19).tex(0.0D, d23).color(j, k, l, 255).endVertex();
            double d24 = 0.0D;
            
            if (entity.ticksExisted % 2 == 0) {
                d24 = 0.5D;
            }
            
            bufferbuilder.pos(d4, d0, d5).tex(0.5D, d24 + 0.5D).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d6, d0, d7).tex(1.0D, d24 + 0.5D).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d10, d0, d11).tex(1.0D, d24).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d8, d0, d9).tex(0.5D, d24).color(j, k, l, 255).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
        }
    }
    
    @Override
    public ModelBase getPoweredModel() {
        return new ModelSeaGuardianCreeper();
    }
    
    @SideOnly(Side.CLIENT)
    public static class ModelSeaGuardianCreeper extends ModelBase {
        
        private final ModelRenderer guardianBody;
        private final ModelRenderer guardianEye;
        private final ModelRenderer[] guardianSpines;
        private final ModelRenderer[] guardianTail;
        
        public ModelSeaGuardianCreeper() {
            this.textureWidth = 64;
            this.textureHeight = 64;
            this.guardianSpines = new ModelRenderer[12];
            this.guardianBody = new ModelRenderer(this);
            this.guardianBody.setTextureOffset(0, 0).addBox(-6.0F, 10.0F, -8.0F, 12, 12, 16);
            this.guardianBody.setTextureOffset(0, 28).addBox(-8.0F, 10.0F, -6.0F, 2, 12, 12);
            this.guardianBody.setTextureOffset(0, 28).addBox(6.0F, 10.0F, -6.0F, 2, 12, 12, true);
            this.guardianBody.setTextureOffset(16, 40).addBox(-6.0F, 8.0F, -6.0F, 12, 2, 12);
            this.guardianBody.setTextureOffset(16, 40).addBox(-6.0F, 22.0F, -6.0F, 12, 2, 12);
            
            for (int i = 0; i < this.guardianSpines.length; ++i) {
                this.guardianSpines[i] = new ModelRenderer(this, 0, 0);
                this.guardianSpines[i].addBox(-1.0F, -4.5F, -1.0F, 2, 9, 2);
                this.guardianBody.addChild(this.guardianSpines[i]);
            }
            
            this.guardianEye = new ModelRenderer(this, 8, 0);
            this.guardianEye.addBox(-1.0F, 15.0F, 0.0F, 2, 2, 1);
            this.guardianBody.addChild(this.guardianEye);
            this.guardianTail = new ModelRenderer[3];
            this.guardianTail[0] = new ModelRenderer(this, 40, 0);
            this.guardianTail[0].addBox(-2.0F, 14.0F, 7.0F, 4, 4, 8);
            this.guardianTail[1] = new ModelRenderer(this, 0, 54);
            this.guardianTail[1].addBox(0.0F, 14.0F, 0.0F, 3, 3, 7);
            this.guardianTail[2] = new ModelRenderer(this);
            this.guardianTail[2].setTextureOffset(41, 32).addBox(0.0F, 14.0F, 0.0F, 2, 2, 6);
            this.guardianTail[2].setTextureOffset(25, 19).addBox(1.0F, 10.5F, 3.0F, 1, 9, 9);
            this.guardianBody.addChild(this.guardianTail[0]);
            this.guardianTail[0].addChild(this.guardianTail[1]);
            this.guardianTail[1].addChild(this.guardianTail[2]);
        }
        
        /**
         * Sets the models various rotation angles then renders the model.
         */
        @Override
        public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float
                scale) {
            this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
            this.guardianBody.render(scale);
        }
        
        /**
         * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
         * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
         * "far" arms and legs can swing at most.
         */
        @Override
        public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float
                scaleFactor, Entity entityIn) {
            EntitySeaGuardianCreeper entityguardian = (EntitySeaGuardianCreeper) entityIn;
            float f = ageInTicks - entityguardian.ticksExisted;
            this.guardianBody.rotateAngleY = netHeadYaw * 0.017453292F;
            this.guardianBody.rotateAngleX = headPitch * 0.017453292F;
            float[] afloat = {1.75F, 0.25F, 0.0F, 0.0F, 0.5F, 0.5F, 0.5F, 0.5F, 1.25F, 0.75F, 0.0F, 0.0F};
            float[] afloat1 = {0.0F, 0.0F, 0.0F, 0.0F, 0.25F, 1.75F, 1.25F, 0.75F, 0.0F, 0.0F, 0.0F, 0.0F};
            float[] afloat2 = {0.0F, 0.0F, 0.25F, 1.75F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.75F, 1.25F};
            float[] afloat3 = {0.0F, 0.0F, 8.0F, -8.0F, -8.0F, 8.0F, 8.0F, -8.0F, 0.0F, 0.0F, 8.0F, -8.0F};
            float[] afloat4 = {-8.0F, -8.0F, -8.0F, -8.0F, 0.0F, 0.0F, 0.0F, 0.0F, 8.0F, 8.0F, 8.0F, 8.0F};
            float[] afloat5 = {8.0F, -8.0F, 0.0F, 0.0F, -8.0F, -8.0F, 8.0F, 8.0F, 8.0F, -8.0F, 0.0F, 0.0F};
            float f1 = (1.0F - entityguardian.getSpikesAnimation(f)) * 0.55F;
            
            for (int i = 0; i < 12; ++i) {
                this.guardianSpines[i].rotateAngleX = (float) Math.PI * afloat[i];
                this.guardianSpines[i].rotateAngleY = (float) Math.PI * afloat1[i];
                this.guardianSpines[i].rotateAngleZ = (float) Math.PI * afloat2[i];
                this.guardianSpines[i].rotationPointX = afloat3[i] * (1.0F + MathHelper.cos(ageInTicks * 1.5F + i) * 0.01F - f1);
                this.guardianSpines[i].rotationPointY = 16.0F + afloat4[i] * (1.0F + MathHelper.cos(ageInTicks * 1.5F + i) * 0.01F - f1);
                this.guardianSpines[i].rotationPointZ = afloat5[i] * (1.0F + MathHelper.cos(ageInTicks * 1.5F + i) * 0.01F - f1);
            }
            
            this.guardianEye.rotationPointZ = -8.25F;
            Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
            
            if (entityguardian.hasTargetedEntity()) {
                entity = entityguardian.getTargetedEntity();
            }
            
            if (entity != null) {
                Vec3d vec3d = entity.getPositionEyes(0.0F);
                Vec3d vec3d1 = entityIn.getPositionEyes(0.0F);
                double d0 = vec3d.y - vec3d1.y;
                
                if (d0 > 0.0D) {
                    this.guardianEye.rotationPointY = 0.0F;
                } else {
                    this.guardianEye.rotationPointY = 1.0F;
                }
                
                Vec3d vec3d2 = entityIn.getLook(0.0F);
                vec3d2 = new Vec3d(vec3d2.x, 0.0D, vec3d2.z);
                Vec3d vec3d3 = new Vec3d(vec3d1.x - vec3d.x, 0.0D, vec3d1.z - vec3d.z).normalize().rotateYaw((float) Math.PI / 2F);
                double d1 = vec3d2.dotProduct(vec3d3);
                this.guardianEye.rotationPointX = MathHelper.sqrt((float) Math.abs(d1)) * 2.0F * (float) Math.signum(d1);
            }
            
            this.guardianEye.showModel = true;
            float f2 = entityguardian.getTailAnimation(f);
            this.guardianTail[0].rotateAngleY = MathHelper.sin(f2) * (float) Math.PI * 0.05F;
            this.guardianTail[1].rotateAngleY = MathHelper.sin(f2) * (float) Math.PI * 0.1F;
            this.guardianTail[1].rotationPointX = -1.5F;
            this.guardianTail[1].rotationPointY = 0.5F;
            this.guardianTail[1].rotationPointZ = 14.0F;
            this.guardianTail[2].rotateAngleY = MathHelper.sin(f2) * (float) Math.PI * 0.15F;
            this.guardianTail[2].rotationPointX = 0.5F;
            this.guardianTail[2].rotationPointY = 0.5F;
            this.guardianTail[2].rotationPointZ = 6.0F;
        }
    }
}
