package com.tntmodders.takumi.entity.item;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityIceologerCreeperSpell extends EntityLiving {
    private double tick;

    public EntityIceologerCreeperSpell(World worldIn) {
        super(worldIn);
        this.tick = 30;
        this.setSize(1, 1);
    }

    public EntityIceologerCreeperSpell(World worldIn, double x, double y, double z, double tickIn) {
        super(worldIn);
        this.setPosition(x, y, z);
        this.tick = tickIn;
        this.setSize(1, 1);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1000);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1);
    }

    @Override
    public void onUpdate() {
        this.setNoGravity(this.ticksExisted < this.tick);
        if (this.ticksExisted < this.tick) {
            for (int i = 0; i < 10; i++) {
                this.world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, this.posX + this.rand.nextDouble() * 2 - 1, this.posY + this.rand.nextDouble() * 2 - 1,
                        this.posZ + this.rand.nextDouble() * 2 - 1, this.rand.nextGaussian() * 1.2, this.rand.nextGaussian() * 1.2, this.rand.nextGaussian() * 1.2);
            }
        } else {
            this.motionX = 0;
            this.motionZ = 0;
            this.motionY = -0.3;
        }
        super.onUpdate();
        if (this.onGround || this.world.collidesWithAnyBlock(this.getEntityBoundingBox().grow(0.1))) {
            if (!this.world.isRemote) {
                this.onGroundUpdate();
            }
            this.setDead();
        }
    }

    protected void onGroundUpdate() {
        this.world.createExplosion(this, this.posX + 0.5, this.posY + 0.5, this.posZ + 0.5, 3f, true);
    }

    @Override
    public void move(MoverType type, double x, double y, double z) {
        if (this.ticksExisted > this.tick) {
            super.move(type, 0, y, 0);
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setDouble("waitTick", this.tick);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("waitTick")) {
            this.tick = compound.getDouble("waitTick");
        }
    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }

    public float getGlowingSize() {
        return ((float) Math.min(this.ticksExisted * 2 / this.tick, 1));
    }

    public boolean canFreezeEntity() {
        return true;
    }
}
