package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.client.render.RenderZombieCreeper;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.ai.EntityAIZombieCreeperAttack;
import com.tntmodders.takumi.entity.ai.EntityAIZombieCreeperSwell;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class EntityZombieCreeper extends EntityTakumiAbstractCreeper {
    protected static final IAttribute SPAWN_REINFORCEMENTS_CHANCE = (new RangedAttribute(null, "zombie.spawnReinforcements", 0.0D, 0.0D, 1.0D)).setDescription("Spawn Reinforcements Chance");
    private static final UUID BABY_SPEED_BOOST_ID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
    private static final AttributeModifier BABY_SPEED_BOOST = new AttributeModifier(BABY_SPEED_BOOST_ID, "Baby speed boost", 0.5D, 1);
    private static final DataParameter<Boolean> IS_CHILD = EntityDataManager.createKey(EntityZombieCreeper.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> VILLAGER_TYPE = EntityDataManager.createKey(EntityZombieCreeper.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> ARMS_RAISED = EntityDataManager.createKey(EntityZombieCreeper.class, DataSerializers.BOOLEAN);
    private final EntityAIBreakDoor breakDoor = new EntityAIBreakDoor(this);
    private boolean isBreakDoorsTaskSet;
    /**
     * The width of the entity
     */
    private float zombieWidth = -1.0F;
    /**
     * The height of the the entity.
     */
    private float zombieHeight;

    public EntityZombieCreeper(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.95F);
    }

    /**
     * Sets the width and height of the entity.
     */
    @Override
    protected final void setSize(float width, float height) {
        boolean flag = this.zombieWidth > 0.0F && this.zombieHeight > 0.0F;
        this.zombieWidth = width;
        this.zombieHeight = height;

        if (!flag) {
            this.multiplySize(1.0F);
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.ENTITY_ZOMBIE_STEP;
    }

    /**
     * Returns the Y Offset of this entity.
     */
    @Override
    public double getYOffset() {
        return this.isChild() ? 0.0D : -0.45D;
    }

    /**
     * This method gets called when the entity kills another one.
     */
    @Override
    public void onKillEntity(EntityLivingBase entityLivingIn) {
        super.onKillEntity(entityLivingIn);

        if ((this.world.getDifficulty() == EnumDifficulty.NORMAL || this.world.getDifficulty() == EnumDifficulty.HARD) && entityLivingIn instanceof EntityVillager) {
            if (this.world.getDifficulty() != EnumDifficulty.EASY && this.rand.nextBoolean()) {
                return;
            }
            this.changeVillager(((EntityVillager) entityLivingIn));
        }
    }

    protected void changeVillager(EntityVillager entityvillager) {
        EntityZombieVillagerCreeper entityzombievillager = new EntityZombieVillagerCreeper(this.world);
        entityzombievillager.copyLocationAndAnglesFrom(entityvillager);
        this.world.removeEntity(entityvillager);
        entityzombievillager.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entityzombievillager)), new EntityZombieCreeper.GroupData(false));
        entityzombievillager.setProfession(entityvillager.getProfession());
        entityzombievillager.setChild(entityvillager.isChild());
        entityzombievillager.setNoAI(entityvillager.isAIDisabled());

        if (entityvillager.hasCustomName()) {
            entityzombievillager.setCustomNameTag(entityvillager.getCustomNameTag());
            entityzombievillager.setAlwaysRenderNameTag(entityvillager.getAlwaysRenderNameTag());
        }
        for (int i = 0; i < 6; i++) {
            this.setItemStackToSlot(EntityEquipmentSlot.values()[i], ItemStack.EMPTY);
        }
        this.world.spawnEntity(entityzombievillager);
        this.world.playEvent(null, 1026, new BlockPos(this), 0);
    }

    /**
     * sets the size of the entity to be half of its current size if true.
     */
    public void setChildSize(boolean isChild) {
        this.multiplySize(isChild ? 0.5F : 1.0F);
    }

    @Override
    public float getEyeHeight() {
        float f = 1.74F;

        if (this.isChild()) {
            f = (float) ((double) f - 0.81D);
        }

        return f;
    }

    public static void registerFixesZombie(DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityZombieCreeper.class);
    }

    /**
     * If Animal, checks if the age timer is negative
     */
    @Override
    public boolean isChild() {
        return this.getDataManager().get(IS_CHILD);
    }

    protected void applyEntityAI() {
        this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, EntityPigZombie.class));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityVillager.class, false));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
    }

    /**
     * Set whether this zombie is a child.
     */
    public void setChild(boolean childZombie) {
        this.getDataManager().set(IS_CHILD, childZombie);

        if (this.world != null && !this.world.isRemote) {
            IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
            iattributeinstance.removeModifier(BABY_SPEED_BOOST);

            if (childZombie) {
                iattributeinstance.applyModifier(BABY_SPEED_BOOST);
            }
        }

        this.setChildSize(childZombie);
    }

    /**
     * Get this Entity's EnumCreatureAttribute
     */
    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        if (IS_CHILD.equals(key)) {
            this.setChildSize(this.isChild());
        }

        super.notifyDataManagerChange(key);
    }

    /**
     * Multiplies the height and width by the provided float.
     */
    protected final void multiplySize(float size) {
        super.setSize(this.zombieWidth * size, this.zombieHeight * size);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIZombieCreeperSwell(this));
        this.tasks.addTask(2, new EntityAIZombieCreeperAttack(this, 1.0D, false));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.applyEntityAI();
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
        this.getAttributeMap().registerAttribute(SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(this.rand.nextDouble() * net.minecraftforge.common.ForgeModContainer.zombieSummonBaseChance);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(IS_CHILD, Boolean.FALSE);
        this.getDataManager().register(VILLAGER_TYPE, 0);
        this.getDataManager().register(ARMS_RAISED, Boolean.FALSE);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);

        if (this.isChild()) {
            compound.setBoolean("IsBaby", true);
        }

        compound.setBoolean("CanBreakDoors", this.isBreakDoorsTaskSet());
    }

    public boolean isBreakDoorsTaskSet() {
        return this.isBreakDoorsTaskSet;
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);

        if (compound.getBoolean("IsBaby")) {
            this.setChild(true);
        }

        this.setBreakDoorsAItask(compound.getBoolean("CanBreakDoors"));
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_ZOMBIE_HURT;
    }

    @SideOnly(Side.CLIENT)
    public boolean isArmsRaised() {
        return this.getDataManager().get(ARMS_RAISED);
    }

    public void setArmsRaised(boolean armsRaised) {
        this.getDataManager().set(ARMS_RAISED, armsRaised);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ZOMBIE_DEATH;
    }

    /**
     * Called when the mob's health reaches 0.
     */
    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        for (ItemStack itemStack : this.getArmorInventoryList()) {
            if (!this.world.isRemote && this.rand.nextBoolean() && itemStack != null &&
                    itemStack.getItem() != Item.getItemFromBlock(Blocks.PUMPKIN) && itemStack.getItem() != Item.getItemFromBlock(Blocks.LIT_PUMPKIN)) {
                this.entityDropItem(itemStack, 0.0f);
            }
        }
        if (cause.getTrueSource() instanceof EntityCreeper) {
            EntityCreeper entitycreeper = (EntityCreeper) cause.getTrueSource();

            if (entitycreeper.getPowered() && entitycreeper.isAIEnabled()) {
                entitycreeper.incrementDroppedSkulls();
                ItemStack itemstack = this.getSkullDrop();

                if (!itemstack.isEmpty()) {
                    this.entityDropItem(itemstack, 0.0F);
                }
            }
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        boolean flag = super.attackEntityAsMob(entityIn);

        if (flag) {
            float f = this.world.getDifficultyForLocation(new BlockPos(this)).getAdditionalDifficulty();

            if (this.getHeldItemMainhand().isEmpty() && this.isBurning() && this.rand.nextFloat() < f * 0.3F) {
                entityIn.setFire(2 * (int) f);
            }
        }

        return flag;
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_ZOMBIE;
    }

    protected ItemStack getSkullDrop() {
        return new ItemStack(Items.SKULL, 1, 2);
    }

    /**
     * Sets or removes EntityAIBreakDoor task
     */
    public void setBreakDoorsAItask(boolean enabled) {
        if (this.isBreakDoorsTaskSet != enabled) {
            this.isBreakDoorsTaskSet = enabled;
            ((PathNavigateGround) this.getNavigator()).setBreakDoors(enabled);

            if (enabled) {
                this.tasks.addTask(1, this.breakDoor);
            } else {
                this.tasks.removeTask(this.breakDoor);
            }
        }
    }

    @Override
    protected int getExperiencePoints(EntityPlayer player) {
        if (this.isChild()) {
            return ((int) (super.getExperiencePoints(player) * 2.5));
        }

        return super.getExperiencePoints(player);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ZOMBIE_AMBIENT;
    }

    @Override
    protected boolean canEquipItem(ItemStack stack) {
        return stack.getItem() != Items.EGG || !this.isChild() || !this.isRiding() && super.canEquipItem(stack);
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        super.setEquipmentBasedOnDifficulty(difficulty);
        if (this.rand.nextFloat() < (this.world.getDifficulty() == EnumDifficulty.HARD ? 0.3F : 0.1F)) {
            this.setArmors();
        }
    }

    /**
     * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
     * when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
     */
    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        float f = difficulty.getClampedAdditionalDifficulty();
        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * f);

        if (livingdata == null) {
            livingdata = new EntityZombieCreeper.GroupData(this.world.rand.nextFloat() < net.minecraftforge.common.ForgeModContainer.zombieBabyChance);
        }

        if (livingdata instanceof EntityZombieCreeper.GroupData) {
            EntityZombieCreeper.GroupData entityzombie$groupdata = (EntityZombieCreeper.GroupData) livingdata;

            if (entityzombie$groupdata.isChild) {
                this.setChild(true);

                if ((double) this.world.rand.nextFloat() < 0.05D) {
                    List<EntityChicken> list = this.world.getEntitiesWithinAABB(EntityChicken.class, this.getEntityBoundingBox().grow(5.0D, 3.0D, 5.0D), EntitySelectors.IS_STANDALONE);

                    if (!list.isEmpty()) {
                        EntityChicken entitychicken = list.get(0);
                        entitychicken.setChickenJockey(true);
                        this.startRiding(entitychicken);
                    }
                } else if ((double) this.world.rand.nextFloat() < 0.05D) {
                    EntityChicken entitychicken1 = new EntityChicken(this.world);
                    entitychicken1.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                    entitychicken1.onInitialSpawn(difficulty, null);
                    entitychicken1.setChickenJockey(true);
                    this.world.spawnEntity(entitychicken1);
                    this.startRiding(entitychicken1);
                }
            }
        }

        this.setBreakDoorsAItask(this.rand.nextBoolean());
        this.setEquipmentBasedOnDifficulty(difficulty);
        this.setEnchantmentBasedOnDifficulty(difficulty);

        if (this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) {
            Calendar calendar = this.world.getCurrentDate();

            if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.rand.nextFloat() < 0.25F) {
                this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(this.rand.nextFloat() < 0.1F ? Blocks.LIT_PUMPKIN : Blocks.PUMPKIN));
                this.inventoryArmorDropChances[EntityEquipmentSlot.HEAD.getIndex()] = 0.0F;
            }
        }

        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextDouble() * 0.05000000074505806D, 0));
        double d0 = this.rand.nextDouble() * 1.5D * (double) f;

        if (d0 > 1.0D) {
            this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random zombie-spawn bonus", d0, 2));
        }

        if (this.rand.nextFloat() < f * 0.05F) {
            this.getEntityAttribute(SPAWN_REINFORCEMENTS_CHANCE).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 0.25D + 0.5D, 0));
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 3.0D + 1.0D, 2));
            this.setBreakDoorsAItask(true);
        }

        return livingdata;
    }

    protected boolean shouldBurnInDay() {
        return true;
    }

    @Override
    public void onLivingUpdate() {
        if (this.world.isDaytime() && !this.world.isRemote && !this.isChild() && this.shouldBurnInDay()) {
            float f = this.getBrightness();

            if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.world.canSeeSky(new BlockPos(this.posX, this.posY + (double) this.getEyeHeight(), this.posZ))) {
                boolean flag = true;
                ItemStack itemstack = this.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

                if (!itemstack.isEmpty()) {
                    if (itemstack.isItemStackDamageable()) {
                        itemstack.setItemDamage(itemstack.getItemDamage() + this.rand.nextInt(2));

                        if (itemstack.getItemDamage() >= itemstack.getMaxDamage()) {
                            this.renderBrokenItemStack(itemstack);
                            this.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack.EMPTY);
                        }
                    }

                    flag = false;
                }

                if (flag) {
                    this.setFire(8);
                }
            }
        }

        super.onLivingUpdate();
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (super.attackEntityFrom(source, amount)) {
            EntityLivingBase entitylivingbase = this.getAttackTarget();

            if (entitylivingbase == null && source.getTrueSource() instanceof EntityLivingBase) {
                entitylivingbase = (EntityLivingBase) source.getTrueSource();
            }

            int i = MathHelper.floor(this.posX);
            int j = MathHelper.floor(this.posY);
            int k = MathHelper.floor(this.posZ);

            if (entitylivingbase != null && this.world.getDifficulty() == EnumDifficulty.HARD &&
                    (double) this.rand.nextFloat() < this.getEntityAttribute(SPAWN_REINFORCEMENTS_CHANCE).getAttributeValue() &&
                    this.world.getGameRules().getBoolean("doMobSpawning")) {
                EntityZombieCreeper entityzombie = new EntityZombieCreeper(this.world);


                for (int l = 0; l < 50; ++l) {
                    int i1 = i + MathHelper.getInt(this.rand, 7, 40) * MathHelper.getInt(this.rand, -1, 1);
                    int j1 = j + MathHelper.getInt(this.rand, 7, 40) * MathHelper.getInt(this.rand, -1, 1);
                    int k1 = k + MathHelper.getInt(this.rand, 7, 40) * MathHelper.getInt(this.rand, -1, 1);

                    if (this.world.getBlockState(new BlockPos(i1, j1 - 1, k1)).isSideSolid(this.world, new BlockPos(i1, j1 - 1, k1), net.minecraft.util.EnumFacing.UP) && this.world.getLightFromNeighbors(new BlockPos(i1, j1, k1)) < 10) {
                        entityzombie.setPosition((double) i1, (double) j1, (double) k1);

                        if (!this.world.isAnyPlayerWithinRangeAt((double) i1, (double) j1, (double) k1, 7.0D) && this.world.checkNoEntityCollision(entityzombie.getEntityBoundingBox(), entityzombie) && this.world.getCollisionBoxes(entityzombie, entityzombie.getEntityBoundingBox()).isEmpty() && !this.world.containsAnyLiquid(entityzombie.getEntityBoundingBox())) {
                            this.world.spawnEntity(entityzombie);
                            if (entitylivingbase != null) entityzombie.setAttackTarget(entitylivingbase);
                            entityzombie.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entityzombie)), null);
                            this.getEntityAttribute(SPAWN_REINFORCEMENTS_CHANCE).applyModifier(new AttributeModifier("Zombie reinforcement caller charge", -0.05000000074505806D, 0));
                            entityzombie.getEntityAttribute(SPAWN_REINFORCEMENTS_CHANCE).applyModifier(new AttributeModifier("Zombie reinforcement callee charge", -0.05000000074505806D, 0));
                            break;
                        }
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    protected void setArmors() {
        if (this.rand.nextInt(3) == 0) {
            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, this.getArmorItem(Items.IRON_SWORD));
        } else if (this.rand.nextInt(3) != 2) {
            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, this.getArmorItem(Items.IRON_SHOVEL));
        }
        if (this.rand.nextInt(25) == 0) {
            this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, this.getArmorItem(Item.getItemFromBlock(TakumiBlockCore.CREEPER_BOMB)));
        }
        if (this.rand.nextInt(5) == 0) {
            this.setItemStackToSlot(EntityEquipmentSlot.HEAD, this.getArmorItem(Items.CHAINMAIL_HELMET));
        }
        if (this.rand.nextInt(5) == 0) {
            this.setItemStackToSlot(EntityEquipmentSlot.CHEST, this.getArmorItem(Items.CHAINMAIL_CHESTPLATE));
        }
        if (this.rand.nextInt(5) == 0) {
            this.setItemStackToSlot(EntityEquipmentSlot.LEGS, this.getArmorItem(Items.CHAINMAIL_LEGGINGS));
        }
        if (this.rand.nextInt(5) == 0) {
            this.setItemStackToSlot(EntityEquipmentSlot.FEET, this.getArmorItem(Items.CHAINMAIL_BOOTS));
        }
    }

    /**
     * Gives armor or weapon for entity based on given DifficultyInstance
     */

    private ItemStack getArmorItem(Item item) {
        ItemStack itemStack = new ItemStack(item);
        int i = item.getMaxDamage(itemStack) - 1;
        if (i > 0) {
            itemStack.setItemDamage(this.rand.nextInt(i) + 1);
        }
        return itemStack;
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
        return 112211;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "zombiecreeper";
    }

    @Override
    public int getRegisterID() {
        return 1;
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        List<Entity> removeList = new ArrayList<>();
        for (Entity entity : event.getAffectedEntities()) {
            if (entity instanceof EntityVillager) {
                removeList.add(entity);
                this.changeVillager(((EntityVillager) entity));
            } else if (entity instanceof EntityZombieCreeper) {
                removeList.add(entity);
            }
        }
        event.getAffectedEntities().removeAll(removeList);
        return true;
    }

    @Override
    public int getPrimaryColor() {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public RenderLiving getRender(RenderManager manager) {
        return new RenderZombieCreeper(manager);
    }

    @Override
    public ResourceLocation getArmor() {
        return new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    }

    class GroupData implements IEntityLivingData {
        public boolean isChild;

        private GroupData(boolean p_i47328_2_) {
            this.isChild = p_i47328_2_;
        }
    }
}
