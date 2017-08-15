package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityFireCreeper extends EntityTakumiAbstractCreeper {
    public EntityFireCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
        int range = this.getPowered() ? 6 : 2;
        for (int x = ((int) this.posX) - range; x < ((int) this.posX) + range; x++) {
            for (int y = ((int) this.posY) - range; y < ((int) this.posY) + range; y++) {
                for (int z = ((int) this.posZ) - range; z < ((int) this.posZ) + range; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (this.world.isAirBlock(pos) && Blocks.FIRE.canPlaceBlockAt(this.world, pos)) {
                        this.world.setBlockState(pos, Blocks.FIRE.getDefaultState());
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
        return EnumTakumiType.FIRE;
    }

    @Override
    public int getExplosionPower() {
        return 0;
    }

    @Override
    public int getSecondaryColor() {
        return 16733525;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "firecreeper";
    }

    @Override
    public int getRegisterID() {
        return 7;
    }
}
