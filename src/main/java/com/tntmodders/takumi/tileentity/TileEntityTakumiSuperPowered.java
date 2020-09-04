package com.tntmodders.takumi.tileentity;

import com.tntmodders.takumi.core.TakumiBlockCore;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityTakumiSuperPowered extends TileEntity implements ITickable {

    public IBlockState state;
    public String owner;
    String location;
    Block block;
    int meta;

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.setPath(compound.getString("location"));
        this.setMeta(compound.getInteger("meta"));
        if (compound.hasKey("owner") && !compound.getString("owner").isEmpty()) {
            this.owner = compound.getString("owner");
        }
    }

    public void setPath(String path) {
        this.location = path;
        this.block = Block.getBlockFromName(this.location);
        try {
            this.state = this.block.getDefaultState().getActualState(this.world, this.pos);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (this.location != null) {
            compound.setString("location", this.location);
        }
        compound.setInteger("meta", this.meta);
        if (this.owner != null && !this.owner.isEmpty()) {
            compound.setString("owner", this.owner);
        }
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

    public void setState(IBlockState state) {
        this.state = state;
    }

    public void setOwner(EntityPlayer player) {
        this.owner = player.getName();
    }

    public boolean isOwner(EntityPlayer player) {
        return this.owner == null || this.owner.isEmpty() || player.getName().matches(this.owner);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return newSate.getBlock() == Blocks.AIR;
    }

    @Override
    public void update() {
        if (!this.world.isRemote && this.world.getBlockState(pos).getBlock() != TakumiBlockCore.TAKUMI_SUPERPOWERED) {
            IBlockState oldState = this.world.getBlockState(pos);
            this.world.setBlockState(pos, TakumiBlockCore.TAKUMI_SUPERPOWERED.getDefaultState());
            this.setState(oldState);
            this.setMeta(oldState.getBlock().getMetaFromState(oldState));
        }
    }
}
