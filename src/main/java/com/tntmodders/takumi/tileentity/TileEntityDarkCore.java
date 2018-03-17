package com.tntmodders.takumi.tileentity;

import com.tntmodders.takumi.core.TakumiBlockCore;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TileEntityDarkCore extends TileEntity implements ITickable {
    @Override
    public void update() {
        if (this.getBlockType() == TakumiBlockCore.DARKCORE) {
            boolean flg = this.world.loadedTileEntityList.stream().noneMatch(
                    tileEntity -> tileEntity.getBlockType() == TakumiBlockCore.DARKBOARD);
            if (flg) {
                this.world.setBlockState(this.getPos(), TakumiBlockCore.DARKCORE_ON.getDefaultState());
                this.world.createExplosion(null, this.getPos().getX() + 0.5, this.getPos().getY() + 0.5,
                        this.getPos().getZ() + 0.5, 0, false);
            }
        } else {
            if (FMLCommonHandler.instance().getSide().isClient()) {
                for (int x = -10; x <= 10; x++) {
                    for (int z = -10; z <= 10; z++) {
                        this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.getPos().getX() + x,
                                this.getPos().getY() + 1, this.getPos().getZ() + z, 0, 0, 0,
                                Block.getIdFromBlock(this.world.getBlockState(this.getPos().add(x, 0, z)).getBlock()));
                    }
                }
            }
        }
    }
}
