package com.tntmodders.takumi.network;

import com.tntmodders.takumi.core.TakumiConfigCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageTHMDetonateHandler implements IMessageHandler<MessageTHMDetonate, IMessage> {

    @Override
    public IMessage onMessage(MessageTHMDetonate message, MessageContext ctx) {
        EntityPlayer entityPlayer = ctx.getServerHandler().player;
        if (message.key == 1 && TakumiConfigCore.TakumiHard) {
            entityPlayer.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 30, 334));
            entityPlayer.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0F, 0.5F);
        }
        return null;
    }
}
