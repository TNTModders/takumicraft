package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.client.render.RenderVergerCreeper;
import com.tntmodders.takumi.core.TakumiWorldCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.item.EntityVergerBall;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityVergerCreeper extends EntityTakumiAbstractCreeper {
    private static final DataParameter<Byte> ON_FIRE =
            EntityDataManager.createKey(EntityVergerCreeper.class, DataSerializers.BYTE);
    private float heightOffset = 0.5F;
    private int heightOffsetUpdateTime;

    public EntityVergerCreeper(World worldIn) {
        super(worldIn);
        this.isImmuneToFire = true;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(ON_FIRE, (byte) 0);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAICreeperSwell(this));
        this.tasks.addTask(4, new AIFireballAttack(this));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D, 0.0F));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.33000000417232513D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(8.0D);
    }

    public boolean isCharged() {
        return (this.dataManager.get(ON_FIRE) & 1) != 0;
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    public void setOnFire(boolean onFire) {
        byte b0 = this.dataManager.get(ON_FIRE);

        if (onFire) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 = (byte) (b0 & -2);
        }

        this.dataManager.set(ON_FIRE, b0);
    }

    @Override
    protected void updateAITasks() {
        --this.heightOffsetUpdateTime;

        if (this.heightOffsetUpdateTime <= 0) {
            this.heightOffsetUpdateTime = 100;
            this.heightOffset = 0.5F + (float) this.rand.nextGaussian() * 3.0F;
        }

        EntityLivingBase entitylivingbase = this.getAttackTarget();

        if (entitylivingbase != null && entitylivingbase.posY + (double) entitylivingbase.getEyeHeight() >
                this.posY + (double) this.getEyeHeight() + (double) this.heightOffset) {
            this.motionY += (0.30000001192092896D - this.motionY) * 0.30000001192092896D;
            this.isAirBorne = true;
        }

        super.updateAITasks();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender() {
        return 15728880;
    }

    @Override
    public float getBrightness() {
        return 1.0F;
    }

    @Override
    public void onLivingUpdate() {
        if (this.world.provider.getDimensionType().getId() != TakumiWorldCore.TAKUMI_WORLD.getId()) {
            this.setDead();
        }
        if (!this.onGround && this.motionY < 0.0D) {
            this.motionY *= 0.6D;
        }

        if (this.world.isRemote) {
        }

        super.onLivingUpdate();
    }

    @Override
    protected boolean isValidLightLevel() {
        return true;
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            this.world.newExplosion(this, this.posX, this.posY, this.posZ, 3 * (this.getPowered() ? 2 : 1), true, true);
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL_MD;
    }

    @Override
    public int getExplosionPower() {
        return 0;
    }

    @Override
    public int getSecondaryColor() {
        return 0x882299;
    }

    @Override
    public boolean isCustomSpawn() {
        return true;
    }

    @Override
    public String getRegisterName() {
        return "vergercreeper";
    }

    @Override
    public int getRegisterID() {
        return 310;
    }

    @Override
    public void customSpawn() {
    }

    @Override
    public int getPrimaryColor() {
        return 0x330022;
    }

    @Override
    public Object getRender(RenderManager manager) {
        return new RenderVergerCreeper<>(manager);
    }

    static class AIFireballAttack extends EntityAIBase {

        private final EntityVergerCreeper blaze;
        private int attackStep;
        private int attackTime;

        public AIFireballAttack(EntityVergerCreeper blazeIn) {
            this.blaze = blazeIn;
            this.setMutexBits(3);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        @Override
        public boolean shouldExecute() {
            EntityLivingBase entitylivingbase = this.blaze.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isEntityAlive();
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        @Override
        public void startExecuting() {
            this.attackStep = 0;
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        @Override
        public void resetTask() {
            this.blaze.setOnFire(false);
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        @Override
        public void updateTask() {
            --this.attackTime;
            EntityLivingBase entitylivingbase = this.blaze.getAttackTarget();
            double d0 = this.blaze.getDistanceSqToEntity(entitylivingbase);

            if (d0 < 4.0D) {
                if (this.attackTime <= 0) {
                    this.attackTime = 20;
                    this.blaze.attackEntityAsMob(entitylivingbase);
                }

                this.blaze.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY,
                        entitylivingbase.posZ, 1.0D);
            } else if (d0 < this.getFollowDistance() * this.getFollowDistance()) {
                double d1 = entitylivingbase.posX - this.blaze.posX;
                double d2 = entitylivingbase.getEntityBoundingBox().minY + (double) (entitylivingbase.height / 2.0F) -
                        (this.blaze.posY + (double) (this.blaze.height / 2.0F));
                double d3 = entitylivingbase.posZ - this.blaze.posZ;

                if (this.attackTime <= 0) {
                    ++this.attackStep;

                    if (this.attackStep == 1) {
                        this.attackTime = 60;
                        this.blaze.setOnFire(true);
                    } else if (this.attackStep <= 2) {
                        this.attackTime = 6;
                    } else {
                        this.attackTime = 100;
                        this.attackStep = 0;
                        this.blaze.setOnFire(false);
                    }

                    if (this.attackStep > 1) {
                        float f = MathHelper.sqrt(MathHelper.sqrt(d0)) * 0.5F;
                        this.blaze.world.playEvent(null, 1018,
                                new BlockPos((int) this.blaze.posX, (int) this.blaze.posY, (int) this.blaze.posZ), 0);

                        EntityVergerBall entitysmallfireball =
                                new EntityVergerBall(this.blaze.world, this.blaze, d1, d2 * 3, d3);
                        entitysmallfireball.posY = this.blaze.posY + (double) (this.blaze.height / 2.0F) + 0.5D;
                        this.blaze.world.spawnEntity(entitysmallfireball);
                    }
                }

                this.blaze.getLookHelper().setLookPositionWithEntity(entitylivingbase, 10.0F, 10.0F);
            } else {
                this.blaze.getNavigator().clearPathEntity();
                this.blaze.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY,
                        entitylivingbase.posZ, 1.0D);
            }

            super.updateTask();
        }

        private double getFollowDistance() {
            IAttributeInstance iattributeinstance = this.blaze.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
            return iattributeinstance == null ? 8.0D : iattributeinstance.getAttributeValue();
        }
    }

}
