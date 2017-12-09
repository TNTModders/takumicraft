package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.world.gen.TakumiWorldGenBigTree;
import com.tntmodders.takumi.world.gen.TakumiWorldGenTrees;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class EntityNaturalCreeper extends EntityTakumiAbstractCreeper {
    
    public EntityNaturalCreeper(World worldIn) {
        super(worldIn);
    }
    
    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, this.getPowered() ? 6 : 3, false); WorldGenAbstractTree tree =
                    this.getPowered() ?
                    new TakumiWorldGenBigTree(true, TakumiBlockCore.GUNORE.getDefaultState(), TakumiBlockCore.CREEPER_BOMB.getDefaultState()) :
                    new TakumiWorldGenTrees(true, false, TakumiBlockCore.GUNORE.getDefaultState(), TakumiBlockCore.CREEPER_BOMB.getDefaultState());
            tree.generate(this.world, this.rand, new BlockPos(this));
        }
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
