package com.tntmodders.asm;

import com.tntmodders.takumi.block.BlockTakumiMonsterBomb;
import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.ITakumiEntity;
import com.tntmodders.takumi.event.TakumiClientEvents;
import com.tntmodders.takumi.item.ItemTakumiShield;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Field;

public class TakumiASMHooks {
    
    public static void TakumiExplodeHook(EntityCreeper creeper) {
        try {
            if (creeper instanceof EntityTakumiAbstractCreeper) {
                int i = ((ITakumiEntity) creeper).getExplosionPower();
                Field field = TakumiASMNameMap.getField(EntityCreeper.class, "explosionRadius");
                field.setAccessible(true);
                field.set(creeper, i); ((ITakumiEntity) creeper).takumiExplode();
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
    
    public static void TakumiRenderByItemHook(ItemStack itemStack) {
        if (itemStack.getItem() instanceof ItemBlock && Block.getBlockFromItem(itemStack.getItem()) instanceof BlockTakumiMonsterBomb) {
            GlStateManager.pushMatrix(); GlStateManager.disableCull();
            TileEntityRendererDispatcher.instance.render(((BlockTakumiMonsterBomb) Block.getBlockFromItem(itemStack.getItem()))
                    .tileEntityMonsterBomb, 0.0D, 0.0D, 0.0D, 0.0F, 1.0f); GlStateManager.enableCull(); GlStateManager.popMatrix();
        } else if (itemStack.getItem() == TakumiItemCore.TAKUMI_SHIELD) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(ItemTakumiShield.SHIELD_TEXTURE); GlStateManager.pushMatrix();
            GlStateManager.scale(1.0F, -1.0F, -1.0F); TakumiClientEvents.MODEL_SHIELD.render(); GlStateManager.popMatrix();
        }
    }
}
