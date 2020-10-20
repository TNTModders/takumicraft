package com.tntmodders.takumi.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageTakumiBossAttackID implements IMessage {
    private int entityID;
    private int attackID;

    public MessageTakumiBossAttackID() {
    }

    public MessageTakumiBossAttackID(int entityIDIn, int attackIDIn) {
        this.entityID = entityIDIn;
        this.attackID = attackIDIn;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityID = buf.readInt();
        this.attackID = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityID);
        buf.writeByte(this.attackID);
    }

    public int getEntityID() {
        return entityID;
    }

    public int getAttackID() {
        return attackID;
    }
}
