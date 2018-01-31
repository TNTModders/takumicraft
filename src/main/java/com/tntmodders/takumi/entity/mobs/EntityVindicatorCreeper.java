package com.tntmodders.takumi.entity.mobs;

import com.google.common.base.Predicate;
import com.tntmodders.takumi.client.render.RenderVindicatorCreeper;
import com.tntmodders.takumi.core.TakumiItemCore;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicReference;

public class EntityVindicatorCreeper extends EntityAbstractIllagerCreeper {
    
    private static final Predicate <Entity> JOHNNY_SELECTOR = p_apply_1_ -> p_apply_1_ instanceof EntityLivingBase && ((EntityLivingBase)
            p_apply_1_).attackable();
    private boolean johnny;
    
    public EntityVindicatorCreeper(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.95F);
    }
    
    @Override
    public Object getRender(RenderManager manager) {
        return new RenderVindicatorCreeper <>(manager);
    }
    
    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        AtomicReference <EntityAIBase> base = new AtomicReference <>();
        this.tasks.taskEntries.forEach(entityAITaskEntry -> {
            if (entityAITaskEntry.action instanceof EntityAICreeperSwell) {
                base.set(entityAITaskEntry.action);
            }
        });
        this.tasks.removeTask(base.get());
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(8, new EntityAIWander(this, 0.6D));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, EntityVindicator.class));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityVillager.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
        this.targetTasks.addTask(4, new AIJohnnyAttack(this));
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IllagerArmPose getArmPose() {
        return this.isAggressive() ? IllagerArmPose.ATTACKING : IllagerArmPose.CROSSED;
    }
    
    @SideOnly(Side.CLIENT)
    public boolean isAggressive() {
        return this.isAggressive(1);
    }
    
    public void setAggressive(boolean p_190636_1_) {
        this.setAggressive(1, p_190636_1_);
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3499999940395355D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(12.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(12.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7.5D);
    }
    
    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        
        if (this.johnny) {
            compound.setBoolean("Johnny", true);
        }
    }
    
    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        
        if (compound.hasKey("Johnny", 99)) {
            this.johnny = compound.getBoolean("Johnny");
        }
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_VINDICATION_ILLAGER_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VINDICATION_ILLAGER_DEATH;
    }
    
    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        if (!this.world.isRemote) {
            this.world.createExplosion(this, entityIn.posX, entityIn.posY, entityIn.posZ, 2.5f, true);
        }
        return super.attackEntityAsMob(entityIn);
    }
    
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_VINDICATION_ILLAGER;
    }
    
    /**
     * Returns whether this Entity is on the same team as the given Entity.
     */
    @Override
    public boolean isOnSameTeam(Entity entityIn) {
        return super.isOnSameTeam(entityIn) || entityIn instanceof EntityLivingBase && ((EntityLivingBase) entityIn).getCreatureAttribute() ==
                EnumCreatureAttribute.ILLAGER && this.getTeam() == null && entityIn.getTeam() == null;
    }
    
    /**
     * Sets the custom name tag for this entity
     */
    @Override
    public void setCustomNameTag(String name) {
        super.setCustomNameTag(name);
        
        if (!this.johnny && "Johnny".equals(name)) {
            this.johnny = true;
        }
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VINDICATION_ILLAGER_AMBIENT;
    }
    
    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        this.setAggressive(this.getAttackTarget() != null);
    }
    
    /**
     * Gives armor or weapon for entity based on given DifficultyInstance
     */
    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(TakumiItemCore.TAKUMI_SWORD));
        this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(TakumiItemCore.TAKUMI_SHIELD));
    }
    
    /**
     * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
     * when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
     */
    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        IEntityLivingData ientitylivingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setEquipmentBasedOnDifficulty(difficulty);
        this.setEnchantmentBasedOnDifficulty(difficulty);
        return ientitylivingdata;
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
        return 0x998899;
    }
    
    @Override
    public boolean isCustomSpawn() {
        return false;
    }
    
    @Override
    public String getRegisterName() {
        return "vindicatorcreeper";
    }
    
    @Override
    public int getRegisterID() {
        return 248;
    }
    
    static class AIJohnnyAttack extends EntityAINearestAttackableTarget <EntityLivingBase> {
        
        public AIJohnnyAttack(EntityVindicatorCreeper vindicator) {
            super(vindicator, EntityLivingBase.class, 0, true, true, JOHNNY_SELECTOR);
        }
        
        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        @Override
        public boolean shouldExecute() {
            return ((EntityVindicatorCreeper) this.taskOwner).johnny && super.shouldExecute();
        }
    }
}
