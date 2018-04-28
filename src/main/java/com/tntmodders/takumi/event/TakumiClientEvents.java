package com.tntmodders.takumi.event;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.client.render.sp.RenderPlayerSP;
import com.tntmodders.takumi.core.TakumiPotionCore;
import com.tntmodders.takumi.core.client.TakumiClientCore;
import com.tntmodders.takumi.entity.item.EntityXMS;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelShield;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TakumiClientEvents {

    @SideOnly(Side.CLIENT)
    public static final ModelShield MODEL_SHIELD = new ModelShield();

    @SubscribeEvent
    public void renderWorld(CameraSetup event) {
        if (FMLCommonHandler.instance().getSide().isClient() &&
                Minecraft.getMinecraft().player.isPotionActive(TakumiPotionCore.INVERSION)) {
            GlStateManager.rotate(180, 0, 0, 1);
        }
    }

    @SubscribeEvent
    public void renderPlayer(RenderLivingEvent.Pre event) {
        if (TakumiUtils.isApril(Minecraft.getMinecraft().world) &&
                (event.getRenderer() instanceof RenderPlayer && !(event.getRenderer() instanceof RenderPlayerSP)) &&
                event.getEntity() instanceof AbstractClientPlayer) {
            event.setCanceled(true);
            RenderPlayerSP sp = new RenderPlayerSP(event.getRenderer().getRenderManager());
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
        }
    }
}
