package com.tntmodders.takumi.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityTakumiBlock extends TileEntity {
    
    public IBlockState state;
    String location;
    Block block;
    int meta;
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.setPath(compound.getString("location"));
        this.setMeta(compound.getInteger("meta"));
    }
    
    public void setPath(String path) {
        this.location = path;
        this.block = Block.getBlockFromName(this.location);
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (this.location != null) {
            compound.setString("location", this.location);
        }
        compound.setInteger("meta", this.meta);
        return compound;
    }
    
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
    }
    
    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }
    
    public Block getBlock() {
        return block;
    }
    
    public int getMeta() {
        return meta;
    }
    
    public void setMeta(int meta) {
        
        this.meta = meta;
    }
}
