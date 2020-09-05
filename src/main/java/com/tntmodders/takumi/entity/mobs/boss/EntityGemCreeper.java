package com.tntmodders.takumi.entity.mobs.boss;

import com.tntmodders.asm.TakumiASMNameMap;
import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.client.render.RenderGemCreeper;
import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.ai.EntityAIBossCreeperSwell;
import com.tntmodders.takumi.entity.item.EntityTakumiLaser;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.*;
import net.minecraftforge.event.world.ExplosionEvent;

import java.lang.reflect.Field;

public class EntityGemCreeper extends EntityTakumiAbstractCreeper {
    private static final DataParameter<Integer> ATTACK_ID =
            EntityDataManager.createKey(EntityGemCreeper.class, DataSerializers.VARINT);
    private final BossInfoServer bossInfo =
            (BossInfoServer) new BossInfoServer(new TextComponentTranslation("entity.gemcreeper.name"), BossInfo.Color.WHITE,
                    BossInfo.Overlay.PROGRESS).setDarkenSky(true).setCreateFog(true);
    public boolean isBook = false;
    private DamageSource lastSource;
    private int lastID;
    private int activeTimer;

    public EntityGemCreeper(World worldIn) {
        super(worldIn);
        this.tasks.addTask(1, new EntityAIBossCreeperSwell(this));
        this.isImmuneToFire = true;
        this.setSize(4f, 5f);
        try {
            Field field = TakumiASMNameMap.getField(EntityCreeper.class, "fuseTime");
            field.setAccessible(true);
            field.set(this, 50);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.experienceValue = 50;
    }

    public int getAttackID() {
        return this.dataManager.get(ATTACK_ID);
    }

    public void setAttackID(int id) {
        this.dataManager.set(ATTACK_ID, id);
    }

    public void setRandomAttackID() {
        int debugID = 0;
        if (debugID != 0) {
            this.dataManager.set(ATTACK_ID, debugID);
        } else {
            double healthP = 1 - this.getHealth() / this.getMaxHealth();
            if (this.rand.nextDouble() < healthP && this.rand.nextBoolean()) {
                this.dataManager.set(ATTACK_ID, 2);
            } else {
                this.dataManager.set(ATTACK_ID, this.rand.nextInt(2));
            }
        }
    }

    @Override
    public ResourceLocation getArmor() {
        return new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/big_creeper_armor.png");
    }

    @Override
    public void setDead() {
        if (!(this.getHealth() <= 0 || this.world.getDifficulty() == EnumDifficulty.PEACEFUL)) {
            if (!this.world.isRemote) {
                EntityGemCreeper kingCreeper = new EntityGemCreeper(this.world);
                NBTTagCompound tagCompound = new NBTTagCompound();
                this.writeEntityToNBT(tagCompound);
                tagCompound.setBoolean("ignited", false);
                kingCreeper.readEntityFromNBT(tagCompound);
                kingCreeper.setHealth(this.getHealth());
                kingCreeper.copyLocationAndAnglesFrom(this);
                kingCreeper.rotationYawHead = this.rotationYawHead;
                if (this.getPowered()) {
                    TakumiUtils.takumiSetPowered(kingCreeper, true);
                }
                kingCreeper.setCreeperState(-1);
                kingCreeper.setAttackTarget(null);
                kingCreeper.setRandomAttackID();
                kingCreeper.lastID = this.getAttackID();
                kingCreeper.activeTimer = 0;
                if (this.getAttackID() == 0 && !this.world.isRemote) {
                    this.addPotionEffect(new PotionEffect(MobEffects.SPEED, 100, 10));
                }
                this.world.spawnEntity(kingCreeper);
            }
        }
        super.setDead();
    }

    @Override
    public void damageEntity(DamageSource damageSrc, float damageAmount) {
        if (!this.world.isRemote && damageSrc == DamageSource.IN_WALL) {
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, this.getPowered() ? 7 : 5, true);
        }
        if (damageSrc == DamageSource.OUT_OF_WORLD || damageSrc.getTrueSource() instanceof EntityPlayer) {
            if (!damageSrc.isExplosion() && !damageSrc.isFireDamage() &&
                    damageSrc != DamageSource.DROWN && damageSrc != DamageSource.IN_WALL) {
                if (damageAmount > 12) {
                    damageAmount = 12;
                }
                if (damageSrc.getTrueSource() instanceof EntityLivingBase) {
                    this.setAttackTarget((EntityLivingBase) damageSrc.getTrueSource());
                }
                super.damageEntity(damageSrc, damageAmount);
            }
            this.ignite();
        }
    }

