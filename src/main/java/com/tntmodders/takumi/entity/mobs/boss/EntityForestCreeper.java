package com.tntmodders.takumi.entity.mobs.boss;

import com.tntmodders.takumi.client.render.RenderForestCreeper;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import com.tntmodders.takumi.world.gen.TakumiWorldGenBigTree;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.event.world.ExplosionEvent;

public class EntityForestCreeper extends EntityTakumiAbstractCreeper {

    public EntityForestCreeper(World worldIn) {
        super(worldIn);
        this.setSize(4, 5.25f);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100);
    }

    private final BossInfoServer bossInfo =
            (BossInfoServer) new BossInfoServer(new TextComponentTranslation("entity.forestcreeper.name"),
                    BossInfo.Color.GREEN, BossInfo.Overlay.PROGRESS);

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
            this.entityDropItem(new ItemStack(TakumiItemCore.TAKUMI_TYPE_CORE, 1 + this.rand.nextInt(2), 1), 0);
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
                EntityForestCreeper kingCreeper = new EntityForestCreeper(this.world);
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

    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        event.getAffectedEntities().clear();
        WorldGenAbstractTree tree = new TakumiWorldGenBigTree(true, TakumiBlockCore.CREEPER_LOG.getDefaultState(),
                TakumiBlockCore.CREEPER_LEAVES.getDefaultState());
        this.world.getEntities(EntityLivingBase.class, input -> input.getDistanceSqToEntity(EntityForestCreeper.this) <
                (EntityForestCreeper.this.getPowered() ? 2500 : 1000)).forEach(entity -> {
            if (!(entity instanceof EntityForestCreeper)) {
                tree.generate(world, this.rand, entity.getPosition());
            }
        });
        event.getAffectedBlocks().clear();
        return true;
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.GRASS_MD;
    }

    @Override
    public int getExplosionPower() {
        return 6;
    }

    @Override
    public int getSecondaryColor() {
        return 0x00ff00;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "forestcreeper";
    }

    @Override
    public int getRegisterID() {
        return 275;
    }

    @Override
    public Object getRender(RenderManager manager) {
        return new RenderForestCreeper<>(manager);
    }
}
