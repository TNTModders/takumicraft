package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.client.render.RenderSkeletonCreeper;
import com.tntmodders.takumi.core.TakumiItemCore;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityStrayCreeper extends EntitySkeletonCreeper {
    
    public EntityStrayCreeper(World worldIn) {
        super(worldIn);
    }
    
    @Override
    public boolean getCanSpawnHere() {
        return super.getCanSpawnHere() && this.world.canSeeSky(new BlockPos(this));
    }
    
    @Override
    public int getPrimaryColor() {
        return 11184895;
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_STRAY_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_STRAY_DEATH;
    }
    
    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        if (this.rand.nextInt(5) == 0 && !this.world.isRemote) {
            this.entityDropItem(this.getHeldItem(EnumHand.OFF_HAND), 0);
        }
    }
    
    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.ENTITY_STRAY_STEP;
    }
    
    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
        super.attackEntityWithRangedAttack(target, distanceFactor);
        this.setEquipmentBasedOnDifficulty(this.world.getDifficultyForLocation(new BlockPos(this)));
    }
    
    @Override
    protected EntityArrow getArrow(float v) {
        return TakumiItemCore.TAKUMI_ARROW_HA.createArrow(this.world, new ItemStack(TakumiItemCore.TAKUMI_ARROW_HA), this);
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_STRAY_AMBIENT;
    }
    
    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        super.setEquipmentBasedOnDifficulty(difficulty);
        ItemStack stack = new ItemStack(Items.POTIONITEM);
        PotionType type = null;
        while (type == null || type.hasInstantEffect() || type.getEffects().isEmpty()) {
            type = PotionType.REGISTRY.getRandomObject(this.rand);
        }
        PotionUtils.addPotionToItemStack(stack, type);
        this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, stack);
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
        return EnumTakumiType.NORMAL_M;
    }
    
    @Override
    public int getExplosionPower() {
        return 3;
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
        return "straycreeper";
    }
    
    @Override
    public int getRegisterID() {
        return 204;
    }
    
    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        if (this.getHeldItem(EnumHand.OFF_HAND).getItem() == Items.POTIONITEM) {
            PotionEffect effect = new PotionEffect(PotionUtils.getPotionFromItem(this.getHeldItem(EnumHand.OFF_HAND)).getEffects().get(0).getPotion
                    (), 400);
            for (Entity entity : event.getAffectedEntities()) {
                if (entity instanceof EntityLivingBase) {
                    ((EntityLivingBase) entity).addPotionEffect(effect);
                }
            }
        }
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public Object getRender(RenderManager manager) {
        return new RenderSkeletonCreeper <>(manager);
    }
}