    @Override
    public void onUpdate() {
        if (!this.dead && !this.isDead && this.getHealth() > 0) {
            if (this.activeTimer >= 0 && this.lastID == 0 &&
                    this.getActivePotionEffect(MobEffects.SPEED) != null && this.getActivePotionEffect(MobEffects.SPEED).getAmplifier() == 10) {
                if (this.getAttackTarget() != null) {
                    this.moveHelper.setMoveTo(this.getAttackTarget().posX, this.getAttackTarget().posY, this.getAttackTarget().posZ, 1.5);
                    if (!this.world.isRemote) {
                        this.world.createExplosion(this, this.posX, this.posY, this.posZ, this.getPowered() ? 5 : 3, true);
                    }

                    this.rotationYaw += 20;
                    this.activeTimer++;
                    this.setCreeperState(-2);
                    if (this.activeTimer > 200 || (this.getDistanceSqToEntity(this.getAttackTarget()) < 9 && this.rand.nextInt(40) == 0)) {
                        this.clearActivePotions();
                        this.activeTimer = -1;
                        this.lastID = -1;
                    }
                }
            } else if (this.lastID == 1) {
                if (this.ticksExisted == 10) {
                    this.spawnLaser(-1);
                } else if (this.ticksExisted == 20) {
                    this.spawnLaser(1);
                    this.lastID = 0;
                }
            } else if (this.lastID == 2 && !this.onGround) {
                this.rotationYaw += 20;
            }
        }
        super.onUpdate();
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        if (this.getHealth() < this.getMaxHealth() / 2) {
            if (!this.getPowered()) {
                this.onStruckByLightning(null);
            }
            this.heal(0.025f);
        }

        if (this.getAttackTarget() != null) {
            this.getLookHelper().setLookPositionWithEntity(this.getAttackTarget(), 5f, 5f);
        }
    }

    @Override
    public void takumiExplode() {
        switch (this.getAttackID()) {
            //0:rush, 1:laser, 2:superjump
            case 0: {
                this.clearActivePotions();
                break;
            }
            case 1: {
                this.spawnLaser(0);
                break;
            }
            case 2: {
                this.superJump();
                this.move(MoverType.SELF, 0, this.motionY, 0);
                break;
            }
        }
    }

    private void spawnLaser(int index) {
        if (!this.world.isRemote) {
            EntityTakumiLaser laser = new EntityTakumiLaser(this.world);
            laser.setPositionAndRotation(this.posX + 5 * index * Math.cos((this.rotationYaw - 90) * Math.PI / 180), this.posY + 2.25,
                    this.posZ + 5 * index * Math.sin((this.rotationYaw - 90) * Math.PI / 180), this.rotationYaw + 90, 0);
            laser.setGlowing(true);
            laser.setThrower(this);
            laser.setHeadingFromThrower(this, 0, this.rotationYaw + 90, 0f, 5f, 0);
            this.world.spawnEntity(laser);
        }
    }

    protected void superJump() {
        this.motionY = this.getJumpUpwardsMotion() * 30;

        if (this.isPotionActive(MobEffects.JUMP_BOOST)) {
            this.motionY += (float) (this.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F;
        }

        if (this.isSprinting()) {
            float f = this.rotationYaw * 0.017453292F;
            this.motionX -= MathHelper.sin(f) * 0.2F;
            this.motionZ += MathHelper.cos(f) * 0.2F;
        }

        this.isAirBorne = true;
        net.minecraftforge.common.ForgeHooks.onLivingJump(this);
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        super.fall(distance, damageMultiplier);
        if (this.lastID == 2) {
            if (!this.world.isRemote) {
                this.world.createExplosion(this, this.posX, this.posY, this.posZ, (this.getPowered() ? 18f : 12f) + distance / 3, true);
            }
            this.lastID = 0;
        }
    }

    @Override
    public void onDeath(DamageSource source) {
        if (!this.world.isRemote) {
            this.dropItem(TakumiItemCore.CHAMP_CORE, this.rand.nextInt(3) + 1);
        }
        super.onDeath(source);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(100);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1000);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(ATTACK_ID, 0);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("attackID", this.getAttackID());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
        this.setAttackID(compound.getInteger("attackID"));
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.BOSS;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL_MD;
    }

    @Override
    public int getExplosionPower() {
        return 0;
    }

    @Override
    public int getPrimaryColor() {
        return 0x003300;
    }

    @Override
    public int getSecondaryColor() {
        return 0x88ff88;
    }

    @Override
    public boolean isCustomSpawn() {
        return true;
    }

    @Override
    public String getRegisterName() {
        return "gemcreeper";
    }

    @Override
    public int getRegisterID() {
        return 507;
    }

    @Override
    protected void outOfWorld() {
        this.setHealth(0);
        super.outOfWorld();
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    public boolean isNonBoss() {
        return false;
    }

    @Override
    public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn) {
        return blockStateIn.getBlockHardness(worldIn, pos) == -1 ? 10000000f : 0.75f;
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
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void removeTrackingPlayer(EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public Object getRender(RenderManager manager) {
        return new RenderGemCreeper<>(manager);
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        event.getAffectedBlocks().removeIf(pos -> pos.getY() < this.posY);
        return true;
    }
}