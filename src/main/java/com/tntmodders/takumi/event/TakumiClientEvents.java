package com.tntmodders.takumi.event;

import com.google.common.collect.Lists;
import com.tntmodders.asm.TakumiASMNameMap;
import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.block.BlockTakumiRedstoneWire;
import com.tntmodders.takumi.client.render.sp.RenderEntityLivingSP;
import com.tntmodders.takumi.client.render.sp.RenderPlayerSP;
import com.tntmodders.takumi.client.render.sp.RenderPlayerTHM;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.core.TakumiConfigCore;
import com.tntmodders.takumi.core.TakumiPacketCore;
import com.tntmodders.takumi.core.TakumiPotionCore;
import com.tntmodders.takumi.core.client.TakumiClientCore;
import com.tntmodders.takumi.entity.item.EntityTakumiBoat;
import com.tntmodders.takumi.entity.item.EntityXMS;
import com.tntmodders.takumi.item.ItemBattleArmor;
import com.tntmodders.takumi.network.MessageMSMove;
import com.tntmodders.takumi.network.MessageTHMDetonate;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelShield;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;

@SideOnly(Side.CLIENT)
public class TakumiClientEvents {

    @SideOnly(Side.CLIENT)
    public static final ModelShield MODEL_SHIELD = new ModelShield();

    @SideOnly(Side.CLIENT)
    public static final ModelSaber MODEL_LIGHTSABER = new ModelSaber();

    /*    @SubscribeEvent
        public void recievedChat(ClientChatReceivedEvent event) {
            if (Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode().equalsIgnoreCase("ja_jp")) {
                ITextComponent component = event.getMessage();
                String[] creepers = event.getMessage().getUnformattedText().split(" ");
                String creeper = "";
                TakumiCraftCore.LOGGER.info(Arrays.toString(creepers));
                for (int i = 0; i < creepers.length; i++) {
                    String str = creepers[i];
                    if (str.equalsIgnoreCase("creeper")) {
                        creeper = creepers[i - 1] +" "+ creepers[i];
                    }
                }
                if (!creeper.isEmpty() && component.toString().contains("death")) {
                    TakumiCraftCore.LOGGER.info(creeper);
                    String creeperJP = TakumiUtils.takumiTranslate("entity." + creeper.toLowerCase().replaceAll(" ","") + ".name");
                    String string = component.getUnformattedText().replace(creeper, creeperJP);
                    component = new TextComponentString(string);
                    event.setMessage(component);
                }
            }
        }*/

    private static long respawnTime;
    private static long prevRespawnTime;

