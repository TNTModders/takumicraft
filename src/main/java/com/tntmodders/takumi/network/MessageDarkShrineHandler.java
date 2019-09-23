package com.tntmodders.takumi.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageDarkShrineHandler implements IMessageHandler<MessageDarkShrine, IMessage> {

    @Override
    public IMessage onMessage(MessageDarkShrine message, MessageContext ctx) {
        if (FMLCommonHandler.instance().getSide().isClient()) {
            EntityPlayer entityPlayer = Minecraft.getMinecraft().player;
            entityPlayer.playSound(SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE, 1f, 1f);
        }
        return null;
    }
}
