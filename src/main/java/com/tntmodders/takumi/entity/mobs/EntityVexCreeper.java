package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.client.render.RenderVexCreeper;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicReference;

public class EntityVexCreeper extends EntityTakumiAbstractCreeper {

    protected static final DataParameter<Byte> VEX_FLAGS =
            EntityDataManager.createKey(EntityVexCreeper.class, DataSerializers.BYTE);
    private EntityLiving owner;
    @Nullable
    private BlockPos boundOrigin;
    private boolean limitedLifespan;
    private int limitedLifeTicks;

    public EntityVexCreeper(World worldIn) {
        super(worldIn);
        this.isImmuneToFire = true;
        this.moveHelper = new AIMoveControl(this);
        this.setSize(0.4F, 0.8F);
        this.experienceValue = 3;
    }

    public static void registerFixesVex(DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityVexCreeper.class);
    }

    /**
     * Tries to move the entity towards the specified location.
     */
    @Override
    public void move(MoverType type, double x, double y, double z) {
        super.move(type, x, y, z);
        this.doBlockCollisions();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender() {
        return 15728880;
    }

    /**
     * Gets how bright this entity is.
     */
    @Override
    public float getBrightness() {
        return 1.0F;
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        AtomicReference<EntityAIBase> base = new AtomicReference<>();
        this.tasks.taskEntries.forEach(entityAITaskEntry -> {
            if (entityAITaskEntry.action instanceof EntityAICreeperSwell) {
                base.set(entityAITaskEntry.action);
            }
        });
        if (base.get() != null) {
            this.tasks.removeTask(base.get());
        }
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(4, new AIChargeAttack());
        this.tasks.addTask(8, new AIMoveRandom());
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, EntityVexCreeper.class));
        this.targetTasks.addTask(2, new AICopyOwnerTarget(this));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(14.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(VEX_FLAGS, (byte) 0);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);

        if (this.boundOrigin != null) {
            compound.setInteger("BoundX", this.boundOrigin.getX());
            compound.setInteger("BoundY", this.boundOrigin.getY());
            compound.setInteger("BoundZ", this.boundOrigin.getZ());
        }

        if (this.limitedLifespan) {
            compound.setInteger("LifeTicks", this.limitedLifeTicks);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);

        if (compound.hasKey("BoundX")) {
            this.boundOrigin = new BlockPos(compound.getInteger("BoundX"), compound.getInteger("BoundY"),
                    compound.getInteger("BoundZ"));
        }

        if (compound.hasKey("LifeTicks")) {
            this.setLimitedLife(compound.getInteger("LifeTicks"));
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        this.noClip = true;
        super.onUpdate();
        this.noClip = false;
        this.setNoGravity(true);

        if (this.limitedLifespan && --this.limitedLifeTicks <= 0) {
            this.limitedLifeTicks = 20;
            this.attackEntityFrom(DamageSource.STARVE, 1.0F);
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_VEX_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_VEX_DEATH;
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        if (!this.world.isRemote) {
            for (int i = 0; i < (this.getPowered() ? 5 : 3); i++) {
                this.world.createExplosion(this, this.posX, this.posY, this.posZ, 1.5f, true);
            }
        }
        this.setDead();
        return true;
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_VEX;
    }

    public void setLimitedLife(int limitedLifeTicksIn) {
        this.limitedLifespan = true;
        this.limitedLifeTicks = limitedLifeTicksIn;
    }

    public EntityLiving getOwner() {
        return this.owner;
    }

    public void setOwner(EntityLiving ownerIn) {
        this.owner = ownerIn;
    }

    @Nullable
    public BlockPos getBoundOrigin() {
        return this.boundOrigin;
    }

    public void setBoundOrigin(
            @Nullable
                    BlockPos boundOriginIn) {
        this.boundOrigin = boundOriginIn;
    }

    public boolean isCharging() {
        return this.getVexFlag(1);
    }

    private boolean getVexFlag(int p_190656_1_) {
        int i = this.dataManager.get(VEX_FLAGS);
        return (i & p_190656_1_) != 0;
    }

    public void setIsCharging(boolean p_190648_1_) {
        this.setVexFlag(1, p_190648_1_);
    }

    private void setVexFlag(int p_190660_1_, boolean p_190660_2_) {
        int i = this.dataManager.get(VEX_FLAGS);

        if (p_190660_2_) {
            i = i | p_190660_1_;
        } else {
            i = i & ~p_190660_1_;
        }

        this.dataManager.set(VEX_FLAGS, (byte) (i & 255));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_VEX_AMBIENT;
    }

    /**
     * Gives armor or weapon for entity based on given DifficultyInstance
     */
    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        this.setDropChance(EntityEquipmentSlot.MAINHAND, 0.0F);
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
        this.setEquipmentBasedOnDifficulty(difficulty);
        this.setEnchantmentBasedOnDifficulty(difficulty);
        return super.onInitialSpawn(difficulty, livingdata);
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
        return EnumTakumiType.NORMAL_D;
    }

    @Override
    public int getExplosionPower() {
        return 0;
    }

    @Override
    public int getSecondaryColor() {
        return 0x996699;
    }

    @Override
    public boolean isCustomSpawn() {
        return true;
    }

    @Override
    public String getRegisterName() {
        return "vexcreeper";
    }

    @Override
    public int getRegisterID() {
        return 48;
    }

    @Override
    public int getPrimaryColor() {
        return 0x336633;
    }

    @Override
    public Object getRender(RenderManager manager) {
        return new RenderVexCreeper<>(manager);
    }

    class AIChargeAttack extends EntityAIBase {

        public AIChargeAttack() {
            this.setMutexBits(1);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        @Override
        public boolean shouldExecute() {
            return EntityVexCreeper.this.getAttackTarget() != null &&
                    !EntityVexCreeper.this.getMoveHelper().isUpdating() && EntityVexCreeper.this.rand.nextInt(7) == 0 &&
                    EntityVexCreeper.this.getDistanceSqToEntity(EntityVexCreeper.this.getAttackTarget()) > 4.0D;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        @Override
        public boolean shouldContinueExecuting() {
            return EntityVexCreeper.this.getMoveHelper().isUpdating() && EntityVexCreeper.this.isCharging() &&
                    EntityVexCreeper.this.getAttackTarget() != null &&
                    EntityVexCreeper.this.getAttackTarget().isEntityAlive();
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        @Override
        public void startExecuting() {
            EntityLivingBase entitylivingbase = EntityVexCreeper.this.getAttackTarget();
            Vec3d vec3d = entitylivingbase.getPositionEyes(1.0F);
            EntityVexCreeper.this.moveHelper.setMoveTo(vec3d.x, vec3d.y, vec3d.z, 1.0D);
            EntityVexCreeper.this.setIsCharging(true);
            EntityVexCreeper.this.playSound(SoundEvents.ENTITY_VEX_CHARGE, 1.0F, 1.0F);
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        @Override
        public void resetTask() {
            EntityVexCreeper.this.setIsCharging(false);
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        @Override
        public void updateTask() {
            EntityLivingBase entitylivingbase = EntityVexCreeper.this.getAttackTarget();

            if (EntityVexCreeper.this.getEntityBoundingBox().intersects(entitylivingbase.getEntityBoundingBox())) {
                EntityVexCreeper.this.attackEntityAsMob(entitylivingbase);
                EntityVexCreeper.this.setIsCharging(false);
            } else {
                double d0 = EntityVexCreeper.this.getDistanceSqToEntity(entitylivingbase);

                if (d0 < 9.0D) {
                    Vec3d vec3d = entitylivingbase.getPositionEyes(1.0F);
                    EntityVexCreeper.this.moveHelper.setMoveTo(vec3d.x, vec3d.y, vec3d.z, 1.0D);
                }
            }
        }
    }

    class AICopyOwnerTarget extends EntityAITarget {

        public AICopyOwnerTarget(EntityCreature creature) {
            super(creature, false);
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        @Override
        public void startExecuting() {
            EntityVexCreeper.this.setAttackTarget(EntityVexCreeper.this.owner.getAttackTarget());
            super.startExecuting();
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        @Override
        public boolean shouldExecute() {
            return EntityVexCreeper.this.owner != null && EntityVexCreeper.this.owner.getAttackTarget() != null &&
                    this.isSuitableTarget(EntityVexCreeper.this.owner.getAttackTarget(), false);
        }


    }

    class AIMoveControl extends EntityMoveHelper {

        public AIMoveControl(EntityVexCreeper vex) {
            super(vex);
        }

        @Override
        public void onUpdateMoveHelper() {
            if (this.action == Action.MOVE_TO) {
                double d0 = this.posX - EntityVexCreeper.this.posX;
                double d1 = this.posY - EntityVexCreeper.this.posY;
                double d2 = this.posZ - EntityVexCreeper.this.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                d3 = (double) MathHelper.sqrt(d3);

                if (d3 < EntityVexCreeper.this.getEntityBoundingBox().getAverageEdgeLength()) {
                    this.action = Action.WAIT;
                    EntityVexCreeper.this.motionX *= 0.5D;
                    EntityVexCreeper.this.motionY *= 0.5D;
                    EntityVexCreeper.this.motionZ *= 0.5D;
                } else {
                    EntityVexCreeper.this.motionX += d0 / d3 * 0.05D * this.speed;
                    EntityVexCreeper.this.motionY += d1 / d3 * 0.05D * this.speed;
                    EntityVexCreeper.this.motionZ += d2 / d3 * 0.05D * this.speed;

                    if (EntityVexCreeper.this.getAttackTarget() == null) {
                        EntityVexCreeper.this.rotationYaw = -((float) MathHelper
                                .atan2(EntityVexCreeper.this.motionX, EntityVexCreeper.this.motionZ)) *
                                (180F / (float) Math.PI);
                        EntityVexCreeper.this.renderYawOffset = EntityVexCreeper.this.rotationYaw;
                    } else {
                        double d4 = EntityVexCreeper.this.getAttackTarget().posX - EntityVexCreeper.this.posX;
                        double d5 = EntityVexCreeper.this.getAttackTarget().posZ - EntityVexCreeper.this.posZ;
                        EntityVexCreeper.this.rotationYaw =
                                -((float) MathHelper.atan2(d4, d5)) * (180F / (float) Math.PI);
                        EntityVexCreeper.this.renderYawOffset = EntityVexCreeper.this.rotationYaw;
                    }
                }
            }
        }
    }

    class AIMoveRandom extends EntityAIBase {

        public AIMoveRandom() {
            this.setMutexBits(1);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        @Override
        public boolean shouldExecute() {
            return !EntityVexCreeper.this.getMoveHelper().isUpdating() && EntityVexCreeper.this.rand.nextInt(7) == 0;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        @Override
        public boolean shouldContinueExecuting() {
            return false;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        @Override
        public void updateTask() {
            BlockPos blockpos = EntityVexCreeper.this.getBoundOrigin();

            if (blockpos == null) {
                blockpos = new BlockPos(EntityVexCreeper.this);
            }

            for (int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.add(EntityVexCreeper.this.rand.nextInt(15) - 7,
                        EntityVexCreeper.this.rand.nextInt(11) - 5, EntityVexCreeper.this.rand.nextInt(15) - 7);

                if (EntityVexCreeper.this.world.isAirBlock(blockpos1)) {
                    EntityVexCreeper.this.moveHelper
                            .setMoveTo((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D,
                                    (double) blockpos1.getZ() + 0.5D, 0.25D);

                    if (EntityVexCreeper.this.getAttackTarget() == null) {
                        EntityVexCreeper.this.getLookHelper().setLookPosition((double) blockpos1.getX() + 0.5D,
                                (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }

                    break;
                }
            }
        }
    }
}
