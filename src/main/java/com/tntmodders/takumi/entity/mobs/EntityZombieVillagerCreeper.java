package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.client.render.RenderZombieVillagerCreeper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityZombieVillagerCreeper extends EntityZombieCreeper {

    private static final DataParameter<Boolean> CONVERTING =
            EntityDataManager.createKey(EntityZombieVillagerCreeper.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> PROFESSION =
            EntityDataManager.createKey(EntityZombieVillagerCreeper.class, DataSerializers.VARINT);
    /**
     * Ticker used to determine the time remaining for this zombie to convert into a villager when cured.
     */
    private int conversionTime;
    private UUID converstionStarter;
    @Nullable
    private VillagerProfession prof;

    public EntityZombieVillagerCreeper(World worldIn) {
        super(worldIn);
    }

    public static void registerFixesZombieVillager(DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityZombieVillagerCreeper.class);
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        if (!this.world.isRemote && this.isConverting()) {
            int i = this.getConversionProgress();
            this.conversionTime -= i;

            if (this.conversionTime <= 0) {
                this.finishConversion();
            }
        }
        super.onUpdate();
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        super.processInteract(player, hand);
        ItemStack itemstack = player.getHeldItem(hand);

        if (itemstack.getItem() == Items.GOLDEN_APPLE && itemstack.getMetadata() == 0 &&
                this.isPotionActive(MobEffects.WEAKNESS)) {
            if (!player.capabilities.isCreativeMode) {
                itemstack.shrink(1);
            }

            if (!this.world.isRemote) {
                this.startConverting(player.getUniqueID(), this.rand.nextInt(2401) + 3600);
            }

            return true;
        } else {
            return false;
        }
    }

    protected void startConverting(
            @Nullable
                    UUID p_191991_1_, int p_191991_2_) {
        this.converstionStarter = p_191991_1_;
        this.conversionTime = p_191991_2_;
        this.getDataManager().set(CONVERTING, Boolean.TRUE);
        this.removePotionEffect(MobEffects.WEAKNESS);
        this.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, p_191991_2_,
                Math.min(this.world.getDifficulty().getDifficultyId() - 1, 0)));
        this.world.setEntityState(this, (byte) 16);
    }

    /**
     * Returns whether this zombie is in the process of converting to a villager
     */
    public boolean isConverting() {
        return this.getDataManager().get(CONVERTING);
    }

    protected int getConversionProgress() {
        int i = 1;

        if (this.rand.nextFloat() < 0.01F) {
            int j = 0;
            MutableBlockPos blockpos$mutableblockpos = new MutableBlockPos();

            for (int k = (int) this.posX - 4; k < (int) this.posX + 4 && j < 14; ++k) {
                for (int l = (int) this.posY - 4; l < (int) this.posY + 4 && j < 14; ++l) {
                    for (int i1 = (int) this.posZ - 4; i1 < (int) this.posZ + 4 && j < 14; ++i1) {
                        Block block = this.world.getBlockState(blockpos$mutableblockpos.setPos(k, l, i1)).getBlock();

                        if (block == Blocks.IRON_BARS || block == Blocks.BED) {
                            if (this.rand.nextFloat() < 0.3F) {
                                ++i;
                            }

                            ++j;
                        }
                    }
                }
            }
        }

        return i;
    }

    protected void finishConversion() {
        EntityVillager entityvillager = new EntityVillager(this.world);
        entityvillager.copyLocationAndAnglesFrom(this);
        entityvillager.setProfession(this.getForgeProfession());
        entityvillager.finalizeMobSpawn(this.world.getDifficultyForLocation(new BlockPos(entityvillager)), null, false);
        entityvillager.setLookingForHome();

        if (this.isChild()) {
            entityvillager.setGrowingAge(-24000);
        }

        this.world.removeEntity(this);
        entityvillager.setNoAI(this.isAIDisabled());

        if (this.hasCustomName()) {
            entityvillager.setCustomNameTag(this.getCustomNameTag());
            entityvillager.setAlwaysRenderNameTag(this.getAlwaysRenderNameTag());
        }

        this.world.spawnEntity(entityvillager);

        if (this.converstionStarter != null) {
            EntityPlayer entityplayer = this.world.getPlayerEntityByUUID(this.converstionStarter);

            if (entityplayer instanceof EntityPlayerMP) {
                CriteriaTriggers.CURED_ZOMBIE_VILLAGER.trigger((EntityPlayerMP) entityplayer, null, entityvillager);
            }
        }

        entityvillager.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 200, 0));
        this.world.playEvent(null, 1027, new BlockPos((int) this.posX, (int) this.posY, (int) this.posZ), 0);
    }

    public VillagerProfession getForgeProfession() {
        if (this.prof == null) {
            this.prof = VillagerRegistry.getById(this.getProfession());
            if (this.prof == null) {
                return VillagerRegistry.FARMER;
            }
        }
        return this.prof;
    }

    public int getProfession() {
        return Math.max(this.dataManager.get(PROFESSION), 0);
    }

    public void setProfession(int profession) {
        this.dataManager.set(PROFESSION, profession);
    }

    public void setForgeProfession(VillagerProfession prof) {
        this.prof = prof;
        this.setProfession(VillagerRegistry.getId(prof));
    }

    /**
     * Handler for {@link World#setEntityState}
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 16) {
            if (!this.isSilent()) {
                this.world.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D,
                        SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, this.getSoundCategory(), 1.0F + this.rand.nextFloat(),
                        this.rand.nextFloat() * 0.7F + 0.3F, false);
            }
        } else {
            super.handleStatusUpdate(id);
        }
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    @Override
    protected boolean canDespawn() {
        return !this.isConverting();
    }

    /**
     * Gets the pitch of living sounds in living entities.
     */
    @Override
    protected float getSoundPitch() {
        return this.isChild() ? (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 2.0F :
                (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F;
    }

    @Override
    public SoundEvent getStepSound() {
        return SoundEvents.ENTITY_ZOMBIE_VILLAGER_STEP;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(CONVERTING, Boolean.FALSE);
        this.dataManager.register(PROFESSION, 0);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Profession", this.getProfession());
        compound.setString("ProfessionName", this.getForgeProfession().getRegistryName().toString());
        compound.setInteger("ConversionTime", this.isConverting() ? this.conversionTime : -1);

        if (this.converstionStarter != null) {
            compound.setUniqueId("ConversionPlayer", this.converstionStarter);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setProfession(compound.getInteger("Profession"));
        if (compound.hasKey("ProfessionName")) {
            VillagerProfession p = ForgeRegistries.VILLAGER_PROFESSIONS
                    .getValue(new ResourceLocation(compound.getString("ProfessionName")));
            if (p == null) {
                p = VillagerRegistry.FARMER;
            }
            this.setForgeProfession(p);
        }

        if (compound.hasKey("ConversionTime", 99) && compound.getInteger("ConversionTime") > -1) {
            this.startConverting(
                    compound.hasUniqueId("ConversionPlayer") ? compound.getUniqueId("ConversionPlayer") : null,
                    compound.getInteger("ConversionTime"));
        }
    }

    @Override
    public SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_ZOMBIE_VILLAGER_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ZOMBIE_VILLAGER_DEATH;
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_ZOMBIE_VILLAGER;
    }

    @Override
    protected ItemStack getSkullDrop() {
        return ItemStack.EMPTY;
    }

    @Override
    public int getPrimaryColor() {
        return 3869451;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Object getRender(RenderManager manager) {
        return new RenderZombieVillagerCreeper(manager);
    }

    @Override
    public ResourceLocation getArmor() {
        return new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ZOMBIE_VILLAGER_AMBIENT;
    }

    /**
     * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
     * when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
     */
    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty,
            @Nullable
                    IEntityLivingData livingdata) {
        this.setProfession(this.world.rand.nextInt(6));
        return super.onInitialSpawn(difficulty, livingdata);
    }

    @Override
    protected void setArmors() {

    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL_M;
    }

    @Override
    public String getRegisterName() {
        return "zombievillagercreeper";
    }

    @Override
    public int getRegisterID() {
        return 2;
    }
}
