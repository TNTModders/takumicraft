package com.tntmodders.takumi.entity.mobs.boss;

import com.tntmodders.takumi.core.TakumiBlockCore;
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
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

public class EntityMineCreeper extends EntityTakumiAbstractCreeper {

    private final BossInfoServer bossInfo =
            (BossInfoServer) new BossInfoServer(new TextComponentTranslation("entity.minecreeper.name"),
                    BossInfo.Color.RED, BossInfo.Overlay.PROGRESS);

    public EntityMineCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50);
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
    public void takumiExplode() {
        this.setHealth(this.getHealth() - this.getMaxHealth() / 10);
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.FIRE_MD;
    }

    @Override
    public int getExplosionPower() {
        return 6;
    }

    @Override
    public int getSecondaryColor() {
        return 0xff8800;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "minecreeper";
    }

    @Override
    public int getRegisterID() {
        return 272;
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        event.getAffectedEntities().clear();
        event.getAffectedBlocks().forEach(pos -> {
            if (pos.getY() < this.posY) {
                TakumiUtils.setBlockStateProtected(this.world, pos, TakumiBlockCore.DUMMY_GUNORE.getDefaultState());
            } else if (Blocks.FIRE.canPlaceBlockAt(this.world, pos)) {
                TakumiUtils.setBlockStateProtected(this.world, pos, Blocks.FIRE.getDefaultState());
            }
        });
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
                EntityMineCreeper kingCreeper = new EntityMineCreeper(this.world);
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
    public void onUpdate() {
        super.onUpdate();
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        if (this.ticksExisted > 10000 && !this.world.isRemote) {
            this.world.createExplosion(this,this.posX,this.posY,this.posZ,8f,true);
            this.setDead();
        }
    }

    @Override
    public void onDeath(DamageSource source) {
        if (!this.world.isRemote) {
            this.entityDropItem(new ItemStack(TakumiItemCore.TAKUMI_TYPE_CORE, 1 + this.rand.nextInt(2), 0), 0);
        }
        super.onDeath(source);
    }
}
