package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityFallingBombCreeper extends EntityTakumiAbstractCreeper {

    public EntityFallingBombCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
        this.buildsand((int) this.posX, (int) this.posY, (int) this.posZ, this.getPowered() ? 20 : 10, this.world);
    }

    protected void buildsand(int ox, int oy, int oz, int height, World par1World) {
        int blockX, blockY, blockZ;
        int searchRange = height * 2;
        for (int x = -1 * searchRange; x < searchRange; ++x) {
            blockX = x + ox;
            for (int y = (int) (-0.25 * searchRange); y < (int) (0.25 * searchRange); ++y) {
                blockY = y + oy;
                for (int z = -1 * searchRange; z < searchRange; ++z) {
                    blockZ = z + oz;

                    BlockPos pos = new BlockPos(blockX, blockY, blockZ);
                    if (!par1World.isAirBlock(pos)) {
                        if (par1World.getBlockState(pos).getBlockHardness(world, pos) != -1 &&
                                TakumiUtils.takumiGetBlockResistance(this, par1World.getBlockState(pos), pos) != -1 &&
                                !world.isRemote) {
                            par1World.setBlockState(pos, TakumiBlockCore.FALLING_BOMB.getDefaultState());
                        }
                    }
                }
            }
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL_D;
    }

    @Override
    public int getExplosionPower() {
        return 0;
    }

    @Override
    public int getSecondaryColor() {
        return 0x002200;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "fallingbombcreeper";
    }

    @Override
    public int getRegisterID() {
        return 257;
    }

    @Override
    public int getPrimaryColor() {
        return 0x000055;
    }
}
