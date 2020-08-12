package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.client.render.RenderBatCreeper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.event.world.ExplosionEvent;

import javax.annotation.Nullable;
import java.util.Calendar;

public class EntityBigBatCreeper extends EntityBatCreeper {

    private static final DataParameter<Byte> HANGING =
            EntityDataManager.createKey(EntityBigBatCreeper.class, DataSerializers.BYTE);
    private BlockPos spawnPosition;

    public EntityBigBatCreeper(World worldIn) {
        super(worldIn);
        this.setSize(1.5F, 2.0F);
        this.setIsBatHanging(true);
    }

    @Override
    @Nullable
    public SoundEvent getAmbientSound() {
        return this.getIsBatHanging() && this.rand.nextInt(4) != 0 ? null : SoundEvents.ENTITY_BAT_AMBIENT;
    }

    @Override
    public boolean getIsBatHanging() {
        return (this.dataManager.get(HANGING) & 1) != 0;
    }

    @Override
    public void setIsBatHanging(boolean isHanging) {
        byte b0 = this.dataManager.get(HANGING);

        if (isHanging) {
            this.dataManager.set(HANGING, (byte) (b0 | 1));
        } else {
            this.dataManager.set(HANGING, (byte) (b0 & -2));
        }
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        BlockPos blockpos = new BlockPos(this);
        BlockPos blockpos1 = blockpos.up();

        if (this.getIsBatHanging()) {
            if (this.world.getBlockState(blockpos1).isNormalCube()) {
                if (this.rand.nextInt(200) == 0) {
                    this.rotationYawHead = (float) this.rand.nextInt(360);
                }

                if (this.world.getNearestPlayerNotCreative(this, 4.0D) != null) {
                    this.setIsBatHanging(false);
                    this.world.playEvent(null, 1025, blockpos, 0);
                }
            } else {
                this.setIsBatHanging(false);
                this.world.playEvent(null, 1025, blockpos, 0);
            }
        } else {
            if (this.spawnPosition != null &&
                    (!this.world.isAirBlock(this.spawnPosition) || this.spawnPosition.getY() < 1)) {
                this.spawnPosition = null;
            }

            if (this.spawnPosition == null || this.rand.nextInt(30) == 0 ||
                    this.spawnPosition.distanceSq((int) this.posX, (int) this.posY,
                            (int) this.posZ) < 4.0D) {
                this.spawnPosition = new BlockPos((int) this.posX + this.rand.nextInt(7) - this.rand.nextInt(7),
                        (int) this.posY + this.rand.nextInt(6) - 2,
                        (int) this.posZ + this.rand.nextInt(7) - this.rand.nextInt(7));
            }

            double d0 = (double) this.spawnPosition.getX() + 0.5D - this.posX;
            double d1 = (double) this.spawnPosition.getY() + 0.1D - this.posY;
            double d2 = (double) this.spawnPosition.getZ() + 0.5D - this.posZ;
            this.motionX += (Math.signum(d0) * 0.5D - this.motionX) * 0.10000000149011612D;
            this.motionY += (Math.signum(d1) * 0.699999988079071D - this.motionY) * 0.10000000149011612D;
            this.motionZ += (Math.signum(d2) * 0.5D - this.motionZ) * 0.10000000149011612D;
            float f = (float) (MathHelper.atan2(this.motionZ, this.motionX) * (180D / Math.PI)) - 90.0F;
            float f1 = MathHelper.wrapDegrees(f - this.rotationYaw);
            this.moveForward = 0.5F;
            this.rotationYaw += f1;

            if (this.rand.nextInt(100) == 0 && this.world.getBlockState(blockpos1).isNormalCube()) {
                this.setIsBatHanging(true);
            }
        }
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 3;
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(HANGING, (byte) 0);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setByte("BatFlags", this.dataManager.get(HANGING));
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.dataManager.set(HANGING, compound.getByte("BatFlags"));
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_BAT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_BAT_DEATH;
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_BAT;
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
     * Return whether this entity should NOT trigger a pressure plate or a tripwire.
     */
    @Override
    public boolean doesEntityNotTriggerPressurePlate() {
        return true;
    }

    @Override
    public float getEyeHeight() {
        return this.height / 2.0F;
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    @Override
    protected float getSoundVolume() {
        return 0.1F;
    }

    /**
     * Gets the pitch of living sounds in living entities.
     */
    @Override
    protected float getSoundPitch() {
        return super.getSoundPitch() * 0.95F;
    }

    @Override
    protected void collideWithNearbyEntities() {
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    @Override
    public boolean canBePushed() {
        return false;
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        } else {
            if (!this.world.isRemote && this.getIsBatHanging()) {
                this.setIsBatHanging(false);
            }

            return super.attackEntityFrom(source, amount);
        }
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
   /* @Override
    public boolean getCanSpawnHere() {
        if (this.world.provider.getDimensionType().getId() != DimensionType.OVERWORLD.getId() &&
                this.world.provider.getDimensionType().getId() != TakumiWorldCore.TAKUMI_WORLD.getId()) {
            return false;
        }
        BlockPos blockpos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);

        if (blockpos.getY() >= this.world.getSeaLevel()) {
            return false;
        } else {
            int i = this.world.getLightFromNeighbors(blockpos);
            int j = 2;

            if (this.isDateAroundHalloween(this.world.getCurrentDate())) {
                j = 7;
            }

            return i <= this.rand.nextInt(j) && super.getCanSpawnHere();
        }
    }*/
    private boolean isDateAroundHalloween(Calendar p_175569_1_) {
        return p_175569_1_.get(Calendar.MONTH) + 1 == 10 && p_175569_1_.get(Calendar.DATE) >= 20 ||
                p_175569_1_.get(Calendar.MONTH) + 1 == 11 && p_175569_1_.get(Calendar.DATE) <= 3;
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            for (int i = 0; i <= (this.getPowered() ? 5 : 3); i++) {
                EntityBatCreeper batCreeper = new EntityBatCreeper(this.world);
                batCreeper.copyLocationAndAnglesFrom(this);
                this.world.spawnEntity(batCreeper);
                EntityRushCreeper rushCreeper = new EntityRushCreeper(this.world);
                rushCreeper.copyLocationAndAnglesFrom(this);
                this.world.spawnEntity(rushCreeper);
                rushCreeper.startRiding(batCreeper, true);
            }
        }
    }

    @Override
    public void onLivingUpdate() {
        if (this.getPowered() && !this.isPotionActive(MobEffects.SPEED)) {
            this.addPotionEffect(new PotionEffect(MobEffects.SPEED, 1200, 3, true, false));
        }
        if (this.world.isRemote) {
            for (int i = 0; i < 5; ++i) {
                this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE,
                        this.posX + (this.rand.nextDouble() - 0.5D) * this.width,
                        this.posY + this.rand.nextDouble() * this.height,
                        this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D);
            }
        }
        super.onLivingUpdate();
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        event.getAffectedEntities().removeIf(entity -> entity instanceof EntityBatCreeper);
        return true;
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL_D;
    }

    @Override
    public int getExplosionPower() {
        return 3;
    }

    @Override
    public int getSecondaryColor() {
        return 0x223322;
    }

    @Override
    public boolean isCustomSpawn() {
        return true;
    }

    @Override
    public String getRegisterName() {
        return "bigbatcreeper";
    }

    @Override
    public int getRegisterID() {
        return 280;
    }

    @Override
    public int getPrimaryColor() {
        return 0xaa0000;
    }

    @Override
    public Object getRender(RenderManager manager) {
        return new RenderBatCreeper(manager);
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        super.onUpdate();

        if (this.getIsBatHanging()) {
            this.motionX = 0.0D;
            this.motionY = 0.0D;
            this.motionZ = 0.0D;
            this.posY = (double) MathHelper.floor(this.posY) + 1.0D - (double) this.height;
        } else {
            this.motionY *= 0.6000000238418579D;
        }
    }
}
