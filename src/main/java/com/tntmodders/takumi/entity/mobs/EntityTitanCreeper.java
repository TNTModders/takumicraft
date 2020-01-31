package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.world.World;

public class EntityTitanCreeper extends EntityTakumiAbstractCreeper {

    public EntityTitanCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
     /*   if (!this.world.isRemote) {
            int i = this.getPowered() ? 30 : 20;
            for (int x = -i; x <= i; x++) {
                for (int z = -i; z <= i; z++) {
                    boolean flg = true;
                    for (int y = -i; y < 0; y++) {
                        if (x * x + y * y + z * z < i * i) {
                            if (flg && this.rand.nextInt(3) <= 1) {
                                EntityFallingBlock block = new EntityFallingBlock(this.world, this.posX + x, 300 + y, this.posZ + z, TakumiBlockCore.FALLING_BOMB.getDefaultState());
                                block.fallTime = 1;
                                block.motionY = -1;
                                this.world.spawnEntity(block);
                                flg = false;
                            } else {
                                EntityFallingBlock block = new EntityFallingBlock(this.world, this.posX + x, 300 + y, this.posZ + z, Blocks.SAND.getDefaultState());
                                block.fallTime = 1;
                                block.motionY = -1;
                                this.world.spawnEntity(block);
                            }
                        }
                    }
                }
            }
        }*/
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
        return 7;
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
