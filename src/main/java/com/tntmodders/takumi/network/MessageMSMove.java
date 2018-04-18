package com.tntmodders.takumi.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageMSMove implements IMessage {
    //0:none 1:forward
    public byte key;

    public MessageMSMove() {
    }

    public MessageMSMove(byte keyPressed) {
        this.key = keyPressed;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.key = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(this.key);
    }
}
