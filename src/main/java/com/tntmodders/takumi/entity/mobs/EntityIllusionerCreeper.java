package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.client.render.RenderIllusionerCreeper;
import com.tntmodders.takumi.core.TakumiItemCore;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class EntityIllusionerCreeper extends EntityAbstractSpellCreeper implements IRangedAttackMob {

    private final Vec3d[][] renderLocations;
    private int ghostTime;

    public EntityIllusionerCreeper(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.95F);
        this.experienceValue = 5;
        this.renderLocations = new Vec3d[2][4];

        for (int i = 0; i < 4; ++i) {
            this.renderLocations[0][i] = new Vec3d(0.0D, 0.0D, 0.0D);
            this.renderLocations[1][i] = new Vec3d(0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.taskEntries.clear();
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new AICastingApell());
        this.tasks.addTask(4, new AIMirriorSpell());
        this.tasks.addTask(5, new AIBlindnessSpell());
        this.tasks.addTask(6, new EntityAIAttackRangedBow(this, 0.5D, 20, 15.0F));
        this.tasks.addTask(8, new EntityAIWander(this, 0.6D));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, EntityIllusionerCreeper.class));
        this.targetTasks.addTask(2,
                new EntityAINearestAttackableTarget(this, EntityPlayer.class, true).setUnseenMemoryTicks(300));
        this.targetTasks.addTask(3,
                new EntityAINearestAttackableTarget(this, EntityVillager.class, false).setUnseenMemoryTicks(300));
        this.targetTasks.addTask(3,
                new EntityAINearestAttackableTarget(this, EntityIronGolem.class, false).setUnseenMemoryTicks(300));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(18.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(16.0D);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_ILLUSION_ILLAGER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ILLAGER_DEATH;
    }

    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_EVOCATION_ILLAGER;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IllagerArmPose getArmPose() {
        if (this.isSpellcasting()) {
            return IllagerArmPose.SPELLCASTING;
        }
        return this.isAggressive() ? IllagerArmPose.BOW_AND_ARROW : IllagerArmPose.CROSSED;
    }

    @SideOnly(Side.CLIENT)
    public boolean isAggressive() {
        return this.isAggressive(1);
    }

    public void setAggressive(boolean flg) {
        this.setAggressive(1, flg);
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        this.setAggressive(1, this.getAttackTarget() != null);
    }

    @Override
    protected SoundEvent getSpellSound() {
        return SoundEvents.ENTITY_ILLAGER_CAST_SPELL;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.isInvisible()) {
            --this.ghostTime;

            if (this.ghostTime < 0) {
                this.ghostTime = 0;
            }

            if (this.hurtTime != 1 && this.ticksExisted % 1200 != 0) {
                if (this.hurtTime == this.maxHurtTime - 1) {
                    this.ghostTime = 3;
                    for (int k = 0; k < 4; ++k) {
                        if (!this.world.isRemote) {
                            this.world.createExplosion(this, this.posX + this.renderLocations[0][k].x,
                                    this.posY + this.renderLocations[0][k].y, this.posZ + this.renderLocations[0][k].z,
                                    2f, true);
                        }
                        this.renderLocations[0][k] = this.renderLocations[1][k];
                        this.renderLocations[1][k] = new Vec3d(0.0D, 0.0D, 0.0D);
                    }
                }
            } else {
                this.ghostTime = 3;
                float f = -6.0F;
                int i = 13;

                for (int j = 0; j < 4; ++j) {
                    this.renderLocations[0][j] = this.renderLocations[1][j];
                    this.renderLocations[1][j] = new Vec3d((double) (-6.0F + (float) this.rand.nextInt(13)) * 0.5D,
                            (double) Math.max(0, this.rand.nextInt(6) - 4),
                            (double) (-6.0F + (float) this.rand.nextInt(13)) * 0.5D);
                }

                for (int l = 0; l < 16; ++l) {
                    this.world.spawnParticle(EnumParticleTypes.CLOUD,
                            this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width,
                            this.posY + this.rand.nextDouble() * (double) this.height,
                            this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width, 0.0D, 0.0D, 0.0D);
                }

                this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ILLAGER_MIRROR_MOVE,
                        this.getSoundCategory(), 1.0F, 1.0F, false);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public Vec3d[] getRenderLocations(float p_193098_1_) {
        if (this.ghostTime <= 0) {
            return this.renderLocations[1];
        }
        double d0 = (double) (((float) this.ghostTime - p_193098_1_) / 3.0F);
        d0 = Math.pow(d0, 0.25D);
        Vec3d[] avec3d = new Vec3d[4];

        for (int i = 0; i < 4; ++i) {
            avec3d[i] = this.renderLocations[1][i].scale(1.0D - d0).add(this.renderLocations[0][i].scale(d0));
        }

        return avec3d;
    }

    /**
     * Returns whether this Entity is on the same team as the given Entity.
     */
    @Override
    public boolean isOnSameTeam(Entity entityIn) {
        return super.isOnSameTeam(entityIn) || entityIn instanceof EntityLivingBase &&
                ((EntityLivingBase) entityIn).getCreatureAttribute() == EnumCreatureAttribute.ILLAGER &&
                this.getTeam() == null && entityIn.getTeam() == null;
    }

    /**
     * Gets the bounding box of this Entity, adjusted to take auxiliary entities into account (e.g. the tile contained
     * by a minecart, such as a command block).
     */
    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return this.getEntityBoundingBox().grow(3.0D, 0.0D, 3.0D);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ILLUSION_ILLAGER_AMBIENT;
    }

    /**
     * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
     * when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
     */
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(TakumiItemCore.TAKUMI_BOW));
        this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND,
                new ItemStack(TakumiItemCore.TAKUMI_ARROW_HA, this.rand.nextInt(15) + 1));
        return super.onInitialSpawn(difficulty, livingdata);
    }

    /**
     * Attack the specified entity using a ranged attack.
     */
    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
        EntityArrow entityarrow = this.createArrowEntity(distanceFactor);
        double d0 = target.posX - this.posX;
        double d1 = target.getEntityBoundingBox().minY + (double) (target.height / 3.0F) - entityarrow.posY;
        double d2 = target.posZ - this.posZ;
        double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
        entityarrow.setThrowableHeading(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F,
                (float) (14 - this.world.getDifficulty().getDifficultyId() * 4));
        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.world.spawnEntity(entityarrow);
    }

    protected EntityArrow createArrowEntity(float p_193097_1_) {
        return TakumiItemCore.TAKUMI_ARROW_HA
                .createArrow(this.world, new ItemStack(TakumiItemCore.TAKUMI_ARROW_HA), this);
    }

    @Override
    public void setSwingingArms(boolean swingingArms) {
        this.setAggressive(1, swingingArms);
    }

    @Override
    public void takumiExplode() {
    }

    @Override
    public int getExplosionPower() {
        return 0;
    }

    @Override
    public int getSecondaryColor() {
        return 0xffaaff;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "illusionercreeper";
    }

    @Override
    public int getRegisterID() {
        return 250;
    }

    @Override
    public Object getRender(RenderManager manager) {
        return new RenderIllusionerCreeper<>(manager);
    }

    class AIBlindnessSpell extends AIUseSpell {

        private int lastTargetId;

        private AIBlindnessSpell() {
            super();
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        @Override
        public boolean shouldExecute() {
            return super.shouldExecute() && EntityIllusionerCreeper.this.getAttackTarget() != null &&
                    EntityIllusionerCreeper.this.getAttackTarget().getEntityId() != this.lastTargetId &&
                    EntityIllusionerCreeper.this.world
                            .getDifficultyForLocation(new BlockPos(EntityIllusionerCreeper.this))
                            .isHarderThan((float) EnumDifficulty.NORMAL.ordinal());
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        @Override
        public void startExecuting() {
            super.startExecuting();
            this.lastTargetId = EntityIllusionerCreeper.this.getAttackTarget().getEntityId();
        }

        @Override
        protected int getCastingTime() {
            return 20;
        }

        @Override
        protected int getCastingInterval() {
            return 180;
        }

        @Override
        protected void castSpell() {
            EntityIllusionerCreeper.this.getAttackTarget().addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 400));
            if (!EntityIllusionerCreeper.this.world.isRemote) {
                EntityIllusionerCreeper.this.world
                        .createExplosion(EntityIllusionerCreeper.this, EntityIllusionerCreeper.this.posX,
                                EntityIllusionerCreeper.this.posY, EntityIllusionerCreeper.this.posZ, 3f, true);
            }
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ENTITY_ILLAGER_PREPARE_BLINDNESS;
        }

        @Override
        protected SpellType getSpellType() {
            return SpellType.BLINDNESS;
        }
    }

    class AIMirriorSpell extends AIUseSpell {

        private AIMirriorSpell() {
            super();
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        @Override
        public boolean shouldExecute() {
            return super.shouldExecute() && !EntityIllusionerCreeper.this.isPotionActive(MobEffects.INVISIBILITY);
        }

        @Override
        protected int getCastingTime() {
            return 20;
        }

        @Override
        protected int getCastingInterval() {
            return 340;
        }

        @Override
        protected void castSpell() {
            EntityIllusionerCreeper.this.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 1200));
        }

        @Override
        @Nullable
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ENTITY_ILLAGER_PREPARE_MIRROR;
        }

        @Override
        protected SpellType getSpellType() {
            return SpellType.DISAPPEAR;
        }
    }
}
