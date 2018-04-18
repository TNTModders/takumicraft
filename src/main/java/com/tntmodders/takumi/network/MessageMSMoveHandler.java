package com.tntmodders.takumi.network;

import com.tntmodders.takumi.entity.item.EntityXMS;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageMSMoveHandler implements IMessageHandler<MessageMSMove, IMessage> {

    @Override
    public IMessage onMessage(MessageMSMove message, MessageContext ctx) {
        EntityPlayer entityPlayer = ctx.getServerHandler().player;
        if (entityPlayer.getRidingEntity() instanceof EntityXMS) {
            switch (message.key) {
                case 1: {
                    entityPlayer.getRidingEntity().move(MoverType.PLAYER, entityPlayer.getLookVec().x,
                            entityPlayer.getLookVec().y, entityPlayer.getLookVec().z);
                    break;
                }
            }
        } return null;
    }
}
