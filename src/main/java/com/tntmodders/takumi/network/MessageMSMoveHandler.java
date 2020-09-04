package com.tntmodders.takumi.network;

import com.tntmodders.takumi.core.TakumiPacketCore;
import com.tntmodders.takumi.entity.item.EntityXMS;
import com.tntmodders.takumi.entity.item.EntityYMS;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageMSMoveHandler implements IMessageHandler<MessageMSMove, IMessage> {

    @Override
    public IMessage onMessage(MessageMSMove message, MessageContext ctx) {
        EntityPlayer entityPlayer = ctx.getServerHandler().player;
        switch (message.key) {
            case 0: {
                if (entityPlayer.getRidingEntity() instanceof EntityXMS ||
                        entityPlayer.getRidingEntity() instanceof EntityYMS) {
                    entityPlayer.getRidingEntity().move(MoverType.PLAYER, entityPlayer.getLookVec().x * 2,
                            entityPlayer.getLookVec().y / 1.5f, entityPlayer.getLookVec().z * 2);
                }
                break;
            }
            case 1: {
                if (entityPlayer.getRidingEntity() instanceof EntityXMS) {
                    entityPlayer.getRidingEntity().move(MoverType.PLAYER, entityPlayer.getLookVec().x,
                            entityPlayer.getLookVec().y, entityPlayer.getLookVec().z);
                }
                break;
            }
            case 2: {
                if (entityPlayer.getRidingEntity() instanceof EntityXMS) {
                    ((EntityXMS) entityPlayer.getRidingEntity()).setAttackMode(true);
                    TakumiPacketCore.INSTANCE.sendToAll(new MessageMSFoil(entityPlayer.getRidingEntity().getEntityId(), true));
                }
                break;
            }
            case 3: {
                if (entityPlayer.getRidingEntity() instanceof EntityXMS) {
                    ((EntityXMS) entityPlayer.getRidingEntity()).setAttackMode(false);
                    TakumiPacketCore.INSTANCE.sendToAll(new MessageMSFoil(entityPlayer.getRidingEntity().getEntityId(), false));
                }
                break;
            }
        }
        return null;
    }
}
