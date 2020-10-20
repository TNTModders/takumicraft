package com.tntmodders.takumi.network;

import com.tntmodders.takumi.entity.mobs.boss.EntityKingCreeper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageTakumiBossAttackIDHandler implements IMessageHandler<MessageTakumiBossAttackID, IMessage> {
    @Override
    public IMessage onMessage(MessageTakumiBossAttackID message, MessageContext ctx) {
        if (FMLCommonHandler.instance().getSide().isClient()) {
            this.onMessageProxy(message);
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    private void onMessageProxy(MessageTakumiBossAttackID message) {
        Entity entity = Minecraft.getMinecraft().world.getEntityByID(message.getEntityID());
        if (entity instanceof EntityKingCreeper) {
            ((EntityKingCreeper) entity).setAttackID(message.getAttackID());
        }
    }
}
