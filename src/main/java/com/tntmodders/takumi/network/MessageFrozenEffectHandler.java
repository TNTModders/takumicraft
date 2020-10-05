package com.tntmodders.takumi.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageFrozenEffectHandler implements IMessageHandler<MessageFrozenEffect, IMessage> {

    @Override
    public IMessage onMessage(MessageFrozenEffect message, MessageContext ctx) {
        if (FMLCommonHandler.instance().getSide().isClient()) {
            this.onMessageProxy(message);
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    private void onMessageProxy(MessageFrozenEffect message) {
        Entity entity = Minecraft.getMinecraft().world.getEntityByID(message.getEntityId());
        if (message.getIsClear()) {
            if (entity instanceof EntityLivingBase) {
                ((EntityLivingBase) entity).removeActivePotionEffect(Potion.getPotionById(message.getEffectId()));
                entity.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1f, 1f);
            }
        } else {

            if (entity instanceof EntityLivingBase) {
                Potion potion = Potion.getPotionById(message.getEffectId() & 0xFF);

                if (potion != null) {
                    PotionEffect potioneffect = new PotionEffect(potion, message.getDuration(), message.getAmplifier(), message.getIsAmbient(), message.doesShowParticles());
                    potioneffect.setPotionDurationMax(message.isMaxDuration());
                    ((EntityLivingBase) entity).addPotionEffect(potioneffect);
                }
            }
        }
    }
}
