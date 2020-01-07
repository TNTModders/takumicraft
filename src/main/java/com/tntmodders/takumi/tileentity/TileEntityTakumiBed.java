package com.tntmodders.takumi.tileentity;

import com.tntmodders.takumi.core.TakumiBlockCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityTakumiBed extends TileEntityBed {
    public TileEntityTakumiBed() {
        super();
    }

    private String texture;
    private int meta;

    public String getTexture() {
        return this.texture;
    }

    public int getMeta() {
        return this.meta;
    }

    public void clear() {
        this.texture = null;
        this.meta = 0;
        this.markDirty();
    }

    public void setTexture(String s) {
        this.texture = s;
    }

    public void setMeta(int i) {
        this.meta = i;
    }

    @Override
    public void setItemValues(ItemStack stack) {
        if (stack.getItem() instanceof ItemBlock) {
            Block block = ((ItemBlock) stack.getItem()).getBlock();
            this.texture = block.getRegistryName().toString();
            this.meta = stack.getMetadata();
            this.markDirty();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.texture = compound.getString("texture");
        this.meta = compound.getInteger("meta");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (this.texture != null && !this.texture.isEmpty()) {
            compound.setString("texture", this.texture);
        }
        compound.setInteger("meta", this.meta);
        return compound;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public EnumDyeColor getColor() {
        return EnumDyeColor.GREEN;
    }

    @Override
    public void setColor(EnumDyeColor color) {
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isHeadPiece() {
        return BlockBed.isHeadPiece(this.getBlockMetadata());
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemStack(Item.getItemFromBlock(TakumiBlockCore.CREEPER_BED), 1);
    }
}
