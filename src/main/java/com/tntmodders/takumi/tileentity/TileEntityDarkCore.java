package com.tntmodders.takumi.tileentity;

import com.tntmodders.takumi.core.TakumiBlockCore;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TileEntityDarkCore extends TileEntity implements ITickable {
    private int tick;

    @Override
    public void update() {
        tick++;
        if (this.getBlockType() == TakumiBlockCore.DARKCORE) {
            boolean flg = this.world.loadedTileEntityList.stream().noneMatch(
                    tileEntity -> tileEntity.getBlockType() == TakumiBlockCore.DARKBOARD) &&
                    this.world.loadedTileEntityList.stream().anyMatch(
                            tileEntity -> tileEntity.getBlockType() == TakumiBlockCore.DARKBOARD_ON);
            if (flg) {
                this.world.setBlockState(this.getPos(), TakumiBlockCore.DARKCORE_ON.getDefaultState());
                this.world.createExplosion(null, this.getPos().getX() + 0.5, this.getPos().getY() + 0.5,
                        this.getPos().getZ() + 0.5, 0, false);
                for (int y = 4; y <= 8; y++) {
                    for (int x = -2; x <= 2; x++) {
                        for (int z = -2; z <= 2; z++) {
                            if (this.world.getBlockState(this.getPos().add(x, y, z)).getBlock() ==
                                    TakumiBlockCore.DARKIRON_BARS) {
                                if (!this.world.isRemote) {
                                    this.world.setBlockToAir(this.getPos().add(x, y, z));
                                }
                                if (FMLCommonHandler.instance().getSide().isClient()) {
                                    this.world.playSound(this.getPos().getX() + x + 0.5, this.getPos().getY() + y + 0.5,
                                            this.getPos().getZ() + z + 0.5, SoundEvents.BLOCK_GLASS_BREAK,
                                            SoundCategory.BLOCKS, 2, 8, true);
                                }
                            }
                        }
                    }
                }
            }
        } else {
            if (FMLCommonHandler.instance().getSide().isClient()) {
                for (int x = -10; x <= 10; x++) {
                    for (int z = -10; z <= 10; z++) {
                        for (int y = 0; y <= 4; y++) {
                            Block block = this.world.getBlockState(this.getPos().add(x, y, z)).getBlock();
                            if (block == TakumiBlockCore.DARKBRICK && this.world.rand.nextInt(3) == 0) {
                                if (this.world.rand.nextBoolean()) {
                                    this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.getPos().getX() + x,
                                            this.getPos().getY() + y + 1, this.getPos().getZ() + z, 0, 0, 0,
                                            Block.getIdFromBlock(block));
                                } else {
                                    this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.getPos().getX() + x,
                                            this.getPos().getY() + y - 1, this.getPos().getZ() + z, 0, 0, 0,
                                            Block.getIdFromBlock(block));
                                }
                            }
                        }
                    }
                }
            }
            if (tick == 100 && !this.world.isRemote) {
                for (int x = -9; x <= 9; x++) {
                    for (int z = -9; z <= 9; z++) {
                        if (!(Math.abs(x) >= 5 && Math.abs(x) <= 6 && Math.abs(z) >= 5 && Math.abs(z) <= 6)) {
                            this.world.setBlockToAir(this.getPos().add(x, 4, z));
                            this.world.createExplosion(null, this.getPos().getX() + x + 0.5, this.getPos().getY() + 4.5,
                                    this.getPos().getZ() + z + 0.5, 0, false);
                        }
                        if (x == 0 || z == 0) {
                            for (int y = 5; y <= 7; y++) {
                                this.world.setBlockToAir(this.getPos().add(x, y, z));
                                this.world.createExplosion(null, this.getPos().getX() + x + 0.5,
                                        this.getPos().getY() + y + 0.5, this.getPos().getZ() + z + 0.5, 0, false);
                            }
                        }
                    }
                }
            }
            if (tick >= 100 && FMLCommonHandler.instance().getSide().isClient()) {
                double d = (tick - 100) / 60 > 6 ? 6 : (tick - 100) / 60;
                for (double x = -6; x <= d; x += 0.1) {
                    for (double z = -6; z <= d; z += 0.1) {
                        if ((x * x + z * z >= 35.75 && x * x + z * z <= 36.25) ||
                                (Math.abs(x) >= 3.975 && Math.abs(x) <= 4.025 && this.world.rand.nextInt(4) == 0) ||
                                (Math.abs(z) >= 3.975 && Math.abs(z) <= 4.025 && this.world.rand.nextInt(4) == 0) ||
                                (Math.abs(x / z) >= 0.95 && Math.abs(x / z) <= 1.05) &&
                                        this.world.rand.nextInt(12) == 0) {
                            this.world.spawnParticle(EnumParticleTypes.FLAME, this.getPos().getX() + x + 0.5,
                                    this.getPos().getY() + 2.5, this.getPos().getZ() + z + 0.5, 0, 0.025, 0);
                        }
                    }
                }
            }
            this.world.playerEntities.forEach(entityPlayer -> {
                if (entityPlayer.posY >= this.getPos().getY() && entityPlayer.posY <= this.getPos().getY() + 5 &&
                        entityPlayer.posX >= this.getPos().getX() - 10 &&
                        entityPlayer.posX <= this.getPos().getX() + 10 &&
                        entityPlayer.posZ >= this.getPos().getZ() - 10 &&
                        entityPlayer.posZ <= this.getPos().getZ() + 10 && entityPlayer.onGround) {
                    this.jump(entityPlayer);
                }
            });
            this.world.playSound(this.pos.getX() + 0.5, this.getPos().getY() + 0.5, this.getPos().getZ() + 0.5,
                    SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 5, -16, false);
        }
    }

    private void jump(EntityPlayer player) {
        player.motionY = 0.105;

        if (player.isSprinting()) {
            float f = player.rotationYaw * 0.017453292F;
            player.motionX -= (double) (MathHelper.sin(f) * 0.2F);
            player.motionZ += (double) (MathHelper.cos(f) * 0.2F);
        }

        player.isAirBorne = true;
        net.minecraftforge.common.ForgeHooks.onLivingJump(player);
    }
}
