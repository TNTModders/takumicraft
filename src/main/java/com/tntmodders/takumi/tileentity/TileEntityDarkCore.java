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
                        for (int y = 0; y <= 4; y++) {
                            Block block = this.world.getBlockState(this.getPos().add(x, y, z)).getBlock();
                            if (block == TakumiBlockCore.DARKBRICK) {
                                if (this.world.rand.nextBoolean()) {
                                    if (this.world.rand.nextBoolean()) {
                                        this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK,
                                                this.getPos().getX() + x, this.getPos().getY() + y + 1,
                                                this.getPos().getZ() + z, 0, 0, 0, Block.getIdFromBlock(block));
                                    } else {
                                        this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK,
                                                this.getPos().getX() + x, this.getPos().getY() + y - 1,
                                                this.getPos().getZ() + z, 0, 0, 0, Block.getIdFromBlock(block));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            this.world.playerEntities.forEach(entityPlayer -> {
                if (entityPlayer.posY >= this.getPos().getY() && entityPlayer.posY <= this.getPos().getY() + 1 &&
                        entityPlayer.posX >= this.getPos().getX() - 10 &&
                        entityPlayer.posX <= this.getPos().getX() + 10 &&
                        entityPlayer.posZ >= this.getPos().getZ() - 10 &&
                        entityPlayer.posZ <= this.getPos().getZ() + 10 && entityPlayer.onGround) {
                    this.jump(entityPlayer);
                }
            });
            this.world.playSound(this.pos.getX() + 0.5, this.getPos().getY() + 0.5, this.getPos().getZ() + 0.5,
                    SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 5, -8, false);
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
