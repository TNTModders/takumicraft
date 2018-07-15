package com.tntmodders.takumi.entity.mobs.boss;

import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityOceanCreeper extends EntityTakumiAbstractCreeper {

    public EntityOceanCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100);
    }

    private final BossInfoServer bossInfo =
            (BossInfoServer) new BossInfoServer(new TextComponentTranslation("entity.oceancreeper.name"),
                    BossInfo.Color.BLUE, BossInfo.Overlay.PROGRESS);

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
    public void onDeath(DamageSource source) {
        if (!this.world.isRemote) {
            this.entityDropItem(new ItemStack(TakumiItemCore.TAKUMI_TYPE_CORE, 1 + this.rand.nextInt(2), 2), 0);
        }
        super.onDeath(source);
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
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
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
    protected void outOfWorld() {
        this.setHealth(0);
        super.outOfWorld();
    }

    @Override
    public boolean isNonBoss() {
        return false;
    }

    @Override
    public void setDead() {
        if (!(this.getHealth() <= 0 || this.world.getDifficulty() == EnumDifficulty.PEACEFUL)) {
            if (!this.world.isRemote) {
                EntityOceanCreeper kingCreeper = new EntityOceanCreeper(this.world);
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
    public boolean getCanSpawnHere() {
        return this.rand.nextInt(5) == 0 && super.getCanSpawnHere();
    }

    @Override
    public int getPrimaryColor() {
        return 0x889988;
    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }

    @Override
    public void takumiExplode() {
        int i = this.getPowered() ? 40 : 20;
        for (int x = -i; x <= i; x++) {
            for (int z = -i; z <= i; z++) {
                if (this.world.isAirBlock(this.getPosition().add(x, 5, z)) ||
                        this.world.getBlockState(this.getPosition().add(x, 5, z)).getBlockHardness(this.world,
                                this.getPosition().add(x, 5, z)) >= 0) {
                    this.world.setBlockState(this.getPosition().add(x, 5, z), Blocks.WATER.getDefaultState());
                }
            }
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.WATER_MD;
    }

    @Override
    public int getExplosionPower() {
        return 6;
    }

    @Override
    public int getSecondaryColor() {
        return 0x0000ff;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "oceancreeper";
    }

    @Override
    public int getRegisterID() {
        return 274;
    }

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        if (damageSrc != DamageSource.DROWN) {
            super.damageEntity(damageSrc, damageAmount);
        }
    }
}
