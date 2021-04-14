package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.block.IBlockTT;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import com.tntmodders.takumi.world.TakumiExplosion;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

public class EntityStoneCreeper extends EntityTakumiAbstractCreeper {

    public EntityStoneCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
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
        return 0xaaaaff;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "stonecreeper";
    }

    @Override
    public int getRegisterID() {
        return 29;
    }

    @Override
    public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn) {
        if (blockStateIn.getMaterial() == Material.AIR) {
            return 0f;
        }else if(blockStateIn.getBlock() instanceof IBlockTT){
            return 1000000f;
        }
        String s = blockStateIn.getBlock().getHarvestTool(blockStateIn);
        return blockStateIn.getBlockHardness(worldIn, pos) == -1 || s == null || !s.equalsIgnoreCase("pickaxe") ?
                10000000f : 2.5f;
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        float power = this.getPowered() ? 6f : 3f;
        if (event.getExplosion() instanceof TakumiExplosion) {
            power = ((TakumiExplosion) event.getExplosion()).getSize();
        }
        if (power > 0.1) {
            for (BlockPos pos : event.getAffectedBlocks()) {
                String s = this.world.getBlockState(pos).getBlock().getHarvestTool(this.world.getBlockState(pos));
                if (!this.world.isRemote && s != null && s.equalsIgnoreCase("pickaxe") &&
                        TakumiUtils.takumiGetBlockResistance(this, this.world.getBlockState(pos), pos) != -1) {
                    this.world.setBlockToAir(pos);
                    TakumiUtils.takumiCreateExplosion(this.world, this, pos.getX() + 0.5, pos.getY() + 0.5,
                            pos.getZ() + 0.5, power - 0.2f, false, true);
                }
            }
        }
        return true;
    }
}
