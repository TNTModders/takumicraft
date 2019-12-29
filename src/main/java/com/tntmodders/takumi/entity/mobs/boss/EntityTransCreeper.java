package com.tntmodders.takumi.entity.mobs.boss;

import com.tntmodders.asm.TakumiASMNameMap;
import com.tntmodders.takumi.client.render.RenderTransCreeper;
import com.tntmodders.takumi.core.TakumiPotionCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.ai.EntityAIBossCreeperSwell;
import com.tntmodders.takumi.entity.item.EntityTransHomingBomb;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.BossInfo.Overlay;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

import java.lang.reflect.Field;

public class EntityTransCreeper extends EntityTakumiAbstractCreeper {

    private final BossInfoServer bossInfo =
            (BossInfoServer) new BossInfoServer(new TextComponentTranslation("entity.transcreeper.name"), Color.GREEN,
                    Overlay.PROGRESS).setDarkenSky(true).setCreateFog(true);
    private EnumAttackFlg flg;

    public EntityTransCreeper(World worldIn) {
        super(worldIn);
        this.isImmuneToFire = true;
        this.tasks.addTask(1, new EntityAIBossCreeperSwell(this));
        try {
            Field field = TakumiASMNameMap.getField(EntityCreeper.class, "fuseTime");
            field.setAccessible(true);
            field.set(this, 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(100);
    }

    @Override
    protected void outOfWorld() {
        this.setHealth(0);
        super.outOfWorld();
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        event.getAffectedEntities().forEach(entity -> {
            if (entity instanceof EntityPlayer) {
                ((EntityPlayer) entity).addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 60));
                ((EntityPlayer) entity).addPotionEffect(new PotionEffect(TakumiPotionCore.INVERSION, 60));
            }
        });
        return true;
    }

    @Override
    public int getPrimaryColor() {
        return 0x001100;
    }

    @Override
    public Object getRender(RenderManager manager) {
        return new RenderTransCreeper<>(manager);
    }

    @Override
    public void setDead() {
        if (!(this.getHealth() <= 0 || this.world.getDifficulty() == EnumDifficulty.PEACEFUL)) {
            if (!this.world.isRemote) {
                EntityTransCreeper transCreeper = new EntityTransCreeper(this.world);
                NBTTagCompound tagCompound = new NBTTagCompound();
                this.writeEntityToNBT(tagCompound);
                tagCompound.setBoolean("ignited", false);
                transCreeper.readEntityFromNBT(tagCompound);
                transCreeper.setHealth(this.getHealth());
                transCreeper.copyLocationAndAnglesFrom(this);
                if (this.getPowered()) {
                    TakumiUtils.takumiSetPowered(transCreeper, true);
                }
                transCreeper.flg = this.flg;
                transCreeper.setCreeperState(-1);
                transCreeper.setAttackTarget(null);
                this.world.spawnEntity(transCreeper);
            }
        } else if (this.world.getDifficulty() != EnumDifficulty.PEACEFUL) {
            if (!this.world.isRemote) {
                EntityTransCreeper_2 transCreeper = new EntityTransCreeper_2(this.world);
                NBTTagCompound tagCompound = new NBTTagCompound();
                this.writeEntityToNBT(tagCompound);
                tagCompound.setBoolean("ignited", false);
                transCreeper.readEntityFromNBT(tagCompound);
                transCreeper.setHealth(transCreeper.getMaxHealth());
                transCreeper.copyLocationAndAnglesFrom(this);
                if (this.getPowered()) {
                    TakumiUtils.takumiSetPowered(transCreeper, true);
                }
                this.world.spawnEntity(transCreeper);
            }
        }
        super.setDead();
    }

    @Override
    public void damageEntity(DamageSource damageSrc, float damageAmount) {
        if (damageSrc == DamageSource.OUT_OF_WORLD || damageSrc.getTrueSource() instanceof EntityPlayer) {
            if (!damageSrc.isExplosion() && !damageSrc.isFireDamage() && damageSrc != DamageSource.DROWN &&
                    damageSrc != DamageSource.IN_WALL && damageSrc != DamageSource.MAGIC) {
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
        super.onUpdate();
        if (this.motionY < 0) {
            this.motionY = this.motionY / (2.5 + Math.sin(this.ticksExisted / 5));
        }
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        this.world.playerEntities.forEach(player -> {
            if (this.getDistanceSqToEntity(player) < 16) {
                player.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 60));
                player.addPotionEffect(new PotionEffect(TakumiPotionCore.INVERSION, 60));
            }
        });
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
    public void takumiExplode() {
        if (this.flg == null) {
            this.flg = EnumAttackFlg.EXPLODE;
        }
        switch (this.flg) {
            case EXPLODE: {
                if (!this.world.isRemote) {
                    for (int i = 0; i < 7; i++) {
                        this.world.createExplosion(this, this.posX - 5, this.posY + i, this.posZ - 5,
                                this.getPowered() ? 5f : 2.5f, true);
                        this.world.createExplosion(this, this.posX - 5, this.posY + i, this.posZ + 5,
                                this.getPowered() ? 5f : 2.5f, true);
                        this.world.createExplosion(this, this.posX + 5, this.posY + i, this.posZ - 5,
                                this.getPowered() ? 5f : 2.5f, true);
                        this.world.createExplosion(this, this.posX + 5, this.posY + i, this.posZ + 5,
                                this.getPowered() ? 5f : 2.5f, true);
                    }
                }
                this.flg = EnumAttackFlg.HOMING;
                break;
            }
            case HOMING: {
                if (!this.world.isRemote) {
                    for (int i = 0; i < 8; i++) {
                        EnumFacing facing = EnumFacing.getHorizontal(i % 4);
                        EntityTransHomingBomb bomb =
                                new EntityTransHomingBomb(this.world, this, this.getAttackTarget(), facing.getAxis());
                        this.world.spawnEntity(bomb);
                    }
                }
                this.flg = EnumAttackFlg.LASER;
                break;
            }
            case LASER: {
                if (!this.world.isRemote) {
                    for (int i = 0; i < 10; i++) {
                        this.world.createExplosion(this, this.posX + this.getLookVec().x * i,
                                this.posY + this.getLookVec().y * i, this.posZ + this.getLookVec().z * i, 2, true);
                    }
                }
                this.flg = EnumAttackFlg.FLOAT;
                break;
            }
            case FLOAT: {
                this.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 1200));
                this.addPotionEffect(new PotionEffect(MobEffects.SPEED, 1200, 5));
                if (!this.world.isRemote) {
                    this.world.createExplosion(this, this.posX, this.posY, this.posZ, 1f, false);
                }
                this.flg = EnumAttackFlg.DOWN;
                break;
            }
            case DOWN: {
                if (!this.world.isRemote) {
                    for (int i = 0; i > -10; i--) {
                        this.world.createExplosion(this, this.posX, this.posY + i, this.posZ, 2f, true);
                        if (this.rand.nextBoolean() && this.getAttackTarget() != null) {
                            EntityTransHomingBomb bomb =
                                    new EntityTransHomingBomb(this.world, this, this.getAttackTarget(),
                                            EnumFacing.DOWN.getAxis());
                            this.world.spawnEntity(bomb);
                        }
                    }
                }
                this.clearActivePotions();
                this.flg = EnumAttackFlg.EXPLODE;
                break;
            }
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.BOSS;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL_M;
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
        return false;
    }

    @Override
    public String getRegisterName() {
        return "transcreeper";
    }

    @Override
    public int getRegisterID() {
        return 502;
    }

    private enum EnumAttackFlg {
        EXPLODE, HOMING, LASER, FLOAT, DOWN
    }
}
