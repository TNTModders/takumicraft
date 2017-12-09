package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import com.tntmodders.takumi.world.TakumiExplosion;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

public class EntityBangCreeper extends EntityTakumiAbstractCreeper {
    
    public EntityBangCreeper(World worldIn) {
        super(worldIn);
    }
    
    @Override
    public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn) {
        return blockStateIn.getBlockHardness(worldIn, pos) == -1 ? 10000000f : 0.75f;
    }
    
    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            for (double dx = posX - 0.5; dx < posX + 0.5; dx += 0.25) {
                for (double dz = posZ - 0.5; dz < posZ + 0.5; dz += 0.25) {
                    this.world.createExplosion(this, dx, posY, dz, 3f * (this.getPowered() ? 2.0f : 1.0f), true);
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
        return EnumTakumiType.WIND_D;
    }
    
    @Override
    public int getExplosionPower() {
        return 3;
    }
    
    @Override
    public int getSecondaryColor() {
        return 4682022;
    }
    
    @Override
    public boolean isCustomSpawn() {
        return false;
    }
    
    @Override
    public String getRegisterName() {
        return "bangcreeper";
    }
    
    @Override
    public int getRegisterID() {
        return 202;
    }
    
    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        float power = this.getPowered() ? 6f : 3f;
        if (event.getExplosion() instanceof TakumiExplosion) {
            power = ((TakumiExplosion) event.getExplosion()).getSize();
        }
        if (power > 0.5) {
            for (BlockPos pos : event.getAffectedBlocks()) {
    
                if (!this.world.isRemote && this.world.getBlockState(pos).getBlock().getExplosionResistance(world, pos, this,
                                                                                                            event.getExplosion()) >= Blocks
                        .OBSIDIAN.getExplosionResistance(
                        world, pos, this, event.getExplosion()) && TakumiUtils.takumiGetBlockResistance(this, this.world.getBlockState(pos),
                                                                                                        pos) != -1) {
                    this.world.setBlockToAir(pos);
                    TakumiUtils.takumiCreateExplosion(this.world, this, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, power - 0.2f, false,
                                                      true);
                }
            }
        }
        return true;
    }
}
