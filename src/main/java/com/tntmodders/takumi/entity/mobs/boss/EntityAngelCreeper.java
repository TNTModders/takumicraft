package com.tntmodders.takumi.entity.mobs.boss;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.client.render.RenderAngelCreeper;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.ai.EntityAIBossCreeperSwell;
import com.tntmodders.takumi.entity.item.EntityTakumiTNTPrimed;
import com.tntmodders.takumi.entity.mobs.noncreeper.EntityOddDummyGhast;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.*;
import net.minecraftforge.event.world.ExplosionEvent;

import java.util.Random;

public class EntityAngelCreeper extends EntityTakumiAbstractCreeper {
    private static final DataParameter<Integer> ATTACK_ID =
            EntityDataManager.createKey(EntityAngelCreeper.class, DataSerializers.VARINT);
    private final BossInfoServer bossInfo =
            (BossInfoServer) new BossInfoServer(new TextComponentTranslation("entity.angelcreeper.name"), BossInfo.Color.BLUE,
                    BossInfo.Overlay.PROGRESS).setDarkenSky(true).setCreateFog(true);
    private DamageSource lastSource;
    public int deathTicks;

    public EntityAngelCreeper(World worldIn) {
        super(worldIn);
        this.tasks.addTask(1, new EntityAIBossCreeperSwell(this));
        this.isImmuneToFire = true;
        this.setSize(1.8f, 5.1f);
    }

    @Override
    public double getSizeAmp() {
        return 3.0;
    }

    @Override
    public int getPrimaryColor() {
        return 0x00ff00;
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        if (this.getAttackID() == 2) {
            for (BlockPos pos : event.getAffectedBlocks()) {
                TakumiUtils.setBlockStateProtected(this.world, pos, Blocks.ICE.getDefaultState());
            }
            event.getAffectedBlocks().clear();
        } else if (this.getAttackID() == 6) {
            for (BlockPos pos : event.getAffectedBlocks()) {
                TakumiUtils.setBlockStateProtected(this.world, pos,
                        this.getPowered() ? TakumiBlockCore.TAKUMI_TNT.getDefaultState() :
                                Blocks.TNT.getDefaultState());
                if (this.rand.nextInt(5) == 0) {
                    Entity entity =
                            this.getPowered() ? new EntityTakumiTNTPrimed(this.world) : new EntityTNTPrimed(this.world);
                    entity.setPosition(pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5);
                    this.world.spawnEntity(entity);
                }
            }
            event.getAffectedBlocks().clear();
        }
        return true;
    }

    @Override
    public ResourceLocation getArmor() {
        return new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/king_creeper_armor.png");
    }

