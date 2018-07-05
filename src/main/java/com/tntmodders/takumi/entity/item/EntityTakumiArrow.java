package com.tntmodders.takumi.entity.item;

import com.tntmodders.takumi.entity.mobs.EntityBoltCreeper;
import com.tntmodders.takumi.item.ItemTakumiArrow;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

public class EntityTakumiArrow extends EntityArrow {

    private ItemStack stack;
    private int pierce;
    private int power;
    private boolean destroy;
    private Class<? extends EntityCreeper> container;
    private EnumArrowType type;

    public EntityTakumiArrow(World worldIn) {
        super(worldIn);
        this.type = EnumArrowType.NORMAL;
    }

    public EntityTakumiArrow(World worldIn, ItemStack itemStack, EntityLivingBase base) {
        super(worldIn, base);
        this.stack = itemStack;
        this.pierce = ((ItemTakumiArrow) stack.getItem()).pierce;
        this.power = ((ItemTakumiArrow) stack.getItem()).power;
        this.destroy = ((ItemTakumiArrow) stack.getItem()).destroy;
        this.container = null;
        this.type = EnumArrowType.NORMAL;
    }

    public EntityTakumiArrow(World worldIn, ItemStack itemStack, EntityLivingBase base,
            Class<? extends EntityCreeper> creeper) {
        this(worldIn, itemStack, base);
        this.type = EnumArrowType.MONSTER;
        this.container = creeper;
    }

    public EntityTakumiArrow(World worldIn, ItemStack itemStack, EntityLivingBase base, EnumArrowType type) {
        this(worldIn, itemStack, base);
        this.type = type;
    }

    public EntityTakumiArrow(World worldIn, EntityLivingBase base, int pierce, int power, boolean destroy,
            Class<EntityCreeper> container, EnumArrowType type) {
        super(worldIn, base);
        this.stack = null;
        this.pierce = pierce;
        this.power = power;
        this.destroy = destroy;
        this.container = null;
        this.type = type;
    }


    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {
        if (raytraceResultIn.typeOfHit == Type.ENTITY) {
            if (raytraceResultIn.entityHit == this.shootingEntity) {
                return;
            }
            raytraceResultIn.entityHit.attackEntityFrom(
                    new EntityDamageSourceIndirect("explosion.player", this.shootingEntity, this), 15.0f);
        }
        if (this.type == null) {
            this.type = EnumArrowType.NORMAL;
        }
        switch (this.type) {
            case NORMAL: {
                TakumiUtils.takumiCreateExplosion(world, this, this.posX, this.posY, this.posZ, power, false, destroy);
                break;
            }
            case MONSTER: {
                try {
                    EntityCreeper creeper = this.container.getConstructor(World.class).newInstance(world);
                    creeper.setPosition(this.posX, this.posY, this.posZ);
                    NBTTagCompound compound = new NBTTagCompound();
                    creeper.writeEntityToNBT(compound);
                    compound.setShort("Fuse", (short) 1);
                    creeper.readEntityFromNBT(compound);
                    if (creeper instanceof EntityBoltCreeper || world.isThundering()) {
                        creeper.onStruckByLightning(null);
                    }
                    creeper.setInvisible(true);
                    creeper.ignite();
                    world.spawnEntity(creeper);
                    creeper.onUpdate();
                    if (creeper instanceof EntityBoltCreeper) {
                        creeper.setDead();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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
        if (this.container != null) {
            compound.setString("container", this.container.getName());
        }
        compound.setString("type", this.type.toString());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        this.power = compound.getInteger("power");
        this.pierce = compound.getInteger("pierce");
        this.destroy = compound.getBoolean("destroy");
        try {
            this.container = (Class<? extends EntityCreeper>) Class.forName(compound.getString("container"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.type = EnumArrowType.valueOf(compound.getString("type"));
        } catch (Exception e) {
            this.type = EnumArrowType.NORMAL;
            e.printStackTrace();
        }
    }

    @Override
    protected ItemStack getArrowStack() {
        return stack;
    }

    public enum EnumArrowType {
        NORMAL, PIERCE, MONSTER, SHOT, LASER
    }
}
