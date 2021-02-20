package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityPulseCreeper extends EntityTakumiAbstractCreeper {

    public EntityPulseCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public int getPrimaryColor() {
        return 0x555500;
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, 3f, false);
        }
        int r = this.getPowered() ? 50 : 25;
        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    BlockPos pos = this.getPosition().add(x, y, z);
                    IBlockState oldState = this.world.getBlockState(pos);
                    IBlockState newState = null;
                    try {
                        if (oldState.getPropertyKeys().stream().anyMatch(iProperty -> iProperty.getName().equals("powered"))) {
                            IProperty property = oldState.getPropertyKeys().stream().filter(iProperty -> iProperty.getName().equals("powered")).iterator().next();
                            if (property instanceof PropertyBool) {
                                newState = this.world.getBlockState(pos).withProperty(property, true);
                                this.world.setBlockState(pos, newState, 0);
                            }
                        }
                        if (this.world.getBlockState(pos).getPropertyKeys().stream().anyMatch(iProperty -> iProperty.getName().equals("power"))) {
                            IProperty property = oldState.getPropertyKeys().stream().filter(iProperty -> iProperty.getName().equals("power")).iterator().next();
                            if (property instanceof PropertyInteger) {
                                newState = this.world.getBlockState(pos).withProperty(property, 15);
                                this.world.setBlockState(pos, newState, 0);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (newState != null) {
                        if (!this.world.isRemote) {
                            this.world.createExplosion(this, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 2f, false);
                        } else {
                            this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + 0.5, pos.getY(), pos.getZ(), 0, 0, 0);
                            this.world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_REDSTONE_TORCH_BURNOUT, SoundCategory.BLOCKS, 0.5f, 1f, true);
                        }
                    }
                }
            }
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.GROUND_D;
    }

    @Override
    public int getExplosionPower() {
        return 0;
    }

    @Override
    public int getSecondaryColor() {
        return 0x003309;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "pulsecreeper";
    }

    @Override
    public int getRegisterID() {
        return 91;
    }
}
