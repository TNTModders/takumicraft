package com.tntmodders.takumi.entity.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.world.World;

public class EntityFallingBomb extends EntityFallingBlock {
    
    public EntityFallingBomb(World worldIn, double x, double y, double z, IBlockState fallingBlockState) {
        super(worldIn, x, y, z, fallingBlockState);
    }
    
    public EntityFallingBomb(World worldIn) {
        super(worldIn);
    }
}
