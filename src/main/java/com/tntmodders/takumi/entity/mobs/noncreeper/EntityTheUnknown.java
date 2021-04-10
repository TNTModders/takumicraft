package com.tntmodders.takumi.entity.mobs.noncreeper;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.entity.EntityUnknownLay;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySpellcasterIllager;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class EntityTheUnknown extends EntitySpellcasterIllager {
    private static final DataParameter<Boolean> IGNITED = EntityDataManager.createKey(EntityTheUnknown.class, DataSerializers.BOOLEAN);
    private final BossInfoServer bossInfo =
            (BossInfoServer) new BossInfoServer(new TextComponentTranslation("entity.theunknown.name"), BossInfo.Color.PURPLE,
                    BossInfo.Overlay.NOTCHED_20).setDarkenSky(true).setCreateFog(true);

    public EntityTheUnknown(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.95F);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(100);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1000);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("ignited", this.hasIgnited());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.getBoolean("ignited")) {
            this.ignite();
        }
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(IGNITED, Boolean.FALSE);
    }

    public boolean hasIgnited() {
        return this.dataManager.get(IGNITED);
    }

    public void ignite() {
        this.dataManager.set(IGNITED, Boolean.TRUE);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new AICastingSpell());
        this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityPlayer.class, 8.0F, 0.6D, 1.0D));
        this.tasks.addTask(4, new AISummonSpell());
        this.tasks.addTask(5, new AIAttackSpell());
        this.tasks.addTask(8, new EntityAIWander(this, 0.6D));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, EntityEvoker.class));
        this.targetTasks.addTask(2, (new EntityAINearestAttackableTarget(this, EntityPlayer.class, true)).setUnseenMemoryTicks(300));
        this.targetTasks.addTask(3, (new EntityAINearestAttackableTarget(this, EntityVillager.class, false)).setUnseenMemoryTicks(300));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, false));
    }

    @Override
    public IllagerArmPose getArmPose() {
        return IllagerArmPose.SPELLCASTING;
    }

    @Override
    protected SoundEvent getSpellSound() {
        return SoundEvents.EVOCATION_ILLAGER_CAST_SPELL;
    }

    @Override
    public boolean isSpellcasting() {
        return this.hasIgnited() && super.isSpellcasting();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.setInvisible(!this.hasIgnited());
        if (!this.hasIgnited()) {
            this.setAttackTarget(null);
            this.setAggressive(1, true);
            this.setSpellType(SpellType.NONE);
            if (!this.world.isRemote) {
                if (this.world.getNearestAttackablePlayer(this, 20, 20) instanceof EntityPlayerMP
                        && TakumiUtils.getAdvancementUnlockedServer(new ResourceLocation(TakumiCraftCore.MODID, "creepertower"),
                        ((EntityPlayerMP) this.world.getNearestAttackablePlayer(this, 20, 20)))) {
                    this.ignite();
                    if (FMLCommonHandler.instance().getSide().isServer()) {
                        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendMessage(new TextComponentTranslation("entity.theunknown.message",
                                this.world.getNearestAttackablePlayer(this, 20, 20).getDisplayName().getFormattedText()), true);
                    }
                }
            }
        } else {
            this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        }
        if (this.ticksExisted > 6000) {
            this.setDead();
        }
    }

    @Override
    public boolean isNonBoss() {
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
    }

    @Override
    public void setCustomNameTag(String name) {
        super.setCustomNameTag(name);
        this.bossInfo.setName(this.getDisplayName());
    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }

    @Override
    public void addTrackingPlayer(EntityPlayerMP player) {
        super.addTrackingPlayer(player);
        if (this.hasIgnited()) {
            this.bossInfo.addPlayer(player);
        }
    }

    @Override
    public void removeTrackingPlayer(EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public boolean getCanSpawnHere() {
        return this.rand.nextInt(10) == 0 && super.getCanSpawnHere();
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 1;
    }

    class AIAttackSpell extends EntitySpellcasterIllager.AIUseSpell {
        private AIAttackSpell() {
            super();
        }

        @Override
        protected int getCastingTime() {
            return 40;
        }

        @Override
        protected int getCastingInterval() {
            return 100;
        }

        @Override
        protected void castSpell() {
            EntityLivingBase entitylivingbase = EntityTheUnknown.this.getAttackTarget();
            double d0 = Math.min(entitylivingbase.posY, EntityTheUnknown.this.posY);
            double d1 = Math.max(entitylivingbase.posY, EntityTheUnknown.this.posY) + 1.0D;
            float f = (float) MathHelper.atan2(entitylivingbase.posZ - EntityTheUnknown.this.posZ, entitylivingbase.posX - EntityTheUnknown.this.posX);

            if (EntityTheUnknown.this.getDistanceSqToEntity(entitylivingbase) < 9.0D) {
                for (int i = 0; i < 5; ++i) {
                    float f1 = f + (float) i * (float) Math.PI * 0.4F;
                    this.spawnFangs(EntityTheUnknown.this.posX + (double) MathHelper.cos(f1) * 1.5D, EntityTheUnknown.this.posZ + (double) MathHelper.sin(f1) * 1.5D, d0, d1, f1, 0);
                }

                for (int k = 0; k < 8; ++k) {
                    float f2 = f + (float) k * (float) Math.PI * 2.0F / 8.0F + ((float) Math.PI * 2F / 5F);
                    this.spawnFangs(EntityTheUnknown.this.posX + (double) MathHelper.cos(f2) * 2.5D, EntityTheUnknown.this.posZ + (double) MathHelper.sin(f2) * 2.5D, d0, d1, f2, 3);
                }
            } else {
                for (int l = 0; l < 16; ++l) {
                    double d2 = 1.25D * (double) (l + 1);
                    int j = 1 * l;
                    this.spawnFangs(EntityTheUnknown.this.posX + (double) MathHelper.cos(f) * d2, EntityTheUnknown.this.posZ + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
                }
            }
        }

        private void spawnFangs(double x, double z, double p_190876_5_, double y, float p_190876_9_, int p_190876_10_) {
            BlockPos blockpos = new BlockPos(x, y, z);
            boolean flag = false;
            double d0 = 0.0D;

            while (true) {
                if (!EntityTheUnknown.this.world.isBlockNormalCube(blockpos, true) && EntityTheUnknown.this.world.isBlockNormalCube(blockpos.down(), true)) {
                    if (!EntityTheUnknown.this.world.isAirBlock(blockpos)) {
                        IBlockState iblockstate = EntityTheUnknown.this.world.getBlockState(blockpos);
                        AxisAlignedBB axisalignedbb = iblockstate.getCollisionBoundingBox(EntityTheUnknown.this.world, blockpos);

                        if (axisalignedbb != null) {
                            d0 = axisalignedbb.maxY;
                        }
                    }

                    flag = true;
                    break;
                }

                blockpos = blockpos.down();

                if (blockpos.getY() < MathHelper.floor(p_190876_5_) - 1) {
                    break;
                }
            }

            if (flag) {
                EntityUnknownLay lay = new EntityUnknownLay(world);
                lay.setPosition(x, y/*blockpos.getY() + d0+1*/, z);
                EntityTheUnknown.this.world.spawnEntity(lay);
            }
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOCATION_ILLAGER_PREPARE_ATTACK;
        }

        @Override
        protected EntitySpellcasterIllager.SpellType getSpellType() {
            return EntitySpellcasterIllager.SpellType.FANGS;
        }
    }

    class AICastingSpell extends EntitySpellcasterIllager.AICastingApell {
        private AICastingSpell() {
            super();
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        @Override
        public void updateTask() {
            if (EntityTheUnknown.this.getAttackTarget() != null) {
                EntityTheUnknown.this.getLookHelper().setLookPositionWithEntity(EntityTheUnknown.this.getAttackTarget(), (float) EntityTheUnknown.this.getHorizontalFaceSpeed(), (float) EntityTheUnknown.this.getVerticalFaceSpeed());
            }
        }
    }

    class AISummonSpell extends EntitySpellcasterIllager.AIUseSpell {
        private AISummonSpell() {
            super();
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        @Override
        public boolean shouldExecute() {
            if (!super.shouldExecute()) {
                return false;
            } else {
                int i = EntityTheUnknown.this.world.getEntitiesWithinAABB(EntityVex.class, EntityTheUnknown.this.getEntityBoundingBox().grow(16.0D)).size();
                return EntityTheUnknown.this.rand.nextInt(8) + 1 > i;
            }
        }

        @Override
        protected int getCastingTime() {
            return 100;
        }

        @Override
        protected int getCastingInterval() {
            return 340;
        }

        @Override
        protected void castSpell() {
            EntityUnknownLay lay = new EntityUnknownLay.EntityUnknownLayEx(world);
            lay.setPosition(posX, posY, posZ);
            world.spawnEntity(lay);
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOCATION_ILLAGER_PREPARE_SUMMON;
        }

        @Override
        protected EntitySpellcasterIllager.SpellType getSpellType() {
            return EntitySpellcasterIllager.SpellType.SUMMON_VEX;
        }
    }
}
