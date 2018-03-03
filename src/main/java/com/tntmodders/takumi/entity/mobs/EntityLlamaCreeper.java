package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.client.render.RenderLlamaCreeper;
import com.tntmodders.takumi.core.TakumiEntityCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.item.EntityLlamaCreeperSpit;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import javax.annotation.Nullable;

public class EntityLlamaCreeper extends EntityTakumiAbstractCreeper implements IRangedAttackMob {

    private boolean didSpit;

    public EntityLlamaCreeper(World worldIn) {
        super(worldIn);
        this.setSize(0.9F, 1.87F);
    }

    protected SoundEvent getAngrySound() {
        return SoundEvents.ENTITY_LLAMA_ANGRY;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_LLAMA_AMBIENT;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(SoundEvents.ENTITY_LLAMA_STEP, 0.15F, 1.0F);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(3, new EntityAIAttackRanged(this, 1.25D, 40, 20.0F));
        this.tasks.addTask(3, new EntityAIPanic(this, 1.2D));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.7D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new AIHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
        this.targetTasks.addTask(2, new AIDefendTarget(this));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40.0D);
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        int i = MathHelper.ceil((distance * 0.5F - 3.0F) * damageMultiplier);

        if (i > 0) {
            if (distance >= 6.0F) {
                this.attackEntityFrom(DamageSource.FALL, (float) i);

                if (this.isBeingRidden()) {
                    for (Entity entity : this.getRecursivePassengers()) {
                        entity.attackEntityFrom(DamageSource.FALL, (float) i);
                    }
                }
            }

            BlockPos pos = new BlockPos(this.posX, this.posY - 0.2D - (double) this.prevRotationYaw, this.posZ);
            IBlockState iblockstate = this.world.getBlockState(pos);
            Block block = iblockstate.getBlock();

            if (iblockstate.getMaterial() != Material.AIR && !this.isSilent()) {
                SoundType soundtype = block.getSoundType(iblockstate, world, pos, this);
                this.world.playSound(null, this.posX, this.posY, this.posZ, soundtype.getStepSound(),
                        this.getSoundCategory(), soundtype.getVolume() * 0.5F, soundtype.getPitch() * 0.75F);
            }
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_LLAMA_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_LLAMA_DEATH;
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_LLAMA;
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
        this.spit(target);
    }

    private void spit(EntityLivingBase target) {
        EntityLlamaCreeperSpit EntityLlamaCreeperspit = new EntityLlamaCreeperSpit(this.world, this);
        double d0 = target.posX - this.posX;
        double d1 = target.getEntityBoundingBox().minY + (double) (target.height / 3.0F) - EntityLlamaCreeperspit.posY;
        double d2 = target.posZ - this.posZ;
        float f = MathHelper.sqrt(d0 * d0 + d2 * d2) * 0.2F;
        EntityLlamaCreeperspit.setThrowableHeading(d0, d1 + (double) f, d2, 1.5F, 10.0F);
        this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LLAMA_SPIT,
                this.getSoundCategory(), 1.0F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
        this.world.spawnEntity(EntityLlamaCreeperspit);
        this.setDidSpit(true);
    }

    private void setDidSpit(boolean didSpitIn) {
        this.didSpit = didSpitIn;
    }

    @Override
    public void setSwingingArms(boolean swingingArms) {
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
        return EnumTakumiType.NORMAL;
    }

    @Override
    public int getExplosionPower() {
        return 3;
    }

    @Override
    public int getSecondaryColor() {
        return 0x55ff55;
    }

    @Override
    public boolean isCustomSpawn() {
        return true;
    }

    @Override
    public String getRegisterName() {
        return "llamacreeper";
    }

    @Override
    public int getRegisterID() {
        return 245;
    }

    @Override
    public void customSpawn() {
        EntityRegistry.addSpawn(this.getClass(), this.takumiRank().getSpawnWeight() * 25, 5, 20,
                TakumiEntityCore.CREATURE_TAKUMI, TakumiEntityCore.biomes.toArray(new Biome[0]));
    }

    @Override
    public boolean isAnimal() {
        return true;
    }

    @Override
    public int getPrimaryColor() {
        return 0x88ff88;
    }

    @Override
    public Object getRender(RenderManager manager) {
        return new RenderLlamaCreeper<>(manager);
    }

    static class AIDefendTarget extends EntityAINearestAttackableTarget<EntityWolf> {

        public AIDefendTarget(EntityLlamaCreeper llama) {
            super(llama, EntityWolf.class, 16, false, true, null);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        @Override
        public boolean shouldExecute() {
            if (super.shouldExecute() && this.targetEntity != null && !this.targetEntity.isTamed()) {
                return true;
            }
            this.taskOwner.setAttackTarget(null);
            return false;
        }

        @Override
        protected double getTargetDistance() {
            return super.getTargetDistance() * 0.25D;
        }
    }

    static class AIHurtByTarget extends EntityAIHurtByTarget {

        public AIHurtByTarget(EntityLlamaCreeper llama) {
            super(llama, false);
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        @Override
        public boolean shouldContinueExecuting() {
            if (this.taskOwner instanceof EntityLlamaCreeper) {
                EntityLlamaCreeper EntityLlamaCreeper = (EntityLlamaCreeper) this.taskOwner;

                if (EntityLlamaCreeper.didSpit) {
                    EntityLlamaCreeper.setDidSpit(false);
                    return false;
                }
            }

            return super.shouldContinueExecuting();
        }
    }

    static class GroupData implements IEntityLivingData {

        public int variant;

        private GroupData(int variantIn) {
            this.variant = variantIn;
        }
    }
}
