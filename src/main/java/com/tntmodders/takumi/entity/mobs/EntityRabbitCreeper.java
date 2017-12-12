package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.client.render.RenderRabbitCreeper;
import com.tntmodders.takumi.core.TakumiEntityCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCarrot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.*;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDesert;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class EntityRabbitCreeper extends EntityTakumiAbstractCreeper {
    
    private int jumpTicks;
    private int jumpDuration;
    private boolean wasOnGround;
    private int currentMoveTypeDuration;
    private int carrotTicks;
    
    public EntityRabbitCreeper(World worldIn) {
        super(worldIn); this.setSize(0.4F, 0.5F); this.jumpHelper = new RabbitJumpHelper(this); this.moveHelper = new RabbitMoveHelper(this);
        this.setMovementSpeed(0.0D);
    }
    
    public void setMovementSpeed(double p_setMovementSpeed_1_) {
        this.getNavigator().setSpeed(p_setMovementSpeed_1_);
        this.moveHelper.setMoveTo(this.moveHelper.getX(), this.moveHelper.getY(), this.moveHelper.getZ(), p_setMovementSpeed_1_);
    }
    
    public static void registerFixesRabbit(DataFixer p_registerFixesRabbit_0_) {
        EntityLiving.registerFixesMob(p_registerFixesRabbit_0_, EntityRabbitCreeper.class);
    }
    
    @Override
    public void takumiExplode() {
    }
    
    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
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
        return 0xeeffee;
    }
    
    @Override
    public boolean isCustomSpawn() {
        return true;
    }
    
    @Override
    public String getRegisterName() {
        return "rabbitcreeper";
    }
    
    @Override
    public int getRegisterID() {
        return 41;
    }
    
    @Override
    protected void initEntityAI() {
        super.initEntityAI(); this.tasks.addTask(1, new AIPanic(this, 2.2D));
        this.tasks.addTask(4, new AIAvoidEntity <>(this, EntityPlayer.class, 8.0F, 2.2D, 2.2D));
        this.tasks.addTask(4, new AIAvoidEntity <>(this, EntityWolf.class, 10.0F, 2.2D, 2.2D));
        this.tasks.addTask(4, new AIAvoidEntity <>(this, EntityMob.class, 4.0F, 2.2D, 2.2D)); this.tasks.addTask(5, new AIRaidFarm(this));
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(3.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
    }
    
    @Override
    public void writeEntityToNBT(NBTTagCompound p_writeEntityToNBT_1_) {
        super.writeEntityToNBT(p_writeEntityToNBT_1_);
        p_writeEntityToNBT_1_.setInteger("MoreCarrotTicks", this.carrotTicks);
    }
    
    @Override
    public void readEntityFromNBT(NBTTagCompound p_readEntityFromNBT_1_) {
        super.readEntityFromNBT(p_readEntityFromNBT_1_);
        this.carrotTicks = p_readEntityFromNBT_1_.getInteger("MoreCarrotTicks");
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource p_getHurtSound_1_) {
        return SoundEvents.ENTITY_RABBIT_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_RABBIT_DEATH;
    }
    
    @Override
    public boolean attackEntityAsMob(Entity p_attackEntityAsMob_1_) {
        this.playSound(SoundEvents.ENTITY_RABBIT_ATTACK, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        return p_attackEntityAsMob_1_.attackEntityFrom(DamageSource.causeMobDamage(this), 8.0F);
    }
    
    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_RABBIT;
    }
    
    @Override
    protected float getJumpUpwardsMotion() {
        if (!this.isCollidedHorizontally && (!this.moveHelper.isUpdating() || this.moveHelper.getY() <= this.posY + 0.5D)) {
            Path lvt_1_1_ = this.navigator.getPath();
            if (lvt_1_1_ != null && lvt_1_1_.getCurrentPathIndex() < lvt_1_1_.getCurrentPathLength()) {
                Vec3d lvt_2_1_ = lvt_1_1_.getPosition(this);
                if (lvt_2_1_.y > this.posY + 0.5D) {
                    return 0.5F;
                }
            }
    
            return this.moveHelper.getSpeed() <= 0.6D ? 0.2F : 0.3F;
        } else {
            return 0.5F;
        }
    }
    
    @Override
    protected void jump() {
        super.jump();
        double lvt_1_1_ = this.moveHelper.getSpeed();
        if (lvt_1_1_ > 0.0D) {
            double lvt_3_1_ = this.motionX * this.motionX + this.motionZ * this.motionZ;
            if (lvt_3_1_ < 0.010000000000000002D) {
                this.moveRelative(0.0F, 0.0F, 1.0F, 0.1F);
            }
        }
        
        if (!this.world.isRemote) {
            this.world.setEntityState(this, (byte) 1);
        }
        
    }
    
    @Override
    public void setJumping(boolean p_setJumping_1_) {
        super.setJumping(p_setJumping_1_);
        if (p_setJumping_1_) {
            this.playSound(this.getJumpSound(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);
        }
        
    }
    
    @SideOnly(Side.CLIENT)
    public float setJumpCompletion(float p_setJumpCompletion_1_) {
        return this.jumpDuration == 0 ? 0.0F : ((float) this.jumpTicks + p_setJumpCompletion_1_) / (float) this.jumpDuration;
    }
    
    @Override
    public void spawnRunningParticles() {
    }
    
    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }
    
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.jumpTicks != this.jumpDuration) {
            ++this.jumpTicks;
        } else if (this.jumpDuration != 0) {
            this.jumpTicks = 0;
            this.jumpDuration = 0;
            this.setJumping(false);
        }
        
    }
    
    @Override
    public boolean attackEntityFrom(DamageSource p_attackEntityFrom_1_, float p_attackEntityFrom_2_) {
        return !this.isEntityInvulnerable(p_attackEntityFrom_1_) && super.attackEntityFrom(p_attackEntityFrom_1_, p_attackEntityFrom_2_);
    }
    
    @Override
    public boolean getCanSpawnHere() {
        int i = MathHelper.floor(this.posX); int j = MathHelper.floor(this.getEntityBoundingBox().minY); int k = MathHelper.floor(this.posZ);
        BlockPos blockpos = new BlockPos(i, j, k); return this.world.getLight(blockpos) > 8 && super.getCanSpawnHere();
    }
    
    protected SoundEvent getJumpSound() {
        return SoundEvents.ENTITY_RABBIT_JUMP;
    }
    
    private int getRandomRabbitType() {
        Biome lvt_1_1_ = this.world.getBiome(new BlockPos(this));
        int lvt_2_1_ = this.rand.nextInt(100);
        if (lvt_1_1_.isSnowyBiome()) {
            return lvt_2_1_ < 80 ? 1 : 3;
        } else if (lvt_1_1_ instanceof BiomeDesert) {
            return 4;
        } else {
            return lvt_2_1_ < 50 ? 0 : lvt_2_1_ < 90 ? 5 : 2;
        }
    }
    
    public boolean isBreedingItem(ItemStack p_isBreedingItem_1_) {
        return this.isRabbitBreedingItem(p_isBreedingItem_1_.getItem());
    }
    
    private boolean isRabbitBreedingItem(Item p_isRabbitBreedingItem_1_) {
        return p_isRabbitBreedingItem_1_ == Items.CARROT || p_isRabbitBreedingItem_1_ == Items.GOLDEN_CARROT || p_isRabbitBreedingItem_1_ == Item
                .getItemFromBlock(Blocks.YELLOW_FLOWER);
    }
    
    private boolean isCarrotEaten() {
        return this.carrotTicks == 0;
    }
    
    protected void createEatingParticles() {
        BlockCarrot lvt_1_1_ = (BlockCarrot) Blocks.CARROTS;
        IBlockState lvt_2_1_ = lvt_1_1_.withAge(lvt_1_1_.getMaxAge());
        this.world.spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this
                .width, this.posY + 0.5D + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width
                * 2.0F) - (double) this.width, 0.0D, 0.0D, 0.0D, Block.getStateId(lvt_2_1_));
        this.carrotTicks = 40;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte p_handleStatusUpdate_1_) {
        if (p_handleStatusUpdate_1_ == 1) {
            this.createRunningParticles();
            this.jumpDuration = 10;
            this.jumpTicks = 0;
        } else {
            super.handleStatusUpdate(p_handleStatusUpdate_1_);
        }
        
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_RABBIT_AMBIENT;
    }
    
    @Override
    public void updateAITasks() {
        if (this.currentMoveTypeDuration > 0) {
            --this.currentMoveTypeDuration;
        }
        
        if (this.carrotTicks > 0) {
            this.carrotTicks -= this.rand.nextInt(3);
            if (this.carrotTicks < 0) {
                this.carrotTicks = 0;
            }
        }
        
        if (this.onGround) {
            if (!this.wasOnGround) {
                this.setJumping(false);
                this.checkLandingDelay();
            }
            
            if (this.currentMoveTypeDuration == 0) {
                EntityLivingBase lvt_1_1_ = this.getAttackTarget();
                if (lvt_1_1_ != null && this.getDistanceSqToEntity(lvt_1_1_) < 16.0D) {
                    this.calculateRotationYaw(lvt_1_1_.posX, lvt_1_1_.posZ);
                    this.moveHelper.setMoveTo(lvt_1_1_.posX, lvt_1_1_.posY, lvt_1_1_.posZ, this.moveHelper.getSpeed());
                    this.startJumping();
                    this.wasOnGround = true;
                }
            }
    
            RabbitJumpHelper lvt_1_2_ = (RabbitJumpHelper) this.jumpHelper;
            if (!lvt_1_2_.getIsJumping()) {
                if (this.moveHelper.isUpdating() && this.currentMoveTypeDuration == 0) {
                    Path lvt_2_1_ = this.navigator.getPath();
                    Vec3d lvt_3_1_ = new Vec3d(this.moveHelper.getX(), this.moveHelper.getY(), this.moveHelper.getZ());
                    if (lvt_2_1_ != null && lvt_2_1_.getCurrentPathIndex() < lvt_2_1_.getCurrentPathLength()) {
                        lvt_3_1_ = lvt_2_1_.getPosition(this);
                    }
    
                    this.calculateRotationYaw(lvt_3_1_.x, lvt_3_1_.z);
                    this.startJumping();
                }
            } else if (!lvt_1_2_.canJump()) {
                this.enableJumpControl();
            }
        }
        
        this.wasOnGround = this.onGround;
    }
    
    private void checkLandingDelay() {
        this.updateMoveTypeDuration();
        this.disableJumpControl();
    }
    
    private void calculateRotationYaw(double p_calculateRotationYaw_1_, double p_calculateRotationYaw_3_) {
        this.rotationYaw = (float) (MathHelper.atan2(p_calculateRotationYaw_3_ - this.posZ, p_calculateRotationYaw_1_ - this.posX) *
                57.2957763671875D) - 90.0F;
    }
    
    public void startJumping() {
        this.setJumping(true);
        this.jumpDuration = 10;
        this.jumpTicks = 0;
    }
    
    private void enableJumpControl() {
        ((RabbitJumpHelper) this.jumpHelper).setCanJump(true);
    }
    
    private void updateMoveTypeDuration() {
        if (this.moveHelper.getSpeed() < 2.2D) {
            this.currentMoveTypeDuration = 10;
        } else {
            this.currentMoveTypeDuration = 1;
        }
        
    }
    
    private void disableJumpControl() {
        ((RabbitJumpHelper) this.jumpHelper).setCanJump(false);
    }
    
    @Override
    public void customSpawn() {
        EntityRegistry.addSpawn(this.getClass(), this.takumiRank().getSpawnWeight() * 25, 5, 20, TakumiEntityCore.CREATURE_TAKUMI, TakumiEntityCore
                .biomes.toArray(new Biome[0]));
    }
    
    @Override
    public int getPrimaryColor() {
        return 0x006666;
    }
    
    @Override
    public Object getRender(RenderManager manager) {
        return new RenderRabbitCreeper <>(manager);
    }
    
    static class AIEvilAttack extends EntityAIAttackMelee {
        
        public AIEvilAttack(EntityRabbitCreeper p_i45867_1_) {
            super(p_i45867_1_, 1.4D, true);
        }
        
        @Override
        protected double getAttackReachSqr(EntityLivingBase p_getAttackReachSqr_1_) {
            return (double) (4.0F + p_getAttackReachSqr_1_.width);
        }
    }
    
    static class AIPanic extends EntityAIPanic {
        
        private final EntityRabbitCreeper rabbit;
        
        public AIPanic(EntityRabbitCreeper p_i45861_1_, double p_i45861_2_) {
            super(p_i45861_1_, p_i45861_2_);
            this.rabbit = p_i45861_1_;
        }
        
        @Override
        public void updateTask() {
            super.updateTask();
            this.rabbit.setMovementSpeed(this.speed);
        }
    }
    
    static class AIRaidFarm extends EntityAIMoveToBlock {
        
        private final EntityRabbitCreeper rabbit;
        private boolean wantsToRaid;
        private boolean canRaid;
        
        public AIRaidFarm(EntityRabbitCreeper p_i45860_1_) {
            super(p_i45860_1_, 0.699999988079071D, 16);
            this.rabbit = p_i45860_1_;
        }
        
        @Override
        public boolean shouldExecute() {
            if (this.runDelay <= 0) {
                if (!this.rabbit.world.getGameRules().getBoolean("mobGriefing")) {
                    return false;
                }
    
                this.canRaid = false;
                this.wantsToRaid = this.rabbit.isCarrotEaten();
                this.wantsToRaid = true;
            }
            
            return super.shouldExecute();
        }
        
        @Override
        public boolean shouldContinueExecuting() {
            return this.canRaid && super.shouldContinueExecuting();
        }
        
        @Override
        public void updateTask() {
            super.updateTask();
            this.rabbit.getLookHelper().setLookPosition((double) this.destinationBlock.getX() + 0.5D, (double) (this.destinationBlock.getY() + 1),
                    (double) this.destinationBlock.getZ() + 0.5D, 10.0F, (float) this.rabbit.getVerticalFaceSpeed());
            if (this.getIsAboveDestination()) {
                World lvt_1_1_ = this.rabbit.world;
                BlockPos lvt_2_1_ = this.destinationBlock.up();
                IBlockState lvt_3_1_ = lvt_1_1_.getBlockState(lvt_2_1_);
                Block lvt_4_1_ = lvt_3_1_.getBlock();
                if (this.canRaid && lvt_4_1_ instanceof BlockCarrot) {
                    Integer lvt_5_1_ = lvt_3_1_.getValue(BlockCarrot.AGE);
                    if (lvt_5_1_ == 0) {
                        lvt_1_1_.setBlockState(lvt_2_1_, Blocks.AIR.getDefaultState(), 2);
                        lvt_1_1_.destroyBlock(lvt_2_1_, true);
                    } else {
                        lvt_1_1_.setBlockState(lvt_2_1_, lvt_3_1_.withProperty(BlockCarrot.AGE, lvt_5_1_ - 1), 2);
                        lvt_1_1_.playEvent(2001, lvt_2_1_, Block.getStateId(lvt_3_1_));
                    }
    
                    this.rabbit.createEatingParticles();
                }
    
                this.canRaid = false;
                this.runDelay = 10;
            }
            
        }
        
        @Override
        protected boolean shouldMoveTo(World p_shouldMoveTo_1_, BlockPos p_shouldMoveTo_2_) {
            Block lvt_3_1_ = p_shouldMoveTo_1_.getBlockState(p_shouldMoveTo_2_).getBlock();
            if (lvt_3_1_ == Blocks.FARMLAND && this.wantsToRaid && !this.canRaid) {
                p_shouldMoveTo_2_ = p_shouldMoveTo_2_.up();
                IBlockState lvt_4_1_ = p_shouldMoveTo_1_.getBlockState(p_shouldMoveTo_2_);
                lvt_3_1_ = lvt_4_1_.getBlock();
                if (lvt_3_1_ instanceof BlockCarrot && ((BlockCarrot) lvt_3_1_).isMaxAge(lvt_4_1_)) {
                    this.canRaid = true;
                    return true;
                }
            }
            
            return false;
        }
    }
    
    static class AIAvoidEntity <T extends Entity> extends EntityAIAvoidEntity <T> {
        
        private final EntityRabbitCreeper rabbit;
        
        public AIAvoidEntity(EntityRabbitCreeper p_i46403_1_, Class <T> p_i46403_2_, float p_i46403_3_, double p_i46403_4_, double p_i46403_6_) {
            super(p_i46403_1_, p_i46403_2_, p_i46403_3_, p_i46403_4_, p_i46403_6_);
            this.rabbit = p_i46403_1_;
        }
    }
    
    static class RabbitMoveHelper extends EntityMoveHelper {
        
        private final EntityRabbitCreeper rabbit;
        private double nextJumpSpeed;
        
        public RabbitMoveHelper(EntityRabbitCreeper p_i45862_1_) {
            super(p_i45862_1_);
            this.rabbit = p_i45862_1_;
        }
        
        @Override
        public void setMoveTo(double p_setMoveTo_1_, double p_setMoveTo_3_, double p_setMoveTo_5_, double p_setMoveTo_7_) {
            if (this.rabbit.isInWater()) {
                p_setMoveTo_7_ = 1.5D;
            }
            
            super.setMoveTo(p_setMoveTo_1_, p_setMoveTo_3_, p_setMoveTo_5_, p_setMoveTo_7_);
            if (p_setMoveTo_7_ > 0.0D) {
                this.nextJumpSpeed = p_setMoveTo_7_;
            }
            
        }
        
        @Override
        public void onUpdateMoveHelper() {
            if (this.rabbit.onGround && !this.rabbit.isJumping && !((RabbitJumpHelper) this.rabbit.jumpHelper).getIsJumping()) {
                this.rabbit.setMovementSpeed(0.0D);
            } else if (this.isUpdating()) {
                this.rabbit.setMovementSpeed(this.nextJumpSpeed);
            }
            
            super.onUpdateMoveHelper();
        }
    }
    
    public static class RabbitTypeData implements IEntityLivingData {
        
        public int typeData;
        
        public RabbitTypeData(int p_i45864_1_) {
            this.typeData = p_i45864_1_;
        }
    }
    
    public class RabbitJumpHelper extends EntityJumpHelper {
        
        private final EntityRabbitCreeper rabbit;
        private boolean canJump;
        
        public RabbitJumpHelper(EntityRabbitCreeper p_i45863_2_) {
            super(p_i45863_2_);
            this.rabbit = p_i45863_2_;
        }
        
        public boolean getIsJumping() {
            return this.isJumping;
        }
        
        public boolean canJump() {
            return this.canJump;
        }
        
        public void setCanJump(boolean p_setCanJump_1_) {
            this.canJump = p_setCanJump_1_;
        }
        
        @Override
        public void doJump() {
            if (this.isJumping) {
                this.rabbit.startJumping();
                this.isJumping = false;
            }
            
        }
    }
}
