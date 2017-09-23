package com.tntmodders.takumi.entity.item;

import com.tntmodders.takumi.item.ItemTakumiArrow;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityTakumiArrow extends EntityArrow {
    private ItemStack stack;
    private int pierce;
    private int power;
    private boolean destroy;

    public EntityTakumiArrow(World worldIn) {
        super(worldIn);
    }

    public EntityTakumiArrow(World worldIn, ItemStack itemStack, EntityLivingBase base) {
        super(worldIn, base);
        this.stack = itemStack;
        this.pierce = ((ItemTakumiArrow) stack.getItem()).pierce;
        this.power = ((ItemTakumiArrow) stack.getItem()).power;
        this.destroy = ((ItemTakumiArrow) stack.getItem()).destroy;
    }

    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {
        if (raytraceResultIn.typeOfHit == RayTraceResult.Type.ENTITY) {
            if (raytraceResultIn.entityHit == this.shootingEntity) {
                return;
            } else {
                raytraceResultIn.entityHit.attackEntityFrom(new EntityDamageSourceIndirect("explosion.player", this.shootingEntity, this), 15.0f);
            }
        }
        TakumiUtils.takumiCreateExplosion(world, this, this.posX, this.posY, this.posZ, power, false, false);
        pierce--;
        if (pierce <= 0) {
            this.setDead();
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        compound.setInteger("power", this.power);
        compound.setInteger("pierce", this.pierce);
        compound.setBoolean("destroy", this.destroy);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        this.power = compound.getInteger("power");
        this.pierce = compound.getInteger("pierce");
        this.destroy = compound.getBoolean("destroy");
    }

    @Override
    protected ItemStack getArrowStack() {
        return stack;
    }
}
