package com.tntmodders.takumi.entity.mobs;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public abstract class EntityAbstractSpellCreeper extends EntityAbstractIllagerCreeper {
    
    private static final DataParameter <Byte> SPELL = EntityDataManager.createKey(EntityAbstractSpellCreeper.class, DataSerializers.BYTE);
    protected int spellTicks;
    private SpellType activeSpell = SpellType.NONE;
    
    public EntityAbstractSpellCreeper(World worldIn) {
        super(worldIn);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(SPELL, (byte) 0);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IllagerArmPose getArmPose() {
        return this.isSpellcasting() ? IllagerArmPose.SPELLCASTING : IllagerArmPose.CROSSED;
    }
    
    public boolean isSpellcasting() {
        if (this.world.isRemote) {
            return this.dataManager.get(SPELL) > 0;
        }
        return this.spellTicks > 0;
    }
    
    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("SpellTicks", this.spellTicks);
    }
    
    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.spellTicks = compound.getInteger("SpellTicks");
    }
    
    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        super.onUpdate();
        
        if (this.world.isRemote && this.isSpellcasting()) {
            SpellType EntityAbstractSpellCreeper$spelltype = this.getSpellType();
            double d0 = EntityAbstractSpellCreeper$spelltype.particleSpeed[0];
            double d1 = EntityAbstractSpellCreeper$spelltype.particleSpeed[1];
            double d2 = EntityAbstractSpellCreeper$spelltype.particleSpeed[2];
            float f = this.renderYawOffset * 0.017453292F + MathHelper.cos((float) this.ticksExisted * 0.6662F) * 0.25F;
            float f1 = MathHelper.cos(f);
            float f2 = MathHelper.sin(f);
            this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + (double) f1 * 0.6D, this.posY + 1.8D, this.posZ + (double) f2 * 0.6D,
                    d0, d1, d2);
            this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX - (double) f1 * 0.6D, this.posY + 1.8D, this.posZ - (double) f2 * 0.6D,
                    d0, d1, d2);
        }
    }
    
    protected SpellType getSpellType() {
        return !this.world.isRemote ? this.activeSpell : SpellType.getFromId(this.dataManager.get(SPELL));
    }
    
    public void setSpellType(SpellType p_193081_1_) {
        this.activeSpell = p_193081_1_;
        this.dataManager.set(SPELL, (byte) p_193081_1_.id);
    }
    
    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        
        if (this.spellTicks > 0) {
            --this.spellTicks;
        }
    }
    
    protected int getSpellTicks() {
        return this.spellTicks;
    }
    
    protected abstract SoundEvent getSpellSound();
    
    public enum SpellType {
        NONE(0, 0.0D, 0.0D, 0.0D), SUMMON_VEX(1, 0.7D, 0.7D, 0.8D), FANGS(2, 0.4D, 0.3D, 0.35D), WOLOLO(3, 0.7D, 0.5D, 0.2D), DISAPPEAR(4, 0.3D,
                0.3D, 0.8D), BLINDNESS(5, 0.1D, 0.1D, 0.2D);
        
        private final int id;
        private final double[] particleSpeed;
        
        SpellType(int p_i47561_3_, double p_i47561_4_, double p_i47561_6_, double p_i47561_8_) {
            this.id = p_i47561_3_;
            this.particleSpeed = new double[]{p_i47561_4_, p_i47561_6_, p_i47561_8_};
        }
        
        public static SpellType getFromId(int p_193337_0_) {
            for (SpellType EntityAbstractSpellCreeper$spelltype : values()) {
                if (p_193337_0_ == EntityAbstractSpellCreeper$spelltype.id) {
                    return EntityAbstractSpellCreeper$spelltype;
                }
            }
            
            return NONE;
        }
    }
    
    public class AICastingApell extends EntityAIBase {
        
        public AICastingApell() {
            this.setMutexBits(3);
        }
        
        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        @Override
        public boolean shouldExecute() {
            return EntityAbstractSpellCreeper.this.getSpellTicks() > 0;
        }
        
        /**
         * Execute a one shot task or start executing a continuous task
         */
        @Override
        public void startExecuting() {
            super.startExecuting();
            EntityAbstractSpellCreeper.this.navigator.clearPathEntity();
        }
        
        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        @Override
        public void resetTask() {
            super.resetTask();
            EntityAbstractSpellCreeper.this.setSpellType(SpellType.NONE);
        }
        
        /**
         * Keep ticking a continuous task that has already been started
         */
        @Override
        public void updateTask() {
            if (EntityAbstractSpellCreeper.this.getAttackTarget() != null) {
                EntityAbstractSpellCreeper.this.getLookHelper().setLookPositionWithEntity(EntityAbstractSpellCreeper.this.getAttackTarget(),
                        (float) EntityAbstractSpellCreeper.this.getHorizontalFaceSpeed(), (float) EntityAbstractSpellCreeper.this
                                .getVerticalFaceSpeed());
            }
        }
    }
    
    public abstract class AIUseSpell extends EntityAIBase {
        
        protected int spellWarmup;
        protected int spellCooldown;
        
        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        @Override
        public boolean shouldExecute() {
            return EntityAbstractSpellCreeper.this.getAttackTarget() != null && !EntityAbstractSpellCreeper.this.isSpellcasting() &&
                    EntityAbstractSpellCreeper.this.ticksExisted >= this.spellCooldown;
        }
        
        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        @Override
        public boolean shouldContinueExecuting() {
            return EntityAbstractSpellCreeper.this.getAttackTarget() != null && this.spellWarmup > 0;
        }
        
        /**
         * Execute a one shot task or start executing a continuous task
         */
        @Override
        public void startExecuting() {
            this.spellWarmup = this.getCastWarmupTime();
            EntityAbstractSpellCreeper.this.spellTicks = this.getCastingTime();
            this.spellCooldown = EntityAbstractSpellCreeper.this.ticksExisted + this.getCastingInterval();
            SoundEvent soundevent = this.getSpellPrepareSound();
            
            if (soundevent != null) {
                EntityAbstractSpellCreeper.this.playSound(soundevent, 1.0F, 1.0F);
            }
            
            EntityAbstractSpellCreeper.this.setSpellType(this.getSpellType());
        }
        
        /**
         * Keep ticking a continuous task that has already been started
         */
        @Override
        public void updateTask() {
            --this.spellWarmup;
            
            if (this.spellWarmup == 0) {
                this.castSpell();
                EntityAbstractSpellCreeper.this.playSound(EntityAbstractSpellCreeper.this.getSpellSound(), 1.0F, 1.0F);
            }
        }
        
        protected abstract void castSpell();
        
        protected int getCastWarmupTime() {
            return 20;
        }
        
        protected abstract int getCastingTime();
        
        protected abstract int getCastingInterval();
        
        @Nullable
        protected abstract SoundEvent getSpellPrepareSound();
        
        protected abstract SpellType getSpellType();
    }
}
