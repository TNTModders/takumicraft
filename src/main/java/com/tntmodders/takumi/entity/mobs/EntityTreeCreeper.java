package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.world.gen.TakumiWorldGenBigTree;
import com.tntmodders.takumi.world.gen.TakumiWorldGenTrees;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class EntityTreeCreeper extends EntityTakumiAbstractCreeper {
    
    public EntityTreeCreeper(World worldIn) {
        super(worldIn);
    }
    
    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, this.getPowered() ? 6 : 3, false); WorldGenAbstractTree tree =
                    this.getPowered() ?
                    new TakumiWorldGenBigTree(true, TakumiBlockCore.CREEPER_LOG.getDefaultState(), TakumiBlockCore.CREEPER_LEAVES.getDefaultState()) :
                    new TakumiWorldGenTrees(true, false, TakumiBlockCore.CREEPER_LOG.getDefaultState(), TakumiBlockCore.CREEPER_LEAVES
                            .getDefaultState());
            tree.generate(this.world, this.rand, new BlockPos(this));
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
        return 142857;
    }
    
    @Override
    public boolean isCustomSpawn() {
        return false;
    }
    
    @Override
    public String getRegisterName() {
        return "treecreeper";
    }
    
    @Override
    public int getRegisterID() {
        return 18;
    }
    
    @Override
    public int getPrimaryColor() {
        return 56593;
    }
}
