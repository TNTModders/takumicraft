package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;

public class EntityDanceCreeper extends EntityTakumiAbstractCreeper {
    private int teleportCounter;
    private static final UUID ATTACKING_SPEED_BOOST_ID = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
    private static final AttributeModifier ATTACKING_SPEED_BOOST =
            new AttributeModifier(ATTACKING_SPEED_BOOST_ID, "Attacking speed boost", 0.2D, 0).setSaved(false);
    private static final DataParameter<Boolean> SCREAMING =
            EntityDataManager.createKey(EntityDanceCreeper.class, DataSerializers.BOOLEAN);

    private int lastCreepySound;
    private int targetChangeTime;

    public EntityDanceCreeper(World worldIn) {
        super(worldIn);
        this.setPathPriority(PathNodeType.WATER, -1.0F);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAICreeperSwell(this));
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D, 0.0F));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        //this.targetTasks.addTask(1, new AIFindPlayer(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityEndermite.class, 10, true, false,
                EntityEndermite :: isSpawnedByPlayer));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(SCREAMING, Boolean.FALSE);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("teleport", this.teleportCounter);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    @Deprecated
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.teleportCounter = compound.getInteger("teleport");
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_ENDERMEN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ENDERMEN_DEATH;
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_ENDERMAN;
    }


    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        if (SCREAMING.equals(key) && this.isScreaming() && this.world.isRemote) {
            this.playEndermanSound();
        }

        super.notifyDataManagerChange(key);
    }

    public boolean isScreaming() {
        return this.dataManager.get(SCREAMING);
    }

    public void playEndermanSound() {
        if (this.ticksExisted >= this.lastCreepySound + 400) {
            this.lastCreepySound = this.ticksExisted;

            if (!this.isSilent()) {
                this.world.playSound(this.posX, this.posY + this.getEyeHeight(), this.posZ,
                        SoundEvents.ENTITY_ENDERMEN_STARE, this.getSoundCategory(), 2.5F, 1.0F, false);
            }
        }
    }

    /**
     * Checks to see if this enderman should be attacking this player
     */
    private boolean shouldAttackPlayer(EntityPlayer player) {
        ItemStack itemstack = player.inventory.armorInventory.get(3);

        if (itemstack.getItem() == Item.getItemFromBlock(Blocks.PUMPKIN)) {
            return false;
        } else {
            Vec3d vec3d = player.getLook(1.0F).normalize();
            Vec3d vec3d1 = new Vec3d(this.posX - player.posX,
                    this.getEntityBoundingBox().minY + this.getEyeHeight() - (player.posY + player.getEyeHeight()),
                    this.posZ - player.posZ);
            double d0 = vec3d1.lengthVector();
            vec3d1 = vec3d1.normalize();
            double d1 = vec3d.dotProduct(vec3d1);
            return d1 > 1.0D - 0.025D / d0 && player.canEntityBeSeen(this);
        }
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
    public void onLivingUpdate() {
        if (this.world.isRemote) {
            for (int i = 0; i < 2; ++i) {
                this.world.spawnParticle(EnumParticleTypes.PORTAL,
                        this.posX + (this.rand.nextDouble() - 0.5D) * this.width,
                        this.posY + this.rand.nextDouble() * this.height - 0.25D,
                        this.posZ + (this.rand.nextDouble() - 0.5D) * this.width,
                        (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(),
                        (this.rand.nextDouble() - 0.5D) * 2.0D);
            }
        }

        this.isJumping = false;
        super.onLivingUpdate();
    }

    /**
     * Teleport the enderman to a random nearby position
     */
    protected boolean teleportRandomly() {
        double d0 = this.posX + (this.rand.nextDouble() - 0.5D) * 16.0D;
        double d1 = this.posY + (this.rand.nextInt(64) - 32);
        double d2 = this.posZ + (this.rand.nextDouble() - 0.5D) * 16.0D;
        return this.teleportTo(d0, d1, d2);
    }

    /**
     * Teleport the enderman to another entity
     */
    protected boolean teleportToEntity(Entity p_70816_1_) {
        Vec3d vec3d = new Vec3d(this.posX - p_70816_1_.posX,
                this.getEntityBoundingBox().minY + this.height / 2.0F - p_70816_1_.posY + p_70816_1_.getEyeHeight(),
                this.posZ - p_70816_1_.posZ);
        vec3d = vec3d.normalize();
        double d0 = 16.0D;
        double d1 = this.posX + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3d.x * 16.0D;
        double d2 = this.posY + (this.rand.nextInt(16) - 8) - vec3d.y * 16.0D;
        double d3 = this.posZ + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3d.z * 16.0D;
        return this.teleportTo(d1, d2, d3);
    }

    /**
     * Teleport the enderman
     */
    private boolean teleportTo(double x, double y, double z) {
        EnderTeleportEvent event = new EnderTeleportEvent(this, x, y, z, 0);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            return false;
        }
        boolean flag = this.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ());

        if (flag) {
            this.world.playSound(null, this.prevPosX, this.prevPosY, this.prevPosZ,
                    SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
            this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
        }

        return flag;
    }

    @Override
    public void takumiExplode() {
    }
    /*===================================== Forge End ==============================*/

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.GROUND_M;
    }

    @Override
    public int getExplosionPower() {
        return this.teleportCounter;
    }

    @Override
    public int getSecondaryColor() {
        return 4128831;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "dancecreeper";
    }

    @Override
    public int getRegisterID() {
        return 262;
    }

    @Override
    public void setAttackTarget(
            @Nullable
                    EntityLivingBase entitylivingbaseIn) {
        super.setAttackTarget(entitylivingbaseIn);
        IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

        if (entitylivingbaseIn == null) {
            this.targetChangeTime = 0;
            this.dataManager.set(SCREAMING, Boolean.FALSE);
            iattributeinstance.removeModifier(ATTACKING_SPEED_BOOST);
        } else {
            this.targetChangeTime = this.ticksExisted;
            this.dataManager.set(SCREAMING, Boolean.TRUE);

            if (!iattributeinstance.hasModifier(ATTACKING_SPEED_BOOST)) {
                iattributeinstance.applyModifier(ATTACKING_SPEED_BOOST);
            }
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isScreaming() ? SoundEvents.ENTITY_ENDERMEN_SCREAM : SoundEvents.ENTITY_ENDERMEN_AMBIENT;
    }

    @Nullable
    @Override
    protected Item getDropItem() {
        return Items.ENDER_PEARL;
    }

    @Override
    protected void updateAITasks() {
        if (this.isWet()) {
            this.attackEntityFrom(DamageSource.DROWN, 1.0F);
        }

        if (this.world.isDaytime() && this.ticksExisted >= this.targetChangeTime + 600) {
            float f = this.getBrightness();

            if (f > 0.5F && this.world.canSeeSky(new BlockPos(this)) &&
                    this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F) {
                this.setAttackTarget(null);
                this.teleportRandomly();
            }
        }

        super.updateAITasks();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.getAttackTarget() != null) {
            if (this.getCreeperState() > 0) {
                if (this.teleportCounter < 10) {
                    if (this.teleportRandomly()) {
                        this.teleportCounter++;
                    } else if (this.teleportToEntity(this.getAttackTarget())) {
                        this.teleportCounter++;
                    }
                }
            }
        }
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        if (!this.world.isRemote) {
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, this.teleportCounter, true);
        }
    }

    protected boolean teleportTo(EntityLivingBase entity) {
        Random rand = new Random();
        int distance = 128;
        double x = entity.posX + (rand.nextDouble() - 0.5D) * distance;
        double y = entity.posY + rand.nextInt(distance + 1) - distance / 2;
        double z = entity.posZ + (rand.nextDouble() - 0.5D) * distance;
        EnderTeleportEvent event = new EnderTeleportEvent(entity, x, y, z, 0);

        double d3 = entity.posX;
        double d4 = entity.posY;
        double d5 = entity.posZ;
        entity.posX = event.getTargetX();
        entity.posY = event.getTargetY();
        entity.posZ = event.getTargetZ();

        int xInt = MathHelper.floor(entity.posX);
        int yInt = MathHelper.floor(entity.posY);
        int zInt = MathHelper.floor(entity.posZ);

        boolean flag = false;
        if (entity.world.isAirBlock(new BlockPos(xInt, yInt, zInt))) {

            boolean foundGround = false;
            while (!foundGround && yInt > 0) {
                IBlockState block = entity.world.getBlockState(new BlockPos(xInt, yInt - 1, zInt));
                if (block.getMaterial().blocksMovement()) {
                    foundGround = true;
                } else {
                    --entity.posY;
                    --yInt;
                }
            }

            if (foundGround) {
                entity.setPosition(entity.posX, entity.posY, entity.posZ);
                if (entity.world.getCollisionBoxes(entity, entity.getEntityBoundingBox()).isEmpty() &&
                        !entity.world.containsAnyLiquid(entity.getEntityBoundingBox())) {
                    flag = true;
                }
            }
        }

        if (!flag) {
            entity.setPosition(d3, d4, d5);
            return false;
        }

        entity.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);

        short short1 = 128;
        for (int l = 0; l < short1; ++l) {
            double d6 = l / (short1 - 1.0D);
            float f = (rand.nextFloat() - 0.5F) * 0.2F;
            float f1 = (rand.nextFloat() - 0.5F) * 0.2F;
            float f2 = (rand.nextFloat() - 0.5F) * 0.2F;
            double d7 = d3 + (entity.posX - d3) * d6 + (rand.nextDouble() - 0.5D) * entity.width * 2.0D;
            double d8 = d4 + (entity.posY - d4) * d6 + rand.nextDouble() * entity.height;
            double d9 = d5 + (entity.posZ - d5) * d6 + (rand.nextDouble() - 0.5D) * entity.width * 2.0D;
            entity.world.spawnParticle(EnumParticleTypes.PORTAL, d7, d8, d9, f, f1, f2);
        }

        entity.world.playSound(null, entity.prevPosX, entity.prevPosY, entity.prevPosZ,
                SoundEvents.ENTITY_ENDERMEN_TELEPORT, entity.getSoundCategory(), 1.0F, 1.0F);
        entity.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
        return true;

    }

    @Override
    public int getPrimaryColor() {
        return 0x4400ff;
    }


    static class AIFindPlayer extends EntityAINearestAttackableTarget<EntityPlayer> {

        private final EntityDanceCreeper enderman;
        /**
         * The player
         */
        private EntityPlayer player;
        private int aggroTime;
        private int teleportTime;

        public AIFindPlayer(EntityDanceCreeper p_i45842_1_) {
            super(p_i45842_1_, EntityPlayer.class, false);
            this.enderman = p_i45842_1_;
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        @Override
        public boolean shouldExecute() {
            double d0 = this.getTargetDistance();
            this.player = this.enderman.world.getNearestAttackablePlayer(this.enderman.posX, this.enderman.posY,
                    this.enderman.posZ, d0, d0, null,
                    p_apply_1_ -> p_apply_1_ != null && AIFindPlayer.this.enderman.shouldAttackPlayer(p_apply_1_));
            return this.player != null;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        @Override
        public void startExecuting() {
            this.aggroTime = 5;
            this.teleportTime = 0;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        @Override
        public boolean shouldContinueExecuting() {
            if (this.player != null) {
                if (!this.enderman.shouldAttackPlayer(this.player)) {
                    return false;
                } else {
                    this.enderman.faceEntity(this.player, 10.0F, 10.0F);
                    return true;
                }
            } else {
                return this.targetEntity != null && this.targetEntity.isEntityAlive() ||
                        super.shouldContinueExecuting();
            }
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        @Override
        public void resetTask() {
            this.player = null;
            super.resetTask();
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        @Override
        public void updateTask() {
            if (this.player != null) {
                if (--this.aggroTime <= 0) {
                    this.targetEntity = this.player;
                    this.player = null;
                    super.startExecuting();
                }
            } else {
                if (this.targetEntity != null) {
                    if (this.enderman.shouldAttackPlayer(this.targetEntity)) {
                        if (this.targetEntity.getDistanceSqToEntity(this.enderman) < 16.0D) {
                            this.enderman.teleportRandomly();
                        }

                        this.teleportTime = 0;
                    } else if (this.targetEntity.getDistanceSqToEntity(this.enderman) > 256.0D &&
                            this.teleportTime++ >= 30 && this.enderman.teleportToEntity(this.targetEntity)) {
                        this.teleportTime = 0;
                    }
                }

                super.updateTask();
            }
        }
    }
}
