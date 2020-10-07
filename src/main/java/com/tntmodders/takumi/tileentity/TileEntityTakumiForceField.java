package com.tntmodders.takumi.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;

public class TileEntityTakumiForceField extends TileEntity implements ITickable {
    private int ticksExisted;
    private int maxTick = 12000;

    public TileEntityTakumiForceField() {
        super();
    }

    @Override
    public void update() {
        this.ticksExisted++;
        if (!this.world.isRemote && this.ticksExisted > maxTick) {
            this.world.setBlockToAir(this.getPos());
            this.world.removeTileEntity(this.getPos());
            this.world.createExplosion(null, this.getPos().getX() + 0.5, this.getPos().getY() + 0.5, this.getPos().getZ() + 0.5, 0f, false);
        }
        if (this.getTicksExisted() % 5 == 0) {
            for (double x = -10; x <= 10; x += 0.5) {
                for (double z = -10; z <= 10; z += 0.5) {
                    for (double y = -10; y <= 10; y += 0.5) {
                        if (x * x + z * z + y * y <= 100 && x * x + z * z + y * y >= 9.5 * 9.5) {
                            double rx = (this.world.rand.nextGaussian() - 0.5) / 4;
                            double ry = (this.world.rand.nextGaussian() - 0.5) / 4;
                            double rz = (this.world.rand.nextGaussian() - 0.5) / 4;
                            this.world.spawnAlwaysVisibleParticle(EnumParticleTypes.ENCHANTMENT_TABLE.getParticleID(),
                                    this.getPos().getX() + x + rx, this.getPos().getY() + y + ry, this.getPos().getZ() + z + rz, 0, -0.01, 0);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.ticksExisted = compound.getInteger("ticksExisted");
        this.maxTick = compound.getInteger("maxTick");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("ticksExisted", this.ticksExisted);
        compound.setInteger("maxTick", this.maxTick);
        return super.writeToNBT(compound);
    }

    public int getTicksExisted() {
        return ticksExisted;
    }
}
