package com.tntmodders.takumi.entity.mobs;

import com.google.common.base.Predicate;
import com.tntmodders.takumi.client.render.RenderSeaGuardianCreeper;
import com.tntmodders.takumi.core.TakumiEntityCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Objects;

public class EntitySeaGuardianCreeper extends EntityTakumiAbstractCreeper {

    private static final DataParameter<Boolean> MOVING =
            EntityDataManager.createKey(EntitySeaGuardianCreeper.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> TARGET_ENTITY =
            EntityDataManager.createKey(EntitySeaGuardianCreeper.class, DataSerializers.VARINT);
    protected float clientSideTailAnimation;
    protected float clientSideTailAnimationO;
    protected float clientSideTailAnimationSpeed;
    protected float clientSideSpikesAnimation;
    protected float clientSideSpikesAnimationO;
    protected EntityAIWander wander;
    private EntityLivingBase targetedEntity;
    private int clientSideAttackTime;
    private boolean clientSideTouchedGround;

    public EntitySeaGuardianCreeper(World worldIn) {
        super(worldIn);
        this.experienceValue = 10;
        this.setSize(0.85F, 0.85F);
        this.moveHelper = new GuardianMoveHelper(this);
        this.clientSideTailAnimation = this.rand.nextFloat();
        this.clientSideTailAnimationO = this.clientSideTailAnimation;
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        event.getAffectedEntities().forEach(entity -> entity.setAir(0));
        return true;
    }

    @Override
    public void customSpawn() {
        EntitySpawnPlacementRegistry.setPlacementType(EntitySeaGuardianCreeper.class, SpawnPlacementType.IN_WATER);
        EntityRegistry.addSpawn(EntitySeaGuardianCreeper.class, this.takumiRank().getSpawnWeight() / 5, 1, 1,
                TakumiEntityCore.WATER_TAKUMI, Biomes.OCEAN, Biomes.DEEP_OCEAN, Biomes.FROZEN_OCEAN);
    }

    @Override
    public int getPrimaryColor() {
        return 0x006600;
    }

    @Override
    public Object getRender(RenderManager manager) {
        return new RenderSeaGuardianCreeper(manager);
    }

    @Override
    protected void initEntityAI() {
        EntityAIMoveTowardsRestriction entityaimovetowardsrestriction = new EntityAIMoveTowardsRestriction(this, 1.0D);
        this.wander = new EntityAIWander(this, 1.0D, 80);
        this.tasks.addTask(2, new EntityAICreeperSwell(this));
        this.tasks.addTask(4, new AIGuardianAttack(this));
        this.tasks.addTask(5, entityaimovetowardsrestriction);
        this.tasks.addTask(7, this.wander);
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntitySeaGuardianCreeper.class, 12.0F, 0.01F));
        this.tasks.addTask(9, new EntityAILookIdle(this));
        this.wander.setMutexBits(3);
        entityaimovetowardsrestriction.setMutexBits(3);
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityLivingBase.class, 10, true, false,
                new GuardianTargetSelector(this)));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(MOVING, Boolean.FALSE);
        this.dataManager.register(TARGET_ENTITY, 0);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return this.isInWater() ? SoundEvents.ENTITY_GUARDIAN_HURT : SoundEvents.ENTITY_GUARDIAN_HURT_LAND;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isInWater() ? SoundEvents.ENTITY_GUARDIAN_DEATH : SoundEvents.ENTITY_GUARDIAN_DEATH_LAND;
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_GUARDIAN;
    }

    /**
     * Returns new PathNavigateGround instance
     */
    @Override
    protected PathNavigate createNavigator(World worldIn) {
        return new PathNavigateSwimmer(this, worldIn);
    }

    /**
     * Get number of ticks, at least during which the living entity will be silent.
     */
    @Override
    public int getTalkInterval() {
        return 160;
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 2;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isInWater() ? SoundEvents.ENTITY_GUARDIAN_AMBIENT : SoundEvents.ENTITY_GUARDIAN_AMBIENT_LAND;
    }

    /**
     * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
     * use in wolves.
     */
    @Override
    public int getVerticalFaceSpeed() {
        return 180;
    }

    /**
     * Checks that the entity is not colliding with any blocks / liquids
     */
    @Override
    public boolean isNotColliding() {
        return this.world.checkNoEntityCollision(this.getEntityBoundingBox(), this) &&
                this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty();
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public float getEyeHeight() {
        return this.height * 0.5F;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
    public void onLivingUpdate() {
        if (this.world.isRemote) {
            this.clientSideTailAnimationO = this.clientSideTailAnimation;

            if (!this.isInWater()) {
                this.clientSideTailAnimationSpeed = 2.0F;

                if (this.motionY > 0.0D && this.clientSideTouchedGround && !this.isSilent()) {
                    this.world.playSound(this.posX, this.posY, this.posZ, this.getFlopSound(), this.getSoundCategory(),
                            1.0F, 1.0F, false);
                }

                this.clientSideTouchedGround =
                        this.motionY < 0.0D && this.world.isBlockNormalCube(new BlockPos(this).down(), false);
            } else if (this.isMoving()) {
                if (this.clientSideTailAnimationSpeed < 0.5F) {
                    this.clientSideTailAnimationSpeed = 4.0F;
                } else {
                    this.clientSideTailAnimationSpeed += (0.5F - this.clientSideTailAnimationSpeed) * 0.1F;
                }
            } else {
                this.clientSideTailAnimationSpeed += (0.125F - this.clientSideTailAnimationSpeed) * 0.2F;
            }

            this.clientSideTailAnimation += this.clientSideTailAnimationSpeed;
            this.clientSideSpikesAnimationO = this.clientSideSpikesAnimation;

            if (!this.isInWater()) {
                this.clientSideSpikesAnimation = this.rand.nextFloat();
            } else if (this.isMoving()) {
                this.clientSideSpikesAnimation += (0.0F - this.clientSideSpikesAnimation) * 0.25F;
            } else {
                this.clientSideSpikesAnimation += (1.0F - this.clientSideSpikesAnimation) * 0.06F;
            }

            if (this.isMoving() && this.isInWater()) {
                Vec3d vec3d = this.getLook(0.0F);

                for (int i = 0; i < 2; ++i) {
                    this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE,
                            this.posX + (this.rand.nextDouble() - 0.5D) * this.width - vec3d.x * 1.5D,
                            this.posY + this.rand.nextDouble() * this.height - vec3d.y * 1.5D,
                            this.posZ + (this.rand.nextDouble() - 0.5D) * this.width - vec3d.z * 1.5D, 0.0D, 0.0D,
                            0.0D);
                }
            }

            if (this.hasTargetedEntity()) {
                if (this.clientSideAttackTime < this.getAttackDuration()) {
                    ++this.clientSideAttackTime;
                }

                EntityLivingBase entitylivingbase = this.getTargetedEntity();

                if (entitylivingbase != null) {
                    this.getLookHelper().setLookPositionWithEntity(entitylivingbase, 90.0F, 90.0F);
                    this.getLookHelper().onUpdateLook();
                    double d5 = this.getAttackAnimationScale(0.0F);
                    double d0 = entitylivingbase.posX - this.posX;
                    double d1 =
                            entitylivingbase.posY + entitylivingbase.height * 0.5F - (this.posY + this.getEyeHeight());
                    double d2 = entitylivingbase.posZ - this.posZ;
                    double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                    d0 = d0 / d3;
                    d1 = d1 / d3;
                    d2 = d2 / d3;
                    double d4 = this.rand.nextDouble();

                    while (d4 < d3) {
                        d4 += 1.8D - d5 + this.rand.nextDouble() * (1.7D - d5);
                        this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + d0 * d4,
                                this.posY + d1 * d4 + this.getEyeHeight(), this.posZ + d2 * d4, 0.0D, 0.0D, 0.0D);
                    }
                }
            }
        }

        if (this.inWater) {
            this.setAir(300);
        } else if (this.onGround) {
            this.motionY += 0.5D;
            this.motionX += (this.rand.nextFloat() * 2.0F - 1.0F) * 0.4F;
            this.motionZ += (this.rand.nextFloat() * 2.0F - 1.0F) * 0.4F;
            this.rotationYaw = this.rand.nextFloat() * 360.0F;
            this.onGround = false;
            this.isAirBorne = true;
        }

        if (this.hasTargetedEntity()) {
            this.rotationYaw = this.rotationYawHead;
        }

        super.onLivingUpdate();
    }

    protected SoundEvent getFlopSound() {
        return SoundEvents.ENTITY_GUARDIAN_FLOP;
    }

    public boolean isMoving() {
        return this.dataManager.get(MOVING);
    }

    private void setMoving(boolean moving) {
        this.dataManager.set(MOVING, moving);
    }

    public boolean hasTargetedEntity() {
        return this.dataManager.get(TARGET_ENTITY) != 0;
    }

    public int getAttackDuration() {
        return 80;
    }

    @Nullable
    public EntityLivingBase getTargetedEntity() {
        if (!this.hasTargetedEntity()) {
            return null;
        }
        if (this.world.isRemote) {
            if (this.targetedEntity != null) {
                return this.targetedEntity;
            }
            Entity entity = this.world.getEntityByID(this.dataManager.get(TARGET_ENTITY));

            if (entity instanceof EntityLivingBase) {
                this.targetedEntity = (EntityLivingBase) entity;
                return this.targetedEntity;
            }
            return null;
        }
        return this.getAttackTarget();
    }

    private void setTargetedEntity(int entityId) {
        this.dataManager.set(TARGET_ENTITY, entityId);
    }

    public float getAttackAnimationScale(float p_175477_1_) {
        return (this.clientSideAttackTime + p_175477_1_) / this.getAttackDuration();
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (!this.isMoving() && !source.isMagicDamage() && source.getImmediateSource() instanceof EntityLivingBase) {
            EntityLivingBase entitylivingbase = (EntityLivingBase) source.getImmediateSource();

            if (!source.isExplosion()) {
                entitylivingbase.attackEntityFrom(DamageSource.causeThornsDamage(this), 2.0F);
            }
        }

        if (this.wander != null) {
            this.wander.makeUpdate();
        }

        return super.attackEntityFrom(source, amount);
    }

    @Override
    public float getBlockPathWeight(BlockPos pos) {
        return this.world.getBlockState(pos).getMaterial() == Material.WATER ?
                10.0F + this.world.getLightBrightness(pos) - 0.5F : super.getBlockPathWeight(pos);
    }

    /**
     * Checks to make sure the light is not too bright where the mob is spawning
     */
    @Override
    protected boolean isValidLightLevel() {
        return true;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    @Override
    public boolean getCanSpawnHere() {
        return (this.rand.nextInt(20) == 0 || !this.world.canBlockSeeSky(new BlockPos(this))) &&
                super.getCanSpawnHere();
    }

    @SideOnly(Side.CLIENT)
    public float getTailAnimation(float p_175471_1_) {
        return this.clientSideTailAnimationO +
                (this.clientSideTailAnimation - this.clientSideTailAnimationO) * p_175471_1_;
    }

    @SideOnly(Side.CLIENT)
    public float getSpikesAnimation(float p_175469_1_) {
        return this.clientSideSpikesAnimationO +
                (this.clientSideSpikesAnimation - this.clientSideSpikesAnimationO) * p_175469_1_;
    }

    @Override
    public void travel(float p_191986_1_, float p_191986_2_, float p_191986_3_) {
        if (this.isServerWorld() && this.isInWater()) {
            this.moveRelative(p_191986_1_, p_191986_2_, p_191986_3_, 0.1F);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.8999999761581421D;
            this.motionY *= 0.8999999761581421D;
            this.motionZ *= 0.8999999761581421D;

            if (!this.isMoving() && this.getAttackTarget() == null) {
                this.motionY -= 0.005D;
            }
        } else {
            super.travel(p_191986_1_, p_191986_2_, p_191986_3_);
        }
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        super.notifyDataManagerChange(key);

        if (Objects.equals(TARGET_ENTITY, key)) {
            this.clientSideAttackTime = 0;
            this.targetedEntity = null;
        }
    }

    @Override
    public void takumiExplode() {
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.WATER_M;
    }

    @Override
    public int getExplosionPower() {
        return 4;
    }

    @Override
    public int getSecondaryColor() {
        return 0x000088;
    }

    @Override
    public boolean isCustomSpawn() {
        return true;
    }

    @Override
    public String getRegisterName() {
        return "seaguardiancreeper";
    }

    @Override
    public int getRegisterID() {
        return 242;
    }

    static class AIGuardianAttack extends EntityAIBase {

        private final EntitySeaGuardianCreeper guardian;
        private int tickCounter;

        public AIGuardianAttack(EntitySeaGuardianCreeper guardian) {
            this.guardian = guardian;
            this.setMutexBits(3);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        @Override
        public boolean shouldExecute() {
            EntityLivingBase entitylivingbase = this.guardian.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isEntityAlive();
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        @Override
        public boolean shouldContinueExecuting() {
            return super.shouldContinueExecuting() &&
                    this.guardian.getDistanceSqToEntity(this.guardian.getAttackTarget()) > 9.0D;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        @Override
        public void startExecuting() {
            this.tickCounter = -10;
            this.guardian.getNavigator().clearPathEntity();
            this.guardian.getLookHelper().setLookPositionWithEntity(this.guardian.getAttackTarget(), 90.0F, 90.0F);
            this.guardian.isAirBorne = true;
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        @Override
        public void resetTask() {
            this.guardian.setTargetedEntity(0);
            this.guardian.setAttackTarget(null);
            this.guardian.wander.makeUpdate();
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        @Override
        public void updateTask() {
            EntityLivingBase entitylivingbase = this.guardian.getAttackTarget();
            this.guardian.getNavigator().clearPathEntity();
            this.guardian.getLookHelper().setLookPositionWithEntity(entitylivingbase, 90.0F, 90.0F);

            if (!this.guardian.canEntityBeSeen(entitylivingbase)) {
                this.guardian.setAttackTarget(null);
            } else {
                ++this.tickCounter;

                if (this.tickCounter == 0) {
                    this.guardian.setTargetedEntity(this.guardian.getAttackTarget().getEntityId());
                    this.guardian.world.setEntityState(this.guardian, (byte) 21);
                } else if (this.tickCounter >= this.guardian.getAttackDuration()) {
                    float f = 1.0F;

                    if (this.guardian.world.getDifficulty() == EnumDifficulty.HARD) {
                        f += 2.0F;
                    }

                    entitylivingbase.attackEntityFrom(
                            DamageSource.causeIndirectMagicDamage(this.guardian, this.guardian), f);
                    entitylivingbase.attackEntityFrom(DamageSource.causeMobDamage(this.guardian),
                            (float) this.guardian.getEntityAttribute(
                                    SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
                    this.guardian.world.createExplosion(this.guardian, entitylivingbase.posX, entitylivingbase.posY,
                            entitylivingbase.posZ, this.guardian.getPowered() ? 2.5f : 1.25f, true);
                    this.guardian.setAttackTarget(null);
                }

                super.updateTask();
            }
        }
    }

    static class GuardianMoveHelper extends EntityMoveHelper {

        private final EntitySeaGuardianCreeper entityGuardian;

        public GuardianMoveHelper(EntitySeaGuardianCreeper guardian) {
            super(guardian);
            this.entityGuardian = guardian;
        }

        @Override
        public void onUpdateMoveHelper() {
            if (this.action == Action.MOVE_TO && !this.entityGuardian.getNavigator().noPath()) {
                double d0 = this.posX - this.entityGuardian.posX;
                double d1 = this.posY - this.entityGuardian.posY;
                double d2 = this.posZ - this.entityGuardian.posZ;
                double d3 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                d1 = d1 / d3;
                float f = (float) (MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
                this.entityGuardian.rotationYaw = this.limitAngle(this.entityGuardian.rotationYaw, f, 90.0F);
                this.entityGuardian.renderYawOffset = this.entityGuardian.rotationYaw;
                float f1 = (float) (this.speed * this.entityGuardian.getEntityAttribute(
                        SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
                this.entityGuardian.setAIMoveSpeed(
                        this.entityGuardian.getAIMoveSpeed() + (f1 - this.entityGuardian.getAIMoveSpeed()) * 0.125F);
                double d4 =
                        Math.sin((this.entityGuardian.ticksExisted + this.entityGuardian.getEntityId()) * 0.5D) * 0.05D;
                double d5 = Math.cos(this.entityGuardian.rotationYaw * 0.017453292F);
                double d6 = Math.sin(this.entityGuardian.rotationYaw * 0.017453292F);
                this.entityGuardian.motionX += d4 * d5;
                this.entityGuardian.motionZ += d4 * d6;
                d4 = Math.sin((this.entityGuardian.ticksExisted + this.entityGuardian.getEntityId()) * 0.75D) * 0.05D;
                this.entityGuardian.motionY += d4 * (d6 + d5) * 0.25D;
                this.entityGuardian.motionY += this.entityGuardian.getAIMoveSpeed() * d1 * 0.1D;
                EntityLookHelper entitylookhelper = this.entityGuardian.getLookHelper();
                double d7 = this.entityGuardian.posX + d0 / d3 * 2.0D;
                double d8 = this.entityGuardian.getEyeHeight() + this.entityGuardian.posY + d1 / d3;
                double d9 = this.entityGuardian.posZ + d2 / d3 * 2.0D;
                double d10 = entitylookhelper.getLookPosX();
                double d11 = entitylookhelper.getLookPosY();
                double d12 = entitylookhelper.getLookPosZ();

                if (!entitylookhelper.getIsLooking()) {
                    d10 = d7;
                    d11 = d8;
                    d12 = d9;
                }

                this.entityGuardian.getLookHelper().setLookPosition(d10 + (d7 - d10) * 0.125D,
                        d11 + (d8 - d11) * 0.125D, d12 + (d9 - d12) * 0.125D, 10.0F, 40.0F);
                this.entityGuardian.setMoving(true);
            } else {
                this.entityGuardian.setAIMoveSpeed(0.0F);
                this.entityGuardian.setMoving(false);
            }
        }
    }

    static class GuardianTargetSelector implements Predicate<EntityLivingBase> {

        private final EntitySeaGuardianCreeper parentEntity;

        public GuardianTargetSelector(EntitySeaGuardianCreeper guardian) {
            this.parentEntity = guardian;
        }

        @Override
        public boolean apply(
                @Nullable
                        EntityLivingBase p_apply_1_) {
            return p_apply_1_ instanceof EntityPlayer && p_apply_1_.getDistanceSqToEntity(this.parentEntity) > 9.0D;
        }
    }
}
