package com.tntmodders.takumi.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageTakumiCannon implements IMessage {
    private int entityid;
    private byte facing;

    public MessageTakumiCannon() {
        super();
    }

    public MessageTakumiCannon(int entityidIn, byte facingIn) {
        this.entityid = entityidIn;
        this.facing = facingIn;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityid = buf.readInt();
        this.facing = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityid);
        buf.writeByte(this.facing);
    }

    public int getEntityid() {
        return this.entityid;
    }

    public byte getFacing() {
        return this.facing;
    }
}
