package com.tntmodders.takumi.entity.mobs.boss;

import com.tntmodders.asm.TakumiASMNameMap;
import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiConfigCore;
import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.ai.EntityAIBossCreeperSwell;
import com.tntmodders.takumi.entity.item.EntityBigHomingBomb;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.*;

import java.lang.reflect.Field;
import java.util.Random;

public class EntityAngelCreeper extends EntityTakumiAbstractCreeper {
    private static final DataParameter<Integer> ATTACK_ID =
            EntityDataManager.createKey(EntityAngelCreeper.class, DataSerializers.VARINT);
    private final BossInfoServer bossInfo =
            (BossInfoServer) new BossInfoServer(new TextComponentTranslation("entity.kingcreeper.name"), BossInfo.Color.GREEN,
                    BossInfo.Overlay.PROGRESS).setDarkenSky(true).setCreateFog(true);
    private DamageSource lastSource;

    public EntityAngelCreeper(World worldIn) {
        super(worldIn);
        this.tasks.addTask(1, new EntityAIBossCreeperSwell(this));
        this.isImmuneToFire = true;
        try {
            Field field = TakumiASMNameMap.getField(EntityCreeper.class, "fuseTime");
            field.setAccessible(true);
            field.set(this, 50);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public void setDead() {
        if (!(this.getHealth() <= 0 || this.world.getDifficulty() == EnumDifficulty.PEACEFUL)) {
            if (!this.world.isRemote) {
                EntityAngelCreeper kingCreeper = new EntityAngelCreeper(this.world);
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
    public void damageEntity(DamageSource damageSrc, float damageAmount) {
        if (damageSrc == DamageSource.OUT_OF_WORLD || damageSrc.getTrueSource() instanceof EntityPlayer) {
            if (!damageSrc.isExplosion() && !damageSrc.isFireDamage() && !damageSrc.isProjectile() &&
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

    public int getAttackID() {
        return this.dataManager.get(ATTACK_ID);
    }

    public void setAttackID(int id) {
        this.dataManager.set(ATTACK_ID, id);
    }

    public void setRandomAttackID() {
        //@TODO: chage maxID if the var of attacks added. & change debugID if you commit it.
        int debugID = 0;
        if (debugID != 0) {
            this.dataManager.set(ATTACK_ID, debugID);
        } else {
            int maxID = 5;
            this.dataManager.set(ATTACK_ID, new Random(System.currentTimeMillis()).nextInt(maxID + 1));
        }
    }


    @Override
    public void onUpdate() {
        super.onUpdate();
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        if (this.getHealth() < this.getMaxHealth() / 2) {
            if (!this.getPowered()) {
                this.onStruckByLightning(null);
            }
            this.heal(0.025f);
        }
        if (this.world.isRemote) {
            switch (this.getAttackID()) {
                //randomFire / N.fire.circle
                case 1: {
                    for (double x = -5; x <= 5; x += 0.25) {
                        for (double z = -5; z <= 5; z += 0.25) {
                            if (x * x + z * z <= 25 && x * x + z * z >= 4.5 * 4.5 && this.rand.nextBoolean()) {
                                this.spawnParticle(EnumParticleTypes.FLAME, x, 0, z,
                                        Math.sin(Math.atan2(z, x) * this.ticksExisted / 10) * 0.2, 0.4,
                                        Math.cos(Math.atan2(z, x) * this.ticksExisted / 10) * 0.2);
                            }
                        }
                    }
                    break;
                }

                //QFire / N.fire.Q
                case 2: {
                    for (double x = -5; x <= 5; x += 0.5) {
                        for (double z = -5; z <= 5; z += 0.5) {
                            for (double y = -5; y <= 5; y += 0.5) {
                                if (x * x + z * z + y * y <= 25 && x * x + z * z + y * y >= 4.5 * 4.5) {
                                    this.spawnParticle(EnumParticleTypes.FLAME, x, y, z,
                                            Math.sin(Math.atan2(z, x) * this.ticksExisted / 10) * 0.01, 0,
                                            Math.cos(Math.atan2(z, x) * this.ticksExisted / 10) * 0.01);
                                }
                            }
                        }
                    }
                    break;
                }

                //Thunder /  N.end.zone
                case 3: {
                    for (double x = -5; x <= 5; x += 0.5) {
                        for (double z = -5; z <= 5; z += 0.5) {
                            if (x * x + z * z <= 25 && this.rand.nextInt(20) == 0) {
                                this.spawnParticle(EnumParticleTypes.END_ROD, x, 0, z,
                                        (this.rand.nextDouble() - 0.5) * 0.3, 0.5,
                                        (this.rand.nextDouble() - 0.5) * 0.3);
                            }
                        }
                    }
                    break;
                }
                //ExpBall / N.smoke_large.zone
                case 4: {
                    for (double x = -5; x <= 5; x += 0.5) {
                        for (double z = -5; z <= 5; z += 0.5) {
                            if (x * x + z * z <= 25 && this.rand.nextInt(20) == 0) {
                                this.spawnParticle(EnumParticleTypes.SMOKE_LARGE, x, 0, z,
                                        (this.rand.nextDouble() - 0.5) * 0.3, 0.5,
                                        (this.rand.nextDouble() - 0.5) * 0.3);
                            }
                        }
                    }
                    break;
                }

                //FireBall / N.fire.zone
                case 5: {
                    for (double x = -5; x <= 5; x += 0.5) {
                        for (double z = -5; z <= 5; z += 0.5) {
                            if (x * x + z * z <= 25 && this.rand.nextInt(20) == 0) {
                                this.spawnParticle(EnumParticleTypes.LAVA, x, 0, z,
                                        (this.rand.nextDouble() - 0.5) * 0.3, 0.5,
                                        (this.rand.nextDouble() - 0.5) * 0.3);
                            }
                        }
                    }
                    break;
                }
            }
        }

    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            switch (this.getAttackID()) {
                //randomFire
                case 1: {
                    for (int i = 0; i < (this.getPowered() ? 15 : 10); i++) {
                        BlockPos pos = this.getRandomPos(9, 4, 9);
                        this.world.newExplosion(this, pos.getX(), pos.getY(), pos.getZ(), this.getPowered() ? 8 : 5,
                                true, true);
                    }
                    break;
                }

                //QFire
                case 2: {
                    for (double x = -5; x <= 5; x += 1) {
                        for (double z = -5; z <= 5; z += 1) {
                            for (double y = -5; y <= 5; y += 1) {
                                if (x * x + z * z + y * y <= 25 && x * x + z * z + y * y >= 4.5 * 4.5) {
                                    this.world.newExplosion(this, this.posX + x, this.posY + y, this.posZ + z,
                                            this.getPowered() ? 5f : 2.5f, true, true);
                                }
                            }
                        }
                    }
                    break;
                }

                //Thunder
                case 3: {
                    for (int i = 0; i < (this.getPowered() ? 150 : 100); i++) {

                        BlockPos pos = this.getRandomPos(2, 1, 2);
                        if (this.rand.nextInt(5) == 0) {
                            this.world.createExplosion(this, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 1.5f,
                                    true);
                        }
                        EntityLightningBolt bolt =
                                new EntityLightningBolt(this.world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
                                        false);
                        this.world.addWeatherEffect(bolt);
                        this.world.spawnEntity(bolt);
                    }
                    break;
                }

                //ExpBall
                case 4: {
                    for (int t = 0; t < 30 * (this.getPowered() ? 2 : 1); t++) {
                        Random rand = new Random();
                        BlockPos pos = this.getRandomPos(8, 2, 8).up(50);
                        EntityBigHomingBomb shulkerBullet =
                                new EntityBigHomingBomb(this.world, this, this.getAttackTarget(), EnumFacing.Axis.Y);
                        shulkerBullet.setPosition(pos.getX(), pos.getY(), pos.getZ());
                        this.world.spawnEntity(shulkerBullet);
                    }
                    break;
                }

                //FireBall
                case 5: {
                    for (int t = 0; t < 30 * (this.getPowered() ? 2 : 1); t++) {
                        Random rand = new Random();
                        BlockPos pos = this.getRandomPos(10, 2, 10).up(50);
                        EntityLargeFireball fireball = new EntityLargeFireball(this.world);
                        fireball.setPosition(pos.getX(), pos.getY(), pos.getZ());
                        fireball.motionY = -1;
                        fireball.accelerationY = -0.2;
                        fireball.explosionPower = this.getPowered() ? 5 : 3;
                        this.world.spawnEntity(fireball);
                    }
                    break;
                }

                default: {
                    this.world.createExplosion(this, this.posX, this.posY, this.posZ, this.getPowered() ? 10 : 7, true);
                }
            }
        }
        this.setRandomAttackID();
    }

    private void spawnParticle(EnumParticleTypes types, double x, double y, double z, double motionX, double motionY,
                               double motionZ) {
        this.world.spawnAlwaysVisibleParticle(types.getParticleID(), this.posX + x, this.posY + y, this.posZ + z,
                motionX, motionY, motionZ);
    }

    @Override
    public void onDeath(DamageSource source) {
        if (!this.world.isRemote) {
            this.dropItem(TakumiItemCore.KING_CORE, this.rand.nextInt(3) + 1);
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

    private BlockPos getRandomPos(int xRange, int yRange, int zRange) {
        return this.getPosition().add(this.rand.nextInt(xRange * 2 + 1) - xRange,
                this.rand.nextInt(yRange * 2 + 1) - yRange, this.rand.nextInt(zRange * 2 + 1) - zRange);
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
        return 0x0000ff;
    }

    @Override
    public boolean isCustomSpawn() {
        return true;
    }

    @Override
    public String getRegisterName() {
        return "angelcreeper";
    }

    @Override
    public int getRegisterID() {
        return 506;
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
        return TakumiConfigCore.inDev;
    }
}
