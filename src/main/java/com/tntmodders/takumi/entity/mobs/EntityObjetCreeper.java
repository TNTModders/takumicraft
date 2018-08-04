package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityObjetCreeper extends EntityTakumiAbstractCreeper {

    public EntityObjetCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
        if (!this.isDead && !this.world.isRemote) {
            int range = this.getPowered() ? 6 : 4;
            IBlockState state = this.rand.nextBoolean() ? TakumiBlockCore.CREEPER_BRICK.getDefaultState() :
                    TakumiBlockCore.CREEPER_IRON.getDefaultState();
            for (int x = (int) this.posX - range; x < (int) this.posX + range; x++) {
                for (int y = (int) this.posY; y < (int) this.posY + range * 1.5 + 3; y++) {
                    for (int z = (int) this.posZ - range; z < (int) this.posZ + range; z++) {
                        BlockPos pos = new BlockPos(x, y, z);
                        if (TakumiUtils.takumiGetBlockResistance(this, this.world.getBlockState(pos), pos) != -1) {
                            if (y > this.posY + range * 1.5) {
                                if (this.rand.nextInt(8) == 0) {
                                    TakumiUtils.setBlockStateProtected(this.world, pos,
                                            TakumiBlockCore.CREEPER_BOMB.getDefaultState());
                                }
                            } else if (this.rand.nextBoolean()) {
                                TakumiUtils.setBlockStateProtected(this.world, pos, state);
                            }
                        }
                    }
                }
            }
            this.setDead();
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.GRASS;
    }

    @Override
    public int getExplosionPower() {
        return 0;
    }

    @Override
    public int getSecondaryColor() {
        return 1171372;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "objetcreeper";
    }

    @Override
    public int getRegisterID() {
        return 16;
    }
}
