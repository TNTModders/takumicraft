package com.tntmodders.takumi.entity.item;

import com.tntmodders.takumi.item.ItemTakumiMineSweeperTool;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityTakumiKingToolArrow extends EntityArrow {
    private ItemTakumiMineSweeperTool.EnumTakumiTool enumTool;

    public EntityTakumiKingToolArrow(World worldIn) {
        super(worldIn);
    }

    public EntityTakumiKingToolArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public EntityTakumiKingToolArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }

    public EntityTakumiKingToolArrow(World worldIn, EntityLivingBase shooter, ItemTakumiMineSweeperTool.EnumTakumiTool tool) {
        super(worldIn, shooter);
        this.enumTool = tool;
    }

    @Override
    protected ItemStack getArrowStack() {
        return null;
    }

    public ItemTakumiMineSweeperTool.EnumTakumiTool getEnumTool() {
        return this.enumTool;
    }

    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {
        if (raytraceResultIn.typeOfHit == RayTraceResult.Type.ENTITY) {
            if (raytraceResultIn.entityHit == this.shootingEntity || this.ticksExisted < 5) {
                return;
            }
        }
        if (!this.world.isRemote) {
            TakumiUtils.takumiCreateExplosion(this.world, this, this.posX, this.posY, this.posZ, 4f, false, true);
        }
        this.setDead();
    }

    @Override
    public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn) {
        return blockStateIn.getBlockHardness(worldIn, pos) < 0 ? super.getExplosionResistance(explosionIn, worldIn, pos, blockStateIn) : 0.25f;
    }
}
