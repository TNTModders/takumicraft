package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityTitanCreeper extends EntityTakumiAbstractCreeper {

    public EntityTitanCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            int i = this.getPowered() ? 20 : 15;
            for (int x = -i; x <= i; x++) {
                for (int z = -i; z <= i; z++) {
                    boolean flg = true;
                    for (int y = -i; y < 0; y++) {
                        if (x * x + y * y + z * z < i * i) {
                            if (flg && this.rand.nextBoolean()) {
                                TakumiUtils.setBlockStateProtected(this.world, new BlockPos(this.posX + x, 256 + y, this.posZ + z),
                                        TakumiBlockCore.FALLING_BOMB.getDefaultState());
                                flg = false;
                            } else {
                                TakumiUtils.setBlockStateProtected(this.world, new BlockPos(this.posX + x, 256 + y, this.posZ + z),
                                        Blocks.SAND.getDefaultState());
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.HIGH;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.GROUND_MD;
    }

    @Override
    public int getExplosionPower() {
        return 0;
    }

    @Override
    public int getSecondaryColor() {
        return 0xffff00;
    }

    @Override
    public int getPrimaryColor() {
        return 0x88ff00;
    }

    @Override
    public boolean isCustomSpawn() {
        return true;
    }

    @Override
    public String getRegisterName() {
        return "titancreeper";
    }

    @Override
    public int getRegisterID() {
        return 410;
    }
}
