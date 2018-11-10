package com.tntmodders.takumi.tileentity;

import com.tntmodders.takumi.block.BlockTakumiAcid;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityAcidBlock extends TileEntity implements ITickable {

    private int tick;
    private int lastTick;

    public TileEntityAcidBlock() {
        super();
    }

    public float getCreeperFlashIntensity(float pertialTicks) {
        return (this.lastTick + (this.tick - this.lastTick) * pertialTicks) / 60;
    }

    @Override
    public void update() {
        if (this.tick == 30) {
            //this.world.addBlockEvent(this.getPos(), this.getBlockType(), 1, 0);
        }
        this.lastTick = this.tick;
        this.tick++;
        int fuseTime = 40;
        if (!this.world.isRemote && this.tick > fuseTime + this.world.rand.nextInt(50)) {
            boolean smoke = this.world.getBlockState(pos).getValue(BlockTakumiAcid.META) < 15;
            this.world.createExplosion(null, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(),
                    smoke ? 3.375f : 8, smoke);
        } else if (!this.world.isRemote && this.world.rand.nextInt(10) == 0) {
            this.world.createExplosion(null, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), 0,
                    false);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        this.tick = compound.getInteger("tick");
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("tick", this.tick);
        return super.writeToNBT(compound);
    }
}
