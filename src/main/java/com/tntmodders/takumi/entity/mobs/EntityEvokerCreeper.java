package com.tntmodders.takumi.entity.mobs;

import com.google.common.base.Predicate;
import com.tntmodders.takumi.client.render.RenderEvokerCreeper;
import com.tntmodders.takumi.entity.item.EntityEvokerCreeperFangs;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

import javax.annotation.Nullable;
import java.util.List;

public class EntityEvokerCreeper extends EntityAbstractSpellCreeper {

    private EntitySheep wololoTarget;

    public EntityEvokerCreeper(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.95F);
        this.experienceValue = 10;
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();

        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new AICastingSpell());
        this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityPlayer.class, 8.0F, 0.6D, 1.0D));
        this.tasks.addTask(4, new AISummonSpell());
        this.tasks.addTask(5, new AIAttackSpell());
        this.tasks.addTask(6, new AIWololoSpell());
        this.tasks.addTask(8, new EntityAIWander(this, 0.6D));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, EntityEvokerCreeper.class));
        this.targetTasks.addTask(2,
                new EntityAINearestAttackableTarget(this, EntityPlayer.class, true).setUnseenMemoryTicks(300));
        this.targetTasks.addTask(3,
                new EntityAINearestAttackableTarget(this, EntityVillager.class, false).setUnseenMemoryTicks(300));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, false));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(12.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(12.0D);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_EVOCATION_ILLAGER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.EVOCATION_ILLAGER_DEATH;
    }

    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_EVOCATION_ILLAGER;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        super.onUpdate();
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
    }

    @Override
    protected SoundEvent getSpellSound() {
        return SoundEvents.EVOCATION_ILLAGER_CAST_SPELL;
    }

    /**
     * Returns whether this Entity is on the same team as the given Entity.
     */
    @Override
    public boolean isOnSameTeam(Entity entityIn) {
        if (entityIn == null) {
            return false;
        }
        if (entityIn == this) {
            return true;
        }
        if (super.isOnSameTeam(entityIn)) {
            return true;
        }
        if (entityIn instanceof EntityVexCreeper) {
            return this.isOnSameTeam(((EntityVexCreeper) entityIn).getOwner());
        }
        return entityIn instanceof EntityLivingBase &&
                ((EntityLivingBase) entityIn).getCreatureAttribute() == EnumCreatureAttribute.ILLAGER &&
                this.getTeam() == null && entityIn.getTeam() == null;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_EVOCATION_ILLAGER_AMBIENT;
    }

    @Nullable
    private EntitySheep getWololoTarget() {
        return this.wololoTarget;
    }

    private void setWololoTarget(
            @Nullable
                    EntitySheep p_190748_1_) {
        this.wololoTarget = p_190748_1_;
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
        return 0xbb99bb;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "evokercreeper";
    }

    @Override
    public int getRegisterID() {
        return 249;
    }

    @Override
    public Object getRender(RenderManager manager) {
        return new RenderEvokerCreeper<>(manager);
    }

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        if (!(damageSrc.getTrueSource() instanceof EntityVexCreeper)) {
            super.damageEntity(damageSrc, damageAmount);
        }
    }

    class AIAttackSpell extends AIUseSpell {

        private AIAttackSpell() {
            super();
        }

        @Override
        protected int getCastingTime() {
            return 40;
        }

        @Override
        protected int getCastingInterval() {
            return 100;
        }

        @Override
        protected void castSpell() {
            EntityLivingBase entitylivingbase = EntityEvokerCreeper.this.getAttackTarget();
            double d0 = Math.min(entitylivingbase.posY, EntityEvokerCreeper.this.posY);
            double d1 = Math.max(entitylivingbase.posY, EntityEvokerCreeper.this.posY) + 1.0D;
            float f = (float) MathHelper.atan2(entitylivingbase.posZ - EntityEvokerCreeper.this.posZ,
                    entitylivingbase.posX - EntityEvokerCreeper
                            .this.posX);

            if (EntityEvokerCreeper.this.getDistanceSqToEntity(entitylivingbase) < 9.0D) {
                for (int i = 0; i < 5; ++i) {
                    float f1 = f + (float) i * (float) Math.PI * 0.4F;
                    this.spawnFangs(EntityEvokerCreeper.this.posX + (double) MathHelper.cos(f1) * 1.5D,
                            EntityEvokerCreeper.this.posZ + (double) MathHelper.sin(f1) * 1.5D, d0, d1, f1, 0);
                }

                for (int k = 0; k < 8; ++k) {
                    float f2 = f + (float) k * (float) Math.PI * 2.0F / 8.0F + (float) Math.PI * 2F / 5F;
                    this.spawnFangs(EntityEvokerCreeper.this.posX + (double) MathHelper.cos(f2) * 2.5D,
                            EntityEvokerCreeper.this.posZ + (double) MathHelper.sin(f2) * 2.5D, d0, d1, f2, 3);
                }
            } else {
                for (int l = 0; l < 16; ++l) {
                    double d2 = 1.25D * (double) (l + 1);
                    this.spawnFangs(EntityEvokerCreeper.this.posX + (double) MathHelper.cos(f) * d2,
                            EntityEvokerCreeper.this.posZ + (double) MathHelper.sin(f) * d2, d0, d1, f, l);
                }
            }
        }

        private void spawnFangs(double p_190876_1_, double p_190876_3_, double p_190876_5_, double p_190876_7_,
                float p_190876_9_, int p_190876_10_) {
            BlockPos blockpos = new BlockPos(p_190876_1_, p_190876_7_, p_190876_3_);
            boolean flag = false;
            double d0 = 0.0D;

            while (true) {
                if (!EntityEvokerCreeper.this.world.isBlockNormalCube(blockpos, true) &&
                        EntityEvokerCreeper.this.world.isBlockNormalCube(blockpos.down(), true)) {
                    if (!EntityEvokerCreeper.this.world.isAirBlock(blockpos)) {
                        IBlockState iblockstate = EntityEvokerCreeper.this.world.getBlockState(blockpos);
                        AxisAlignedBB axisalignedbb =
                                iblockstate.getCollisionBoundingBox(EntityEvokerCreeper.this.world, blockpos);

                        if (axisalignedbb != null) {
                            d0 = axisalignedbb.maxY;
                        }
                    }

                    flag = true;
                    break;
                }

                blockpos = blockpos.down();

                if (blockpos.getY() < MathHelper.floor(p_190876_5_) - 1) {
                    break;
                }
            }

            if (flag) {
                EntityEvokerCreeperFangs EntityEvokerCreeperfangs =
                        new EntityEvokerCreeperFangs(EntityEvokerCreeper.this.world, p_190876_1_,
                                (double) blockpos.getY() + d0, p_190876_3_, p_190876_9_, p_190876_10_,
                                EntityEvokerCreeper.this);
                EntityEvokerCreeper.this.world.spawnEntity(EntityEvokerCreeperfangs);
            }
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOCATION_ILLAGER_PREPARE_ATTACK;
        }

        @Override
        protected SpellType getSpellType() {
            return SpellType.FANGS;
        }
    }

    class AICastingSpell extends AICastingApell {

        private AICastingSpell() {
            super();
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        @Override
        public void updateTask() {
            if (EntityEvokerCreeper.this.getAttackTarget() != null) {
                EntityEvokerCreeper.this.getLookHelper().setLookPositionWithEntity(
                        EntityEvokerCreeper.this.getAttackTarget(),
                        (float) EntityEvokerCreeper.this.getHorizontalFaceSpeed(),
                        (float) EntityEvokerCreeper.this.getVerticalFaceSpeed());
            } else if (EntityEvokerCreeper.this.getWololoTarget() != null) {
                EntityEvokerCreeper.this.getLookHelper().setLookPositionWithEntity(
                        EntityEvokerCreeper.this.getWololoTarget(),
                        (float) EntityEvokerCreeper.this.getHorizontalFaceSpeed(),
                        (float) EntityEvokerCreeper.this.getVerticalFaceSpeed());
            }
        }
    }

    class AISummonSpell extends AIUseSpell {

        private AISummonSpell() {
            super();
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        @Override
        public boolean shouldExecute() {
            if (!super.shouldExecute()) {
                return false;
            }
            int i = EntityEvokerCreeper.this.world.getEntitiesWithinAABB(EntityVexCreeper.class,
                    EntityEvokerCreeper.this.getEntityBoundingBox().grow(16.0D)).size();
            return EntityEvokerCreeper.this.rand.nextInt(8) + 1 > i;
        }

        @Override
        protected int getCastingTime() {
            return 100;
        }

        @Override
        protected int getCastingInterval() {
            return 340;
        }

        @Override
        protected void castSpell() {
            for (int i = 0; i < 3; ++i) {
                BlockPos blockpos =
                        new BlockPos(EntityEvokerCreeper.this).add(-2 + EntityEvokerCreeper.this.rand.nextInt(5), 1,
                                -2 + EntityEvokerCreeper.this.rand.nextInt(5));
                EntityVexCreeper entityvex = new EntityVexCreeper(EntityEvokerCreeper.this.world);
                entityvex.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
                entityvex.onInitialSpawn(EntityEvokerCreeper.this.world.getDifficultyForLocation(blockpos), null);
                entityvex.setOwner(EntityEvokerCreeper.this);
                entityvex.setBoundOrigin(blockpos);
                entityvex.setLimitedLife(20 * (30 + EntityEvokerCreeper.this.rand.nextInt(90)));
                EntityEvokerCreeper.this.world.spawnEntity(entityvex);
            }
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOCATION_ILLAGER_PREPARE_SUMMON;
        }

        @Override
        protected SpellType getSpellType() {
            return SpellType.SUMMON_VEX;
        }
    }

    public class AIWololoSpell extends AIUseSpell {

        final Predicate<EntitySheep> wololoSelector = p_apply_1_ -> p_apply_1_.getFleeceColor() == EnumDyeColor.BLUE;

        public AIWololoSpell() {
            super();
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        @Override
        public void resetTask() {
            super.resetTask();
            EntityEvokerCreeper.this.setWololoTarget(null);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        @Override
        public boolean shouldExecute() {
            if (EntityEvokerCreeper.this.getAttackTarget() != null) {
                return false;
            }
            if (EntityEvokerCreeper.this.isSpellcasting()) {
                return false;
            }
            if (EntityEvokerCreeper.this.ticksExisted < this.spellCooldown) {
                return false;
            }
            if (!EntityEvokerCreeper.this.world.getGameRules().getBoolean("mobGriefing")) {
                return false;
            }
            List<EntitySheep> list = EntityEvokerCreeper.this.world.getEntitiesWithinAABB(EntitySheep.class,
                    EntityEvokerCreeper.this.getEntityBoundingBox().grow(16.0D, 4.0D, 16.0D), this.wololoSelector);

            if (list.isEmpty()) {
                return false;
            }
            EntityEvokerCreeper.this.setWololoTarget(list.get(EntityEvokerCreeper.this.rand.nextInt(list.size())));
            return true;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        @Override
        public boolean shouldContinueExecuting() {
            return EntityEvokerCreeper.this.getWololoTarget() != null && this.spellWarmup > 0;
        }


        @Override
        protected void castSpell() {
            EntitySheep entitysheep = EntityEvokerCreeper.this.getWololoTarget();

            if (entitysheep != null && entitysheep.isEntityAlive()) {
                EntitySheepCreeper sheepCreeper = new EntitySheepCreeper(world);
                sheepCreeper.copyLocationAndAnglesFrom(entitysheep);
                sheepCreeper.setRainbow(true);
                TakumiUtils.takumiSetPowered(sheepCreeper, true);
                if (world.spawnEntity(sheepCreeper)) {
                    entitysheep.setDead();
                }
            }
        }

        @Override
        protected int getCastWarmupTime() {
            return 40;
        }

        @Override
        protected int getCastingTime() {
            return 60;
        }

        @Override
        protected int getCastingInterval() {
            return 140;
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOCATION_ILLAGER_PREPARE_WOLOLO;
        }

        @Override
        protected SpellType getSpellType() {
            return SpellType.WOLOLO;
        }
    }
}
