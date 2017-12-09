package com.tntmodders.takumi.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityTakumiTNTPrimed extends Entity {
    
    private static final DataParameter <Integer> FUSE = EntityDataManager.createKey(EntityTNTPrimed.class, DataSerializers.VARINT);
    @Nullable
    private EntityLivingBase tntPlacedBy;
    /**
     * How long the fuse is
     */
    private int fuse;
    
    public EntityTakumiTNTPrimed(World worldIn, double x, double y, double z, EntityLivingBase igniter) {
        this(worldIn); this.setPosition(x, y, z); float f = (float) (Math.random() * (Math.PI * 2D)); this.motionX = -((float) Math.sin(f)) * 0.02F;
        this.motionY = 0.20000000298023224D; this.motionZ = -((float) Math.cos(f)) * 0.02F; this.setFuse(80); this.prevPosX = x; this.prevPosY = y;
        this.prevPosZ = z; this.tntPlacedBy = igniter;
    }
    
    public EntityTakumiTNTPrimed(World worldIn) {
        super(worldIn); this.fuse = 80; this.preventEntitySpawning = true; this.isImmuneToFire = true; this.setSize(0.98F, 0.98F);
    }
    
    @Override
    protected void entityInit() {
        this.dataManager.register(FUSE, 80);
    }
    
    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        this.prevPosX = this.posX; this.prevPosY = this.posY; this.prevPosZ = this.posZ;
        
        if (!this.hasNoGravity()) {
            this.motionY -= 0.03999999910593033D;
        }
        
        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ); this.motionX *= 0.9800000190734863D; this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;
        
        if (this.onGround) {
            this.motionX *= 0.699999988079071D; this.motionZ *= 0.699999988079071D; this.motionY *= -0.5D;
        }
        
        --this.fuse;
        
        if (this.fuse <= 0) {
            this.setDead();
            
            if (!this.world.isRemote) {
                this.explode();
            }
        } else {
            this.handleWaterMovement();
            this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
        }
    }
    
    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    @Override
    protected boolean canTriggerWalking() {
        return false;
    }
    
    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }
    
    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        this.setFuse(compound.getShort("Fuse"));
    }
    
    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setShort("Fuse", (short) this.getFuse());
    }
    
    @Override
    public void notifyDataManagerChange(DataParameter <?> key) {
        if (FUSE == key) {
            this.fuse = this.getFuseDataManager();
        }
    }
    
    @Override
    public float getEyeHeight() {
        return 0.0F;
    }
    
    /**
     * Gets the fuse from the data manager
     */
    public int getFuseDataManager() {
        return this.dataManager.get(FUSE);
    }
    
    public int getFuse() {
        return this.fuse;
    }
    
    public void setFuse(int fuseIn) {
        this.dataManager.set(FUSE, fuseIn); this.fuse = fuseIn;
    }
    
    private void explode() {
        float f = 4.0F; this.world.createExplosion(this, this.posX, this.posY + this.height / 16.0F, this.posZ, 7.0F, true);
    }
    
    /**
     * returns null or the entityliving it was placed or ignited by
     */
    @Nullable
    public EntityLivingBase getTntPlacedBy() {
        return this.tntPlacedBy;
    }
}