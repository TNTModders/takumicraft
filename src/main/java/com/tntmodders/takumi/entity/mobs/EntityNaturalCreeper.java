package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityNaturalCreeper extends EntityTakumiAbstractCreeper {
    public EntityNaturalCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, 2 * (this.getPowered() ? 2 : 1), false);
        }
        int bloX = ((int) this.posX);
        int bloY = ((int) this.posY);
        int bloZ = ((int) this.posZ);

        this.world.setBlockState(new BlockPos(bloX, bloY, bloZ), TakumiBlockCore.GUNORE.getDefaultState());

        this.world.setBlockState(new BlockPos(bloX, bloY + 1, bloZ), TakumiBlockCore.GUNORE.getDefaultState());

        this.world.setBlockState(new BlockPos(bloX, bloY + 2, bloZ), TakumiBlockCore.GUNORE.getDefaultState());

        this.world.setBlockState(new BlockPos(bloX, bloY + 3, bloZ), TakumiBlockCore.GUNORE.getDefaultState());

        this.world.setBlockState(new BlockPos(bloX - 1, bloY + 3, bloZ), TakumiBlockCore.CREEPER_BOMB.getDefaultState());
        this.world.setBlockState(new BlockPos(bloX, bloY + 3, bloZ - 1), TakumiBlockCore.CREEPER_BOMB.getDefaultState());
        this.world.setBlockState(new BlockPos(bloX + 1, bloY + 3, bloZ), TakumiBlockCore.CREEPER_BOMB.getDefaultState());
        this.world.setBlockState(new BlockPos(bloX, bloY + 3, bloZ + 1), TakumiBlockCore.CREEPER_BOMB.getDefaultState());

        this.world.setBlockState(new BlockPos(bloX, bloY + 4, bloZ), TakumiBlockCore.GUNORE.getDefaultState());

        this.world.setBlockState(new BlockPos(bloX - 2, bloY + 4, bloZ - 1), TakumiBlockCore.CREEPER_BOMB.getDefaultState());
        this.world.setBlockState(new BlockPos(bloX - 2, bloY + 4, bloZ), TakumiBlockCore.CREEPER_BOMB.getDefaultState());
        this.world.setBlockState(new BlockPos(bloX - 2, bloY + 4, bloZ + 1), TakumiBlockCore.CREEPER_BOMB.getDefaultState());
        this.world.setBlockState(new BlockPos(bloX - 1, bloY + 4, bloZ - 2), TakumiBlockCore.CREEPER_BOMB.getDefaultState());
        this.world.setBlockState(new BlockPos(bloX - 1, bloY + 4, bloZ - 1), TakumiBlockCore.CREEPER_BOMB.getDefaultState());
        this.world.setBlockState(new BlockPos(bloX - 1, bloY + 4, bloZ), TakumiBlockCore.CREEPER_BOMB.getDefaultState());
        this.world.setBlockState(new BlockPos(bloX - 1, bloY + 4, bloZ + 1), TakumiBlockCore.CREEPER_BOMB.getDefaultState());
        this.world.setBlockState(new BlockPos(bloX - 1, bloY + 4, bloZ + 2), TakumiBlockCore.CREEPER_BOMB.getDefaultState());
        this.world.setBlockState(new BlockPos(bloX, bloY + 4, bloZ - 2), TakumiBlockCore.CREEPER_BOMB.getDefaultState());
        this.world.setBlockState(new BlockPos(bloX, bloY + 4, bloZ - 1), TakumiBlockCore.CREEPER_BOMB.getDefaultState());
        this.world.setBlockState(new BlockPos(bloX, bloY + 4, bloZ + 1), TakumiBlockCore.CREEPER_BOMB.getDefaultState());
        this.world.setBlockState(new BlockPos(bloX, bloY + 4, bloZ + 2), TakumiBlockCore.CREEPER_BOMB.getDefaultState());
        this.world.setBlockState(new BlockPos(bloX + 1, bloY + 4, bloZ - 2), TakumiBlockCore.CREEPER_BOMB.getDefaultState());
        this.world.setBlockState(new BlockPos(bloX + 1, bloY + 4, bloZ - 1), TakumiBlockCore.CREEPER_BOMB.getDefaultState());
        this.world.setBlockState(new BlockPos(bloX + 1, bloY + 4, bloZ), TakumiBlockCore.CREEPER_BOMB.getDefaultState());
        this.world.setBlockState(new BlockPos(bloX + 1, bloY + 4, bloZ + 1), TakumiBlockCore.CREEPER_BOMB.getDefaultState());
        this.world.setBlockState(new BlockPos(bloX + 1, bloY + 4, bloZ + 2), TakumiBlockCore.CREEPER_BOMB.getDefaultState());
        this.world.setBlockState(new BlockPos(bloX + 2, bloY + 4, bloZ - 1), TakumiBlockCore.CREEPER_BOMB.getDefaultState());
        this.world.setBlockState(new BlockPos(bloX + 2, bloY + 4, bloZ), TakumiBlockCore.CREEPER_BOMB.getDefaultState());
        this.world.setBlockState(new BlockPos(bloX + 2, bloY + 4, bloZ + 1), TakumiBlockCore.CREEPER_BOMB.getDefaultState());

        this.world.setBlockState(new BlockPos(bloX, bloY + 5, bloZ), TakumiBlockCore.CREEPER_BOMB.getDefaultState());
        this.world.setBlockState(new BlockPos(bloX - 1, bloY + 5, bloZ), TakumiBlockCore.CREEPER_BOMB.getDefaultState());
        this.world.setBlockState(new BlockPos(bloX, bloY + 5, bloZ - 1), TakumiBlockCore.CREEPER_BOMB.getDefaultState());
        this.world.setBlockState(new BlockPos(bloX + 1, bloY + 5, bloZ), TakumiBlockCore.CREEPER_BOMB.getDefaultState());
        this.world.setBlockState(new BlockPos(bloX, bloY + 5, bloZ + 1), TakumiBlockCore.CREEPER_BOMB.getDefaultState());
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.GRASS_D;
    }

    @Override
    public int getExplosionPower() {
        return 0;
    }

    @Override
    public int getSecondaryColor() {
        return 122752;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "naturalcreeper";
    }

    @Override
    public int getRegisterID() {
        return 201;
    }
}
