package com.tntmodders.takumi.entity.mobs.boss;

import com.tntmodders.asm.TakumiASMNameMap;
import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.client.render.RenderTransCreeper_2;
import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.core.TakumiPotionCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.ai.EntityAIBossCreeperSwell;
import com.tntmodders.takumi.entity.item.EntityTransHomingBomb;
import com.tntmodders.takumi.entity.mobs.EntityDashCreeper;
import com.tntmodders.takumi.entity.mobs.EntityRushCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
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
import java.util.ArrayList;
import java.util.Random;

public class EntityTransCreeper_2 extends EntityTakumiAbstractCreeper {

    private final BossInfoServer bossInfo =
            (BossInfoServer) new BossInfoServer(new TextComponentTranslation("entity.transcreeper_2.name"), Color.GREEN,
                    Overlay.PROGRESS).setDarkenSky(true).setCreateFog(true);

    public EntityTransCreeper_2(World worldIn) {
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
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.5D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(400);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(100);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1000);
    }

    @Override
    protected void outOfWorld() {
        this.setHealth(0);
        super.outOfWorld();
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        if (!this.world.isRemote) {
            event.getAffectedEntities().forEach(entity -> {
                if (entity instanceof EntityPlayer) {
                    ((EntityPlayer) entity).addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 60));
                    ((EntityPlayer) entity).addPotionEffect(new PotionEffect(TakumiPotionCore.INVERSION, 60));
                }
            });
        }
        if (this.getActivePotionEffect(MobEffects.SLOWNESS) != null) {
            ArrayList<BlockPos> posArrayList = new ArrayList<>();
            event.getAffectedBlocks().forEach(blockPos -> {
                if (blockPos.getY() < this.posY - 1) {
                    posArrayList.add(blockPos);
                }
            });
            event.getAffectedBlocks().removeAll(posArrayList);
        }
        return true;
    }

    @Override
    public int getPrimaryColor() {
        return 0x001100;
    }

    @Override
    public Object getRender(RenderManager manager) {
        return new RenderTransCreeper_2<>(manager);
    }

    @Override
    public void setDead() {
        if (!(this.getHealth() <= 1 || this.world.getDifficulty() == EnumDifficulty.PEACEFUL)) {
            if (!this.world.isRemote) {
                EntityTransCreeper_2 transCreeper = new EntityTransCreeper_2(this.world);
                NBTTagCompound tagCompound = new NBTTagCompound();
                this.writeEntityToNBT(tagCompound);
                tagCompound.setBoolean("ignited", false);
                transCreeper.readEntityFromNBT(tagCompound);
                transCreeper.setHealth(this.getHealth());
                transCreeper.copyLocationAndAnglesFrom(this);
                if (this.getPowered()) {
                    TakumiUtils.takumiSetPowered(transCreeper, true);
                }
                transCreeper.setCreeperState(-1);
                transCreeper.setAttackTarget(null);
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

        if (this.world.getNearestAttackablePlayer(this, 64, 32) != null) {
            this.setAttackTarget(this.world.getNearestAttackablePlayer(this, 64, 32));
        }

        if (this.getActivePotionEffect(MobEffects.SLOWNESS) != null) {
            //TakumiCraftCore.LOGGER.info(this.getActivePotionEffects());
            this.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 10, 255));
            this.moveHelper.setMoveTo(this.posX, this.posY, this.posZ, 0d);
            this.moveHelper.action = EntityMoveHelper.Action.WAIT;
            if (this.getActivePotionEffect(MobEffects.REGENERATION) != null) {
                if (!this.world.isRemote) {
                    for (int x = -10; x <= 10; x++) {
                        for (int y = -10; y <= 10; y++) {
                            for (int z = -10; z <= 10; z++) {
                                if (x * x + y * y + z * z > 98 && x * x + y * y + z * z < 102 &&
                                        this.rand.nextInt(20) == 0) {
                                    this.world.createExplosion(this, this.posX + x, this.posY + y, this.posZ + z,
                                            this.getPowered() ? 4 : 2f, true);
                                }
                            }
                        }
                    }
                }
            }
            if (this.getActivePotionEffect(MobEffects.BLINDNESS) != null) {
                if (!this.world.isRemote) {
                    this.world.playerEntities.forEach(entityPlayer -> {
                        if (this.getDistanceSqToEntity(entityPlayer) < 256 && !this.world.isRemote && !entityPlayer.isSpectator()) {
                            entityPlayer.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 100, 0));
                            entityPlayer.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 100, 0));
                        }
                    });
                }
            }
            if (this.getActivePotionEffect(MobEffects.RESISTANCE) != null) {
                this.world.playerEntities.forEach(entityPlayer -> {
                    if (this.getDistanceSqToEntity(entityPlayer) < 9 && !entityPlayer.isSpectator()) {
                        if (!this.world.isRemote) {
                            entityPlayer.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 100, 100));
                            if (this.ticksExisted % 20 == 0) {
                                entityPlayer.addPotionEffect(new PotionEffect(MobEffects.INSTANT_DAMAGE, 0, 0));
                            }
                            entityPlayer.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 100, -5));
                        }
                        entityPlayer.getEquipmentAndArmor().forEach(itemStack -> {
                            if (itemStack.getItem().isDamageable() && this.rand.nextInt(10) == 0) {
                                itemStack.damageItem(1, this);
                            }
                        });
                    }
                });
            }
        } else {
            /*if (this.getCreeperState() > 0)*/
            {
                if (this.getAttackTarget() != null) {
                    this.getLookHelper().setLookPositionWithEntity(this.getAttackTarget(), 1f, 1f);
                    int i = 5;
                    this.getNavigator().tryMoveToEntityLiving(this.getAttackTarget(), 1.5);
                    this.moveHelper.setMoveTo(this.posX + this.getLookVec().x * i, this.posY + this.getLookVec().y * i,
                            this.posZ + this.getLookVec().z * i, 1.5);
                    if (!this.world.isRemote && this.rand.nextInt(5) == 0) {
                        this.world.createExplosion(this, this.posX, this.posY, this.posZ, (this.getPowered() ? 5f : 3f),
                                this.getDistanceSqToEntity(this.getAttackTarget()) < 49);
                    }
                    if (this.getDistanceSqToEntity(this.getAttackTarget()) < 25 && this.ticksExisted % 10 == 0 &&
                            this.rand.nextBoolean()) {
                        if (!this.world.isRemote) {
                            this.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 10, 255));
                            //TakumiCraftCore.LOGGER.info(this.getActivePotionEffects());
                        }
                    }
                }
            }
        }
    }

    @Override
    protected boolean isMovementBlocked() {
        return super.isMovementBlocked() && this.getActivePotionEffect(MobEffects.SLOWNESS) != null &&
                this.getActivePotionEffect(MobEffects.SLOWNESS).getAmplifier() == 255;
    }

    @Override
    protected float getWaterSlowDown() {
        return 1f;
    }

    @Override
    public void onLivingUpdate() {
        if (this.world.isRemote) {
            for (int i = 0; i < 20; ++i) {
                this.world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE,
                        this.posX + (this.rand.nextDouble() - 0.5D) * this.width,
                        this.posY + this.rand.nextDouble() * this.height,
                        this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D);
            }
        }
        super.onLivingUpdate();
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
        if (!this.world.isRemote && this.getActivePotionEffect(MobEffects.SLOWNESS) != null) {
            switch (new Random(System.currentTimeMillis()).nextInt(7)) {
                case 0: {
                    if (!this.world.isRemote) {
                        for (int i = 0; i < 10; i++) {
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
                    break;
                }
                case 1: {
                    if (!this.world.isRemote) {
                        for (int i = 0; i < 12; i++) {
                            EnumFacing facing = EnumFacing.getHorizontal(i % 4);
                            EntityTransHomingBomb bomb =
                                    new EntityTransHomingBomb(this.world, this, this.getAttackTarget(),
                                            facing.getAxis());
                            this.world.spawnEntity(bomb);
                        }
                    }
                    break;
                }
                case 2: {
                    if (!this.world.isRemote) {
                        for (int i = 0; i < 14; i++) {
                            this.world.createExplosion(this, this.posX + this.getLookVec().x * i,
                                    this.posY + this.getLookVec().y * i, this.posZ + this.getLookVec().z * i, 2, true);
                        }
                    }
                    break;
                }
                case 3: {
                    if (!this.world.isRemote) {
                        this.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 120));
                        this.world.createExplosion(this, this.posX, this.posY, this.posZ, 1f, false);
                    }
                    break;
                }
                case 4: {
                    if (!this.world.isRemote) {
                        for (int i = 0; i > -10; i--) {
                            this.world.createExplosion(this, this.posX, this.posY + i, this.posZ, 4f, true);
                            if (this.rand.nextBoolean() && this.getAttackTarget() != null) {
                                EntityTransHomingBomb bomb =
                                        new EntityTransHomingBomb(this.world, this, this.getAttackTarget(),
                                                EnumFacing.DOWN.getAxis());
                                this.world.spawnEntity(bomb);
                            }
                        }
                    }
                    break;
                }
                case 5: {
                    if (!this.world.isRemote) {
                        for (double x = -7; x <= 7; x += 0.5) {
                            for (double z = -7; z <= 7; z += 0.5) {
                                EntityTransHomingBomb bomb =
                                        new EntityTransHomingBomb(this.world, this, this.getAttackTarget(),
                                                EnumFacing.DOWN.getAxis());
                                bomb.setPosition(this.posX + x, this.posY + 20, this.posZ + z);
                                this.world.spawnEntity(bomb);
                            }
                        }
                        this.world.createExplosion(this, this.posX, this.posY - 1, this.posZ, 5f, true);
                    }
                    break;
                }
                case 6: {
                    if (!this.world.isRemote) {
                        for (int i = 0; i < 6; i++) {
                            EntityCreeper creeper = this.rand.nextBoolean() ? new EntityRushCreeper(this.world) :
                                    new EntityDashCreeper(this.world);
                            creeper.copyLocationAndAnglesFrom(this);
                            TakumiUtils.takumiSetPowered(creeper, true);
                            this.world.spawnEntity(creeper);
                        }
                    }
                }
            }

            int rnd = new Random(System.currentTimeMillis()).nextInt(8);
            if (!this.world.isRemote) {
                if (rnd == 4) {
                    //fung
                } else if (rnd == 5) {
                    this.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 20 * 15, 0));
                } else if (rnd == 6) {
                    this.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 20 * 15, 0));
                } else if (rnd == 7) {
                    this.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 20 * 15, 0));
                } else if (this.rand.nextInt(3) == 0) {
                    this.removePotionEffect(MobEffects.SLOWNESS);
                }
            }
        }
    }

    @Override
    public double getSizeAmp() {
        return 1.5;
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
        return "transcreeper_2";
    }

    @Override
    public int getRegisterID() {
        return 504;
    }

    @Override
    public ResourceLocation getArmor() {
        return new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/big_creeper_armor.png");
    }

    @Override
    public void onDeath(DamageSource source) {
        if (!this.world.isRemote) {
            this.dropItem(TakumiItemCore.CREEPER_CORE, this.rand.nextInt(3) + 1);
        }
        super.onDeath(source);
    }
}
