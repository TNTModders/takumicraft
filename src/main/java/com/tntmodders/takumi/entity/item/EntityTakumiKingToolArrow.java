package com.tntmodders.takumi.entity.item;

import com.tntmodders.takumi.item.ItemTakumiMineSweeperTool;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityTakumiKingToolArrow extends EntityArrow {
    public boolean isSilk;
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
        this.isSilk = false;
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
            TakumiUtils.takumiCreateExplosion(this.world, this, this.posX, this.posY, this.posZ, 2.5f, false, true);
        }
        this.setDead();
    }
}
