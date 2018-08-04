package com.tntmodders.takumi.entity.mobs;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import com.tntmodders.takumi.client.render.RenderDarkCreeper;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class EntityDarkCreeper extends EntityTakumiAbstractCreeper {

    private static final UUID ATTACKING_SPEED_BOOST_ID = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
    private static final AttributeModifier ATTACKING_SPEED_BOOST =
            new AttributeModifier(ATTACKING_SPEED_BOOST_ID, "Attacking speed boost", 0.15000000596046448D, 0).setSaved(
                    false);
    private static final Set<Block> CARRIABLE_BLOCKS = Sets.newIdentityHashSet();
    private static final DataParameter<Optional<IBlockState>> CARRIED_BLOCK =
            EntityDataManager.createKey(EntityDarkCreeper.class, DataSerializers.OPTIONAL_BLOCK_STATE);
    private static final DataParameter<Boolean> SCREAMING =
            EntityDataManager.createKey(EntityDarkCreeper.class, DataSerializers.BOOLEAN);

    static {
        CARRIABLE_BLOCKS.add(Blocks.GRASS);
        CARRIABLE_BLOCKS.add(Blocks.DIRT);
        CARRIABLE_BLOCKS.add(Blocks.SAND);
        CARRIABLE_BLOCKS.add(Blocks.GRAVEL);
        CARRIABLE_BLOCKS.add(Blocks.YELLOW_FLOWER);
        CARRIABLE_BLOCKS.add(Blocks.RED_FLOWER);
        CARRIABLE_BLOCKS.add(Blocks.BROWN_MUSHROOM);
        CARRIABLE_BLOCKS.add(Blocks.RED_MUSHROOM);
        CARRIABLE_BLOCKS.add(Blocks.TNT);
        CARRIABLE_BLOCKS.add(Blocks.CACTUS);
        CARRIABLE_BLOCKS.add(Blocks.CLAY);
        CARRIABLE_BLOCKS.add(Blocks.PUMPKIN);
        CARRIABLE_BLOCKS.add(Blocks.MELON_BLOCK);
        CARRIABLE_BLOCKS.add(Blocks.MYCELIUM);
        CARRIABLE_BLOCKS.add(Blocks.NETHERRACK);
    }

    private int lastCreepySound;
    private int targetChangeTime;

    public EntityDarkCreeper(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 2.9F);
        this.stepHeight = 1.0F;
        this.setPathPriority(PathNodeType.WATER, -1.0F);
    }

    /*===================================== Forge Start ==============================*/
    public static void setCarriable(Block block, boolean canCarry) {
        if (canCarry) {
            CARRIABLE_BLOCKS.add(block);
        } else {
            CARRIABLE_BLOCKS.remove(block);
        }
    }

    public static boolean getCarriable(Block block) {
        return CARRIABLE_BLOCKS.contains(block);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAICreeperSwell(this));
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D, 0.0F));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.tasks.addTask(10, new AIPlaceBlock(this));
        this.tasks.addTask(11, new AITakeBlock(this));
        this.targetTasks.addTask(1, new AIFindPlayer(this));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityEndermite.class, 10, true, false,
                EntityEndermite :: isSpawnedByPlayer));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(CARRIED_BLOCK, Optional.absent());
        this.dataManager.register(SCREAMING, Boolean.FALSE);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        IBlockState iblockstate = this.getHeldBlockState();

        if (iblockstate != null) {
            compound.setShort("carried", (short) Block.getIdFromBlock(iblockstate.getBlock()));
            compound.setShort("carriedData", (short) iblockstate.getBlock().getMetaFromState(iblockstate));
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    @Deprecated
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        IBlockState iblockstate;

        if (compound.hasKey("carried", 8)) {
            iblockstate = Block.getBlockFromName(compound.getString("carried")).getStateFromMeta(
                    compound.getShort("carriedData") & 65535);
        } else {
            iblockstate = Block.getBlockById(compound.getShort("carried")).getStateFromMeta(
                    compound.getShort("carriedData") & 65535);
        }

        if (iblockstate == null || iblockstate.getBlock() == null || iblockstate.getMaterial() == Material.AIR) {
            iblockstate = null;
        }

        this.setHeldBlockState(iblockstate);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_ENDERMEN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ENDERMEN_DEATH;
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_ENDERMAN;
    }

    /**
     * Gets this enderman's held block state
     */
    @Nullable
    public IBlockState getHeldBlockState() {
        return (IBlockState) ((Optional) this.dataManager.get(CARRIED_BLOCK)).orNull();
    }

    /**
     * Sets this enderman's held block state
     */
    public void setHeldBlockState(
            @Nullable
                    IBlockState state) {
        this.dataManager.set(CARRIED_BLOCK, Optional.fromNullable(state));
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        if (SCREAMING.equals(key) && this.isScreaming() && this.world.isRemote) {
            this.playEndermanSound();
        }

        super.notifyDataManagerChange(key);
    }

    public boolean isScreaming() {
        return this.dataManager.get(SCREAMING);
    }

    public void playEndermanSound() {
        if (this.ticksExisted >= this.lastCreepySound + 400) {
            this.lastCreepySound = this.ticksExisted;

            if (!this.isSilent()) {
                this.world.playSound(this.posX, this.posY + this.getEyeHeight(), this.posZ,
                        SoundEvents.ENTITY_ENDERMEN_STARE, this.getSoundCategory(), 2.5F, 1.0F, false);
            }
        }
    }

    @Override
    public float getEyeHeight() {
        return 2.55F;
    }

    /**
     * Checks to see if this enderman should be attacking this player
     */
    private boolean shouldAttackPlayer(EntityPlayer player) {
        ItemStack itemstack = player.inventory.armorInventory.get(3);

        if (itemstack.getItem() == Item.getItemFromBlock(Blocks.PUMPKIN)) {
            return false;
        } else {
            Vec3d vec3d = player.getLook(1.0F).normalize();
            Vec3d vec3d1 = new Vec3d(this.posX - player.posX,
                    this.getEntityBoundingBox().minY + this.getEyeHeight() - (player.posY + player.getEyeHeight()),
                    this.posZ - player.posZ);
            double d0 = vec3d1.lengthVector();
            vec3d1 = vec3d1.normalize();
            double d1 = vec3d.dotProduct(vec3d1);
            return d1 > 1.0D - 0.025D / d0 && player.canEntityBeSeen(this);
        }
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
    public void onLivingUpdate() {
        if (this.world.isRemote) {
            for (int i = 0; i < 2; ++i) {
                this.world.spawnParticle(EnumParticleTypes.PORTAL,
                        this.posX + (this.rand.nextDouble() - 0.5D) * this.width,
                        this.posY + this.rand.nextDouble() * this.height - 0.25D,
                        this.posZ + (this.rand.nextDouble() - 0.5D) * this.width,
                        (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(),
                        (this.rand.nextDouble() - 0.5D) * 2.0D);
            }
        }

        this.isJumping = false;
        super.onLivingUpdate();
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        } else if (source instanceof EntityDamageSourceIndirect) {
            for (int i = 0; i < 64; ++i) {
                if (this.teleportRandomly()) {
                    return true;
                }
            }

            return false;
        } else {
            boolean flag = super.attackEntityFrom(source, amount);

            if (source.isUnblockable() && this.rand.nextInt(10) != 0) {
                this.teleportRandomly();
            }

            return flag;
        }
    }

    /**
     * Teleport the enderman to a random nearby position
     */
    protected boolean teleportRandomly() {
        double d0 = this.posX + (this.rand.nextDouble() - 0.5D) * 64.0D;
        double d1 = this.posY + (this.rand.nextInt(64) - 32);
        double d2 = this.posZ + (this.rand.nextDouble() - 0.5D) * 64.0D;
        return this.teleportTo(d0, d1, d2);
    }

    /**
     * Teleport the enderman to another entity
     */
    protected boolean teleportToEntity(Entity p_70816_1_) {
        Vec3d vec3d = new Vec3d(this.posX - p_70816_1_.posX,
                this.getEntityBoundingBox().minY + this.height / 2.0F - p_70816_1_.posY + p_70816_1_.getEyeHeight(),
                this.posZ - p_70816_1_.posZ);
        vec3d = vec3d.normalize();
        double d0 = 16.0D;
        double d1 = this.posX + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3d.x * 16.0D;
        double d2 = this.posY + (this.rand.nextInt(16) - 8) - vec3d.y * 16.0D;
        double d3 = this.posZ + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3d.z * 16.0D;
        return this.teleportTo(d1, d2, d3);
    }

    /**
     * Teleport the enderman
     */
    private boolean teleportTo(double x, double y, double z) {
        EnderTeleportEvent event = new EnderTeleportEvent(this, x, y, z, 0);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            return false;
        }
        boolean flag = this.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ());

        if (flag) {
            this.world.playSound(null, this.prevPosX, this.prevPosY, this.prevPosZ,
                    SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
            this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
        }

        return flag;
    }

    @Override
    public void takumiExplode() {
    }
    /*===================================== Forge End ==============================*/

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.GROUND_M;
    }

    @Override
    public int getExplosionPower() {
        return 3;
    }

    @Override
    public int getSecondaryColor() {
        return 4128831;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "darkcreeper";
    }

    @Override
    public int getRegisterID() {
        return 220;
    }

    @Override
    public void setAttackTarget(
            @Nullable
                    EntityLivingBase entitylivingbaseIn) {
        super.setAttackTarget(entitylivingbaseIn);
        IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

        if (entitylivingbaseIn == null) {
            this.targetChangeTime = 0;
            this.dataManager.set(SCREAMING, Boolean.FALSE);
            iattributeinstance.removeModifier(ATTACKING_SPEED_BOOST);
        } else {
            this.targetChangeTime = this.ticksExisted;
            this.dataManager.set(SCREAMING, Boolean.TRUE);

            if (!iattributeinstance.hasModifier(ATTACKING_SPEED_BOOST)) {
                iattributeinstance.applyModifier(ATTACKING_SPEED_BOOST);
            }
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isScreaming() ? SoundEvents.ENTITY_ENDERMEN_SCREAM : SoundEvents.ENTITY_ENDERMEN_AMBIENT;
    }

    @Nullable
    @Override
    protected Item getDropItem() {
        return Items.ENDER_EYE;
    }

    @Override
    protected void updateAITasks() {
        if (this.isWet()) {
            this.attackEntityFrom(DamageSource.DROWN, 1.0F);
        }

        if (this.world.isDaytime() && this.ticksExisted >= this.targetChangeTime + 600) {
            float f = this.getBrightness();

            if (f > 0.5F && this.world.canSeeSky(new BlockPos(this)) &&
                    this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F) {
                this.setAttackTarget(null);
                this.teleportRandomly();
            }
        }

        super.updateAITasks();
    }

    /**
     * Drop the equipment for this entity.
     */
    @Override
    protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier) {
        super.dropEquipment(wasRecentlyHit, lootingModifier);
        IBlockState iblockstate = this.getHeldBlockState();

        if (iblockstate != null) {
            Item item = Item.getItemFromBlock(iblockstate.getBlock());
            int i = item.getHasSubtypes() ? iblockstate.getBlock().getMetaFromState(iblockstate) : 0;
            this.entityDropItem(new ItemStack(item, 1, i), 0.0F);
        }
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        for (Entity entity : event.getAffectedEntities()) {
            if (entity instanceof EntityLivingBase) {
                boolean done = false;
                for (int i = 0; i < 50 && !done; i++) {
                    done = this.teleportTo((EntityLivingBase) entity);
                }
                Potion potion = MobEffects.BLINDNESS;
                switch (this.rand.nextInt(6)) {
                    case 0:
                        break;
                    case 1:
                        potion = MobEffects.NAUSEA;
                        break;
                    case 2:
                        potion = MobEffects.SLOWNESS;
                        break;
                    case 3:
                        potion = MobEffects.HUNGER;
                        break;
                    case 4:
                        potion = MobEffects.UNLUCK;
                        break;
                    case 5:
                        if (this.rand.nextBoolean() && this.getPowered()) {
                            potion = MobEffects.LEVITATION;
                        }
                        break;
                }
                EntityAreaEffectCloud entityareaeffectcloud =
                        new EntityAreaEffectCloud(entity.world, entity.posX, entity.posY, entity.posZ);
                entityareaeffectcloud.setRadius(5F);
                entityareaeffectcloud.setRadiusOnUse(-0.5F);
                entityareaeffectcloud.setWaitTime(10);
                entityareaeffectcloud.setDuration(entityareaeffectcloud.getDuration());
                entityareaeffectcloud.setRadiusPerTick(
                        -entityareaeffectcloud.getRadius() / entityareaeffectcloud.getDuration());
                entityareaeffectcloud.addEffect(new PotionEffect(potion, potion == MobEffects.LEVITATION ? 100 : 1200,
                        this.getPowered() ? 1 : 0));
                this.world.spawnEntity(entityareaeffectcloud);
            }
        }
        return true;
    }

    protected boolean teleportTo(EntityLivingBase entity) {
        Random rand = new Random();
        int distance = 128;
        double x = entity.posX + (rand.nextDouble() - 0.5D) * distance;
        double y = entity.posY + rand.nextInt(distance + 1) - distance / 2;
        double z = entity.posZ + (rand.nextDouble() - 0.5D) * distance;
        EnderTeleportEvent event = new EnderTeleportEvent(entity, x, y, z, 0);

        double d3 = entity.posX;
        double d4 = entity.posY;
        double d5 = entity.posZ;
        entity.posX = event.getTargetX();
        entity.posY = event.getTargetY();
        entity.posZ = event.getTargetZ();

        int xInt = MathHelper.floor(entity.posX);
        int yInt = MathHelper.floor(entity.posY);
        int zInt = MathHelper.floor(entity.posZ);

        boolean flag = false;
        if (entity.world.isAirBlock(new BlockPos(xInt, yInt, zInt))) {

            boolean foundGround = false;
            while (!foundGround && yInt > 0) {
                IBlockState block = entity.world.getBlockState(new BlockPos(xInt, yInt - 1, zInt));
                if (block.getMaterial().blocksMovement()) {
                    foundGround = true;
                } else {
                    --entity.posY;
                    --yInt;
                }
            }

            if (foundGround) {
                entity.setPosition(entity.posX, entity.posY, entity.posZ);
                if (entity.world.getCollisionBoxes(entity, entity.getEntityBoundingBox()).isEmpty() &&
                        !entity.world.containsAnyLiquid(entity.getEntityBoundingBox())) {
                    flag = true;
                }
            }
        }

        if (!flag) {
            entity.setPosition(d3, d4, d5);
            return false;
        }

        entity.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);

        short short1 = 128;
        for (int l = 0; l < short1; ++l) {
            double d6 = l / (short1 - 1.0D);
            float f = (rand.nextFloat() - 0.5F) * 0.2F;
            float f1 = (rand.nextFloat() - 0.5F) * 0.2F;
            float f2 = (rand.nextFloat() - 0.5F) * 0.2F;
            double d7 = d3 + (entity.posX - d3) * d6 + (rand.nextDouble() - 0.5D) * entity.width * 2.0D;
            double d8 = d4 + (entity.posY - d4) * d6 + rand.nextDouble() * entity.height;
            double d9 = d5 + (entity.posZ - d5) * d6 + (rand.nextDouble() - 0.5D) * entity.width * 2.0D;
            entity.world.spawnParticle(EnumParticleTypes.PORTAL, d7, d8, d9, f, f1, f2);
        }

        entity.world.playSound(null, entity.prevPosX, entity.prevPosY, entity.prevPosZ,
                SoundEvents.ENTITY_ENDERMEN_TELEPORT, entity.getSoundCategory(), 1.0F, 1.0F);
        entity.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
        return true;

    }

    @Override
    public int getPrimaryColor() {
        return 0x440066;
    }

    @Override
    public Object getRender(RenderManager manager) {
        return new RenderDarkCreeper(manager);
    }

    static class AIFindPlayer extends EntityAINearestAttackableTarget<EntityPlayer> {

        private final EntityDarkCreeper enderman;
        /**
         * The player
         */
        private EntityPlayer player;
        private int aggroTime;
        private int teleportTime;

        public AIFindPlayer(EntityDarkCreeper p_i45842_1_) {
            super(p_i45842_1_, EntityPlayer.class, false);
            this.enderman = p_i45842_1_;
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        @Override
        public boolean shouldExecute() {
            double d0 = this.getTargetDistance();
            this.player = this.enderman.world.getNearestAttackablePlayer(this.enderman.posX, this.enderman.posY,
                    this.enderman.posZ, d0, d0, null,
                    p_apply_1_ -> p_apply_1_ != null && AIFindPlayer.this.enderman.shouldAttackPlayer(p_apply_1_));
            return this.player != null;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        @Override
        public void startExecuting() {
            this.aggroTime = 5;
            this.teleportTime = 0;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        @Override
        public boolean shouldContinueExecuting() {
            if (this.player != null) {
                if (!this.enderman.shouldAttackPlayer(this.player)) {
                    return false;
                } else {
                    this.enderman.faceEntity(this.player, 10.0F, 10.0F);
                    return true;
                }
            } else {
                return this.targetEntity != null && this.targetEntity.isEntityAlive() ||
                        super.shouldContinueExecuting();
            }
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        @Override
        public void resetTask() {
            this.player = null;
            super.resetTask();
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        @Override
        public void updateTask() {
            if (this.player != null) {
                if (--this.aggroTime <= 0) {
                    this.targetEntity = this.player;
                    this.player = null;
                    super.startExecuting();
                }
            } else {
                if (this.targetEntity != null) {
                    if (this.enderman.shouldAttackPlayer(this.targetEntity)) {
                        if (this.targetEntity.getDistanceSqToEntity(this.enderman) < 16.0D) {
                            this.enderman.teleportRandomly();
                        }

                        this.teleportTime = 0;
                    } else if (this.targetEntity.getDistanceSqToEntity(this.enderman) > 256.0D &&
                            this.teleportTime++ >= 30 && this.enderman.teleportToEntity(this.targetEntity)) {
                        this.teleportTime = 0;
                    }
                }

                super.updateTask();
            }
        }
    }

    static class AIPlaceBlock extends EntityAIBase {

        private final EntityDarkCreeper enderman;

        public AIPlaceBlock(EntityDarkCreeper p_i45843_1_) {
            this.enderman = p_i45843_1_;
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        @Override
        public boolean shouldExecute() {
            return this.enderman.getHeldBlockState() != null &&
                    this.enderman.world.getGameRules().getBoolean("mobGriefing") &&
                    this.enderman.getRNG().nextInt(2000) == 0;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        @Override
        public void updateTask() {
            Random random = this.enderman.getRNG();
            World world = this.enderman.world;
            int i = MathHelper.floor(this.enderman.posX - 1.0D + random.nextDouble() * 2.0D);
            int j = MathHelper.floor(this.enderman.posY + random.nextDouble() * 2.0D);
            int k = MathHelper.floor(this.enderman.posZ - 1.0D + random.nextDouble() * 2.0D);
            BlockPos blockpos = new BlockPos(i, j, k);
            IBlockState iblockstate = world.getBlockState(blockpos);
            IBlockState iblockstate1 = world.getBlockState(blockpos.down());
            IBlockState iblockstate2 = this.enderman.getHeldBlockState();

            if (iblockstate2 != null &&
                    this.canPlaceBlock(world, blockpos, iblockstate2.getBlock(), iblockstate, iblockstate1)) {
                world.setBlockState(blockpos, iblockstate2, 3);
                this.enderman.setHeldBlockState(null);
            }
        }

        private boolean canPlaceBlock(World p_188518_1_, BlockPos p_188518_2_, Block p_188518_3_,
                IBlockState p_188518_4_, IBlockState p_188518_5_) {
            return p_188518_3_.canPlaceBlockAt(p_188518_1_, p_188518_2_) && p_188518_4_.getMaterial() == Material.AIR &&
                    p_188518_5_.getMaterial() != Material.AIR && p_188518_5_.isFullCube();
        }
    }

    static class AITakeBlock extends EntityAIBase {

        private final EntityDarkCreeper enderman;

        public AITakeBlock(EntityDarkCreeper p_i45841_1_) {
            this.enderman = p_i45841_1_;
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        @Override
        public boolean shouldExecute() {
            return this.enderman.getHeldBlockState() == null &&
                    this.enderman.world.getGameRules().getBoolean("mobGriefing") &&
                    this.enderman.getRNG().nextInt(20) == 0;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        @Override
        public void updateTask() {
            Random random = this.enderman.getRNG();
            World world = this.enderman.world;
            int i = MathHelper.floor(this.enderman.posX - 2.0D + random.nextDouble() * 4.0D);
            int j = MathHelper.floor(this.enderman.posY + random.nextDouble() * 3.0D);
            int k = MathHelper.floor(this.enderman.posZ - 2.0D + random.nextDouble() * 4.0D);
            BlockPos blockpos = new BlockPos(i, j, k);
            IBlockState iblockstate = world.getBlockState(blockpos);
            Block block = iblockstate.getBlock();
            RayTraceResult raytraceresult = world.rayTraceBlocks(
                    new Vec3d(MathHelper.floor(this.enderman.posX) + 0.5F, j + 0.5F,
                            MathHelper.floor(this.enderman.posZ) + 0.5F), new Vec3d(i + 0.5F, j + 0.5F, k + 0.5F),
                    false, true, false);
            boolean flag = raytraceresult != null && raytraceresult.getBlockPos().equals(blockpos);

            if (CARRIABLE_BLOCKS.contains(block) && flag) {
                this.enderman.setHeldBlockState(iblockstate);
                world.setBlockToAir(blockpos);
            }
        }
    }


}
