package com.tntmodders.takumi.tileentity;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityDarkBoard extends TileEntity {
    public String name;

    public TileEntityDarkBoard() {
        super();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (this.name == null || this.name.isEmpty()) {
            this.name = compound.getString("monster");
        }
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        this.readFromNBT(pkt.getNbtCompound());
        TakumiCraftCore.LOGGER.info("pkt");
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        if (this.name != null) {
            compound.setString("monster", this.name);
        }
        return super.writeToNBT(compound);
    }
}
