package com.tntmodders.takumi.entity.mobs.boss;

import com.tntmodders.takumi.client.render.RenderSpiderCreeper;
import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.entity.mobs.EntitySpiderCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.*;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class EntityBigSpiderCreeper extends EntitySpiderCreeper {

    private static final DataParameter<Byte> CLIMBING =
            EntityDataManager.createKey(EntityBigSpiderCreeper.class, DataSerializers.BYTE);
    private final BossInfoServer bossInfo =
            (BossInfoServer) new BossInfoServer(new TextComponentTranslation("entity.bigspidercreeper.name"),
                    BossInfo.Color.YELLOW, BossInfo.Overlay.PROGRESS);

    public EntityBigSpiderCreeper(World worldIn) {
        super(worldIn);
        this.setSize(2.8F, 1.8F);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(CLIMBING, (byte) 0);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_SPIDER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SPIDER_DEATH;
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_SPIDER;
    }

    /**
     * Returns new PathNavigateGround instance
     */
    @Override
    protected PathNavigate createNavigator(World worldIn) {
        return new PathNavigateClimber(this, worldIn);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SPIDER_AMBIENT;
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
        livingdata = super.onInitialSpawn(difficulty, livingdata);

        if (this.world.rand.nextInt(100) == 0) {
            EntitySkeleton entityskeleton = new EntitySkeleton(this.world);
            entityskeleton.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
            entityskeleton.onInitialSpawn(difficulty, null);
            this.world.spawnEntity(entityskeleton);
            entityskeleton.startRiding(this);
        }

        if (livingdata == null) {
            livingdata = new GroupData();

            if (this.world.getDifficulty() == EnumDifficulty.HARD &&
                    this.world.rand.nextFloat() < 0.1F * difficulty.getClampedAdditionalDifficulty()) {
                ((GroupData) livingdata).setRandomEffect(this.world.rand);
            }
        }

        if (livingdata instanceof GroupData) {
            Potion potion = ((GroupData) livingdata).effect;

            if (potion != null) {
                this.addPotionEffect(new PotionEffect(potion, Integer.MAX_VALUE));
            }
        }

        return livingdata;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(SoundEvents.ENTITY_SPIDER_STEP, 0.15F, 1.0F);
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding this one.
     */
    @Override
    public double getMountedYOffset() {
        return (double) (this.height * 0.5F);
    }

    /**
     * Sets the Entity inside a web block.
     */
    @Override
    public void setInWeb() {
    }

    @Override
    public float getEyeHeight() {
        return 0.65F * 2;
    }

    @Override
    public boolean isPotionApplicable(PotionEffect potioneffectIn) {
        return potioneffectIn.getPotion() != MobEffects.POISON && super.isPotionApplicable(potioneffectIn);
    }

    /**
     * returns true if this entity is by a ladder, false otherwise
     */
    @Override
    public boolean isOnLadder() {
        return this.isBesideClimbableBlock();
    }

    /**
     * Get this Entity's EnumCreatureAttribute
     */
    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.ARTHROPOD;
    }

    /**
     * Returns true if the WatchableObject (Byte) is 0x01 otherwise returns false. The WatchableObject is updated using
     * setBesideClimableBlock.
     */
    @Override
    public boolean isBesideClimbableBlock() {
        return (this.dataManager.get(CLIMBING) & 1) != 0;
    }

    /**
     * Updates the WatchableObject (Byte) created in entityInit(), setting it to 0x01 if par1 is true or 0x00 if it is
     * false.
     */
    @Override
    public void setBesideClimbableBlock(boolean climbing) {
        byte b0 = this.dataManager.get(CLIMBING);

        if (climbing) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 = (byte) (b0 & -2);
        }

        this.dataManager.set(CLIMBING, b0);
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            int r = this.getPowered() ? 15 : 10;
            for (int t = 0; t < r * 1.5; t++) {
                EntitySpiderCreeper spiderCreeper = new EntitySpiderCreeper(this.world);
                spiderCreeper.setPosition(this.posX, this.posY, this.posZ);
                spiderCreeper.setAttackTarget(this.getAttackTarget());
                this.world.spawnEntity(spiderCreeper);
            }
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.GROUND_MD;
    }

    @Override
    public int getExplosionPower() {
        return 6;
    }

    @Override
    public int getSecondaryColor() {
        return 1334334;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "bigspidercreeper";
    }

    @Override
    public int getRegisterID() {
        return 271;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Object getRender(RenderManager manager) {
        return new RenderSpiderCreeper<>(manager);
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        super.onUpdate();
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        if (!this.world.isRemote) {
            this.setBesideClimbableBlock(this.isCollidedHorizontally);
        }

        if (this.getAttackTarget() != null && this.getDistanceSqToEntity(this.getAttackTarget()) < 49f) {
            if (!(this.getAttackTarget() instanceof EntityPlayer &&
                    ((EntityPlayer) this.getAttackTarget()).isCreative())) {
                this.ignite();
            }
        }
    }

    @Override
    public void onLivingUpdate() {
        if (this.world.playerEntities.stream().anyMatch(this :: canEntityBeSeen)) {
            bossInfo.setVisible(true);
            if (this.world.isRemote) {
                this.world.spawnEntity(
                        new EntityLightningBolt(this.world, this.posX + this.rand.nextDouble() * 7 - 3, this.posY,
                                this.posZ + this.rand.nextDouble() * 7 - 3, true));
            }
        } else {
            bossInfo.setVisible(false);
        }
        super.onLivingUpdate();
    }

    @Override
    public boolean getCanSpawnHere() {
        return this.rand.nextInt(5) == 0 && super.getCanSpawnHere() && TakumiUtils.canSpawnElementBoss(this.world);
    }

    @Override
    protected void outOfWorld() {
        this.setHealth(0);
        super.outOfWorld();
    }

    @Override
    public boolean isNonBoss() {
        return false;
    }

/*    @Override
    public void onUpdate() {
        super.onUpdate();
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
    }*/

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }

    @Override
    public void addTrackingPlayer(EntityPlayerMP player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void removeTrackingPlayer(EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        event.getAffectedEntities().clear();
        event.getAffectedBlocks().forEach(
                pos -> TakumiUtils.setBlockStateProtected(this.world, pos, Blocks.WEB.getDefaultState()));
        event.getAffectedBlocks().clear();
        return true;
    }

    @Override
    public int getPrimaryColor() {
        return 0x889988;
    }

    @Override
    public void setDead() {
        if (!(this.getHealth() <= 0 || this.world.getDifficulty() == EnumDifficulty.PEACEFUL)) {
            if (!this.world.isRemote) {
                EntityBigSpiderCreeper kingCreeper = new EntityBigSpiderCreeper(this.world);
                NBTTagCompound tagCompound = new NBTTagCompound();
                this.writeEntityToNBT(tagCompound);
                tagCompound.setBoolean("ignited", false);
                kingCreeper.readEntityFromNBT(tagCompound);
                kingCreeper.setHealth(this.getHealth());
                kingCreeper.copyLocationAndAnglesFrom(this);
                if (this.getPowered()) {
                    TakumiUtils.takumiSetPowered(kingCreeper, true);
                }
                kingCreeper.setCreeperState(-1);
                kingCreeper.setAttackTarget(null);
                this.world.spawnEntity(kingCreeper);
            }
        }
        super.setDead();
    }

    @Override
    public void onDeath(DamageSource source) {
        if (!this.world.isRemote) {
            this.entityDropItem(new ItemStack(TakumiItemCore.TAKUMI_TYPE_CORE, 1 + this.rand.nextInt(2), 4), 0);
        }
        super.onDeath(source);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
    }
}