    @SubscribeEvent
    public void guiScreenEvent(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (event.getGui() instanceof GuiGameOver) {
            try {
                if ((event.getGui().mc.getCurrentServerData() != null && event.getGui().mc.getCurrentServerData().serverMOTD.contains(":tc_server")) || TakumiConfigCore.inEventServerClient) {
                    if (event.getGui().mc.world != null && event.getGui().mc.player != null) {
                        if (event.getGui().mc.player.deathTime < 3) {
                            TakumiClientEvents.respawnTime = event.getGui().mc.world.getTotalWorldTime();
                            TakumiClientEvents.prevRespawnTime = TakumiClientEvents.respawnTime;
                        } else {
                            TakumiClientEvents.respawnTime = event.getGui().mc.world.getTotalWorldTime();
                            long time = respawnTime - prevRespawnTime;
                            event.getGui().drawCenteredString(event.getGui().mc.fontRenderer, "リスポーンまで: " + Math.max(0, 5 - Math.round(time / 20)), event.getGui().width / 2, 120, 16777215);
                            if (time > 100) {
                                event.getGui().mc.player.respawnPlayer();
                                event.getGui().mc.displayGuiScreen(null);
                            }
                        }
                    }
                    Field field = TakumiASMNameMap.getField(GuiGameOver.class, "enableButtonsTimer");
                    field.setAccessible(true);
                    int timer = (int) field.get(event.getGui());
                    if (timer > 100) {
                        event.getGui().mc.player.respawnPlayer();
                        event.getGui().mc.displayGuiScreen(null);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SubscribeEvent
    public void color(ColorHandlerEvent.Block event) {
        event.getBlockColors().registerBlockColorHandler((state, worldIn, pos, tintIndex) -> BlockTakumiRedstoneWire.colorMultiplier(state.getValue(BlockTakumiRedstoneWire.POWER)),
                TakumiBlockCore.CREEPER_REDSTONE_WIRE);
    }

    @SubscribeEvent
    public void renderWorld(CameraSetup event) {
        if (FMLCommonHandler.instance().getSide().isClient() &&
                Minecraft.getMinecraft().player.isPotionActive(TakumiPotionCore.INVERSION)) {
            GlStateManager.rotate(180, 0, 0, 1);
        }
    }

    @SubscribeEvent
    public void renderPlayerPost(RenderLivingEvent.Post event) {
        if (Lists.newArrayList(event.getEntity().getArmorInventoryList()).stream().allMatch(
                itemStack -> itemStack.getItem() instanceof ItemBattleArmor &&
                        ((ItemBattleArmor) itemStack.getItem()).isPowered)) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(TakumiClientEvents.ModelSaber.SABER_TEXTURE);
            GlStateManager.pushMatrix();
            GlStateManager.translate(event.getX(), event.getY(), event.getZ());
            GlStateManager.depthMask(true);
            GlStateManager.scale(1.0F, -1.0F, -1.0F);
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
            GlStateManager.translate(0, -1.45, 0);
            GlStateManager.scale(1.175, 1.175, 1.175);
            GlStateManager.rotate(event.getEntity().rotationYaw, 0, 1, 0);
            event.getRenderer().getMainModel().render(event.getEntity(), event.getEntity().limbSwing,
                    event.getEntity().limbSwingAmount, event.getPartialRenderTick(), 0, event.getEntity().rotationPitch,
                    0.0625f);
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

    protected float interpolateRotation(float prevYawOffset, float yawOffset, float partialTicks) {
        float f;
        for (f = yawOffset - prevYawOffset; f < -180.0F; f += 360.0F) {
        }
        while (f >= 180.0F) {
            f -= 360.0F;
        }
        return prevYawOffset + partialTicks * f;
    }

    @SubscribeEvent
    public void renderPlayer(RenderLivingEvent.Pre event) {
        if (TakumiUtils.isApril(Minecraft.getMinecraft().world)) {
            if ((event.getRenderer() instanceof RenderPlayer && !(event.getRenderer() instanceof RenderPlayerSP)) &&
                    event.getEntity() instanceof AbstractClientPlayer) {
                event.setCanceled(true);
                RenderPlayerSP sp = new RenderPlayerSP(event.getRenderer().getRenderManager());
                sp.doRender(((AbstractClientPlayer) event.getEntity()), event.getX(), event.getY(), event.getZ(),
                        ((AbstractClientPlayer) event.getEntity()).rotationYaw, event.getPartialRenderTick());
            } else if (event.getEntity() instanceof EntityLivingBase && event.getRenderer() instanceof RenderLivingBase && !(event.getRenderer() instanceof RenderEntityLivingSP)) {
                event.getEntity().setCustomNameTag("匠");
                event.setCanceled(true);
                RenderEntityLivingSP sp = new RenderEntityLivingSP(event.getRenderer().getRenderManager());
                sp.doRender(event.getEntity(), event.getX(), event.getY(), event.getZ(),
                        (event.getEntity()).rotationYaw, event.getPartialRenderTick());
            }
        }
        if (event.getEntity().world.getDifficulty() == EnumDifficulty.HARD && TakumiConfigCore.TakumiHard &&
                (event.getRenderer() instanceof RenderPlayer && !(event.getRenderer() instanceof RenderPlayerSP)) &&
                event.getEntity() instanceof AbstractClientPlayer) {
            event.setCanceled(true);
            RenderPlayerTHM sp = new RenderPlayerTHM(event.getRenderer().getRenderManager());
            sp.doRender(((AbstractClientPlayer) event.getEntity()), event.getX(), event.getY(), event.getZ(),
                    ((AbstractClientPlayer) event.getEntity()).rotationYaw, event.getPartialRenderTick());
        }
        if (event.getEntity() instanceof EntityPlayer && event.getEntity().isPotionActive(TakumiPotionCore.INVERSION)) {
            GlStateManager.popMatrix();
            GlStateManager.rotate(180, 1, 0, 0);
            GlStateManager.translate(0, -1.9, 0);
        }
        if (event.getEntity().isPotionActive(TakumiPotionCore.CREEPERED)) {
            GlStateManager.popMatrix();
            float f = this.getCreeperFlashIntensity(event.getEntity(), event.getPartialRenderTick());
            if (f > 0) {
                float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
                f = MathHelper.clamp(f, 0.0F, 1.0F);
                f = f * f;
                f = f * f;
                float f2 = (1.0F + f * 0.4F) * f1;
                float f3 = (1.0F + f * 0.1F) / f1;
                GlStateManager.scale(f2, f3, f2);
            }
        }
        if (event.getEntity().getRidingEntity() instanceof EntityXMS) {
            event.setCanceled(true);
        }
        if (event.getEntity().getRidingEntity() instanceof EntityTakumiBoat) {
            event.getEntity().extinguish();
        }
    }

    public float getCreeperFlashIntensity(EntityLivingBase entityLivingBase, float partialTicks) {
        if (entityLivingBase.getActivePotionEffect(TakumiPotionCore.CREEPERED) == null ||
                entityLivingBase.getActivePotionEffect(TakumiPotionCore.CREEPERED).getDuration() > 30) {
            return 0;
        }
        return (30 - (float) entityLivingBase.getActivePotionEffect(TakumiPotionCore.CREEPERED).getDuration() +
                partialTicks) / 28f;
    }

    protected int getColorMultiplier(EntityLivingBase entitylivingbaseIn, float lightBrightness,
                                     float partialTickTime) {
        float f = this.getCreeperFlashIntensity(entitylivingbaseIn, partialTickTime);

        if ((int) (f * 10.0F) % 2 == 0) {
            return 0;
        } else {
            int i = (int) (f * 0.2F * 255.0F);
            i = MathHelper.clamp(i, 0, 255);
            return i << 24 | 822083583;
        }
    }

    @SubscribeEvent
    public void renderHand(RenderHandEvent event) {
        if (TakumiUtils.isApril(Minecraft.getMinecraft().world)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void renderPlayer(RenderLivingEvent.Post event) {
        if (event.getEntity() instanceof EntityPlayer && event.getEntity().isPotionActive(TakumiPotionCore.INVERSION)) {
            GlStateManager.translate(0, 1.9, 0);
            GlStateManager.rotate(-180, 1, 0, 0);
            GlStateManager.pushMatrix();
        }

        if (event.getEntity().isPotionActive(TakumiPotionCore.CREEPERED)) {
            float f = this.getCreeperFlashIntensity(event.getEntity(), event.getPartialRenderTick());
            if (f > 0) {
                float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
                f = MathHelper.clamp(f, 0.0F, 1.0F);
                f = f * f;
                f = f * f;
                float f2 = (1.0F + f * 0.4F) * f1;
                float f3 = (1.0F + f * 0.1F) / f1;
                GlStateManager.scale(1 / f2, 1 / f3, 1 / f2);
            }
            GlStateManager.pushMatrix();
        }
    }

    @SubscribeEvent
    public void onKeyPressed(KeyInputEvent event) {
        if (TakumiClientCore.keyBindingTakumiBook.isPressed()) {
            EntityPlayer playerIn = Minecraft.getMinecraft().player;
            playerIn.openGui(TakumiCraftCore.TakumiInstance, 0, playerIn.world, (int) playerIn.posX,
                    (int) playerIn.posY, (int) playerIn.posZ);
        } else if (TakumiClientCore.keyBindingYMS.isPressed() &&
                Minecraft.getMinecraft().player.getRidingEntity() instanceof EntityXMS) {
            boolean flg = ((EntityXMS) Minecraft.getMinecraft().player.getRidingEntity()).isAttackMode;
            ((EntityXMS) Minecraft.getMinecraft().player.getRidingEntity()).isAttackMode = !flg;
            TakumiPacketCore.INSTANCE.sendToServer(new MessageMSMove((byte) 2));
        } else if (TakumiClientCore.keyBindingTHMDetonate.isPressed() && TakumiConfigCore.TakumiHard &&
                Minecraft.getMinecraft().world.getDifficulty() == EnumDifficulty.HARD) {
            TakumiPacketCore.INSTANCE.sendToServer(new MessageTHMDetonate((byte) 1));
            Minecraft.getMinecraft().player.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0F, 0.5F);
        }
    }

    @SubscribeEvent
    public void renderFire(RenderBlockOverlayEvent event) {
        if (event.getPlayer() != null && event.getPlayer().getRidingEntity() instanceof EntityTakumiBoat && event.getBlockForOverlay().getBlock() == Blocks.FIRE &&
                event.getBlockPos().equals(new BlockPos(event.getPlayer()))) {
            event.setCanceled(true);
        }
    }

    public static class ModelSaber extends ModelBase {
        public static final ResourceLocation HANDLE_TEXTURE =
                new ResourceLocation(TakumiCraftCore.MODID, "textures/blocks/creeperiron.png");
        public static final ResourceLocation SABER_TEXTURE =
                new ResourceLocation("textures/entity/creeper/creeper_armor.png");
        public ModelRenderer handle;
        public ModelRenderer saber;

        public ModelSaber() {
            this.handle = new ModelRenderer(this, 0, 0);
            this.handle.addBox(-1.0F, -3.0F, 1F, 3, 6, 3, 0.0F);
            this.saber = new ModelRenderer(this, 0, 0);
            this.saber.addBox(-1F, -36.0F, 3F, 4, 30, 4, 0.0F);
        }

        public void renderHandle() {
            //this.plate.render(0.0625F);
            this.handle.render(0.0625F);
        }

        public void renderSaber() {
            //this.plate.render(0.0625F);
            this.saber.render(0.0625F);
        }

    }
}
