package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.client.render.RenderSquidCreeper;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class EntitySquidCreeper extends EntityTakumiAbstractCreeper {

    public float squidPitch;
    public float prevSquidPitch;
    public float squidYaw;
    public float prevSquidYaw;
    public float squidRotation;
    public float prevSquidRotation;
    public float tentacleAngle;
    public float lastTentacleAngle;
    private float randomMotionSpeed;
    private float rotationVelocity;
    private float rotateSpeed;
    private float randomMotionVecX;
    private float randomMotionVecY;
    private float randomMotionVecZ;

    public EntitySquidCreeper(World worldIn) {
        super(worldIn);
        this.setSize(0.8F, 0.8F);
        this.rand.setSeed((long) (1 + this.getEntityId()));
        this.rotationVelocity = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
    }

    //from EntitySquid
    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntitySquidCreeper.AIMoveRandom(this));
        this.tasks.addTask(2, new EntityAICreeperSwell(this));
        this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityOcelot.class, 6.0F, 1.0D, 1.2D));
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 0.8D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_SQUID_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SQUID_DEATH;
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_SQUID;
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
    public boolean isPushedByWater() {
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
        super.onLivingUpdate();
        this.prevSquidPitch = this.squidPitch;
        this.prevSquidYaw = this.squidYaw;
        this.prevSquidRotation = this.squidRotation;
        this.lastTentacleAngle = this.tentacleAngle;
        this.squidRotation += this.rotationVelocity;

        if ((double) this.squidRotation > (Math.PI * 2D)) {
            if (this.world.isRemote) {
                this.squidRotation = ((float) Math.PI * 2F);
            } else {
                this.squidRotation = (float) ((double) this.squidRotation - (Math.PI * 2D));

                if (this.rand.nextInt(10) == 0) {
                    this.rotationVelocity = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
                }

                this.world.setEntityState(this, (byte) 19);
            }
        }

        if (this.inWater) {
            if (this.squidRotation < (float) Math.PI) {
                float f = this.squidRotation / (float) Math.PI;
                this.tentacleAngle = MathHelper.sin(f * f * (float) Math.PI) * (float) Math.PI * 0.25F;

                if ((double) f > 0.75D) {
                    this.randomMotionSpeed = 1.0F;
                    this.rotateSpeed = 1.0F;
                } else {
                    this.rotateSpeed *= 0.8F;
                }
            } else {
                this.tentacleAngle = 0.0F;
                this.randomMotionSpeed *= 0.9F;
                this.rotateSpeed *= 0.99F;
            }

            if (!this.world.isRemote) {
                this.motionX = (double) (this.randomMotionVecX * this.randomMotionSpeed);
                this.motionY = (double) (this.randomMotionVecY * this.randomMotionSpeed);
                this.motionZ = (double) (this.randomMotionVecZ * this.randomMotionSpeed);
            }

            float f1 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.renderYawOffset += (-((float) MathHelper.atan2(this.motionX, this.motionZ)) * (180F / (float) Math.PI) - this.renderYawOffset) * 0.1F;
            this.rotationYaw = this.renderYawOffset;
            this.squidYaw = (float) ((double) this.squidYaw + Math.PI * (double) this.rotateSpeed * 1.5D);
            this.squidPitch += (-((float) MathHelper.atan2((double) f1, this.motionY)) * (180F / (float) Math.PI) - this.squidPitch) * 0.1F;
        } else {
            this.tentacleAngle = MathHelper.abs(MathHelper.sin(this.squidRotation)) * (float) Math.PI * 0.25F;

            if (!this.world.isRemote) {
                this.motionX = 0.0D;
                this.motionZ = 0.0D;

                if (this.isPotionActive(MobEffects.LEVITATION)) {
                    this.motionY += 0.05D * (double) (this.getActivePotionEffect(MobEffects.LEVITATION).getAmplifier() + 1) - this.motionY;
                } else if (!this.hasNoGravity()) {
                    this.motionY -= 0.08D;
                }

                this.motionY *= 0.9800000190734863D;
            }

            this.squidPitch = (float) ((double) this.squidPitch + (double) (-90.0F - this.squidPitch) * 0.02D);
        }
    }

    @Override
    protected boolean isValidLightLevel() {
        return true;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    @Override
    public boolean getCanSpawnHere() {
        return this.posY > 45.0D && this.posY < (double) this.world.getSeaLevel();
    }

    public void setMovementVector(float randomMotionVecXIn, float randomMotionVecYIn, float randomMotionVecZIn) {
        this.randomMotionVecX = randomMotionVecXIn;
        this.randomMotionVecY = randomMotionVecYIn;
        this.randomMotionVecZ = randomMotionVecZIn;
    }

    public boolean hasMovementVector() {
        return this.randomMotionVecX != 0.0F || this.randomMotionVecY != 0.0F || this.randomMotionVecZ != 0.0F;
    }

    //from EntityWaterMob
    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    public void travel(float p_191986_1_, float p_191986_2_, float p_191986_3_) {
        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
    }

    @Override
    public int getTalkInterval() {
        return 120;
    }

    @Override
    public void onEntityUpdate() {
        int i = this.getAir();
        super.onEntityUpdate();

        if (this.isEntityAlive() && !this.isInWater()) {
            --i;
            this.setAir(i);

            if (this.getAir() == -20) {
                this.setAir(0);
                this.attackEntityFrom(DamageSource.DROWN, 2.0F);
            }
        } else {
            this.setAir(300);
        }
    }

    /**
     * Handler for {@link World#setEntityState}
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 19) {
            this.squidRotation = 0.0F;
        } else {
            super.handleStatusUpdate(id);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SQUID_AMBIENT;
    }

    @Override
    protected boolean canDespawn() {
        return true;
    }

    @Override
    public boolean isNotColliding() {
        return this.world.checkNoEntityCollision(this.getEntityBoundingBox(), this);
    }

    @Override
    public void takumiExplode() {
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
        return 3;
    }

    @Override
    public int getSecondaryColor() {
        return 255;
    }

    @Override
    public boolean isCustomSpawn() {
        return true;
    }

    @Override
    public String getRegisterName() {
        return "squidcreeper";
    }

    @Override
    public int getRegisterID() {
        return 21;
    }

    @Override
    public void customSpawn() {
    }

    @Override
    @SideOnly(Side.CLIENT)
    public RenderLiving getRender(RenderManager manager) {
        return new RenderSquidCreeper(manager);
    }

    static class AIMoveRandom extends EntityAIBase {
        private final EntitySquidCreeper squid;

        public AIMoveRandom(EntitySquidCreeper p_i45859_1_) {
            this.squid = p_i45859_1_;
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
            int i = this.squid.getIdleTime();

            if (i > 100) {
                this.squid.setMovementVector(0.0F, 0.0F, 0.0F);
            } else if (this.squid.getRNG().nextInt(50) == 0 || !this.squid.inWater || !this.squid.hasMovementVector()) {
                float f = this.squid.getRNG().nextFloat() * ((float) Math.PI * 2F);
                float f1 = MathHelper.cos(f) * 0.2F;
                float f2 = -0.1F + this.squid.getRNG().nextFloat() * 0.2F;
                float f3 = MathHelper.sin(f) * 0.2F;
                this.squid.setMovementVector(f1, f2, f3);
            }
        }
    }
}
