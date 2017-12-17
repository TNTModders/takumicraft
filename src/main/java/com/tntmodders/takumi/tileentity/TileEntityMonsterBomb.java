package com.tntmodders.takumi.tileentity;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class TileEntityMonsterBomb extends TileEntity {
    
    public ResourceLocation location;
    
    public TileEntityMonsterBomb(String name) {
        this();
        this.location = new ResourceLocation(TakumiCraftCore.MODID, name);
    }
    
    public TileEntityMonsterBomb() {
        super();
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        if (this.location == null) {
            this.location = new ResourceLocation(compound.getString("monster"));
        }
        super.readFromNBT(compound);
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setString("monster", this.location.toString());
        return super.writeToNBT(compound);
    }
}
