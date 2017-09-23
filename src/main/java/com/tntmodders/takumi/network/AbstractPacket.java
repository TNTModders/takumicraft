package com.tntmodders.takumi.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public abstract class AbstractPacket implements IMessage, IMessageHandler<AbstractPacket, IMessage> {
    @Override
    public void fromBytes(ByteBuf buf) {
        this.decodeInto(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        this.encodeInto(buf);
    }

    public abstract void encodeInto(ByteBuf buffer);

    public abstract void decodeInto(ByteBuf buffer);

    @Override
    public IMessage onMessage(AbstractPacket message, MessageContext ctx) {
        EntityPlayer player;
        IMessage reply = null;
        switch (FMLCommonHandler.instance().getEffectiveSide()) {
            case CLIENT:
                if (message instanceof MessageToClient) {
                    player = this.getClientPlayer();
                    reply = ((MessageToClient) message).handleClientSide(player);
                }
                break;

            case SERVER:
                if (message instanceof MessageToServer) {
                    player = ((NetHandlerPlayServer) ctx.netHandler).player;
                    reply = ((MessageToServer) message).handleServerSide(player);
                }
                break;

            default:
        }
        return reply;
    }

    @SideOnly(Side.CLIENT)
    public EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().player;
    }
}