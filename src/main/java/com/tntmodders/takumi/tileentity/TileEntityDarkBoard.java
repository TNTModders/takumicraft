package com.tntmodders.takumi.tileentity;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiBlockCore;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.Random;

public class TileEntityDarkBoard extends TileEntity implements ITickable {
    public String name;

    @Override
    public double getMaxRenderDistanceSquared() {
        return super.getMaxRenderDistanceSquared() * 16;
    }

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

    @Override
    public void update() {
        if (FMLCommonHandler.instance().getSide().isClient()) {
            this.randomDisplayTick(this.world.getBlockState(this.getPos()), this.world, this.getPos(),
                    this.getWorld().rand);
        }
    }

    private void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (world.isRemote && state.getBlock() == TakumiBlockCore.DARKBOARD_ON) {
            world.loadedTileEntityList.forEach(tileEntity -> {
                if (tileEntity instanceof TileEntityDarkCore) {
                    BlockPos end = tileEntity.getPos();
                    double x = pos.getX() + 0.5;
                    double endX = end.getX() + 0.5;
                    double y = pos.getY() + 0.5;
                    double endY = end.getY() + 0.5;
                    double z = pos.getZ() + 0.5;
                    double endZ = end.getZ() + 0.5;
                    int count = 0;
                    for (int i = 0; i < 100; i++) {
                        world.spawnAlwaysVisibleParticle(EnumParticleTypes.PORTAL.getParticleID(),
                                x + world.rand.nextDouble() * 0.2 - 0.1 + (endX - x) / 100 * i,
                                y + world.rand.nextDouble() * 0.2 - 0.1 + (endY - y) / 100 * i,
                                z + world.rand.nextDouble() * 0.2 - 0.1 + (endZ - z) / 100 * i, -(endX - x) / 500 * i,
                                -(endY - y) / 500 * i, -(endZ - z) / 500 * i);
                    }
                }
            });
        }
    }
}
