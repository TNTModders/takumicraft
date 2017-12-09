package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.client.render.RenderSnowCreeper;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.item.EntityTakumiSnowBall;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import javax.annotation.Nullable;

public class EntitySnowCreeper extends EntityTakumiAbstractCreeper implements IRangedAttackMob {
    
    public EntitySnowCreeper(World worldIn) {
        super(worldIn);
        this.setSize(0.7F, 1.9F);
    }
    
    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(1, new EntityAIAttackRanged(this, 1.25D, 20, 10.0F));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget <>(this, EntityPlayer.class, 10, true, false, IMob.MOB_SELECTOR));
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224D);
    }
    
    @Override
    @Nullable
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_SNOWMAN_HURT;
    }
    
    @Override
    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SNOWMAN_DEATH;
    }
    
    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_SNOWMAN;
    }
    
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        
        if (!this.world.isRemote) {
            int i = MathHelper.floor(this.posX);
            int j = MathHelper.floor(this.posY);
            int k = MathHelper.floor(this.posZ);
            
            if (this.isWet()) {
                this.attackEntityFrom(DamageSource.DROWN, 1.0F);
            }
            
            if (this.world.getBiome(new BlockPos(i, 0, k)).getFloatTemperature(new BlockPos(i, j, k)) > 1.0F) {
                this.attackEntityFrom(DamageSource.ON_FIRE, 1.0F);
            }
            
            if (!this.world.getGameRules().getBoolean("mobGriefing")) {
                return;
            }
            
            for (int l = 0; l < 4; ++l) {
                i = MathHelper.floor(this.posX + (double) ((float) (l % 2 * 2 - 1) * 0.25F));
                j = MathHelper.floor(this.posY);
                k = MathHelper.floor(this.posZ + (double) ((float) (l / 2 % 2 * 2 - 1) * 0.25F));
                BlockPos blockpos = new BlockPos(i, j, k);
                
                if (this.world.getBlockState(blockpos).getMaterial() == Material.AIR && this.world.getBiome(blockpos).getFloatTemperature(
                        blockpos) < 0.8F && Blocks.SNOW_LAYER.canPlaceBlockAt(this.world, blockpos)) {
                    this.world.setBlockState(blockpos, Blocks.SNOW_LAYER.getDefaultState());
                }
            }
        }
    }
    
    /**
     * Attack the specified entity using a ranged attack.
     */
    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
        EntityTakumiSnowBall entitysnowball = new EntityTakumiSnowBall(this.world, this);
        double d0 = target.posY + (double) target.getEyeHeight() - 1.100000023841858D;
        double d1 = target.posX - this.posX;
        double d2 = d0 - entitysnowball.posY;
        double d3 = target.posZ - this.posZ;
        float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
        entitysnowball.setThrowableHeading(d1, d2 + (double) f, d3, 1.6F, 12.0F);
        this.playSound(SoundEvents.ENTITY_SNOWMAN_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.world.spawnEntity(entitysnowball);
    }
    
    @Override
    public void setSwingingArms(boolean swingingArms) {
    }
    
    @Override
    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SNOWMAN_AMBIENT;
    }
    
    @Override
    public float getEyeHeight() {
        return 1.7f;
    }
    
    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            for (int i = 0; i < 20 * (this.getPowered() ? 1.5 : 1); i++) {
                double x = this.posX + this.rand.nextInt(20) - 10;
                double z = this.posZ + this.rand.nextInt(20) - 10;
                double y = this.world.getHeight(new BlockPos(x, 0, z)).getY() + 100;
                EntityTakumiSnowBall arrow = new EntityTakumiSnowBall(this.world, this);
    
                arrow.setLocationAndAngles(x, y, z, 0, 0);
                arrow.motionX = 0;
                arrow.motionZ = 0;
                arrow.motionY = -0.5;
                arrow.setThrowableHeading(arrow.motionX, arrow.motionY, arrow.motionZ,
                                          (float) (14 - this.world.getDifficulty().getDifficultyId() * 8), 1f);
                this.world.spawnEntity(arrow);
            }
        }
    }
    
    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
    }
    
    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.WIND;
    }
    
    @Override
    public int getExplosionPower() {
        return 3;
    }
    
    @Override
    public int getSecondaryColor() {
        return 0x00ff00;
    }
    
    @Override
    public boolean isCustomSpawn() {
        return true;
    }
    
    @Override
    public String getRegisterName() {
        return "snowcreeper";
    }
    
    @Override
    public int getRegisterID() {
        return 45;
    }
    
    @Override
    public void customSpawn() {
        EntityRegistry.addSpawn(this.getClass(), this.takumiRank().getSpawnWeight(), 1, 3, EnumCreatureType.MONSTER, Biomes.COLD_BEACH,
                                Biomes.COLD_TAIGA, Biomes.COLD_TAIGA_HILLS, Biomes.MUTATED_TAIGA_COLD, Biomes.FROZEN_RIVER, Biomes.ICE_PLAINS,
                                Biomes.ICE_MOUNTAINS, Biomes.MUTATED_ICE_FLATS, Biomes.EXTREME_HILLS);
    }
    
    @Override
    public int getPrimaryColor() {
        return 0xddffdd;
    }
    
    @Override
    public Object getRender(RenderManager manager) {
        return new RenderSnowCreeper(manager);
    }
}
