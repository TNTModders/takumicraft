package com.tntmodders.takumi.event;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiPotionCore;
import com.tntmodders.takumi.core.client.TakumiClientCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelShield;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup;
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
        if (FMLCommonHandler.instance().getSide().isClient() && Minecraft.getMinecraft().player.isPotionActive(TakumiPotionCore.INVERSION)) {
            GlStateManager.rotate(180, 0, 0, 1);
        }
    }
    
    @SubscribeEvent
    public void renderPlayer(Pre event) {
        if (event.getEntity().isPotionActive(TakumiPotionCore.INVERSION)) {
            GlStateManager.popMatrix();
            GlStateManager.rotate(180, 1, 0, 0);
            GlStateManager.translate(0, -1.9, 0);
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
            playerIn.openGui(TakumiCraftCore.TakumiInstance, 0, playerIn.world, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
        }
    }
}
