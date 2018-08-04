package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.client.render.RenderSlimeCreeper;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAICreeperSwell;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class EntitySlimeCreeper extends EntityTakumiAbstractCreeper {

    private static final DataParameter<Integer> SLIME_SIZE =
            EntityDataManager.createKey(EntitySlimeCreeper.class, DataSerializers.VARINT);
    public float squishAmount;
    public float squishFactor;
    public float prevSquishFactor;
    private boolean wasOnGround;

    public EntitySlimeCreeper(World worldIn) {
        super(worldIn);
        this.moveHelper = new SlimeMoveHelper(this);
    }

    public static void registerFixesSlime(DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntitySlime.class);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAICreeperSwell(this));
        this.tasks.addTask(1, new AISlimeFloat(this));
        this.tasks.addTask(2, new AISlimeAttack(this));
        this.tasks.addTask(3, new AISlimeFaceRandom(this));
        this.tasks.addTask(5, new AISlimeHop(this));
/*        this.targetTasks.addTask(1, new EntityAIFindEntityNearestPlayer(this));
        this.targetTasks.addTask(3, new EntityAIFindEntityNearest(this, EntityIronGolem.class));*/
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(SLIME_SIZE, 1);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Size", this.getSlimeSize() - 1);
        compound.setBoolean("wasOnGround", this.wasOnGround);
    }

    /**
     * Returns the size of the slime.
     */
    public int getSlimeSize() {
        return this.dataManager.get(SLIME_SIZE);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        int i = compound.getInteger("Size");

        if (i < 0) {
            i = 0;
        }

        this.setSlimeSize(i + 1, false);
        this.wasOnGround = compound.getBoolean("wasOnGround");
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return this.isSmallSlime() ? SoundEvents.ENTITY_SMALL_SLIME_HURT : SoundEvents.ENTITY_SLIME_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isSmallSlime() ? SoundEvents.ENTITY_SMALL_SLIME_DEATH : SoundEvents.ENTITY_SLIME_DEATH;
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return this.getSlimeSize() == 1 ? LootTableList.ENTITIES_SLIME : LootTableList.EMPTY;
    }

    public void setSlimeSize(int size, boolean resetHealth) {
        this.dataManager.set(SLIME_SIZE, size);
        this.setSize(0.51000005F * size, 0.51000005F * size);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(size * size);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2F + 0.1F * size);

        if (resetHealth) {
            this.setHealth(this.getMaxHealth());
        }

        this.experienceValue = size;
    }

    /**
     * Gets the amount of time the slime needs to wait between jumps.
     */
    protected int getJumpDelay() {
        return this.rand.nextInt(20) + 10;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Object getRender(RenderManager manager) {
        return new RenderSlimeCreeper<>(manager);
    }

    /**
     * Will get destroyed next tick.
     */
    @Override
    public void setDead() {
        if (!this.world.isRemote && this.world.getDifficulty() != EnumDifficulty.PEACEFUL) {
            this.world.createExplosion(this, this.posX, this.posY, this.posZ,
                    this.getSlimeSize() * (this.getPowered() ? 1.5f : 1.0f), true);
            int i = this.getSlimeSize();
            if (i > 1 && this.getHealth() <= 0.0F) {
                this.spawnSlime();
            }
        }
        super.setDead();
    }

    protected void spawnSlime() {

        int i = this.getSlimeSize();
        int j = 2 + this.rand.nextInt(5);

        for (int k = 0; k < j; ++k) {
            float f = (k % 2 - 0.5F) * i / 4.0F;
            float f1 = (k / 2 - 0.5F) * i / 4.0F;
            EntitySlimeCreeper entityslime = this.createInstance();

            if (this.hasCustomName()) {
                entityslime.setCustomNameTag(this.getCustomNameTag());
            }

            if (this.isNoDespawnRequired()) {
                entityslime.enablePersistence();
            }

            entityslime.setSlimeSize(i / 2, true);
            entityslime.setLocationAndAngles(this.posX + f, this.posY + 0.5D, this.posZ + f1,
                    this.rand.nextFloat() * 360.0F, 0.0F);
            entityslime.setVelocitySlime(this.rand.nextDouble() * (this.rand.nextBoolean() ? -1 : 1), 1,
                    this.rand.nextDouble() * (this.rand.nextBoolean() ? -1 : 1));
            this.world.spawnEntity(entityslime);
        }
    }

    protected EntitySlimeCreeper createInstance() {
        EntitySlimeCreeper slime = new EntitySlimeCreeper(this.world);
        if (this.getPowered()) {
            TakumiUtils.takumiSetPowered(slime, true);
        }
        return slime;
    }

    public void setVelocitySlime(double x, double y, double z) {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
    }

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        if (damageSrc.isExplosion() && damageSrc.getTrueSource() != null &&
                damageSrc.getTrueSource().getClass() == EntitySlimeCreeper.class) {
            damageAmount = 0;
        }
        super.damageEntity(damageSrc, damageAmount);
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        if (!this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL && this.getSlimeSize() > 0) {
            this.isDead = true;
        }

        this.squishFactor += (this.squishAmount - this.squishFactor) * 0.5F;
        this.prevSquishFactor = this.squishFactor;
        super.onUpdate();

        if (this.onGround && !this.wasOnGround) {
            int i = this.getSlimeSize();
            if (spawnCustomParticles()) {
                i = 0;
            } // don't spawn particles if it's handled by the implementation itself
            for (int j = 0; j < i * 8; ++j) {
                float f = (float) (this.rand.nextFloat() * Math.PI * 2F);
                float f1 = this.rand.nextFloat() * 0.5F + 0.5F;
                float f2 = MathHelper.sin(f) * i * 0.5F * f1;
                float f3 = MathHelper.cos(f) * i * 0.5F * f1;
                World world = this.world;
                EnumParticleTypes enumparticletypes = this.getParticleType();
                double d0 = this.posX + f2;
                double d1 = this.posZ + f3;
                world.spawnParticle(enumparticletypes, d0, this.getEntityBoundingBox().minY, d1, 0.0D, 0.0D, 0.0D);
            }

            this.playSound(this.getSquishSound(), this.getSoundVolume(),
                    ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) / 0.8F);
            this.squishAmount = -0.5F;
        } else if (!this.onGround && this.wasOnGround) {
            this.squishAmount = 1.0F;
        }

        this.wasOnGround = this.onGround;
        this.alterSquishAmount();
    }

    protected boolean spawnCustomParticles() {
        return false;
    }

    protected EnumParticleTypes getParticleType() {
        return EnumParticleTypes.SLIME;
    }

    protected SoundEvent getSquishSound() {
        return this.isSmallSlime() ? SoundEvents.ENTITY_SMALL_SLIME_SQUISH : SoundEvents.ENTITY_SLIME_SQUISH;
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    @Override
    protected float getSoundVolume() {
        return 0.4F * this.getSlimeSize();
    }

    protected void alterSquishAmount() {
        this.squishAmount *= 0.6F;
    }

    public boolean isSmallSlime() {
        return this.getSlimeSize() <= 1;
    }

    /**
     * Causes this entity to do an upwards motion (jumping).
     */
    @Override
    protected void jump() {
        this.motionY = 0.41999998688697815D;
        this.isAirBorne = true;
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        if (SLIME_SIZE.equals(key)) {
            int i = this.getSlimeSize();
            this.setSize(0.51000005F * i, 0.51000005F * i);
            this.rotationYaw = this.rotationYawHead;
            this.renderYawOffset = this.rotationYawHead;

            if (this.isInWater() && this.rand.nextInt(20) == 0) {
                this.doWaterSplashEffect();
            }
        }

        super.notifyDataManagerChange(key);
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    @Override
    public boolean getCanSpawnHere() {
        BlockPos blockpos = new BlockPos(MathHelper.floor(this.posX), 0, MathHelper.floor(this.posZ));
        Chunk chunk = this.world.getChunkFromBlockCoords(blockpos);

        if (this.world.getWorldInfo().getTerrainType().handleSlimeSpawnReduction(rand, world)) {
            return false;
        } else {
            if (this.world.getDifficulty() != EnumDifficulty.PEACEFUL) {
                Biome biome = this.world.getBiome(blockpos);

                if (biome == Biomes.SWAMPLAND && this.posY > 50.0D && this.posY < 70.0D &&
                        this.rand.nextFloat() < 0.5F &&
                        this.rand.nextFloat() < this.world.getCurrentMoonPhaseFactor() &&
                        this.world.getLightFromNeighbors(new BlockPos(this)) <= this.rand.nextInt(8)) {
                    return super.getCanSpawnHere();
                }

                if (this.rand.nextInt(10) == 0 && chunk.getRandomWithSeed(987234911L).nextInt(10) == 0 &&
                        this.posY < 40.0D) {
                    return super.getCanSpawnHere();
                }
            }

            return false;
        }
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        if (this.canDamagePlayer()) {
            this.dealDamage(entityIn);
        }
    }

    /**
     * Applies a velocity to the entities, to push them away from eachother.
     */
    @Override
    public void applyEntityCollision(Entity entityIn) {
        super.applyEntityCollision(entityIn);

        if (entityIn instanceof EntityIronGolem && this.canDamagePlayer()) {
            this.dealDamage((EntityLivingBase) entityIn);
        }
    }

    @Override
    public float getEyeHeight() {
        return 0.625F * this.height;
    }

    /**
     * Indicates weather the slime is able to damage the player (based upon the slime's size)
     */
    protected boolean canDamagePlayer() {
        return !this.isSmallSlime();
    }

    protected void dealDamage(EntityLivingBase entityIn) {
        int i = this.getSlimeSize();

        if (this.canEntityBeSeen(entityIn) && this.getDistanceSqToEntity(entityIn) < 0.6D * i * 0.6D * i &&
                entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), this.getAttackStrength())) {
            this.playSound(SoundEvents.ENTITY_SLIME_ATTACK, 1.0F,
                    (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            this.applyEnchantments(this, entityIn);
        }
    }

    /**
     * Gets the amount of damage dealt to the player when "attacked" by the slime.
     */
    protected int getAttackStrength() {
        return this.getSlimeSize();
    }

    @Override
    protected Item getDropItem() {
        return this.getSlimeSize() == 1 ? Items.SLIME_BALL : Items.GUNPOWDER;
    }

    /**
     * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
     * use in wolves.
     */
    @Override
    public int getVerticalFaceSpeed() {
        return 0;
    }

    /**
     * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
     * when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
     */
    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty,
            @Nullable
                    IEntityLivingData livingdata) {
        int i = this.rand.nextInt(3);

        if (i < 2 && this.rand.nextFloat() < 0.5F * difficulty.getClampedAdditionalDifficulty()) {
            ++i;
        }

        int j = 1 << i;
        this.setSlimeSize(j, true);
        return super.onInitialSpawn(difficulty, livingdata);
    }

    /**
     * Returns true if the slime makes a sound when it jumps (based upon the slime's size)
     */
    protected boolean makesSoundOnJump() {
        return this.getSlimeSize() > 0;
    }

    protected SoundEvent getJumpSound() {
        return this.isSmallSlime() ? SoundEvents.ENTITY_SMALL_SLIME_JUMP : SoundEvents.ENTITY_SLIME_JUMP;
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            this.spawnSlime();
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL;
    }

    @Override
    public int getExplosionPower() {
        return 0;
    }

    @Override
    public int getSecondaryColor() {
        return 1179545;
    }

    @Override
    public boolean isCustomSpawn() {
        return true;
    }

    @Override
    public String getRegisterName() {
        return "slimecreeper";
    }

    @Override
    public int getRegisterID() {
        return 11;
    }

    static class AISlimeAttack extends EntityAIBase {

        private final EntitySlimeCreeper slime;
        private int growTieredTimer;

        public AISlimeAttack(EntitySlimeCreeper slimeIn) {
            this.slime = slimeIn;
            this.setMutexBits(2);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        @Override
        public boolean shouldExecute() {
            EntityLivingBase entitylivingbase = this.slime.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isEntityAlive() &&
                    (!(entitylivingbase instanceof EntityPlayer) ||
                            !((EntityPlayer) entitylivingbase).capabilities.disableDamage);
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        @Override
        public void startExecuting() {
            this.growTieredTimer = 300;
            super.startExecuting();
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        @Override
        public boolean shouldContinueExecuting() {
            EntityLivingBase entitylivingbase = this.slime.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isEntityAlive() &&
                    (!(entitylivingbase instanceof EntityPlayer) ||
                            !((EntityPlayer) entitylivingbase).capabilities.disableDamage) &&
                    --this.growTieredTimer > 0;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        @Override
        public void updateTask() {
            this.slime.faceEntity(this.slime.getAttackTarget(), 10.0F, 10.0F);
            ((SlimeMoveHelper) this.slime.getMoveHelper()).setDirection(this.slime.rotationYaw,
                    this.slime.canDamagePlayer());
        }
    }

    static class AISlimeFaceRandom extends EntityAIBase {

        private final EntitySlimeCreeper slime;
        private float chosenDegrees;
        private int nextRandomizeTime;

        public AISlimeFaceRandom(EntitySlimeCreeper slimeIn) {
            this.slime = slimeIn;
            this.setMutexBits(2);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        @Override
        public boolean shouldExecute() {
            return this.slime.getAttackTarget() == null &&
                    (this.slime.onGround || this.slime.isInWater() || this.slime.isInLava() ||
                            this.slime.isPotionActive(MobEffects.LEVITATION));
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        @Override
        public void updateTask() {
            if (--this.nextRandomizeTime <= 0) {
                this.nextRandomizeTime = 40 + this.slime.getRNG().nextInt(60);
                this.chosenDegrees = this.slime.getRNG().nextInt(360);
            }

            ((SlimeMoveHelper) this.slime.getMoveHelper()).setDirection(this.chosenDegrees, false);
        }
    }

    static class AISlimeFloat extends EntityAIBase {

        private final EntitySlimeCreeper slime;

        public AISlimeFloat(EntitySlimeCreeper slimeIn) {
            this.slime = slimeIn;
            this.setMutexBits(5);
            ((PathNavigateGround) slimeIn.getNavigator()).setCanSwim(true);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        @Override
        public boolean shouldExecute() {
            return this.slime.isInWater() || this.slime.isInLava();
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        @Override
        public void updateTask() {
            if (this.slime.getRNG().nextFloat() < 0.8F) {
                this.slime.getJumpHelper().setJumping();
            }

            ((SlimeMoveHelper) this.slime.getMoveHelper()).setSpeed(1.2D);
        }
    }

    static class AISlimeHop extends EntityAIBase {

        private final EntitySlimeCreeper slime;

        public AISlimeHop(EntitySlimeCreeper slimeIn) {
            this.slime = slimeIn;
            this.setMutexBits(5);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        @Override
        public boolean shouldExecute() {
            return true;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        @Override
        public void updateTask() {
            ((SlimeMoveHelper) this.slime.getMoveHelper()).setSpeed(1.0D);
        }
    }

    static class SlimeMoveHelper extends EntityMoveHelper {

        private final EntitySlimeCreeper slime;
        private float yRot;
        private int jumpDelay;
        private boolean isAggressive;

        public SlimeMoveHelper(EntitySlimeCreeper slimeIn) {
            super(slimeIn);
            this.slime = slimeIn;
            this.yRot = (float) (180.0F * slimeIn.rotationYaw / Math.PI);
        }

        public void setDirection(float p_179920_1_, boolean p_179920_2_) {
            this.yRot = p_179920_1_;
            this.isAggressive = p_179920_2_;
        }

        public void setSpeed(double speedIn) {
            this.speed = speedIn;
            this.action = Action.MOVE_TO;
        }

        @Override
        public void onUpdateMoveHelper() {
            this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, this.yRot, 90.0F);
            this.entity.rotationYawHead = this.entity.rotationYaw;
            this.entity.renderYawOffset = this.entity.rotationYaw;

            if (this.action != Action.MOVE_TO) {
                this.entity.setMoveForward(0.0F);
            } else {
                this.action = Action.WAIT;

                if (this.entity.onGround) {
                    this.entity.setAIMoveSpeed((float) (this.speed * this.entity.getEntityAttribute(
                            SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));

                    if (this.jumpDelay-- <= 0) {
                        this.jumpDelay = this.slime.getJumpDelay();

                        if (this.isAggressive) {
                            this.jumpDelay /= 3;
                        }

                        this.slime.getJumpHelper().setJumping();

                        if (this.slime.makesSoundOnJump()) {
                            this.slime.playSound(this.slime.getJumpSound(), this.slime.getSoundVolume(),
                                    ((this.slime.getRNG().nextFloat() - this.slime.getRNG().nextFloat()) * 0.2F +
                                            1.0F) * 0.8F);
                        }
                    } else {
                        this.slime.moveStrafing = 0.0F;
                        this.slime.moveForward = 0.0F;
                        this.entity.setAIMoveSpeed(0.0F);
                    }
                } else {
                    this.entity.setAIMoveSpeed((float) (this.speed * this.entity.getEntityAttribute(
                            SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
                }
            }
        }
    }
}
