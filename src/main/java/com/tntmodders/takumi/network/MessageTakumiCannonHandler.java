package com.tntmodders.takumi.network;

import com.tntmodders.takumi.entity.item.EntityTakumiCannon;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageTakumiCannonHandler implements IMessageHandler<MessageTakumiCannon, IMessage> {
    @Override
    public IMessage onMessage(MessageTakumiCannon message, MessageContext ctx) {
        if (FMLCommonHandler.instance().getSide().isClient()) {
            this.onMessageProxy(message);
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    private void onMessageProxy(MessageTakumiCannon message) {
        Entity entity = Minecraft.getMinecraft().world.getEntityByID(message.getEntityid());
        if (entity instanceof EntityTakumiCannon) {
            ((EntityTakumiCannon) entity).setFacing(EnumFacing.getHorizontal(message.getFacing()));
        }
    }
}
