package com.tntmodders.takumi.client.render;

import com.tntmodders.takumi.entity.mobs.EntityMirrorCreeper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMirrorCreeper extends RenderLivingBase<EntityMirrorCreeper> implements ITakumiRender {

    public RenderMirrorCreeper(RenderManager renderManager) {
        this(renderManager, false);
    }

    public RenderMirrorCreeper(RenderManager renderManager, boolean useSmallArms) {
        super(renderManager, new ModelPlayer(0.0F, useSmallArms), 0.5F);
        boolean smallArms = useSmallArms;
        this.addLayer(new LayerBipedArmor(this));
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerArrow(this));
        this.addLayer(new LayerDeadmauHeadDummy(this));
        this.addLayer(new LayerCapeDummy(this));
        this.addLayer(new LayerCustomHead(this.getMainModel().bipedHead));
        this.addLayer(new LayerElytra(this));
        this.addLayer(new LayerCreeperChargeDummy(this));
    }

    @Override
    protected int getColorMultiplier(EntityMirrorCreeper entitylivingbaseIn, float lightBrightness,
            float partialTickTime) {
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
    public ModelPlayer getMainModel() {
        return (ModelPlayer) super.getMainModel();
    }

    /**
     * Renders the desired {@code T} type Entity.
     */
    @Override
    public void doRender(EntityMirrorCreeper entitydummy, double x, double y, double z, float entityYaw,
            float partialTicks) {
        EntityPlayer entity = Minecraft.getMinecraft().player;
        entitydummy.setItemStackToSlot(EntityEquipmentSlot.HEAD, entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD));
        entitydummy.setItemStackToSlot(EntityEquipmentSlot.CHEST,
                entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST));
        entitydummy.setItemStackToSlot(EntityEquipmentSlot.LEGS, entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS));
        entitydummy.setItemStackToSlot(EntityEquipmentSlot.FEET, entity.getItemStackFromSlot(EntityEquipmentSlot.FEET));
        entitydummy.setItemStackToSlot(EntityEquipmentSlot.MAINHAND,
                entity.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND));
        entitydummy.setItemStackToSlot(EntityEquipmentSlot.OFFHAND,
                entity.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND));
        if (entity != null) {
            //if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event
            // .RenderPlayerEvent.Pre(entity, this, partialTicks, x, y, z))) return;
            if (!entity.isUser() || this.renderManager.renderViewEntity == entity) {
                double d0 = y;

                if (entity.isSneaking()) {
                    d0 = y - 0.125D;
                }

                this.setModelVisibilities(entitydummy);
                GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
                if (!entitydummy.isDead) {
                    super.doRender(entitydummy, x, d0, z, entityYaw, partialTicks);
                }
                GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
            }
            //net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(
            //new net.minecraftforge.client.event.RenderPlayerEvent.Post(entity, this, partialTicks, x, y, z));
        }
    }

    private void setModelVisibilities(EntityMirrorCreeper clientPlayerdummy) {
        EntityPlayer clientPlayer = Minecraft.getMinecraft().player;
        if (clientPlayer != null) {
            ModelPlayer modelplayer = this.getMainModel();

            if (clientPlayer.isSpectator()) {
                modelplayer.setVisible(false);
                modelplayer.bipedHead.showModel = true;
                modelplayer.bipedHeadwear.showModel = true;
            } else {
                ItemStack itemstack = clientPlayer.getHeldItemMainhand();
                ItemStack itemstack1 = clientPlayer.getHeldItemOffhand();
                modelplayer.setVisible(true);
                modelplayer.bipedHeadwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.HAT);
                modelplayer.bipedBodyWear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.JACKET);
                modelplayer.bipedLeftLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_PANTS_LEG);
                modelplayer.bipedRightLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_PANTS_LEG);
                modelplayer.bipedLeftArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_SLEEVE);
                modelplayer.bipedRightArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_SLEEVE);
                modelplayer.isSneak = false;
                ModelBiped.ArmPose modelbiped$armpose = ModelBiped.ArmPose.EMPTY;
                ModelBiped.ArmPose modelbiped$armpose1 = ModelBiped.ArmPose.EMPTY;

                if (!itemstack.isEmpty()) {
                    modelbiped$armpose = ModelBiped.ArmPose.ITEM;

                    if (clientPlayer.getItemInUseCount() > 0) {
                        EnumAction enumaction = itemstack.getItemUseAction();

                        if (enumaction == EnumAction.BLOCK) {
                            modelbiped$armpose = ModelBiped.ArmPose.BLOCK;
                        } else if (enumaction == EnumAction.BOW) {
                            modelbiped$armpose = ModelBiped.ArmPose.BOW_AND_ARROW;
                        }
                    }
                }

                if (!itemstack1.isEmpty()) {
                    modelbiped$armpose1 = ModelBiped.ArmPose.ITEM;

                    if (clientPlayer.getItemInUseCount() > 0) {
                        EnumAction enumaction1 = itemstack1.getItemUseAction();

                        if (enumaction1 == EnumAction.BLOCK) {
                            modelbiped$armpose1 = ModelBiped.ArmPose.BLOCK;
                        }
                        // FORGE: fix MC-88356 allow offhand to use bow and arrow animation
                        else if (enumaction1 == EnumAction.BOW) {
                            modelbiped$armpose1 = ModelBiped.ArmPose.BOW_AND_ARROW;
                        }
                    }
                }

                if (clientPlayer.getPrimaryHand() == EnumHandSide.RIGHT) {
                    modelplayer.rightArmPose = modelbiped$armpose;
                    modelplayer.leftArmPose = modelbiped$armpose1;
                } else {
                    modelplayer.rightArmPose = modelbiped$armpose1;
                    modelplayer.leftArmPose = modelbiped$armpose;
                }
            }
        }
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    public ResourceLocation getEntityTexture(EntityMirrorCreeper entity) {
        AbstractClientPlayer player = Minecraft.getMinecraft().player;
        return player != null ? player.getLocationSkin() : null;
    }

    @Override
    public void transformHeldFull3DItemLayer() {
        GlStateManager.translate(0.0F, 0.1875F, 0.0F);
    }

    /**
     * Allows the render to do state modifications necessary before the model is rendered.
     */
    @Override
    protected void preRenderCallback(EntityMirrorCreeper entitylivingbaseIn, float partialTickTime) {
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
    protected void renderEntityName(EntityMirrorCreeper entity, double x, double y, double z, String name,
            double distanceSq) {
        EntityPlayer entityIn = Minecraft.getMinecraft().player;
        if (entityIn != null) {
            if (distanceSq < 100.0D) {
                Scoreboard scoreboard = entityIn.getWorldScoreboard();
                ScoreObjective scoreobjective = scoreboard.getObjectiveInDisplaySlot(2);

                if (scoreobjective != null) {
                    Score score = scoreboard.getOrCreateScore(entityIn.getName(), scoreobjective);
                    this.renderLivingLabel(entity, score.getScorePoints() + " " + scoreobjective.getDisplayName(), x, y,
                            z, 64);
                    y += (double) ((float) this.getFontRendererFromRenderManager().FONT_HEIGHT * 1.15F * 0.025F);
                }
            }

            super.renderEntityName(entity, x, y, z, name, distanceSq);
        }
    }

    public void renderRightArm(EntityMirrorCreeper clientPlayer) {
        float f = 1.0F;
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        float f1 = 0.0625F;
        ModelPlayer modelplayer = this.getMainModel();
        this.setModelVisibilities(clientPlayer);
        GlStateManager.enableBlend();
        modelplayer.swingProgress = 0.0F;
        modelplayer.isSneak = false;
        modelplayer.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, clientPlayer);
        modelplayer.bipedRightArm.rotateAngleX = 0.0F;
        modelplayer.bipedRightArm.render(0.0625F);
        modelplayer.bipedRightArmwear.rotateAngleX = 0.0F;
        modelplayer.bipedRightArmwear.render(0.0625F);
        GlStateManager.disableBlend();
    }

    public void renderLeftArm(EntityMirrorCreeper clientPlayer) {
        float f = 1.0F;
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        float f1 = 0.0625F;
        ModelPlayer modelplayer = this.getMainModel();
        this.setModelVisibilities(clientPlayer);
        GlStateManager.enableBlend();
        modelplayer.isSneak = false;
        modelplayer.swingProgress = 0.0F;
        modelplayer.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, clientPlayer);
        modelplayer.bipedLeftArm.rotateAngleX = 0.0F;
        modelplayer.bipedLeftArm.render(0.0625F);
        modelplayer.bipedLeftArmwear.rotateAngleX = 0.0F;
        modelplayer.bipedLeftArmwear.render(0.0625F);
        GlStateManager.disableBlend();
    }

    /**
     * Sets a simple glTranslate on a LivingEntity.
     */
    @Override
    protected void renderLivingAt(EntityMirrorCreeper entityLivingBase, double x, double y, double z) {
        EntityPlayer entityLivingBaseIn = Minecraft.getMinecraft().player;
        if (entityLivingBaseIn != null) {
            super.renderLivingAt(entityLivingBase, x + (double) entityLivingBaseIn.renderOffsetX,
                    y + (double) entityLivingBaseIn.renderOffsetY, z + (double) entityLivingBaseIn.renderOffsetZ);
        }
    }

    @Override
    protected void applyRotations(EntityMirrorCreeper entityLivingIn, float p_77043_2_, float rotationYaw,
            float partialTicks) {
        EntityPlayer entityLiving = Minecraft.getMinecraft().player;
        /*if (entityLiving != null) {
            GlStateManager.rotate(entityLiving.getBedOrientationInDegrees(), 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(this.getDeathMaxRotation(entityLivingIn), 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(270.0F, 0.0F, 1.0F, 0.0F);
        }*/
    }

    @Override
    public ModelBase getPoweredModel() {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public class LayerDeadmauHeadDummy implements LayerRenderer<EntityMirrorCreeper> {
        private final RenderMirrorCreeper playerRenderer;

        public LayerDeadmauHeadDummy(RenderMirrorCreeper playerRendererIn) {
            this.playerRenderer = playerRendererIn;
        }

        @Override
        public void doRenderLayer(EntityMirrorCreeper entitylivingbase, float limbSwing, float limbSwingAmount,
                float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            AbstractClientPlayer entitylivingbaseIn = Minecraft.getMinecraft().player;
            if (entitylivingbaseIn != null && "deadmau5".equals(entitylivingbaseIn.getName()) &&
                    entitylivingbaseIn.hasSkin() && !entitylivingbaseIn.isInvisible()) {
                this.playerRenderer.bindTexture(entitylivingbaseIn.getLocationSkin());

                for (int i = 0; i < 2; ++i) {
                    float f = entitylivingbaseIn.prevRotationYaw +
                            (entitylivingbaseIn.rotationYaw - entitylivingbaseIn.prevRotationYaw) * partialTicks -
                            (entitylivingbaseIn.prevRenderYawOffset +
                                    (entitylivingbaseIn.renderYawOffset - entitylivingbaseIn.prevRenderYawOffset) *
                                            partialTicks);
                    float f1 = entitylivingbaseIn.prevRotationPitch +
                            (entitylivingbaseIn.rotationPitch - entitylivingbaseIn.prevRotationPitch) * partialTicks;
                    GlStateManager.pushMatrix();
                    GlStateManager.rotate(f, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(f1, 1.0F, 0.0F, 0.0F);
                    GlStateManager.translate(0.375F * (float) (i * 2 - 1), 0.0F, 0.0F);
                    GlStateManager.translate(0.0F, -0.375F, 0.0F);
                    GlStateManager.rotate(-f1, 1.0F, 0.0F, 0.0F);
                    GlStateManager.rotate(-f, 0.0F, 1.0F, 0.0F);
                    float f2 = 1.3333334F;
                    GlStateManager.scale(1.3333334F, 1.3333334F, 1.3333334F);
                    this.playerRenderer.getMainModel().renderDeadmau5Head(0.0625F);
                    GlStateManager.popMatrix();
                }
            }
        }

        @Override
        public boolean shouldCombineTextures() {
            return true;
        }
    }

    @SideOnly(Side.CLIENT)
    public class LayerCapeDummy implements LayerRenderer<EntityMirrorCreeper> {
        private final RenderMirrorCreeper playerRenderer;

        public LayerCapeDummy(RenderMirrorCreeper playerRendererIn) {
            this.playerRenderer = playerRendererIn;
        }

        @Override
        public void doRenderLayer(EntityMirrorCreeper entitylivingbase, float limbSwing, float limbSwingAmount,
                float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            AbstractClientPlayer entitylivingbaseIn = Minecraft.getMinecraft().player;
            if (entitylivingbaseIn != null && entitylivingbaseIn.hasPlayerInfo() && !entitylivingbaseIn.isInvisible() &&
                    entitylivingbaseIn.isWearing(EnumPlayerModelParts.CAPE) &&
                    entitylivingbaseIn.getLocationCape() != null) {
                ItemStack itemstack = entitylivingbaseIn.getItemStackFromSlot(EntityEquipmentSlot.CHEST);

                if (itemstack.getItem() != Items.ELYTRA) {
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    this.playerRenderer.bindTexture(entitylivingbaseIn.getLocationCape());
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(0.0F, 0.0F, 0.125F);
                    double d0 = entitylivingbaseIn.prevChasingPosX +
                            (entitylivingbaseIn.chasingPosX - entitylivingbaseIn.prevChasingPosX) *
                                    (double) partialTicks - (entitylivingbaseIn.prevPosX +
                            (entitylivingbaseIn.posX - entitylivingbaseIn.prevPosX) * (double) partialTicks);
                    double d1 = entitylivingbaseIn.prevChasingPosY +
                            (entitylivingbaseIn.chasingPosY - entitylivingbaseIn.prevChasingPosY) *
                                    (double) partialTicks - (entitylivingbaseIn.prevPosY +
                            (entitylivingbaseIn.posY - entitylivingbaseIn.prevPosY) * (double) partialTicks);
                    double d2 = entitylivingbaseIn.prevChasingPosZ +
                            (entitylivingbaseIn.chasingPosZ - entitylivingbaseIn.prevChasingPosZ) *
                                    (double) partialTicks - (entitylivingbaseIn.prevPosZ +
                            (entitylivingbaseIn.posZ - entitylivingbaseIn.prevPosZ) * (double) partialTicks);
                    float f = entitylivingbaseIn.prevRenderYawOffset +
                            (entitylivingbaseIn.renderYawOffset - entitylivingbaseIn.prevRenderYawOffset) *
                                    partialTicks;
                    double d3 = (double) MathHelper.sin(f * 0.017453292F);
                    double d4 = (double) (-MathHelper.cos(f * 0.017453292F));
                    float f1 = (float) d1 * 10.0F;
                    f1 = MathHelper.clamp(f1, -6.0F, 32.0F);
                    float f2 = (float) (d0 * d3 + d2 * d4) * 100.0F;
                    float f3 = (float) (d0 * d4 - d2 * d3) * 100.0F;

                    if (f2 < 0.0F) {
                        f2 = 0.0F;
                    }

                    float f4 = entitylivingbaseIn.prevCameraYaw +
                            (entitylivingbaseIn.cameraYaw - entitylivingbaseIn.prevCameraYaw) * partialTicks;
                    f1 = f1 + MathHelper.sin((entitylivingbaseIn.prevDistanceWalkedModified +
                            (entitylivingbaseIn.distanceWalkedModified -
                                    entitylivingbaseIn.prevDistanceWalkedModified) * partialTicks) * 6.0F) * 32.0F * f4;

                    if (entitylivingbaseIn.isSneaking()) {
                        f1 += 25.0F;
                    }

                    GlStateManager.rotate(6.0F + f2 / 2.0F + f1, 1.0F, 0.0F, 0.0F);
                    GlStateManager.rotate(f3 / 2.0F, 0.0F, 0.0F, 1.0F);
                    GlStateManager.rotate(-f3 / 2.0F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                    this.playerRenderer.getMainModel().renderCape(0.0625F);
                    GlStateManager.popMatrix();
                }
            }
        }

        @Override
        public boolean shouldCombineTextures() {
            return false;
        }
    }

    @SideOnly(Side.CLIENT)
    public class LayerCreeperChargeDummy implements LayerRenderer<EntityCreeper> {
        private final ResourceLocation LIGHTNING_TEXTURE =
                new ResourceLocation("textures/entity/creeper/creeper_armor.png");
        private final RenderMirrorCreeper creeperRenderer;
        private final ModelCreeper creeperModel = new ModelCreeper(2.0F);

        public LayerCreeperChargeDummy(RenderMirrorCreeper creeperRendererIn) {
            this.creeperRenderer = creeperRendererIn;
        }

        @Override
        public void doRenderLayer(EntityCreeper entitylivingbaseIn, float limbSwing, float limbSwingAmount,
                float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            if (entitylivingbaseIn.getPowered()) {
                boolean flag = entitylivingbaseIn.isInvisible();
                GlStateManager.depthMask(!flag);
                this.creeperRenderer.bindTexture(LIGHTNING_TEXTURE);
                GlStateManager.matrixMode(5890);
                GlStateManager.loadIdentity();
                float f = (float) entitylivingbaseIn.ticksExisted + partialTicks;
                GlStateManager.translate(f * 0.01F, f * 0.01F, 0.0F);
                GlStateManager.matrixMode(5888);
                GlStateManager.enableBlend();
                float f1 = 0.5F;
                GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
                GlStateManager.disableLighting();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
                this.creeperModel.setModelAttributes(this.creeperRenderer.getMainModel());
                Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
                this.creeperModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw,
                        headPitch, scale);
                Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
                GlStateManager.matrixMode(5890);
                GlStateManager.loadIdentity();
                GlStateManager.matrixMode(5888);
                GlStateManager.enableLighting();
                GlStateManager.disableBlend();
                GlStateManager.depthMask(flag);
            }
        }

        @Override
        public boolean shouldCombineTextures() {
            return false;
        }
    }
}
