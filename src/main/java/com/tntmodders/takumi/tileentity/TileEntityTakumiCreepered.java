package com.tntmodders.takumi.tileentity;

import net.minecraft.nbt.NBTTagCompound;

public class TileEntityTakumiCreepered extends TileEntityTakumiSuperPowered {

    private int tick;
    private int lastTick;
    private int fuseTime = 120;

    public TileEntityTakumiCreepered() {
        super();
    }

    public float getCreeperFlashIntensity(float pertialTicks) {
        return (this.lastTick + (this.tick - this.lastTick) * pertialTicks) / this.fuseTime;
    }

    @Override
    public void update() {
        if (this.tick == 40) {
            this.world.addBlockEvent(this.getPos(), this.getBlockType(), 1, 0);
        }
        this.lastTick = this.tick;
        this.tick++;
        if (this.tick > this.fuseTime) {
            if (!this.world.isRemote) {
                this.world.createExplosion(null, this.getPos().getX(), this.getPos().getY() + 1, this.getPos().getZ(),
                        6, true);
            }
            this.world.setBlockToAir(this.pos);
        }
    }


    @Override
    public void readFromNBT(NBTTagCompound compound) {
        this.tick = compound.getInteger("tick");
        this.fuseTime = compound.getInteger("fuseTime");
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("tick", this.tick);
        compound.setInteger("fuseTime", this.fuseTime);
        return super.writeToNBT(compound);
    }
}
