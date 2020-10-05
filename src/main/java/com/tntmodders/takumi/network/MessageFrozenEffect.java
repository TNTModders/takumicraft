package com.tntmodders.takumi.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageFrozenEffect implements IMessage {
    private int entityId;
    private byte effectId;
    private byte amplifier;
    private int duration;
    private byte flags;
    private boolean clear;

    public MessageFrozenEffect() {
    }

    public MessageFrozenEffect(int entityIdIn, PotionEffect effect, boolean flg) {
        this.entityId = entityIdIn;
        this.effectId = (byte) (Potion.getIdFromPotion(effect.getPotion()) & 255);
        this.amplifier = (byte) (effect.getAmplifier() & 255);

        this.duration = Math.min(effect.getDuration(), 32767);

        this.flags = 0;

        if (effect.getIsAmbient()) {
            this.flags = (byte) (this.flags | 1);
        }

        if (effect.doesShowParticles()) {
            this.flags = (byte) (this.flags | 2);
        }
        this.clear = flg;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityId = buf.readInt();
        this.effectId = buf.readByte();
        this.amplifier = buf.readByte();
        this.duration = buf.readInt();
        this.flags = buf.readByte();
        this.clear = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeByte(this.effectId);
        buf.writeByte(this.amplifier);
        buf.writeInt(this.duration);
        buf.writeByte(this.flags);
        buf.writeBoolean(this.clear);
    }

    @SideOnly(Side.CLIENT)
    public boolean isMaxDuration() {
        return this.duration == 32767;
    }

    @SideOnly(Side.CLIENT)
    public int getEntityId() {
        return this.entityId;
    }

    @SideOnly(Side.CLIENT)
    public byte getEffectId() {
        return this.effectId;
    }

    @SideOnly(Side.CLIENT)
    public byte getAmplifier() {
        return this.amplifier;
    }

    @SideOnly(Side.CLIENT)
    public int getDuration() {
        return this.duration;
    }

    @SideOnly(Side.CLIENT)
    public boolean doesShowParticles() {
        return (this.flags & 2) == 2;
    }

    @SideOnly(Side.CLIENT)
    public boolean getIsAmbient() {
        return (this.flags & 1) == 1;
    }

    @SideOnly(Side.CLIENT)
    public boolean getIsClear() {
        return this.clear;
    }
}
