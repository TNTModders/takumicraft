package com.tntmodders.asm;

import com.tntmodders.takumi.block.BlockTakumiMonsterBomb;
import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.ITakumiEntity;
import com.tntmodders.takumi.event.TakumiClientEvents;
import com.tntmodders.takumi.item.ItemBattleShield;
import com.tntmodders.takumi.item.ItemTakumiShield;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;

public class TakumiASMHooks {

    public static void TakumiExplodeHook(EntityCreeper creeper) {
        try {
            if (creeper instanceof EntityTakumiAbstractCreeper) {
                int i = ((ITakumiEntity) creeper).getExplosionPower();
                Field field = TakumiASMNameMap.getField(EntityCreeper.class, "explosionRadius");
                field.setAccessible(true);
                field.set(creeper, i);
                ((ITakumiEntity) creeper).takumiExplode();
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void TakumiRenderByItemHook(ItemStack itemStack) {
        if (itemStack.getItem() instanceof ItemBlock &&
                Block.getBlockFromItem(itemStack.getItem()) instanceof BlockTakumiMonsterBomb) {
            GlStateManager.pushMatrix();
            GlStateManager.disableCull();
            TileEntityRendererDispatcher.instance.render(
                    ((BlockTakumiMonsterBomb) Block.getBlockFromItem(itemStack.getItem())).tileEntityMonsterBomb, 0.0D,
                    0.0D, 0.0D, 0.0F, 1.0f);
            GlStateManager.enableCull();
            GlStateManager.popMatrix();
        } else if (itemStack.getItem() == TakumiItemCore.TAKUMI_SHIELD) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(ItemTakumiShield.SHIELD_TEXTURE);
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0F, -1.0F, -1.0F);
            TakumiClientEvents.MODEL_SHIELD.render();
            GlStateManager.popMatrix();
        } else if (itemStack.getItem() == TakumiItemCore.BATTLE_SHIELD_POWERED) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(ItemBattleShield.SHIELD_TEXTURE);
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0F, -1.0F, -1.0F);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            TakumiClientEvents.MODEL_SHIELD.render();
            GlStateManager.popMatrix();

            Minecraft.getMinecraft().getTextureManager().bindTexture(TakumiClientEvents.ModelSaber.SABER_TEXTURE);
            GlStateManager.pushMatrix();
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            float f = Minecraft.getMinecraft().player.ticksExisted * 2;
            GL11.glTranslated(f * 0.01F, f * 0.01F, 0.0F);
            GlStateManager.matrixMode(5888);
            GlStateManager.enableBlend();
            GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
            GlStateManager.disableLighting();
            int i = 15728880;
            int j = i % 65536;
            int k = i / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            GlStateManager.scale(1.0F, -1.0F, -1.0F);
            GlStateManager.scale(1.2,1.2,1.2);
            TakumiClientEvents.MODEL_SHIELD.render();
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        } else if (itemStack.getItem() == TakumiItemCore.BATTLE_SHIELD) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(ItemBattleShield.SHIELD_TEXTURE);
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0F, -1.0F, -1.0F);
            TakumiClientEvents.MODEL_SHIELD.render();
            GlStateManager.popMatrix();
        } else if (itemStack.getItem() == TakumiItemCore.TAKUMI_TYPE_SWORD_NORMAL) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(TakumiClientEvents.ModelSaber.HANDLE_TEXTURE);
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0F, -1.0F, -1.0F);
            TakumiClientEvents.MODEL_LIGHTSABER.renderHandle();
            GlStateManager.popMatrix();

            Minecraft.getMinecraft().getTextureManager().bindTexture(TakumiClientEvents.ModelSaber.SABER_TEXTURE);
            GlStateManager.pushMatrix();
            //GlStateManager.depthMask(true);
            GlStateManager.scale(1.0F, -1.0F, -1.0F);
            GlStateManager.scale(0.5, 0.5, 0.5);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            float f = Minecraft.getMinecraft().player.ticksExisted * 2;
            GL11.glTranslated(f * 0.01F, f * 0.01F, 0.0F);
            GlStateManager.matrixMode(5888);
            GlStateManager.enableBlend();
            GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
            GlStateManager.disableLighting();
            int i = 15728880;
            int j = i % 65536;
            int k = i / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            TakumiClientEvents.MODEL_LIGHTSABER.renderSaber();
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            //GlStateManager.depthMask(false);
            GlStateManager.popMatrix();
        }
    }
}
