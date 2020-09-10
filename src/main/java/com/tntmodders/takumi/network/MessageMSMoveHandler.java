package com.tntmodders.takumi.network;

import com.tntmodders.takumi.core.TakumiPacketCore;
import com.tntmodders.takumi.entity.item.EntityXMS;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageMSMoveHandler implements IMessageHandler<MessageMSMove, IMessage> {

    @Override
    public IMessage onMessage(MessageMSMove message, MessageContext ctx) {
        EntityPlayer entityPlayer = ctx.getServerHandler().player;
        if(entityPlayer != null && entityPlayer.getRidingEntity() != null){
            switch (message.key) {
                case 0: {
                    //attack mode
                    if (entityPlayer.getRidingEntity() instanceof EntityXMS) {
                        entityPlayer.getRidingEntity().motionX = MathHelper.sin(-entityPlayer.getRidingEntity().rotationYaw * 0.017453292F) * 2;
                        entityPlayer.getRidingEntity().motionY = MathHelper.sin(-entityPlayer.getRidingEntity().rotationPitch * 0.017453292F) * 1.25;
                        entityPlayer.getRidingEntity().motionZ = MathHelper.cos(entityPlayer.getRidingEntity().rotationYaw * 0.017453292F) * 2;
                    } else {
                        entityPlayer.getRidingEntity().motionX = MathHelper.sin(-entityPlayer.getRidingEntity().rotationYaw * 0.017453292F) * 2;
                        entityPlayer.getRidingEntity().motionY = MathHelper.sin(-entityPlayer.getRidingEntity().rotationPitch * 0.017453292F) * 2;
                        entityPlayer.getRidingEntity().motionZ = MathHelper.cos(entityPlayer.getRidingEntity().rotationYaw * 0.017453292F) * 2;
                    }
                    entityPlayer.move(MoverType.PLAYER, entityPlayer.getRidingEntity().motionX, entityPlayer.getRidingEntity().motionY, entityPlayer.getRidingEntity().motionZ);
                    break;
                }
                case 1: {
                    if (entityPlayer.getRidingEntity() instanceof EntityXMS) {
                        entityPlayer.getRidingEntity().motionX = MathHelper.sin(-entityPlayer.getRidingEntity().rotationYaw * 0.017453292F) * 3;
                        entityPlayer.getRidingEntity().motionY = MathHelper.sin(-entityPlayer.getRidingEntity().rotationPitch * 0.017453292F) * 3;
                        entityPlayer.getRidingEntity().motionZ = MathHelper.cos(entityPlayer.getRidingEntity().rotationYaw * 0.017453292F) * 3;
                    }
                    entityPlayer.move(MoverType.PLAYER, entityPlayer.getRidingEntity().motionX, entityPlayer.getRidingEntity().motionY, entityPlayer.getRidingEntity().motionZ);
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
        }
        return null;
    }
}