    @Override
    public void setDead() {
        if (!(this.getHealth() <= 0 || this.world.getDifficulty() == EnumDifficulty.PEACEFUL || this.deathTicks > 0)) {
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
    public void move(MoverType type, double x, double y, double z) {
        if (this.deathTicks == 0) {
            super.move(type, x, y, z);
        }
    }

    @Override
    protected void onDeathUpdate() {
        ++this.deathTicks;
        TakumiUtils.takumiSetPowered(this, false);
        this.setAttackID(0);
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        if (this.deathTicks >= 180 && this.deathTicks <= 200) {
            float f = (this.rand.nextFloat() - 0.5F) * 8.0F;
            float f1 = (this.rand.nextFloat() - 0.5F) * 4.0F;
            float f2 = (this.rand.nextFloat() - 0.5F) * 8.0F;
            this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posX + (double) f, this.posY + 2.0D + (double) f1, this.posZ + (double) f2, 0.0D, 0.0D, 0.0D);
        }

        boolean flag = this.world.getGameRules().getBoolean("doMobLoot");

        if (!this.world.isRemote) {
            if (this.deathTicks == 1) {
                this.world.playBroadcastSound(1028, new BlockPos(this), 0);
            }
        }

        if (this.deathTicks == 200 && !this.world.isRemote) {
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, 16, true);
            this.entityDropItem(new ItemStack(TakumiItemCore.CHAMP_CORE, this.rand.nextInt(3)+1), 0.5f);
            this.setDead();
        }
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
            int maxID = 6;
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
        if (this.deathTicks > 0) {
            this.world.loadedEntityList.forEach(entity -> {
                if (!(entity instanceof EntityAngelCreeper) && this.getDistanceSqToEntity(entity) < 100) {
                    entity.motionX += (this.posX - entity.posX) / 100;
                    entity.motionY += (this.posY - entity.posY) / 100;
                    entity.motionZ += (this.posZ - entity.posZ) / 100;
                }
            });
        }
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            switch (this.getAttackID()) {
                //demon
                case 1: {
                    for (int t = 0; t < (this.getPowered() ? 100 : 50); t++) {
                        Random rand = new Random();
                        int i = this.getPowered() ? 50 : 30;
                        double x = this.posX + this.rand.nextInt(i * 2) - i;
                        double y = this.posY + 50 + this.rand.nextInt(i) - i / 2;
                        double z = this.posZ + this.rand.nextInt(i * 2) - i;
                        EntityLargeFireball fireball = new EntityLargeFireball(this.world);
                        fireball.setPosition(x, y, z);
                        fireball.motionX = 0;
                        fireball.motionY = -1;
                        fireball.motionZ = 0;
                        fireball.accelerationY = -1;
                        fireball.explosionPower = this.getPowered() ? 5 : 3;
                        if (!this.world.isRemote) {
                            this.world.spawnEntity(fireball);
                        }
                    }
                    break;
                }

                //rare
                case 2: {
                    for (int t = 0; t < (this.getPowered() ? 40 : 20); t++) {
                        Random rand = new Random();
                        int i = this.getPowered() ? 50 : 30;
                        double x = this.posX + this.rand.nextInt(i * 2) - i;
                        double y = this.posY + this.rand.nextInt(i) - i / 2;
                        double z = this.posZ + this.rand.nextInt(i * 2) - i;
                        this.world.createExplosion(this, x, y, z, this.getPowered() ? 5 : 3, true);
                    }
                    break;
                }

                //odd
                case 3: {
                    for (int t = 0; t < (this.getPowered() ? 25 : 10); t++) {
                        EntityOddDummyGhast ghast = new EntityOddDummyGhast(this.world);
                        ghast.setPosition(this.posX + this.rand.nextInt(10) - 5, this.posY + this.rand.nextInt(10) - 5,
                                this.posZ + this.rand.nextInt(10) - 5);
                        this.world.spawnEntity(ghast);
                    }
                    break;
                }

                //bolt
                case 4: {
                    for (int i = 0; i < 10 * (this.getPowered() ? 3 : 1); i++) {
                        BlockPos pos = this.getPosition().add(
                                MathHelper.nextDouble(this.rand, -5 * (this.getPowered() ? 2 : 1), 5 * (this.getPowered() ? 2 : 1)),
                                MathHelper.nextDouble(this.rand, -5 * (this.getPowered() ? 2 : 1), 5 * (this.getPowered() ? 2 : 1)),
                                MathHelper.nextDouble(this.rand, -5 * (this.getPowered() ? 2 : 1),
                                        5 * (this.getPowered() ? 2 : 1)));
                        EntityLightningBolt bolt =
                                new EntityLightningBolt(this.world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, false);
                        this.world.addWeatherEffect(bolt);
                        this.world.spawnEntity(bolt);
                    }
                    break;
                }

                //ofalen
                case 5: {
                    for (int x = -10; x <= 10; x++) {
                        for (int y = -10; y <= 10; y++) {
                            for (int z = -10; z <= 10; z++) {
                                if (x * x + y * y + z * z > 98 && x * x + y * y + z * z < 102) {
                                    this.world.createExplosion(this, this.posX + x, this.posY + y, this.posZ + z,
                                            this.getPowered() ? 3 : 1.5f, true);
                                }
                            }
                        }
                    }
                    break;
                }
                default: {
                    this.world.createExplosion(this, this.posX, this.posY, this.posZ, this.getPowered() ? 7 : 4, true);
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
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100);
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
        return true;
    }

    @Override
    public Object getRender(RenderManager manager) {
        return new RenderAngelCreeper<>(manager);
    }
}
