package com.tntmodders.takumi.entity.item;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiEntityCore;
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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class EntityAttackBlock extends Entity {
    //default: 12000
    public static final int attackTick = 12000;
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
    //default 1500f
    private final float maxTP = 10f;
    //default 600
    private final float maxChargeTick = 600;
    private final boolean[] msgflgs = new boolean[3];
    private final List<ITakumiEntity> entities = new ArrayList<>();
    private final List<BlockPos> thunderPoint = new ArrayList<>();
    //default 2400;
    private final int maxDamagedTick = 2400;
    //default 100
    public int dist = 100;
    //default 50
    public int nearest = 50;
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
                        ((EntityCreeper) iTakumiEntity).isNonBoss()) {
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
        if (this.dummy == null) {
            if (this.world.loadedEntityList.stream().anyMatch(entity -> entity instanceof EntityBigCreeperDummy)) {
                this.dummy = ((EntityBigCreeperDummy) this.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityBigCreeperDummy).toArray()[0]);
            }
        }
        if (this.dummy != null) {
            if (this.getDistanceSqToEntity(this.dummy) > this.nearest * this.nearest) {
                if (this.getTP() > 0) {
                    double x = this.dummy.posX - this.getDX();
                    double z = this.dummy.posZ - this.getDZ();
                    double dy = (this.world.getHeight(((int) x), (int) z) - this.dummy.posY) / 20;
                    this.dummy.setPosition(x, this.dummy.posY + dy, z);

                    float distance = this.dist * this.dist - this.nearest * this.nearest;
                    float maxDistance = distance;
                    if (this.dummy != null) {
                        distance = (float) ((this.getDistanceSqXZ(this.dummy.getPosition()) - this.nearest * this.nearest));
                        if (distance > maxDistance) {
                            distance = maxDistance;
                        }
                    }
                    this.attackblock.setPercent((distance / maxDistance));
                    this.bigcreeper.setPercent(this.getTP() / this.maxTP);
                    if (!this.world.isRemote && this.rand.nextInt(50) == 0) {
                        if (this.world.getEntitiesWithinAABB(EntityMob.class, this.dummy.getEntityBoundingBox().grow(100)).size() < 100) {
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
                                creeper.setPosition(pos.getX(), pos.getY() + 0.5, pos.getZ());
                                creeper.setGlowing(true);
                                TakumiUtils.takumiSetPowered(creeper, true);
                                this.world.spawnEntity(creeper);
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
                                this.world.addWeatherEffect(bolt);
                                this.world.spawnEntity(bolt);
                            }
                        }
                    }
                } else {
                    if (this.damagedTick > this.maxDamagedTick) {
                        this.damagedTick = 0;
                        this.bigcreeper.setColor(BossInfo.Color.GREEN);
                        this.dummy.setDamaged(false);
                        if (this.lastTP < 1) {
                            this.lastTP = this.maxTP;
                        }
                        float tp = this.lastTP / 2;
                        this.setTP(tp);
                        this.lastTP = tp;
                        TakumiCraftCore.LOGGER.info("reboot");
                    } else {
                        this.bigcreeper.setColor(BossInfo.Color.PURPLE);
                        float health = this.dummy.getHP() / this.dummy.maxHP;
                        this.bigcreeper.setPercent(health);
                        this.dummy.setDamaged(true);
                        this.damagedTick++;

                        if (health < 0 && !this.world.isRemote) {
                            this.setDead();
                            this.dummy.setDead();
                            this.world.getPlayers(EntityPlayer.class, input -> EntityAttackBlock.this.getDistanceSqToEntity(input) < 10000).forEach(player
                                    -> player.sendMessage(new TextComponentString("done")));
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
                                            this.world.addWeatherEffect(boltX);
                                            this.world.spawnEntity(boltX);
                                            EntityLightningBolt boltZ = new EntityLightningBolt(this.world, this.dummy.posX, this.dummy.posY, this.dummy.posZ + d, false);
                                            this.world.addWeatherEffect(boltZ);
                                            this.world.spawnEntity(boltZ);
                                        }
                                        break;
                                    }
                                    //random thunder
                                    case 2: {
                                        for (BlockPos pos : this.thunderPoint) {
                                            for (int i = 0; i < 10; i++) {
                                                EntityLightningBolt bolt = new EntityLightningBolt(this.world, pos.getX(), pos.getY(), pos.getZ(), false);
                                                this.world.addWeatherEffect(bolt);
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
                                                    creeper.setPosition(pos.getX(), pos.getY() + 0.5, pos.getZ());
                                                    creeper.setGlowing(true);
                                                    TakumiUtils.takumiSetPowered(creeper, true);
                                                    this.world.spawnEntity(creeper);
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
            } else {
                this.attackblock.setColor(BossInfo.Color.RED);
                this.chargeTick++;
                float f = this.chargeTick / this.maxChargeTick;
                this.attackblock.setPercent(f);
                this.dummy.setCharging(true);
                if (!this.world.isRemote) {
                    if (this.chargeTick < this.maxChargeTick && this.chargeTick % (50 - Math.round((this.chargeTick / this.maxChargeTick) * 48)) == 0) {
                        for (int i = 0; i < (this.chargeTick / this.maxChargeTick) * 10; i++) {
                            this.world.playSound(null, this.dummy.posX, this.dummy.posY, this.dummy.posZ,
                                    SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.MASTER, ((float) Math.exp(this.chargeTick / this.maxChargeTick) * 10),
                                    (float) (Math.exp(this.chargeTick / this.maxChargeTick) - 1) * 5f);
                        }
                    }
                    if (this.chargeTick > 0 & !msgflgs[0]) {
                        this.world.getPlayers(EntityPlayer.class, input -> EntityAttackBlock.this.getDistanceSqToEntity(input) < 10000).forEach(player
                                -> player.sendMessage(new TextComponentString("01")));
                        this.msgflgs[0] = true;
                    }
                    if (this.chargeTick > (this.maxChargeTick / 3) && !msgflgs[1]) {
                        this.world.getPlayers(EntityPlayer.class, input -> EntityAttackBlock.this.getDistanceSqToEntity(input) < 10000).forEach(player
                                -> player.sendMessage(new TextComponentString("02")));
                        this.msgflgs[1] = true;
                    }
                    if (this.chargeTick > (this.maxChargeTick * 2 / 3) && !msgflgs[2]) {
                        this.world.getPlayers(EntityPlayer.class, input -> EntityAttackBlock.this.getDistanceSqToEntity(input) < 10000).forEach(player
                                -> player.sendMessage(new TextComponentString("03")));
                        this.msgflgs[2] = true;
                    }
                    if (this.chargeTick > this.maxChargeTick - 25) {
                        double r = 8 * (25 - this.maxChargeTick + this.chargeTick);
                        for (int i = 0; i < 360; i++) {
                            if (i % 2 == 0) {
                                double x = this.dummy.posX + r * Math.cos(Math.toRadians(i));
                                double z = this.dummy.posZ + r * Math.sin(Math.toRadians(i));
                                double y = this.world.getHeight((int) x, (int) z);
                                if (!this.world.isRemote) {
                                    this.world.newExplosion(this.dummy, x, y, z, 3f, true, false);
                                }
                                EntityLightningBolt bolt = new EntityLightningBolt(this.world, x, y, z, true);
                                this.world.addWeatherEffect(bolt);
                                this.world.spawnEntity(bolt);
                            }
                        }
                    }
                }
                if (f > 1) {
                    if (!this.world.isRemote) {
                        this.setDead();
                        this.dummy.setDead();
                        this.world.createExplosion(this.dummy, this.dummy.posX, this.dummy.posY, this.dummy.posZ, 20, false);
                        this.world.loadedEntityList.forEach(entity -> {
                            if (entity.isGlowing() && entity instanceof EntityCreeper && !(entity instanceof EntityPlayer) && !entity.isDead) {
                                ((EntityCreeper) entity).setHealth(0);
                            } else if (entity instanceof EntityPlayer && !((EntityPlayer) entity).isCreative() && !((EntityPlayer) entity).isSpectator() && entity.getDistanceSqToEntity(this.dummy) < 150 * 150) {
                                entity.attackEntityFrom(DamageSource.causeExplosionDamage(this.world.createExplosion(this.dummy, this.dummy.posX, this.dummy.posY, this.dummy.posZ, 1f, false)),
                                        20f);
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
        this.dataManager.register(TP, this.maxTP);
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
        this.setTP(compound.getFloat("tp"));
        int x = compound.getInteger("bigX");
        int y = compound.getInteger("bigY");
        int z = compound.getInteger("bigZ");
        this.setPos(new BlockPos(x, y, z));
        this.setDX(compound.getFloat("dx"));
        this.setDZ(compound.getFloat("dz"));
        this.chargeTick = compound.getFloat("chargetick");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setFloat("tp", this.getTP());
        compound.setInteger("bigX", this.getPos().getX());
        compound.setInteger("bigY", this.getPos().getY());
        compound.setInteger("bigZ", this.getPos().getZ());
        compound.setFloat("dx", this.getDX());
        compound.setFloat("dz", this.getDZ());
        compound.setFloat("chargetick", this.chargeTick);
    }

    @Override
    public boolean hitByEntity(Entity entityIn) {
        if (entityIn instanceof EntityPlayer && ((EntityPlayer) entityIn).isCreative()) {
            this.setDead();
        }
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

/*public class EntityAttackBlock extends EntityLiving {
    public static final List<Class<? extends Entity>> ENGINEERS = new ArrayList<>();
    public static final List<Class<? extends Entity>> ARTILLERIES = new ArrayList<>();

    static {
        ENGINEERS.add(EntityIceCreeper.class);
        ENGINEERS.add(EntitySpongeCreeper.class);
        ENGINEERS.add(EntitySandCreeper.class);
        ENGINEERS.add(EntityTerracottaCreeper.class);

        ARTILLERIES.add(EntityCannonCreeper.class);
        ARTILLERIES.add(EntitySkeletonCreeper.class);
        ARTILLERIES.add(EntitySnowCreeper.class);
        ARTILLERIES.add(EntityStrayCreeper.class);
    }

    private final BossInfoServer bossInfo =
            (BossInfoServer) new BossInfoServer(new TextComponentTranslation("entity.attackblock.name"),
                    BossInfo.Color.BLUE, BossInfo.Overlay.NOTCHED_20).setDarkenSky(true);
    private final List<ITakumiEntity> entities = new ArrayList<>();
    private int spawnTick;

    public List<EntityLivingBase> spawns = new ArrayList<>();

    public EntityAttackBlock(World worldIn) {
        super(worldIn);
        this.setSize(1, 2);
        this.isImmuneToFire = true;

        TakumiEntityCore.getEntityList().forEach(iTakumiEntity -> {
            if (iTakumiEntity.takumiRank() == ITakumiEntity.EnumTakumiRank.LOW ||
                    iTakumiEntity.takumiRank() == ITakumiEntity.EnumTakumiRank.MID) {
                if (iTakumiEntity.getClass() != EntitySeaGuardianCreeper.class &&
                        iTakumiEntity.getClass() != EntitySquidCreeper.class &&
                        ((EntityLivingBase) iTakumiEntity).isNonBoss() &&
                        iTakumiEntity.getClass() != EntityEvokerCreeper.class &&
                        iTakumiEntity.getClass() != EntityVexCreeper.class &&
                        iTakumiEntity.getClass() != EntityRoboCreeper.class &&
                        iTakumiEntity.getClass() != EntityRoboCreeper_Evo.class) {
                    entities.add(iTakumiEntity);
                }
            }
        });
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.taskEntries.clear();
        this.targetTasks.taskEntries.clear();
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(100000);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5000);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        this.spawnTick++;
        this.world.setBlockToAir(this.getPosition());
        this.world.setBlockToAir(this.getPosition().up());
        if (this.ticksExisted % 200 == 0) {
            List<Entity> list = new ArrayList<>(this.world.loadedEntityList);
            list.removeIf(entity -> this.getDistanceSqToEntity(entity) > 10000 || !(entity instanceof EntityLivingBase));
            list.sort(Comparator.comparingDouble(EntityAttackBlock.this::getDistanceSqToEntity));
            list.forEach(entity -> {
                if (entity instanceof EntityCreeper) {
                    if (((EntityCreeper) entity).getAttackTarget() == null) {
                        ((EntityCreeper) entity).setAttackTarget(this);
                    }
                }
            });
            BlockPos pos = null;
            if (list.size() < 500) {
                boolean flg = true;
                while (flg) {
                    Random random = this.rand;
                    double x = this.posX - 50 + random.nextInt(100);
                    double z = this.posZ - 50 + random.nextInt(100);
                    double y = Math.min(this.world.getHeight((int) x, (int) z), this.posY) + random.nextInt(40) - random.nextInt(40);
                    pos = new BlockPos(x, y, z);
                    if (y > 1 && this.world.isAirBlock(pos) && this.world.isAirBlock(pos.up()) && this.getDistanceSq(pos) > 400) {
                        flg = false;
                    }
                }

                if (pos != null) {
                    if (!this.world.isRemote && this.world.getGameRules().getBoolean("doMobSpawning")) {
                        if (this.rand.nextInt(10) == 0) {
                            int id = this.rand.nextInt(2);
                            switch (id) {
                                case 1: {
                                    for (int i = 0; i < 10; i++) {
                                        double offX = -4 + this.rand.nextInt(9);
                                        double offY = -4 + this.rand.nextInt(9);
                                        double offZ = -4 + this.rand.nextInt(9);
                                        try {
                                            EntityLiving entity = (EntityCreeper) ARTILLERIES.get(this.rand.nextInt(ARTILLERIES.size())).getConstructor(World.class).newInstance(this.world);
                                            entity.setPosition(pos.getX() + offX, pos.getY() + offY, pos.getZ() + offZ);
                                            entity.setGlowing(true);
                                            entity.setAttackTarget(this);
                                            this.world.spawnEntity(entity);
                                            this.spawns.add(entity);
                                            if (entity.isNotColliding()) {
                                                if (entity.posY > 1 && this.world.spawnEntity(entity)) {
                                                    EntityLightningBolt bolt = new EntityLightningBolt(this.world, entity.posX, entity.posY, entity.posZ, true);
                                                    this.world.addWeatherEffect(bolt);
                                                    this.world.spawnEntity(bolt);
                                                    i++;
                                                }
                                            } else if (this.rand.nextInt(20) == 0) {
                                                i++;
                                            }
                                        } catch (Exception ignored) {
                                        }
                                    }
                                    break;
                                }
                                default: {
                                    for (int i = 0; i < 5; i++) {
                                        EntityLapisCreeper lapisCreeper = new EntityLapisCreeper(this.world);
                                        lapisCreeper.setPosition(pos.getX(), pos.getY(), pos.getZ());
                                        lapisCreeper.setGlowing(true);
                                        lapisCreeper.setHealth(2f);
                                        this.world.spawnEntity(lapisCreeper);
                                        this.spawns.add(lapisCreeper);
                                        try {
                                            EntityCreeper creeper = (EntityCreeper) ENGINEERS.get(this.rand.nextInt(ENGINEERS.size())).getConstructor(World.class).newInstance(this.world);
                                            creeper.setPosition(pos.getX(), pos.getY() + 1, pos.getZ());
                                            creeper.setGlowing(true);
                                            this.world.spawnEntity(creeper);
                                            this.spawns.add(creeper);
                                            creeper.startRiding(lapisCreeper);
                                            EntityLightningBolt bolt = new EntityLightningBolt(this.world, creeper.posX, creeper.posY, creeper.posZ, true);
                                            this.world.addWeatherEffect(bolt);
                                            this.world.spawnEntity(bolt);
                                        } catch (Exception ignored) {
                                        }
                                    }
                                    break;
                                }
                            }
                        } else {
                            int max = 10;
                            if (this.world.playerEntities != null) {
                                max += this.world.playerEntities.size() * 1.5;
                            }
                            for (int i = 0; i < max; ) {
                                double offX = -4 + this.rand.nextInt(9);
                                double offY = -4 + this.rand.nextInt(9);
                                double offZ = -4 + this.rand.nextInt(9);
                                try {
                                    EntityLiving entity = ((EntityLiving) entities.get(this.rand.nextInt(entities.size())).getClass().getConstructor(World.class).newInstance(this.world));
                                    entity.setPosition(pos.getX() + offX, pos.getY() + offY, pos.getZ() + offZ);
                                    if (entity.posY > 1 && entity.isNotColliding()) {
                                        if (this.world.spawnEntity(entity)) {
                                            this.spawns.add(entity);
                                            EntityLightningBolt bolt = new EntityLightningBolt(this.world, entity.posX, entity.posY, entity.posZ, true);
                                            this.world.addWeatherEffect(bolt);
                                            this.world.spawnEntity(bolt);
                                            i++;
                                        }
                                    } else if (this.rand.nextInt(20) == 0) {
                                        i++;
                                    }
                                } catch (Exception ignored) {
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("sp", spawnTick);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
        this.spawnTick = compound.getInteger("sp");
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    public void move(MoverType type, double x, double y, double z) {
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender() {
        return 15728880;
    }

    @Override
    public float getBrightness() {
        return 1.0F;
    }

    @Override
    public void setCustomNameTag(String name) {
        super.setCustomNameTag(name);
        this.bossInfo.setName(this.getDisplayName());
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
    protected void onDeathUpdate() {
        super.onDeathUpdate();
        if (this.getHealth() != -334 && this.getHealth() <= 0 && !this.world.isRemote) {
            this.world.playerEntities.forEach(player -> {
                player.sendMessage(new TextComponentTranslation("entity.attackblock.win"));
                player.addExperience(100);
            });
            EntityBigCreeper bigCreeper = new EntityBigCreeper(this.world);
            bigCreeper.setPosition(this.posX, this.posY, this.posZ);
            //this.world.spawnEntity(bigCreeper);
            this.setDead();
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source == DamageSource.OUT_OF_WORLD) {
            return super.attackEntityFrom(source, amount);
        }
        if (source.getTrueSource() instanceof EntityPlayer && ((EntityPlayer) source.getTrueSource()).isCreative()) {
            this.setHealth(-334);
            return true;
        }
        if (source.isExplosion() && amount > 2) {
            this.setDead();
            this.world.loadedEntityList.forEach(entity -> {
                if (entity instanceof EntityLiving) {
                    ((EntityLiving) entity).setAttackTarget(null);
                }
            });
            this.world.playerEntities.forEach(player -> {
                if (*//*!player.isCreative() && *//*!player.isSpectator()) {
                    player.sendMessage(new TextComponentTranslation("entity.attackblock.lose"));
                }
            });
        }
        return false;
    }

    @Override
    public void knockBack(Entity entityIn, float strength, double xRatio, double zRatio) {
    }

    @Override
    protected boolean isMovementBlocked() {
        return true;
    }
}*/
