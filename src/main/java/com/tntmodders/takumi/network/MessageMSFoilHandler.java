package com.tntmodders.takumi.network;

import com.tntmodders.takumi.entity.item.EntityXMS;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageMSFoilHandler implements IMessageHandler<MessageMSFoil, IMessage> {

    @Override
    public IMessage onMessage(MessageMSFoil message, MessageContext ctx) {
        if (FMLCommonHandler.instance().getSide().isClient()) {
            onMessageProxy(message, ctx);
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    private void onMessageProxy(MessageMSFoil message, MessageContext ctx) {
        Entity entity = Minecraft.getMinecraft().world.getEntityByID(message.id);
        if (entity instanceof EntityXMS) {
            ((EntityXMS) entity).setAttackMode(message.isAttack);
        }
    }
}
