package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.lang.reflect.Field;
import java.util.Random;

public class EntityKingCreeper extends EntityTakumiAbstractCreeper {
    private static final DataParameter<Integer> ATTACK_ID = EntityDataManager.createKey(EntityKingCreeper.class, DataSerializers.VARINT);
    private final BossInfoServer bossInfo =
            (BossInfoServer) new BossInfoServer(new TextComponentTranslation("entity.kingcreeper.name"),
                    BossInfo.Color.GREEN, BossInfo.Overlay.PROGRESS).setDarkenSky(true).setCreateFog(true);
    private DamageSource lastSource;

    public EntityKingCreeper(World worldIn) {
        super(worldIn);
        this.isImmuneToFire = true;
        try {
            Field field = EntityCreeper.class.getDeclaredField("fuseTime");
            field.setAccessible(true);
            field.set(this, 90);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(500);
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
        compound.setInteger("attackid", this.getAttackID());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setAttackID(compound.getInteger("attackid"));
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public void takumiExplode() {
        if (this.lastSource != null && this.lastSource.isProjectile() && this.lastSource.getTrueSource() != null) {
            this.projectileCounter();
        }
        int maxID = 3;
        float power = this.getPowered() ? 10 : 6;
        if (!this.world.isRemote) {
            this.setAttackID(this.rand.nextInt(maxID + 1));
        }
        TakumiCraftCore.LOGGER.info(this.getAttackID());
        switch (this.getAttackID()) {
            case 1: {
                if (!this.world.isRemote) {
                    for (int i = 0; i < (this.getPowered() ? 20 : 10); i++) {
                        BlockPos pos = this.createRandomPos(this.getPosition(), 2.5);
                        this.world.createExplosion(this, pos.getX() + 0.5, pos.getY() - 0.5, pos.getZ() + 0.5,
                                power / 2, true);
                    }
                }
                break;
            }
            case 2: {
                this.motionY = 75d;
                if (this.isPotionActive(MobEffects.JUMP_BOOST)) {
                    this.motionY += (this.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F;
                }

                if (this.isSprinting()) {
                    float f = this.rotationYaw * 0.017453292F;
                    this.motionX -= MathHelper.sin(f) * 0.2F;
                    this.motionZ += MathHelper.cos(f) * 0.2F;
                }
                this.move(MoverType.SELF, motionX, motionY, motionZ);
                this.isAirBorne = true;
                ForgeHooks.onLivingJump(this);
                for (int t = 0; t < (this.getPowered() ? 20 : 10); t++) {
                    Random rand = new Random();
                    int i = this.getPowered() ? 10 : 5;
                    double x = this.posX + this.rand.nextInt(i * 2) - i;
                    double y = this.posY + this.rand.nextInt(i) - i / 2;
                    double z = this.posZ + this.rand.nextInt(i * 2) - i;

                    EntityLargeFireball fireball = new EntityLargeFireball(this.world, x, y, z, 0, -0.5, 0);
                    fireball.motionX = 0;
                    fireball.motionY = -1;
                    fireball.motionZ = 0;
                    fireball.explosionPower = this.getPowered() ? 5 : 3;
                    if (!this.world.isRemote) {
                        this.world.spawnEntity(fireball);
                    }
                }
                break;
            }
            case 3: {
                for (int i = 0; i < 10 * (this.getPowered() ? 3 : 1); i++) {
                    BlockPos pos = this.createRandomPos(this.getPosition(), 1.5);
                    EntityLightningBolt bolt = new EntityLightningBolt(this.world,
                            pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, false);
                    this.world.addWeatherEffect(bolt);
                    this.world.spawnEntity(bolt);
                    if (!this.world.isRemote) {
                        this.world.newExplosion(this, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
                                power / 2.5f, true, true);
                    }
                }
                break;
            }
            default: {
                if (!this.world.isRemote) {
                    this.world.createExplosion(this, this.posX, this.posY, this.posZ, power, true);
                }
                break;
            }
        }
    }

    private void projectileCounter() {
        for (int i = 0; i < 10 * (this.getPowered() ? 3 : 1); i++) {
            BlockPos pos = this.createRandomPos(this.lastSource.getTrueSource().getPosition(), 2);
            EntityLightningBolt bolt = new EntityLightningBolt(this.world,
                    pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, false);
            this.world.addWeatherEffect(bolt);
            this.world.spawnEntity(bolt);
        }
        if (!this.world.isRemote) {
            this.world.createExplosion(this, this.lastSource.getTrueSource().posX, this.lastSource.getTrueSource().posY - 0.25
                    , this.lastSource.getTrueSource().posZ, 3f, true);
        }
        this.onStruckByLightning(null);
    }

    public int getAttackID() {
        return this.dataManager.get(ATTACK_ID);
    }

    private BlockPos createRandomPos(BlockPos point, double range) {
        return point.add(
                MathHelper.nextDouble(this.rand, -1 * range * (this.getPowered() ? 2 : 1), range * (this.getPowered() ? 2 : 1)),
                MathHelper.nextDouble(this.rand, -1 * range * (this.getPowered() ? 2 : 1), range * (this.getPowered() ? 2 : 1)),
                MathHelper.nextDouble(this.rand, -1 * range * (this.getPowered() ? 2 : 1), range * (this.getPowered() ? 2 : 1)));
    }

    public void setAttackID(int id) {
        this.dataManager.set(ATTACK_ID, id);
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
    public int getSecondaryColor() {
        return 0xff8800;
    }

    @Override
    public boolean isCustomSpawn() {
        return true;
    }

    @Override
    public String getRegisterName() {
        return "kingcreeper";
    }

    @Override
    public int getRegisterID() {
        return 501;
    }

    @Override
    public int getPrimaryColor() {
        return 0x00ff00;
    }

    @Override
    public ResourceLocation getArmor() {
        return new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/king_creeper_armor.png");
    }

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        if (damageSrc == DamageSource.OUT_OF_WORLD || damageSrc.getTrueSource() instanceof EntityPlayer) {
            this.lastSource = damageSrc;
            if (damageSrc.isProjectile() && damageAmount > 2.5f) {
                damageAmount = 2.5f;
            }
            super.damageEntity(damageSrc, damageAmount);
        }
    }

    @Override
    protected void outOfWorld() {
        this.setHealth(0);
        super.outOfWorld();
    }

    @Override
    public void setCustomNameTag(String name) {
        super.setCustomNameTag(name);
        this.bossInfo.setName(this.getDisplayName());
    }


    @Override
    public void removeTrackingPlayer(EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public void addTrackingPlayer(EntityPlayerMP player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void setDead() {
        if (!(this.getHealth() <= 0 || this.world.getDifficulty() == EnumDifficulty.PEACEFUL)) {
            if (!this.world.isRemote) {
                EntityKingCreeper kingCreeper = new EntityKingCreeper(this.world);
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
                kingCreeper.setAttackID(this.getAttackID());
                this.world.spawnEntity(kingCreeper);
            }
        }
        super.setDead();
    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    protected void despawnEntity() {
        Event.Result result;
        if ((this.idleTime & 0x1F) == 0x1F && (result = ForgeEventFactory.canEntityDespawn(this)) != Event.Result.DEFAULT) {
            if (result == Event.Result.DENY) {
                this.idleTime = 0;
            } else {
                this.setHealth(0);
                this.setDead();
            }
        } else {
            Entity entity = this.world.getClosestPlayerToEntity(this, -1.0D);

            if (entity != null) {
                double d0 = entity.posX - this.posX;
                double d1 = entity.posY - this.posY;
                double d2 = entity.posZ - this.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (this.canDespawn() && d3 > 16384.0D) {
                    this.setHealth(0);
                    this.setDead();
                }

                if (this.idleTime > 600 && this.rand.nextInt(800) == 0 && d3 > 1024.0D && this.canDespawn()) {
                    this.setHealth(0);
                    this.setDead();
                } else if (d3 < 1024.0D) {
                    this.idleTime = 0;
                }
            }
        }
    }

    @Override
    public boolean isNonBoss() {
        return false;
    }
}
