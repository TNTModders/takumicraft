package com.tntmodders.takumi.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageMSFoil implements IMessage {
    public int id;
    public boolean isAttack;

    public MessageMSFoil() {
    }

    public MessageMSFoil(int id, boolean isAttack) {
        this.id = id;
        this.isAttack = isAttack;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.id = buf.readInt();
        this.isAttack = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.id);
        buf.writeBoolean(this.isAttack);
    }
}
