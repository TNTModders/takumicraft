package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class EntityTitanCreeper extends EntityTakumiAbstractCreeper {

    public EntityTitanCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            int i = this.getPowered() ? 25 : 15;
            for (int x = -i; x <= i; x++) {
                for (int y = -i; y < 0; y++) {
                    for (int z = -i; z <= i; z++) {
                        if (x * x + y * y + z * z < i * i) {
                            TakumiUtils.setBlockStateProtected(this.world, this.getPosition().add(x, 256 + y, z),
                                    Blocks.SAND.getDefaultState());
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
