package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.client.render.RenderBirdCreeper;
import com.tntmodders.takumi.core.TakumiEntityCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.item.EntityTakumiExpEgg;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import javax.annotation.Nullable;

public class EntityBirdCreeper extends EntityTakumiAbstractCreeper {
    
    public float wingRotation;
    public float destPos;
    public float oFlapSpeed;
    public float oFlap;
    public float wingRotDelta = 1.0F;
    
    public EntityBirdCreeper(World worldIn) {
        super(worldIn);
        this.setSize(0.4F, 0.7F);
        this.setPathPriority(PathNodeType.WATER, 0.0F);
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }
    
    @Override
    public void fall(float distance, float damageMultiplier) {
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_CHICKEN_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_CHICKEN_DEATH;
    }
    
    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_CHICKEN;
    }
    
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.onGround) {
            this.jump();
        }
        this.oFlap = this.wingRotation;
        this.oFlapSpeed = this.destPos;
        this.destPos = (float) (this.destPos + (this.onGround ? -1 : 4) * 0.3D);
        this.destPos = MathHelper.clamp(this.destPos, 0.0F, 1.0F);
        
        if (!this.onGround && this.wingRotDelta < 1.0F) {
            this.wingRotDelta = 1.0F;
        }
        
        this.wingRotDelta = (float) (this.wingRotDelta * 0.9D);
        
        if (!this.onGround && this.motionY < 0.0D) {
            this.motionY *= 0.6D;
        }
        
        this.wingRotation += this.wingRotDelta * 2.0F;
    }
    
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return !source.isExplosion() && super.attackEntityFrom(source, amount);
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_CHICKEN_AMBIENT;
    }
    
    @Nullable
    @Override
    protected Item getDropItem() {
        return Items.COOKED_CHICKEN;
    }
    
    @Override
    public void setDead() {
        if (!(this.getHealth() <= 0 || this.world.getDifficulty() == EnumDifficulty.PEACEFUL)) {
            if (!this.world.isRemote) {
                EntityBirdCreeper birdCreeper = new EntityBirdCreeper(this.world);
                birdCreeper.copyLocationAndAnglesFrom(this);
                if (this.getPowered()) {
                    TakumiUtils.takumiSetPowered(birdCreeper, true);
                }
                this.world.spawnEntity(birdCreeper);
            }
        }
        super.setDead();
    }
    
    @Override
    public void customSpawn() {
        EntityRegistry.addSpawn(this.getClass(), this.takumiRank().getSpawnWeight() * 25, 5, 20, TakumiEntityCore.CREATURE_TAKUMI, TakumiEntityCore
                .biomes.toArray(new Biome[0]));
    }
    
    @Override
    public Object getRender(RenderManager manager) {
        return new RenderBirdCreeper <>(manager);
    }
    
    @Override
    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(SoundEvents.ENTITY_CHICKEN_STEP, 0.15F, 1.0F);
    }
    
    @Override
    public float getEyeHeight() {
        return this.height;
    }
    
    @Override
    protected void outOfWorld() {
        this.setHealth(0);
        super.outOfWorld();
    }
    
    @Override
    protected float getJumpUpwardsMotion() {
        return 1f;
    }
    
    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            EntityTakumiExpEgg expEgg = new EntityTakumiExpEgg(this.world, this);
            expEgg.setPosition(this.posX + this.width / 2, this.posY - 0.05, this.posZ + this.width / 2);
            expEgg.motionY = -0.25;
            this.world.spawnEntity(expEgg);
        }
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
        return 0;
    }
    
    @Override
    public int getSecondaryColor() {
        return 0x77ff77;
    }
    
    @Override
    public boolean isCustomSpawn() {
        return true;
    }
    
    @Override
    public String getRegisterName() {
        return "birdcreeper";
    }
    
    @Override
    public int getRegisterID() {
        return 229;
    }
}
