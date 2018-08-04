package com.tntmodders.takumi.entity.mobs.boss;

import com.google.common.collect.Lists;
import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiEntityCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.ITakumiEntity;
import com.tntmodders.takumi.entity.item.EntityBigHomingBomb;
import com.tntmodders.takumi.entity.mobs.EntityGiantCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityBigCreeper extends EntityTakumiAbstractCreeper {
    private final BossInfoServer bossInfo =
            (BossInfoServer) new BossInfoServer(new TextComponentTranslation("entity.bigcreeper.name"),
                    BossInfo.Color.GREEN, BossInfo.Overlay.PROGRESS).setDarkenSky(true).setCreateFog(true);

    private final List<ITakumiEntity> takumiEntities = new ArrayList<>();

    public EntityBigCreeper(World worldIn) {
        super(worldIn);
        takumiEntities.addAll(Lists.newArrayList(TakumiEntityCore.getEntityList().stream().filter(
                iTakumiEntity -> iTakumiEntity.takumiRank() == EnumTakumiRank.HIGH).iterator()));
        takumiEntities.removeIf(iTakumiEntity -> iTakumiEntity instanceof EntityGiantCreeper);
        this.isImmuneToFire = true;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1000);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
    }

    @Override
    public void onLivingUpdate() {
        if (this.world.isRemote) {
            for (int i = 0; i < 10; ++i) {
                this.world.spawnAlwaysVisibleParticle(EnumParticleTypes.TOWN_AURA.getParticleID(),
                        this.posX + (this.rand.nextDouble() - 0.5D) * this.width,
                        this.posY + this.rand.nextDouble() * this.height,
                        this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D);
            }
        }
        super.onLivingUpdate();
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        double d0 = this.getEntityBoundingBox().getAverageEdgeLength();

        if (Double.isNaN(d0)) {
            d0 = 1.0D;
        }

        d0 = d0 * 64.0D * 10;
        return distance < d0 * d0;
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
    public int getPrimaryColor() {
        return 0x90a090;
    }

    @Override
    public ResourceLocation getArmor() {
        return new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/big_creeper_armor.png");
    }

    @Override
    public void setDead() {
        if (!(this.getHealth() <= 0 || this.world.getDifficulty() == EnumDifficulty.PEACEFUL)) {
            if (!this.world.isRemote) {
                EntityBigCreeper bigCreeper = new EntityBigCreeper(this.world);
                NBTTagCompound tagCompound = new NBTTagCompound();
                this.writeEntityToNBT(tagCompound);
                tagCompound.setBoolean("ignited", false);
                bigCreeper.readEntityFromNBT(tagCompound);
                bigCreeper.setHealth(this.getHealth());
                bigCreeper.copyLocationAndAnglesFrom(this);
                if (this.getPowered()) {
                    TakumiUtils.takumiSetPowered(bigCreeper, true);
                }
                bigCreeper.setCreeperState(-1);
                bigCreeper.setAttackTarget(null);
                this.world.spawnEntity(bigCreeper);
            }
        }
        super.setDead();
    }

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        if (!damageSrc.isMagicDamage()) {
            super.damageEntity(damageSrc, damageAmount);
        }
    }

    @Override
    public double getSizeAmp() {
        return 100;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            int rnd = 5;
            int amp = this.getPowered() ? 3 : 1;
            switch (this.rand.nextInt(rnd)) {
                case 1: {
                    for (int i = 0; i < 5; i++) {
                        try {
                            EntityTakumiAbstractCreeper creeper = ((EntityTakumiAbstractCreeper) takumiEntities.get(
                                    this.rand.nextInt(takumiEntities.size())).getClass().getConstructor(
                                    World.class).newInstance(this.world));
                            creeper.setPosition(this.posX + this.rand.nextInt(50) - 25,
                                    this.posY + this.rand.nextInt(25), this.posZ + this.rand.nextInt(50) - 25);
                            this.world.spawnEntity(creeper);
                            this.world.spawnEntity(
                                    new EntityLightningBolt(this.world, creeper.posX, creeper.posY, creeper.posZ,
                                            false));
                        } catch (Exception ignored) {
                        }
                    }
                    break;
                }
                case 2: {
                    for (int i = 0; i < 5; i++) {
                        double x = this.posX + this.rand.nextInt(50) - 25;
                        double y = this.posY;
                        double z = this.posZ + this.rand.nextInt(50) - 25;
                        for (int t = 0; t < 25 * amp; t++) {
                            this.world.spawnEntity(new EntityLightningBolt(this.world, x + this.rand.nextInt(8) - 4,
                                    y + this.rand.nextInt(2) - 1, z + this.rand.nextInt(8) - 4, false));
                        }
                    }
                    break;
                }
                case 3: {
                    for (double y = 0; y <= 10; y += 0.1) {
                        EntityAreaEffectCloud cloud = new EntityAreaEffectCloud(this.world);
                        cloud.setColor(0);
                        cloud.addEffect(new PotionEffect(MobEffects.INSTANT_DAMAGE, 1, 1));
                        cloud.setRadius(((int) ((10 - y) / 5)));
                        cloud.setDuration(200);
                        cloud.setRadiusOnUse(0);
                        cloud.setRadiusPerTick(0.05f);
                        cloud.setOwner(this);
                        cloud.setPosition(this.posX, this.posY + y, this.posZ);
                        this.world.spawnEntity(cloud);
                    }
                    break;
                }
                case 4: {
                    for (int t = 0; t < 20 * (this.getPowered() ? 2 : 1); t++) {
                        Random rand = new Random();
                        double x = this.posX + this.rand.nextInt(20) - 10;
                        double y = this.posY + this.rand.nextInt(20) + 30;
                        double z = this.posZ + this.rand.nextInt(20) - 10;
                        EntityBigHomingBomb homingBomb =
                                new EntityBigHomingBomb(this.world, this, this.getAttackTarget(), EnumFacing.Axis.Y);
                        homingBomb.setPosition(x, y, z);
                        if (!this.world.isRemote) {
                            this.world.spawnEntity(homingBomb);

                        }
                    }
                    break;
                }
                default: {
                    this.world.createExplosion(this, this.posX, this.posY, this.posZ, 8 * amp, true);
                    break;
                }
            }
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.BOSS;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL;
    }

    @Override
    public int getExplosionPower() {
        return 0;
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
        return "bigcreeper";
    }

    @Override
    public int getRegisterID() {
        return 503;
    }
}
