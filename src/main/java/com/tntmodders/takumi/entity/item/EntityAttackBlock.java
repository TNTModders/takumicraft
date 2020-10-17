package com.tntmodders.takumi.entity.item;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiEntityCore;
import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.entity.ITakumiEntity;
import com.tntmodders.takumi.entity.mobs.*;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class EntityAttackBlock extends Entity {
    //default: 18000
    public static final int ATTACK_TICK = 18000;
    //default 100
    public static final int DIST = 100;
    //default 30
    public static final int NEAREST = 30;
    //default 2000f
    public static final float MAX_TP = 2000f;
    //default 200f
    public static final float MIN_TP = 200f;
    //default 600
    public static final float MAX_CHARGE_TICK = 600;
    //default 2400;
    public static final int MAX_DAMAGED_TICK = 2400;

    private static final DataParameter<Float> TP = EntityDataManager.createKey(EntityAttackBlock.class, DataSerializers.FLOAT);
    private static final DataParameter<BlockPos> POS = EntityDataManager.createKey(EntityAttackBlock.class, DataSerializers.BLOCK_POS);
    private static final DataParameter<Float> DX = EntityDataManager.createKey(EntityAttackBlock.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> DZ = EntityDataManager.createKey(EntityAttackBlock.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> ATTACK_ID =
            EntityDataManager.createKey(EntityAttackBlock.class, DataSerializers.VARINT);
    private final BossInfoServer bigcreeper =
            (BossInfoServer) new BossInfoServer(new TextComponentTranslation("entity.bigcreeper.name"), BossInfo.Color.GREEN,
                    BossInfo.Overlay.PROGRESS).setDarkenSky(true).setCreateFog(true);
    private final BossInfoServer attackblock =
            new BossInfoServer(new TextComponentTranslation("entity.attackblock.name"), BossInfo.Color.BLUE,
                    BossInfo.Overlay.NOTCHED_20);

    private final boolean[] msgflgs = new boolean[3];
    private final List<ITakumiEntity> entities = new ArrayList<>();
    private final List<BlockPos> thunderPoint = new ArrayList<>();
    private float chargeTick;
    private EntityBigCreeperDummy dummy;
    private int damagedTick;
    private float lastTP;

    public EntityAttackBlock(World worldIn) {
        super(worldIn);
        this.setSize(0.75f, 1.65f);
        TakumiEntityCore.getEntityList().forEach(iTakumiEntity -> {
            if (iTakumiEntity.takumiRank() == ITakumiEntity.EnumTakumiRank.LOW ||
                    iTakumiEntity.takumiRank() == ITakumiEntity.EnumTakumiRank.MID ||
                    iTakumiEntity.takumiRank() == ITakumiEntity.EnumTakumiRank.HIGH) {
                if (iTakumiEntity.getClass() != EntitySeaGuardianCreeper.class &&
                        iTakumiEntity.getClass() != EntitySquidCreeper.class &&
                        ((EntityCreeper) iTakumiEntity).isNonBoss() &&
                        !iTakumiEntity.takumiType().isMagic()) {
                    entities.add(iTakumiEntity);
                }
            }
        });
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
            int maxID = 5;
            this.dataManager.set(ATTACK_ID, new Random(System.currentTimeMillis()).nextInt(maxID + 1));
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.dummy == null) {
            if (this.world.loadedEntityList.stream().anyMatch(entity -> entity instanceof EntityBigCreeperDummy)) {
                this.dummy = ((EntityBigCreeperDummy) this.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityBigCreeperDummy).toArray()[0]);
            }
        }
        if (this.dummy != null) {
            if (this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
               /* this.dummy.setDead();
                this.setDead();*/
            }
            if (this.getTP() > 0) {

                float distance = DIST * DIST - NEAREST * NEAREST;
                float maxDistance = distance;
                if (this.dummy != null) {
                    distance = (float) ((this.getDistanceSqXZ(this.dummy.getPosition()) - NEAREST * NEAREST));
                    if (distance > maxDistance) {
                        distance = maxDistance;
                    }
                }
                boolean flg = this.getDistanceSqToEntity(this.dummy) > NEAREST * NEAREST;
                if (flg) {
                    double x = this.dummy.posX - this.getDX();
                    double z = this.dummy.posZ - this.getDZ();
                    double dy = (this.world.getHeight(((int) x), (int) z) - this.dummy.posY) / 20;
                    this.dummy.setPosition(x, this.dummy.posY + dy, z);

                    this.attackblock.setPercent((distance / maxDistance));
                    this.bigcreeper.setPercent(this.getTP() / MAX_TP);
                    if (!this.world.isRemote && this.rand.nextInt(50) == 0) {
                        if (this.world.getEntitiesWithinAABB(EntityMob.class, this.dummy.getEntityBoundingBox().grow(150)).size() < 100) {
                            int index = 2 + this.rand.nextInt(4);
                            for (int i = 0; i <= index; i++) {
                                EntityCreeper creeper = null;
                                try {
                                    creeper = (EntityCreeper) entities.get(
                                            this.rand.nextInt(entities.size())).getClass().getConstructor(World.class).newInstance(this.world);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (creeper != null) {
                                    creeper.setWorld(this.world);
                                    BlockPos pos = new BlockPos(this.dummy.posX + this.rand.nextFloat() * 50 - this.rand.nextFloat() * 50,
                                            0, this.dummy.posZ + this.rand.nextFloat() * 50 - this.rand.nextFloat() * 50);
                                    pos = this.world.getHeight(pos);
                                    creeper.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                                    creeper.setGlowing(true);
                                    TakumiUtils.takumiSetPowered(creeper, true);
                                    if (this.world.collidesWithAnyBlock(creeper.getEntityBoundingBox().grow(0.5))) {
                                        if (this.world.spawnEntity(creeper)) {
                                            this.world.createExplosion(this.dummy, creeper.posX, creeper.posY, creeper.posZ, 0f, false);
                                        }
                                    }
                                }
                            }
                        }
                    } else if (this.rand.nextInt(100) == 0) {
                        for (int i = 0; i < 20; i++) {
                            BlockPos pos = this.dummy.getPosition().add(
                                    MathHelper.nextDouble(this.rand, -50, 50),
                                    MathHelper.nextDouble(this.rand, -50, 50),
                                    MathHelper.nextDouble(this.rand, -50, 50));
                            for (int j = 0; j < 10; j++) {
                                EntityLightningBolt bolt =
                                        new EntityLightningBolt(this.world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, false);
                                //this.world.addWeatherEffect(bolt);
                                this.world.spawnEntity(bolt);
                            }
                        }
                    }
                }
            } else {
                if (this.damagedTick > MAX_DAMAGED_TICK) {
                    this.damagedTick = 0;
                    this.bigcreeper.setColor(BossInfo.Color.GREEN);
                    this.dummy.setDamaged(false);
                    this.bigcreeper.setName(new TextComponentTranslation("entity.bigcreeper.name"));
                    this.bigcreeper.setCreateFog(true);
                    if (this.lastTP < 1) {
                        this.lastTP = MAX_TP;
                    }
                    float tp = this.lastTP * 2 / 3;
                    if (tp < MIN_TP) {
                        tp = MIN_TP;
                    }
                    this.setTP(tp);
                    this.lastTP = tp;
                    this.world.getPlayers(EntityPlayer.class, input -> EntityAttackBlock.this.getDistanceSqToEntity(input) < 10000).forEach(player
                            -> player.sendMessage(new TextComponentTranslation("entity.attackblock.message.restart")));
                } else {
                    this.damagedTick++;
                    if (this.damagedTick == 1 && this.bigcreeper.getColor() == BossInfo.Color.GREEN) {
                        this.world.getPlayers(EntityPlayer.class, input -> EntityAttackBlock.this.getDistanceSqToEntity(input) < 10000).forEach(player
                                -> player.sendMessage(new TextComponentTranslation("entity.attackblock.message.attack")));
                    }
                    this.bigcreeper.setColor(BossInfo.Color.PURPLE);
                    this.bigcreeper.setName(new TextComponentTranslation("entity.attackblock.info.damaged"));
                    this.bigcreeper.setCreateFog(false);
                    float health = this.dummy.getHP() / EntityBigCreeperDummy.MAX_HP;
                    this.bigcreeper.setPercent(health);
                    this.dummy.setDamaged(true);


                    if (health < 0 && !this.world.isRemote) {
                        this.setDead();
                        this.dummy.setDead();
                        this.world.getPlayers(EntityPlayer.class, input -> EntityAttackBlock.this.getDistanceSqToEntity(input) < 10000).forEach(player
                                -> {
                            player.sendMessage(new TextComponentTranslation("entity.attackblock.message.done"));
                            player.addItemStackToInventory(new ItemStack(TakumiItemCore.ATTACK_CORE, 1));
                            if (player instanceof EntityPlayerMP) {
                                ((EntityPlayerMP) player).getAdvancements().grantCriterion(player.getServer().getAdvancementManager().getAdvancement(
                                        new ResourceLocation(TakumiCraftCore.MODID, "slay/slay_bigcreeper")), "bigcreeper");
                            }
                        });
                    }
                    if (this.ticksExisted % 200 == 0) {
                        long l = Math.floorDiv(System.currentTimeMillis(), 1000);
                        Random random = new Random(l);
                        random.setSeed(l);
                        this.setRandomAttackID();

                        for (int i = 0; i < 5; i++) {
                            double x = this.dummy.posX + random.nextDouble() * 51 - 25;
                            double z = this.dummy.posZ + random.nextDouble() * 51 - 25;
                            double y = this.world.getHeight((int) x, (int) z);
                            this.thunderPoint.add(new BlockPos(x, y, z));
                        }
                    } else if (this.ticksExisted % 200 == 100) {
                        if (!this.thunderPoint.isEmpty()) {
                            switch (this.getAttackID()) {
                                //straight thunder
                                case 1: {
                                    for (int d = -10; d <= 10; d++) {
                                        EntityLightningBolt boltX = new EntityLightningBolt(this.world, this.dummy.posX + d, this.dummy.posY, this.dummy.posZ, false);
                                        //this.world.addWeatherEffect(boltX);
                                        this.world.spawnEntity(boltX);
                                        EntityLightningBolt boltZ = new EntityLightningBolt(this.world, this.dummy.posX, this.dummy.posY, this.dummy.posZ + d, false);
                                        //this.world.addWeatherEffect(boltZ);
                                        this.world.spawnEntity(boltZ);
                                    }
                                    break;
                                }
                                //random thunder
                                case 2: {
                                    for (BlockPos pos : this.thunderPoint) {
                                        for (int i = 0; i < 10; i++) {
                                            EntityLightningBolt bolt = new EntityLightningBolt(this.world, pos.getX(), pos.getY(), pos.getZ(), false);
                                            //this.world.addWeatherEffect(bolt);
                                            this.world.spawnEntity(bolt);
                                        }
                                    }
                                    break;
                                }
                                //explode
                                case 3: {
                                    if (!this.world.isRemote) {
                                        for (BlockPos pos : this.thunderPoint) {
                                            for (int i = 0; i < 5; i++) {
                                                this.world.newExplosion(this.dummy, pos.getX(), pos.getY(), pos.getZ(), 3f, true, true);
                                            }
                                        }
                                    }
                                    break;
                                }
                                //summon
                                case 4: {
                                    if (!this.world.isRemote) {
                                        for (BlockPos pos : this.thunderPoint) {
                                            for (int i = 0; i < 5; i++) {
                                                int type = this.rand.nextInt(3);
                                                EntityCreeper creeper = new EntityCreeper(this.world);
                                                switch (type) {
                                                    case 0: {
                                                        creeper = new EntityBigBatCreeper(this.world);
                                                        break;
                                                    }
                                                    case 1: {
                                                        creeper = new EntityPhantomCreeper(this.world);
                                                        break;
                                                    }
                                                    case 2: {
                                                        creeper = new EntityBlazeCreeper(this.world);
                                                        break;
                                                    }
                                                }
                                                creeper.setPosition(pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5);
                                                creeper.setGlowing(true);
                                                TakumiUtils.takumiSetPowered(creeper, true);
                                                if (!this.world.collidesWithAnyBlock(creeper.getEntityBoundingBox().grow(0.25))) {
                                                    if (this.world.spawnEntity(creeper)) {
                                                        this.world.createExplosion(this.dummy, creeper.posX, creeper.posY, creeper.posZ, 0f, false);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                        this.thunderPoint.clear();
                    } else if (this.getAttackID() > 0) {
                        if (this.ticksExisted % 200 < 100) {
                            if (!this.thunderPoint.isEmpty()) {
                                switch (this.getAttackID()) {
                                    case 1: {
                                        for (int d = -10; d <= 10; d++) {
                                            for (int i = 0; i < 30; i++) {
                                                this.world.spawnParticle(EnumParticleTypes.END_ROD, this.dummy.posX + d + this.rand.nextDouble() - 0.5d, this.dummy.posY, this.dummy.posZ + this.rand.nextDouble() - 0.5
                                                        , (this.rand.nextDouble() - 0.5) / 10, 0.1, (this.rand.nextDouble() - 0.5) / 10);
                                                this.world.spawnParticle(EnumParticleTypes.END_ROD, this.dummy.posX + this.rand.nextDouble() - 0.5d, this.dummy.posY, this.dummy.posZ + d + this.rand.nextDouble() - 0.5
                                                        , (this.rand.nextDouble() - 0.5) / 10, 0.1, (this.rand.nextDouble() - 0.5) / 10);
                                            }
                                        }
                                        break;
                                    }
                                    case 2: {
                                        for (BlockPos pos : this.thunderPoint) {
                                            for (int i = 0; i < 30; i++) {
                                                this.world.spawnParticle(EnumParticleTypes.END_ROD, pos.getX() + this.rand.nextDouble() - 0.5, pos.getY(), pos.getZ() + this.rand.nextDouble() - 0.5,
                                                        (this.rand.nextDouble() - 0.5) / 10, 0.1, (this.rand.nextDouble() - 0.5) / 10);
                                            }
                                        }
                                        break;
                                    }
                                    case 3: {
                                        for (BlockPos pos : this.thunderPoint) {
                                            for (int i = 0; i < 30; i++) {
                                                this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, pos.getX() + this.rand.nextDouble() - 0.5, pos.getY(), pos.getZ() + this.rand.nextDouble() - 0.5,
                                                        (this.rand.nextDouble() - 0.5) / 10, 0.1, (this.rand.nextDouble() - 0.5) / 10);
                                            }
                                        }
                                        break;
                                    }
                                    case 4: {
                                        for (BlockPos pos : this.thunderPoint) {
                                            for (int i = 0; i < 30; i++) {
                                                this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.getX() + this.rand.nextDouble() - 0.5, pos.getY(), pos.getZ() + this.rand.nextDouble() - 0.5,
                                                        (this.rand.nextDouble() - 0.5) / 10, 0.1, (this.rand.nextDouble() - 0.5) / 10);
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (this.getDistanceSqToEntity(this.dummy) <= NEAREST * NEAREST) {
                this.attackblock.setColor(BossInfo.Color.RED);
                this.chargeTick++;
                float f = this.chargeTick / MAX_CHARGE_TICK;
                this.attackblock.setPercent(f);
                this.dummy.setCharging(true);
                if (!this.world.isRemote) {
                    if (this.chargeTick < MAX_CHARGE_TICK && this.chargeTick % (50 - Math.round((this.chargeTick / MAX_CHARGE_TICK) * 48)) == 0) {
                        for (int i = 0; i < (this.chargeTick / MAX_CHARGE_TICK) * 10; i++) {
                            this.world.playSound(null, this.dummy.posX, this.dummy.posY, this.dummy.posZ,
                                    SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.WEATHER, ((float) Math.exp(this.chargeTick / MAX_CHARGE_TICK) * 10),
                                    (float) (Math.exp(this.chargeTick / MAX_CHARGE_TICK) - 1) * 5f);
                        }
                    }
                    if (this.chargeTick > 0 & !msgflgs[0]) {
                        this.world.getPlayers(EntityPlayer.class, input -> EntityAttackBlock.this.getDistanceSqToEntity(input) < 10000).forEach(player
                                -> player.sendMessage(new TextComponentTranslation("entity.attackblock.message.charge.01")));
                        this.msgflgs[0] = true;
                    }
                    if (this.chargeTick > (MAX_CHARGE_TICK / 3) && !msgflgs[1]) {
                        this.world.getPlayers(EntityPlayer.class, input -> EntityAttackBlock.this.getDistanceSqToEntity(input) < 10000).forEach(player
                                -> player.sendMessage(new TextComponentTranslation("entity.attackblock.message.charge.02")));
                        this.msgflgs[1] = true;
                    }
                    if (this.chargeTick > (MAX_CHARGE_TICK * 2 / 3) && !msgflgs[2]) {
                        this.world.getPlayers(EntityPlayer.class, input -> EntityAttackBlock.this.getDistanceSqToEntity(input) < 10000).forEach(player
                                -> player.sendMessage(new TextComponentTranslation("entity.attackblock.message.charge.03")));
                        this.msgflgs[2] = true;
                    }
                    if (this.chargeTick > MAX_CHARGE_TICK - 25) {
                        double r = 8 * (25 - MAX_CHARGE_TICK + this.chargeTick);
                        for (int i = 0; i < 360; i++) {
                            if (i % 2 == 0) {
                                double x = this.dummy.posX + r * Math.cos(Math.toRadians(i));
                                double z = this.dummy.posZ + r * Math.sin(Math.toRadians(i));
                                double y = this.world.getHeight((int) x, (int) z);
                                if (!this.world.isRemote) {
                                    this.world.newExplosion(this.dummy, x, y, z, 3f, true, false);
                                }
                                EntityLightningBolt bolt = new EntityLightningBolt(this.world, x, y, z, true);
                                //this.world.addWeatherEffect(bolt);
                                this.world.spawnEntity(bolt);
                            }
                        }
                    }
                }
                if (f > 1) {
                    if (!this.world.isRemote) {
                        this.setDead();
                        this.dummy.setDead();
                        this.world.createExplosion(this.dummy, this.dummy.posX, this.dummy.posY + 1, this.dummy.posZ, 20, false);
                        this.world.createExplosion(this.dummy, this.dummy.posX, this.dummy.posY + 50, this.dummy.posZ, 20, false);
                        this.world.loadedEntityList.forEach(entity -> {
                            if (entity.isGlowing() && entity instanceof EntityCreeper && !(entity instanceof EntityPlayer) && !entity.isDead) {
                                ((EntityCreeper) entity).setHealth(0);
                            } else if (entity instanceof EntityPlayer && entity.getDistanceSqToEntity(this.dummy) < 150 * 150) {
                                if (!((EntityPlayer) entity).isCreative() && !((EntityPlayer) entity).isSpectator()) {
                                    entity.attackEntityFrom(DamageSource.causeExplosionDamage(this.world.createExplosion(this.dummy, this.dummy.posX, this.dummy.posY, this.dummy.posZ,
                                            1f, false)), 20f);
                                }
                                entity.sendMessage(new TextComponentTranslation("entity.attackblock.message.failed"));
                            }
                        });
                    }
                }
            }
        }
    }

    protected double getDistanceSqXZ(BlockPos pos) {
        double x = this.posX - pos.getX();
        double z = this.posZ - pos.getZ();
        return x * x + z * z;
    }


    /**
     * Returns a boundingBox used to collide the entity with other entities and blocks. This enables the entity to be
     * pushable on contact, like boats or minecarts.
     */
    @Override
    @Nullable
    public AxisAlignedBB getCollisionBox(Entity entityIn) {
        return this.getEntityBoundingBox();
    }

    /**
     * Returns the collision bounding box for this entity
     */
    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox() {
        return this.getEntityBoundingBox();
    }

    @Override
    protected void entityInit() {
        this.dataManager.register(TP, MAX_TP);
        this.dataManager.register(POS, this.getPosition());
        this.dataManager.register(DX, 0f);
        this.dataManager.register(DZ, 0f);
        this.dataManager.register(ATTACK_ID, 0);
    }

    public float getTP() {
        return this.dataManager.get(TP);
    }

    public void setTP(float f) {
        this.dataManager.set(TP, f);
    }

    public BlockPos getPos() {
        return this.dataManager.get(POS);
    }

    public void setPos(BlockPos pos) {
        this.dataManager.set(POS, pos);
    }

    public float getDX() {
        return this.dataManager.get(DX);
    }

    public void setDX(float x) {
        this.dataManager.set(DX, x);
    }

    public float getDZ() {
        return this.dataManager.get(DZ);
    }

    public void setDZ(float z) {
        this.dataManager.set(DZ, z);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        this.setTP(compound.getFloat("TP"));
        int x = compound.getInteger("bigX");
        int y = compound.getInteger("bigY");
        int z = compound.getInteger("bigZ");
        this.setPos(new BlockPos(x, y, z));
        this.setDX(compound.getFloat("dx"));
        this.setDZ(compound.getFloat("dz"));
        this.chargeTick = compound.getFloat("chargetick");
        if (compound.hasKey("lastTP")) {
            this.lastTP = compound.getFloat("lastTP");
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setFloat("TP", this.getTP());
        compound.setInteger("bigX", this.getPos().getX());
        compound.setInteger("bigY", this.getPos().getY());
        compound.setInteger("bigZ", this.getPos().getZ());
        compound.setFloat("dx", this.getDX());
        compound.setFloat("dz", this.getDZ());
        compound.setFloat("chargetick", this.chargeTick);
        if (this.lastTP > 0) {
            compound.setFloat("lastTP", this.lastTP);
        }
    }

    @Override
    public boolean hitByEntity(Entity entityIn) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }

    @Override
    public void addTrackingPlayer(EntityPlayerMP player) {
        super.addTrackingPlayer(player);
        this.bigcreeper.addPlayer(player);
        this.attackblock.addPlayer(player);
    }

    @Override
    public void removeTrackingPlayer(EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        this.bigcreeper.removePlayer(player);
        this.attackblock.removePlayer(player);
    }

    @Override
    public boolean isInRangeToRender3d(double x, double y, double z) {
        return true;
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        return true;
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return true;
    }
}
