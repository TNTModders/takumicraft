package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityAngryCreeper extends EntityTakumiAbstractCreeper {

    public EntityAngryCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.FIRE_D;
    }

    @Override
    public int getExplosionPower() {
        return 4;
    }

    @Override
    public int getSecondaryColor() {
        return 0xff0000;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "angrycreeper";
    }

    @Override
    public int getRegisterID() {
        return 259;
    }

    @Override
    public int getPrimaryColor() {
        return 0x00ffff;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.getCreeperState() > 0) {
            this.world.playerEntities.forEach(player -> {
                if (this.getDistanceSqToEntity(player) <= 16 && player.onGround) {
                    this.jump(player);
                }
            });
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

    @Override
    public void onLivingUpdate() {
        if (this.world.isRemote) {
            if (!this.world.isAirBlock(this.getPosition().down())) {
                for (int i = 0; i < 10; ++i) {
                    this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK,
                            this.posX + (this.rand.nextDouble() - 0.5D) * this.width,
                            this.posY + this.rand.nextDouble() * this.height,
                            this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D,
                            Block.getIdFromBlock(this.world.getBlockState(this.getPosition().down()).getBlock()));
                }
            }

            if (this.getCreeperState() > 0) {
                for (int x = -4; x <= 4; x++) {
                    for (int y = -4; y <= 4; y++) {
                        for (int z = -4; z <= 4; z++) {
                            if (!this.world.isAirBlock(this.getPosition().add(x, y, z))) {
                                this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK,
                                        this.posX + x + this.rand.nextDouble(),
                                        this.posY + y + 0.75 + this.rand.nextDouble(),
                                        this.posZ + z + this.rand.nextDouble(), 0, 0, 0, Block.getIdFromBlock(
                                                this.world.getBlockState(this.getPosition().add(x, y, z)).getBlock()));
                            }
                        }
                    }
                }
            }
        }
        super.onLivingUpdate();
    }
}
