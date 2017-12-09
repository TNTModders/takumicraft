package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityPierceCreeper extends EntityTakumiAbstractCreeper {
    
    public EntityPierceCreeper(World worldIn) {
        super(worldIn);
    }
    
    @Override
    public void takumiExplode() {
    }
    
    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }
    
    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.GROUND_D;
    }
    
    @Override
    public int getExplosionPower() {
        return 4;
    }
    
    @Override
    public int getSecondaryColor() {
        return 0x001155;
    }
    
    @Override
    public boolean isCustomSpawn() {
        return false;
    }
    
    @Override
    public String getRegisterName() {
        return "piercecreeper";
    }
    
    @Override
    public int getRegisterID() {
        return 240;
    }
    
    @Override
    public int getPrimaryColor() {
        return 0x88ff00;
    }
    
    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        if (damageSrc != DamageSource.FALL) {
            super.damageEntity(damageSrc, damageAmount);
        }
    }
    
    @Override
    public void setDead() {
        this.setInvisible(true);
        if ((this.canDeath() || this.getHealth() <= 0 || this.world.getDifficulty() == EnumDifficulty.PEACEFUL) && this.onGround) {
            super.setDead();
        }
    }
    
    private boolean canDeath() {
        return this.world.getBlockState(this.getPosition().down()).getBlockHardness(this.world, this.getPosition().down()) < 0 && !this.world
                .isAirBlock(this.getPosition().down()) && this.onGround;
    }
    
    @Override
    public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn) {
        return blockStateIn.getBlockHardness(worldIn, pos) == -1 ? 10000000f : 1f;
    }
}
