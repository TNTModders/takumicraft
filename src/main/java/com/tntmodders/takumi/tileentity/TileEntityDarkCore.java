package com.tntmodders.takumi.tileentity;

import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.core.TakumiPotionCore;
import com.tntmodders.takumi.entity.mobs.boss.EntityTransCreeper;
import com.tntmodders.takumi.entity.mobs.noncreeper.EntityDarkVillager;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.HashMap;
import java.util.Map;

public class TileEntityDarkCore extends TileEntity implements ITickable {
    private int tick;

    @Override
    public void update() {
        tick++;
        if (this.getBlockType() == TakumiBlockCore.DARKCORE) {
            boolean flg = this.world.loadedTileEntityList.stream().noneMatch(
                    tileEntity -> world.getBlockState(tileEntity.getPos()).getBlock() == TakumiBlockCore.DARKBOARD &&
                            this.getDistanceSq(tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ()) < 64) &&
                    this.world.loadedTileEntityList.stream().filter(
                            tileEntity -> world.getBlockState(tileEntity.getPos()).getBlock() == TakumiBlockCore.DARKBOARD_ON &&
                                    this.getDistanceSq(tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ()) < 64).toArray().length > 7;
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
            if (tick >= 100) {
                double d = (tick - 100) / 60 > 6 ? 6 : (tick - 100) / 60;
                if (d >= 6) {
                    this.world.loadedEntityList.forEach(entity -> {
                        if (entity instanceof EntityDarkVillager) {
                            ((EntityDarkVillager) entity).setHealth(0);
                            entity.setDead();
                        } else if (entity instanceof EntityLivingBase &&
                                entity.getDistanceSq(this.getPos()) < 15 * 15) {
                            ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 600));
                            ((EntityLivingBase) entity).addPotionEffect(
                                    new PotionEffect(TakumiPotionCore.INVERSION, 600));
                        }
                    });
                    this.world.setBlockState(this.getPos(), TakumiBlockCore.DARKCORE_SP.getDefaultState());
                    if (!this.world.isRemote) {
                        EntityTransCreeper transCreeper = new EntityTransCreeper(this.world);
                        transCreeper.setPosition(this.getPos().getX() + 0.5, this.getPos().getY() - 5,
                                this.getPos().getZ() + 0.5);
                        this.world.spawnEntity(transCreeper);
                        this.world.createExplosion(transCreeper, transCreeper.posX, transCreeper.posY,
                                transCreeper.posZ, 0f, false);
                        Map<BlockPos, IBlockState> stateMap = new HashMap<>();
                        int i = 16;
                        for (int x = -i; x < i; x++) {
                            for (int y = -i; y < i; y++) {
                                for (int z = -i; z < i; z++) {
                                    if (x * x + y * y + z * z <= i * i &&
                                            this.world.getBlockState(this.getPos().add(x, y, z)).getBlock() !=
                                                    Blocks.BEDROCK) {
                                        if (this.world.getTileEntity(this.getPos().add(x, y, z)) == null ||
                                                this.world.getTileEntity(
                                                        this.getPos().add(x, y, z)) instanceof TileEntityDarkCore ||
                                                this.world.getTileEntity(
                                                        this.getPos().add(x, y, z)) instanceof TileEntityDarkBoard) {
                                            stateMap.put(this.getPos().add(x, 10 - y, z),
                                                    this.world.getBlockState(this.getPos().add(x, y, z)));
                                        }
                                        this.world.setBlockToAir(this.getPos().add(x, y, z));
                                    }
                                }
                            }
                        }
                        stateMap.forEach((key, value) -> this.world.setBlockState(key, value));
                    }
                } else if (FMLCommonHandler.instance().getSide().isClient()) {
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
