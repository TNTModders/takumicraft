package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.core.TakumiItemCore;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Biomes;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import javax.annotation.Nullable;

public class EntityWitherSkeletonCreeper extends EntitySkeletonCreeper {
    public EntityWitherSkeletonCreeper(World worldIn) {
        super(worldIn);
        this.setSize(0.7F, 2.4F);
        this.isImmuneToFire = true;
    }

    @Override
    public void customSpawn() {
        EntityRegistry.addSpawn(this.getClass(), this.takumiRank().getSpawnWeight(), 1, 3, EnumCreatureType.MONSTER, Biomes.HELL);
    }

    @Override
    public int getPrimaryColor() {
        return 26112;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_WITHER_SKELETON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_WITHER_SKELETON_DEATH;
    }

    /**
     * Called when the mob's health reaches 0.
     */
    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);

        if (cause.getTrueSource() instanceof EntityCreeper) {
            EntityCreeper entitycreeper = (EntityCreeper) cause.getTrueSource();

            if (entitycreeper.getPowered() && entitycreeper.isAIEnabled()) {
                entitycreeper.incrementDroppedSkulls();
                this.entityDropItem(new ItemStack(Items.SKULL, 1, 1), 0.0F);
            }
        }
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_WITHER_SKELETON;
    }

    @Override
    public float getEyeHeight() {
        return 2.1F;
    }

    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.ENTITY_WITHER_SKELETON_STEP;
    }

    @Override
    protected EntityArrow getArrow(float p_190726_1_) {
        EntityArrow entityarrow = super.getArrow(p_190726_1_);
        entityarrow.setFire(100);
        return entityarrow;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_WITHER_SKELETON_AMBIENT;
    }

    /**
     * Gives armor or weapon for entity based on given DifficultyInstance
     */
    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(TakumiItemCore.TAKUMI_SWORD));
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        IEntityLivingData ientitylivingdata = super.onInitialSpawn(difficulty, livingdata);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
        this.setCombatTask();
        return ientitylivingdata;
    }

    @Override
    public int getSecondaryColor() {
        return 7846775;
    }

    @Override
    public boolean isCustomSpawn() {
        return true;
    }

    @Override
    public String getRegisterName() {
        return "witherskeletoncreeper";
    }

    @Override
    public int getRegisterID() {
        return 15;
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        for (Entity entity : event.getAffectedEntities()) {
            if (entity instanceof EntityLivingBase) {
                ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.WITHER, 200));
            }
        }
        return true;
    }

    /**
     * Enchants Entity's current equipments based on given DifficultyInstance
     */
    @Override
    protected void setEnchantmentBasedOnDifficulty(DifficultyInstance difficulty) {
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        if (!super.attackEntityAsMob(entityIn)) {
            return false;
        } else {
            if (entityIn instanceof EntityLivingBase) {
                ((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(MobEffects.WITHER, 200));
            }

            return true;
        }
    }
}
