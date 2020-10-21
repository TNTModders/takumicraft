package com.tntmodders.takumi.entity.mobs.boss;

import com.tntmodders.asm.TakumiASMNameMap;
import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.client.render.RenderKingCreeper;
import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.core.TakumiPacketCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.ai.EntityAIBossCreeperSwell;
import com.tntmodders.takumi.entity.item.EntityBigHomingBomb;
import com.tntmodders.takumi.entity.item.EntityKingBlock;
import com.tntmodders.takumi.network.MessageTakumiBossAttackID;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.BossInfo.Overlay;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

public class EntityKingCreeper extends EntityTakumiAbstractCreeper {

    private static final DataParameter<Integer> ATTACK_ID =
            EntityDataManager.createKey(EntityKingCreeper.class, DataSerializers.VARINT);
    private final BossInfoServer bossInfo =
            (BossInfoServer) new BossInfoServer(new TextComponentTranslation("entity.kingcreeper.name"), Color.GREEN,
                    Overlay.PROGRESS).setDarkenSky(true).setCreateFog(true);
    private DamageSource lastSource;
    private boolean hasUltimateCalled;

    public EntityKingCreeper(World worldIn) {
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
        this.experienceValue = 50;
    }

    public void setHasUltimateCalled(boolean hasUltimateCalled) {
        this.hasUltimateCalled = hasUltimateCalled;
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
                EntityKingCreeper kingCreeper = new EntityKingCreeper(this.world);
                NBTTagCompound tagCompound = new NBTTagCompound();
                this.writeEntityToNBT(tagCompound);
                tagCompound.setBoolean("ignited", false);
                kingCreeper.readEntityFromNBT(tagCompound);
                kingCreeper.setHealth(this.getHealth());
                kingCreeper.copyLocationAndAnglesFrom(this);
                if (this.hasCustomName()) {
                    kingCreeper.setCustomNameTag(this.getCustomNameTag());
                }
                if (this.getPowered()) {
                    TakumiUtils.takumiSetPowered(kingCreeper, true);
                }
                kingCreeper.setCreeperState(-1);
                kingCreeper.setAttackTarget(null);
                kingCreeper.setHasUltimateCalled(this.hasUltimateCalled);
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

    /**
     * server use, in client, you can sync the ID by MessageTakumiBossAttackID.class
     */
    public void setRandomAttackID() {
        if (!this.world.isRemote) {
            int id;
            if (!this.hasUltimateCalled && this.getHealth() <= this.getMaxHealth() / 2) {
                this.hasUltimateCalled = true;
                id = 99;
            } else {
                //@TODO: chage maxID if the var of attacks added. & change debugID if you commit it.
                int debugID = 0;
                if (debugID != 0) {
                    id = debugID;
                } else {
                    int maxID = 7;
                    id = new Random(System.currentTimeMillis()).nextInt(maxID + 1);
                }
            }
            this.dataManager.set(ATTACK_ID, id);
            TakumiPacketCore.INSTANCE.sendToAll(new MessageTakumiBossAttackID(this.getEntityId(), this.getAttackID()));
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
                //kingblock / N.magic.zone
                case 6: {
                    for (double x = -5; x <= 5; x += 0.5) {
                        for (double z = -5; z <= 5; z += 0.5) {
                            if (x * x + z * z <= 25 && this.rand.nextInt(20) == 0) {
                                this.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, x, 0, z,
                                        (this.rand.nextDouble() - 0.5) * 0.3, 0.5,
                                        (this.rand.nextDouble() - 0.5) * 0.3);
                            }
                        }
                    }
                    break;
                }
                //sword / null
                case 7: {
                    break;
                }
            }
        } else {
            if (this.getAttackID() == 7) {
                try {
                    Field field = TakumiASMNameMap.getField(EntityCreeper.class, "timeSinceIgnited");
                    field.setAccessible(true);
                    int time = field.getInt(this);
                    if (time > 44) {
                        AxisAlignedBB aabb = this.getEntityBoundingBox().grow(5, 0, 5).contract(0, 1, 0);
                        List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, aabb);
                        if (!list.isEmpty()) {
                            list.forEach(entity -> {
                                if (entity instanceof EntityLivingBase) {
                                    entity.attackEntityFrom(DamageSource.causeMobDamage(this), 10);
                                }
                            });
                        }
                        int t = 50 - time;
                        for (int i = 0; i <= 5; i++) {
                            double x = this.posX + Math.cos(Math.toRadians(60 * t)) * i;
                            double z = this.posZ + Math.sin(Math.toRadians(60 * t)) * i;
                            this.world.createExplosion(this, x, this.posY, z, 0f, false);
                        }
                    }
                } catch (Exception e) {
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
                        this.world.newExplosion(this, pos.getX(), pos.getY(), pos.getZ(), this.getPowered() ? 9 : 6,
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
                                            this.getPowered() ? 4f : 2f, true, true);
                                }
                            }
                        }
                    }
                    break;
                }

                //Thunder
                case 3: {
                    for (int i = 0; i < (this.getPowered() ? 100 : 70); i++) {

                        BlockPos pos = this.getRandomPos(2, 1, 2);
                        if (this.rand.nextInt(5) == 0) {
                            this.world.createExplosion(this, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 1.5f,
                                    true);
                        }
                        EntityLightningBolt bolt =
                                new EntityLightningBolt(this.world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
                                        false);
                        //this.world.addWeatherEffect(bolt);
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
                        fireball.explosionPower = this.getPowered() ? 6 : 4;
                        this.world.spawnEntity(fireball);
                    }
                    break;
                }
                //kingblock
                case 6: {
                    EntityLivingBase entitylivingbase = this.getAttackTarget();
                    if (entitylivingbase != null) {
                        double d0 = Math.min(entitylivingbase.posY, this.posY);
                        double d1 = Math.max(entitylivingbase.posY, this.posY) + 1.0D;
                        float f = (float) MathHelper.atan2(entitylivingbase.posZ - this.posZ,
                                entitylivingbase.posX - this.posX);

                        if (this.getDistanceSqToEntity(entitylivingbase) < 16.0D) {
                            for (int i = 0; i < 5; ++i) {
                                float f1 = f + (float) i * (float) Math.PI * 0.4F;
                                this.spawnFangs(this.posX + (double) MathHelper.cos(f1) * 3D,
                                        this.posZ + (double) MathHelper.sin(f1) * 3D, d0, d1, f1, 0);
                            }

                            for (int k = 0; k < 8; ++k) {
                                float f2 = f + (float) k * (float) Math.PI * 2.0F / 8.0F + (float) Math.PI * 2F / 5F;
                                this.spawnFangs(this.posX + (double) MathHelper.cos(f2) * 7D,
                                        this.posZ + (double) MathHelper.sin(f2) * 7D, d0, d1, f2, 3);
                            }
                        } else {
                            for (int l = 0; l < 16; ++l) {
                                double d2 = 2D * (double) (l + 1);
                                this.spawnFangs(this.posX + (double) MathHelper.cos(f) * d2,
                                        this.posZ + (double) MathHelper.sin(f) * d2, d0, d1, f, l / 2);
                            }
                        }
                        break;
                    }
                }
                //bigsword
                case 7: {
                    this.world.createExplosion(this, this.posX, this.posY, this.posZ, 0, false);
                    break;
                }

                //ultimate
                case 99: {
                    List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(5));
                    if (!list.isEmpty()) {
                        list.forEach(entity -> {
                            if (entity instanceof EntityLivingBase && !entity.isEntityInvulnerable(DamageSource.causeMobDamage(this)) &&
                                    !(entity instanceof EntityPlayer && (((EntityPlayer) entity).isCreative() || ((EntityPlayer) entity).isSpectator()))) {
                                entity.attackEntityFrom(DamageSource.causeMobDamage(this).setDamageIsAbsolute().setDamageBypassesArmor(),
                                        ((EntityLivingBase) entity).getHealth() - 1f);
                            }
                        });
                    }
                    for (int x = -5; x <= 5; x++) {
                        for (int y = -5; y <= 5; y++) {
                            for (int z = -5; z <= 5; z++) {
                                this.world.createExplosion(this, this.posX, this.posY, this.posZ, 4f, true);
                            }
                        }
                    }
                }

                default: {
                    this.world.createExplosion(this, this.posX, this.posY, this.posZ, this.getPowered() ? 12 : 8, true);
                }
            }
        }
        this.setRandomAttackID();
    }

    private void spawnFangs(double p_190876_1_, double p_190876_3_, double p_190876_5_, double p_190876_7_,
                            float p_190876_9_, int p_190876_10_) {
        BlockPos blockpos = new BlockPos(p_190876_1_, p_190876_7_, p_190876_3_);
        boolean flag = false;
        double d0 = 0.0D;

        while (true) {
            if (!this.world.isBlockNormalCube(blockpos, true) &&
                    this.world.isBlockNormalCube(blockpos.down(), true)) {
                if (!this.world.isAirBlock(blockpos)) {
                    IBlockState iblockstate = this.world.getBlockState(blockpos);
                    AxisAlignedBB axisalignedbb =
                            iblockstate.getCollisionBoundingBox(this.world, blockpos);

                    if (axisalignedbb != null) {
                        d0 = axisalignedbb.maxY;
                    }
                }

                flag = true;
                break;
            }

            blockpos = blockpos.down();

            if (blockpos.getY() < MathHelper.floor(p_190876_5_) - 1) {
                break;
            }
        }

        if (flag) {
            EntityKingBlock spell = new EntityKingBlock(this.world, p_190876_1_,
                    (double) blockpos.getY() + d0 + 5, p_190876_3_, p_190876_10_ * 10 + 20);
            this.world.spawnEntity(spell);
        }
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
/*            if (FMLCommonHandler.instance().getSide().isServer()) {
                FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendMessage(new TextComponentString(this.getName() + " has killed!"), true);
            }*/
        }
        super.onDeath(source);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(260);
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
        compound.setBoolean("hasUltimateCalled", this.hasUltimateCalled);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setAttackID(compound.getInteger("attackid"));
        this.hasUltimateCalled = compound.getBoolean("hasUltimateCalled");
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
        return 0x88ff00;
    }

    @Override
    public boolean isCustomSpawn() {
        return true;
    }

    @Override
    public String getRegisterName() {
        return "kingcreeper";
    }

    @Override
    public int getRegisterID() {
        return 501;
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
    public Object getRender(RenderManager manager) {
        return new RenderKingCreeper(manager);
    }
}
