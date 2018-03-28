package com.tntmodders.takumi.event;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.client.render.sp.RenderPlayerSP;
import com.tntmodders.takumi.core.TakumiPotionCore;
import com.tntmodders.takumi.core.client.TakumiClientCore;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelShield;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent.Post;
import net.minecraftforge.client.event.RenderLivingEvent.Pre;
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
    public void renderPlayer(Pre event) {
        if (TakumiUtils.isApril(Minecraft.getMinecraft().world) &&
                (event.getRenderer() instanceof RenderPlayer && !(event.getRenderer() instanceof RenderPlayerSP)) &&
                event.getEntity() instanceof AbstractClientPlayer) {
            event.setCanceled(true);
            RenderPlayerSP sp = new RenderPlayerSP(event.getRenderer().getRenderManager());
            sp.doRender(((AbstractClientPlayer) event.getEntity()), event.getX(), event.getY(), event.getZ(),
                    ((AbstractClientPlayer) event.getEntity()).rotationYaw, event.getPartialRenderTick());
        }
        if (event.getEntity().isPotionActive(TakumiPotionCore.INVERSION)) {
            GlStateManager.popMatrix();
            GlStateManager.rotate(180, 1, 0, 0);
            GlStateManager.translate(0, -1.9, 0);
        }
    }

    @SubscribeEvent
    public void renderHand(RenderHandEvent event) {
        if (TakumiUtils.isApril(Minecraft.getMinecraft().world)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void renderPlayer(Post event) {
        if (event.getEntity().isPotionActive(TakumiPotionCore.INVERSION)) {
            GlStateManager.translate(0, 1.9, 0);
            GlStateManager.rotate(-180, 1, 0, 0);
            GlStateManager.pushMatrix();
        }
    }

    @SubscribeEvent
    public void onKeyPressed(KeyInputEvent event) {
        if (TakumiClientCore.keyBindingTakumiBook.isPressed()) {
            EntityPlayer playerIn = Minecraft.getMinecraft().player;
            playerIn.openGui(TakumiCraftCore.TakumiInstance, 0, playerIn.world, (int) playerIn.posX,
                    (int) playerIn.posY, (int) playerIn.posZ);
        }
    }
}
