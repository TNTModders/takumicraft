package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.client.render.RenderPhantomCreeper;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.block.Block;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityPhantomCreeper extends EntityTakumiAbstractCreeper {

    protected static final DataParameter<Byte> VEX_FLAGS =
            EntityDataManager.createKey(EntityPhantomCreeper.class, DataSerializers.BYTE);
    private EntityLiving owner;
    @Nullable
    private BlockPos boundOrigin;

    public EntityPhantomCreeper(World worldIn) {
        super(worldIn);
        this.moveHelper = new AIMoveControl(this);
        this.setSize(1.3F, 0.7F);
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
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.taskEntries.clear();
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(8, new AIMoveRandom());
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, EntityPhantomCreeper.class));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(5.0D);
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
    public void fall(float distance, float damageMultiplier) {
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
        return EnumTakumiType.NORMAL_D;
    }

    @Override
    public int getExplosionPower() {
        return 0;
    }

    @Override
    public int getSecondaryColor() {
        return 0x333366;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "phantomcreeper";
    }

    @Override
    public int getRegisterID() {
        return 305;
    }

    @Override
    public Object getRender(RenderManager manager) {
        return new RenderPhantomCreeper<>(manager);
    }

    @Override
    public int getPrimaryColor() {
        return 0x336633;
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        super.onUpdate();
        this.setNoGravity(true);
        if (this.getAttackTarget() == null) {
            EntityPlayer player = this.world.getNearestAttackablePlayer(this, 100, 256);
            if (player != null) {
                this.setAttackTarget(player);
            }
        } else {
            if (this.ticksExisted % 60 == 0) {
                this.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0F, 0.5F);
                if (!this.world.isRemote) {
                    double dx = this.posX - this.getAttackTarget().posX;
                    double dz = this.posZ - this.getAttackTarget().posZ;
                    double dy = this.posY - this.getAttackTarget().posY;

                    if (dx * dx + dz * dz < 25 && dy < 20) {
                        this.world.createExplosion(this, this.posX, this.posY, this.posZ, 0f, false);
                        EntityZombieCreeper creeper = new EntityZombieCreeper(this.world);
                        creeper.setPosition(this.posX, this.posY, this.posZ);
                        creeper.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.ELYTRA));
                        creeper.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
                        creeper.setAttackTarget(this.getAttackTarget());
                        this.world.spawnEntity(creeper);
                    }
                }
            }
        }
    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }

    @Override
    public boolean getCanSpawnHere() {
        return super.getCanSpawnHere() && this.posY > this.world.getSeaLevel();
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
            return EntityPhantomCreeper.this.getAttackTarget() != null &&
                    !EntityPhantomCreeper.this.getMoveHelper().isUpdating() && EntityPhantomCreeper.this.rand.nextInt(7) == 0 &&
                    EntityPhantomCreeper.this.getDistanceSqToEntity(EntityPhantomCreeper.this.getAttackTarget()) > 4.0D;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        @Override
        public boolean shouldContinueExecuting() {
            return EntityPhantomCreeper.this.getMoveHelper().isUpdating() && EntityPhantomCreeper.this.isCharging() &&
                    EntityPhantomCreeper.this.getAttackTarget() != null &&
                    EntityPhantomCreeper.this.getAttackTarget().isEntityAlive();
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        @Override
        public void startExecuting() {
            EntityLivingBase entitylivingbase = EntityPhantomCreeper.this.getAttackTarget();
            Vec3d vec3d = entitylivingbase.getPositionEyes(1.0F);
            EntityPhantomCreeper.this.moveHelper.setMoveTo(vec3d.x, vec3d.y, vec3d.z, 1.0D);
            EntityPhantomCreeper.this.setIsCharging(true);
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        @Override
        public void resetTask() {
            EntityPhantomCreeper.this.setIsCharging(false);
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        @Override
        public void updateTask() {
            EntityLivingBase entitylivingbase = EntityPhantomCreeper.this.getAttackTarget();

            if (EntityPhantomCreeper.this.getEntityBoundingBox().intersects(entitylivingbase.getEntityBoundingBox())) {
                EntityPhantomCreeper.this.attackEntityAsMob(entitylivingbase);
                EntityPhantomCreeper.this.setIsCharging(false);
            } else {
                double d0 = EntityPhantomCreeper.this.getDistanceSqToEntity(entitylivingbase);

                if (d0 < 9.0D) {
                    Vec3d vec3d = entitylivingbase.getPositionEyes(1.0F);
                    EntityPhantomCreeper.this.moveHelper.setMoveTo(vec3d.x, vec3d.y, vec3d.z, 1.0D);
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
            EntityPhantomCreeper.this.setAttackTarget(EntityPhantomCreeper.this.owner.getAttackTarget());
            super.startExecuting();
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        @Override
        public boolean shouldExecute() {
            return EntityPhantomCreeper.this.owner != null && EntityPhantomCreeper.this.owner.getAttackTarget() != null &&
                    this.isSuitableTarget(EntityPhantomCreeper.this.owner.getAttackTarget(), false);
        }


    }

    class AIMoveControl extends EntityMoveHelper {

        public AIMoveControl(EntityPhantomCreeper vex) {
            super(vex);
        }

        @Override
        public void onUpdateMoveHelper() {
            if (this.action == Action.MOVE_TO) {
                double d0 = this.posX - EntityPhantomCreeper.this.posX;
                double d1 = this.posY - EntityPhantomCreeper.this.posY;
                double d2 = this.posZ - EntityPhantomCreeper.this.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                d3 = MathHelper.sqrt(d3);

                if (d3 < EntityPhantomCreeper.this.getEntityBoundingBox().getAverageEdgeLength()) {
                    this.action = Action.WAIT;
                    EntityPhantomCreeper.this.motionX *= 0.5D;
                    EntityPhantomCreeper.this.motionY *= 0.5D;
                    EntityPhantomCreeper.this.motionZ *= 0.5D;
                } else {
                    EntityPhantomCreeper.this.motionX += d0 / d3 * 0.05D * this.speed * 2;
                    EntityPhantomCreeper.this.motionY += d1 / d3 * 0.05D * this.speed * 1.25;
                    EntityPhantomCreeper.this.motionZ += d2 / d3 * 0.05D * this.speed * 2;

                    if (EntityPhantomCreeper.this.getAttackTarget() == null) {
                        EntityPhantomCreeper.this.rotationYaw = -((float) MathHelper.atan2(EntityPhantomCreeper.this.motionX,
                                EntityPhantomCreeper.this.motionZ)) * (180F / (float) Math.PI);
                    } else {
                        double d4 = EntityPhantomCreeper.this.getAttackTarget().posX - EntityPhantomCreeper.this.posX;
                        double d5 = EntityPhantomCreeper.this.getAttackTarget().posZ - EntityPhantomCreeper.this.posZ;
                        EntityPhantomCreeper.this.rotationYaw =
                                -((float) MathHelper.atan2(d4, d5)) * (180F / (float) Math.PI);
                    }
                    EntityPhantomCreeper.this.renderYawOffset = EntityPhantomCreeper.this.rotationYaw;
                }
            }
        }
    }

    class AIMoveRandom extends EntityAIBase {
        private int groundTick = -1;

        public AIMoveRandom() {
            this.setMutexBits(1);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        @Override
        public boolean shouldExecute() {
            return !EntityPhantomCreeper.this.getMoveHelper().isUpdating() && EntityPhantomCreeper.this.rand.nextInt(7) == 0;
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
            BlockPos blockpos = EntityPhantomCreeper.this.getBoundOrigin();

            if (blockpos == null) {
                blockpos = new BlockPos(EntityPhantomCreeper.this);
                EntityPhantomCreeper.this.setBoundOrigin(blockpos);
            }

            if (EntityPhantomCreeper.this.getAttackTarget() != null) {
                blockpos = EntityPhantomCreeper.this.getAttackTarget().getPosition();
                blockpos.up(EntityPhantomCreeper.this.rand.nextInt(4));
            }

            int r = EntityPhantomCreeper.this.getAttackTarget() != null ? 1 : 10;
            double y = EntityPhantomCreeper.this.rand.nextDouble() * r - (r / 2);
            World world = EntityPhantomCreeper.this.world;
            BlockPos pos = EntityPhantomCreeper.this.getPosition().down();
            if (this.groundTick >= 0 || EntityPhantomCreeper.this.onGround
                    || world.getBlockState(pos).getCollisionBoundingBox(world, pos) != Block.NULL_AABB
                    || EntityPhantomCreeper.this.world.collidesWithAnyBlock(EntityPhantomCreeper.this.getEntityBoundingBox().expand(1, 1, 1))) {

                y += EntityPhantomCreeper.this.rand.nextDouble() * r;
                this.groundTick++;
                if (this.groundTick > 10) {
                    this.groundTick = -1;
                }
            }

            blockpos = blockpos.add(r * Math.cos(EntityPhantomCreeper.this.ticksExisted), y, r * Math.sin(EntityPhantomCreeper.this.ticksExisted));

            for (int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.add(EntityPhantomCreeper.this.rand.nextInt(15) - 7,
                        EntityPhantomCreeper.this.rand.nextInt(7) - 3, EntityPhantomCreeper.this.rand.nextInt(15) - 7);
                if (EntityPhantomCreeper.this.world.isAirBlock(blockpos1)) {
                    EntityPhantomCreeper.this.moveHelper.setMoveTo((double) blockpos1.getX() + 0.5D,
                            (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 0.25D);
                    if (EntityPhantomCreeper.this.getAttackTarget() == null) {
                        EntityPhantomCreeper.this.getLookHelper().setLookPosition((double) blockpos1.getX() + 0.5D,
                                (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }

                    break;
                }
            }
        }
    }
}
