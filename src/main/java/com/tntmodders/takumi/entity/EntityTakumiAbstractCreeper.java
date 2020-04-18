package com.tntmodders.takumi.entity;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.client.render.RenderTakumiCreeper;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.core.TakumiWorldCore;
import com.tntmodders.takumi.entity.ai.EntityAIFollowCatCreeper;
import com.tntmodders.takumi.entity.ai.EntityAIMoveToAttackBlock;
import com.tntmodders.takumi.entity.item.EntityAttackBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class EntityTakumiAbstractCreeper extends EntityCreeper implements ITakumiEntity {

    private int stoppingCounter;
    private int dmgCount;
    private float prevHealth;

    public EntityTakumiAbstractCreeper(World worldIn) {
        super(worldIn);
        this.experienceValue = this.takumiRank().getExperiment();
        this.tasks.addTask(3, new EntityAIFollowCatCreeper(this));
        this.tasks.addTask(3, new EntityAIMoveToAttackBlock(this, 1.5, true));
        this.targetTasks.addTask(0, new EntityAINearestAttackableTarget(this, EntityAttackBlock.class, false) {
            @Override
            protected double getTargetDistance() {
                return 256;
            }
        });
    }

    @Override
    public void additionalSpawn() {
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        return true;
    }

    @Override
    public void customSpawn() {
    }

    @Override
    public boolean isAnimal() {
        return false;
    }

    @Override
    public int getPrimaryColor() {
        return 39168;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Object getRender(RenderManager manager) {
        return new RenderTakumiCreeper<>(manager);
    }

    @Override
    public ResourceLocation getArmor() {
        if (this instanceof ITakumiEvoEntity && ((ITakumiEvoEntity) this).isEvo()) {
            return new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/dragon_armor.png");
        }
        return new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    }

    @Override
    public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn) {
        if (this instanceof ITakumiEvoEntity && ((ITakumiEvoEntity) this).isEvo()) {
            return blockStateIn.getBlockHardness(worldIn, pos) < 0 ? super.getExplosionResistance(explosionIn, worldIn, pos, blockStateIn) : 1f;
        }
        return super.getExplosionResistance(explosionIn, worldIn, pos, blockStateIn);
    }

    @Override
    protected void despawnEntity() {
        Result result;
        if (this.isNoDespawnRequired()) {
            this.idleTime = 0;
        } else if ((this.idleTime & 0x1F) == 0x1F &&
                (result = ForgeEventFactory.canEntityDespawn(this)) != Result.DEFAULT) {
            if (result == Result.DENY) {
                this.idleTime = 0;
            } else {
                this.setHealth(0);
                this.setDead();
            }
        } else {
            Entity entity = this.world.getClosestPlayerToEntity(this, -1.0D);

            if (entity != null) {
                double d0 = entity.posX - this.posX;
                double d1 = entity.posY - this.posY;
                double d2 = entity.posZ - this.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (this.canDespawn() && d3 > 16384.0D) {
                    this.setHealth(0);
                    this.setDead();
                }

                if (this.idleTime > 600 && this.rand.nextInt(800) == 0 && d3 > 1024.0D && this.canDespawn()) {
                    this.setHealth(0);
                    this.setDead();
                } else if (d3 < 1024.0D) {
                    this.idleTime = 0;
                }
            }
        }
    }

    @Override
    public void setDead() {
        super.setDead();
    }

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        if (damageSrc.isExplosion()) {
            damageAmount = damageAmount / 2;
            if (this.takumiType().getId() == EnumTakumiType.NORMAL.getId()) {
                damageAmount = damageAmount / 2;
            }
        }
        if (damageSrc.getTrueSource() instanceof EntityTakumiAbstractCreeper) {
            damageAmount *= getTypeMatchFactor(((EntityTakumiAbstractCreeper) damageSrc.getTrueSource()).takumiType(),
                    this.takumiType());
            if (this.takumiRank().getLevel() >= 3 &&
                    ((EntityTakumiAbstractCreeper) damageSrc.getTrueSource()).takumiRank().getLevel() <= 2) {
                damageAmount *= 0.25;
            }
        }
        if (!this.isNonBoss() && this.getCreeperState() > 0) {
            if (this.dmgCount > 0) {
                damageAmount = damageAmount / (dmgCount * 1.25f);
                if (this.dmgCount % 10 == 0 && !this.world.isRemote) {
                    this.addPotionEffect(PotionTypes.REGENERATION.getEffects().get(0));
                }
                if (!this.world.isRemote && this.rand.nextInt(25) == 0 && damageSrc.getTrueSource() != null) {
                    this.world.spawnEntity(new EntityLightningBolt(this.world, damageSrc.getTrueSource().posX + this.rand.nextInt(5) - 2,
                            damageSrc.getTrueSource().posY, damageSrc.getTrueSource().posZ + this.rand.nextInt(5) - 2, false));
                }
            }
            this.dmgCount++;
        }
        if (this.takumiType().getId() == EnumTakumiType.FIRE.getId() && damageSrc.isFireDamage()) {
            return;
        }
        if (this.takumiType().getId() == EnumTakumiType.WATER.getId() && damageSrc == DamageSource.DROWN) {
            return;
        }
        if (this.takumiType().getId() == EnumTakumiType.GRASS.getId() && damageSrc == DamageSource.FALL) {
            return;
        }
        if (damageSrc == DamageSource.LIGHTNING_BOLT) {
            return;
        }
        if ((damageSrc == DamageSource.IN_WALL || damageSrc == DamageSource.FALL) &&
                this.takumiRank().getLevel() >= 3) {
            return;
        }
        super.damageEntity(damageSrc, damageAmount);
    }

    public static float getTypeMatchFactor(EnumTakumiType attacker, EnumTakumiType damaged) {
        float f = 1f;
        if ((attacker.isDest() && damaged.isMagic()) || (attacker.isMagic() && damaged.isDest())) {
            f *= 1.2;
        }
        if ((attacker.isDest() && damaged.isDest()) || (attacker.isMagic() && damaged.isMagic())) {
            f *= 0.8;
        }
        if (attacker.getId() == EnumTakumiType.GROUND.getId() || damaged.getId() == EnumTakumiType.GROUND.getId()) {
            f *= 1.15;
        }
        if (attacker.getId() == EnumTakumiType.WIND.getId() || damaged.getId() == EnumTakumiType.WIND.getId()) {
            f *= 0.85;
        }
        if ((attacker.getId() == EnumTakumiType.GRASS.getId() && damaged.getId() == EnumTakumiType.WATER.getId()) ||
                attacker.getId() == EnumTakumiType.WATER.getId() && damaged.getId() == EnumTakumiType.FIRE.getId() ||
                (attacker.getId() == EnumTakumiType.FIRE.getId() && damaged.getId() == EnumTakumiType.GRASS.getId())) {
            f *= 1.25;
        }
        if ((damaged.getId() == EnumTakumiType.GRASS.getId() && attacker.getId() == EnumTakumiType.WATER.getId()) ||
                damaged.getId() == EnumTakumiType.WATER.getId() && attacker.getId() == EnumTakumiType.FIRE.getId() ||
                (damaged.getId() == EnumTakumiType.FIRE.getId() && attacker.getId() == EnumTakumiType.GRASS.getId())) {
            f *= 0.75;
        }
        if (damaged.getId() == EnumTakumiType.DRAGON.getId() || damaged.getId() == EnumTakumiType.TAKUMI.getId()) {
            f *= 0.5;
        }
        return f;
    }

    public double getSizeAmp() {
        return 1;
    }

    @Override
    public void onUpdate() {
        if (this.takumiType().getId() == EnumTakumiType.WIND.getId() && this.getAttackTarget() != null) {
            this.addPotionEffect(new PotionEffect(MobEffects.SPEED, 10, 0, true, false));
        } else if (this.takumiType().getId() == EnumTakumiType.GROUND.getId() && this.ticksExisted % 100 == 0) {
            this.heal(1f);
        }
        if (this.getAttackTarget() instanceof EntityAttackBlock) {
            this.getMoveHelper().setMoveTo(this.getAttackTarget().posX, this.getAttackTarget().posY,
                    this.getAttackTarget().posZ, 0.5);
            double x = this.posX - this.getAttackTarget().posX;
            double z = this.posZ - this.getAttackTarget().posZ;
            if (x * x + z * z <= 9) {
                this.ignite();
            }
        }
        if (!this.isNonBoss()) {
            if (this.prevHealth > 1 && this.prevHealth - this.getHealth() > 70) {
                for (int i = 0; i < 100; i++) {
                    EntityLightningBolt bolt = new EntityLightningBolt(this.world, this.posX, this.posY, this.posZ, false);
                    this.world.addWeatherEffect(bolt);
                    this.world.spawnEntity(bolt);
                }

            }
            this.prevHealth = this.getHealth();
        }
        super.onUpdate();
    }

    @Override
    public void onStruckByLightning(EntityLightningBolt lightningBolt) {
        this.heal(0.05f);
        super.onStruckByLightning(lightningBolt);
    }

    @Override
    public void onDeath(DamageSource source) {
        if (!this.world.isRemote) {
            this.dropItem(this.getDropItem(), this.rand.nextInt(3));
            if (this.takumiRank() == EnumTakumiRank.MID && this.rand.nextInt(5) == 0) {
                this.dropItem(Item.getItemFromBlock(TakumiBlockCore.CREEPER_BOMB), 1);
            }
            int drop = this.rand.nextInt(2);
            int i = this.takumiType().getId();
            if (this.rand.nextInt(this.takumiRank() == EnumTakumiRank.LOW ? 15 : 10) == 0 && drop > 0 && i > 0 &&
                    i < 7) {
                this.entityDropItem(new ItemStack(TakumiItemCore.TAKUMI_TYPE_CORE, drop, i - 1), 0f);
            }
            if (this.rand.nextInt(this.takumiRank() == EnumTakumiRank.LOW ? 10 : 5) == 0 && drop > 0) {
                if (this.takumiType().isMagic() && this.rand.nextBoolean()) {
                    this.entityDropItem(new ItemStack(TakumiItemCore.TAKUMI_TYPE_CORE_MAGIC, drop), 0f);
                }
                if (this.takumiType().isDest() && this.rand.nextBoolean()) {
                    this.entityDropItem(new ItemStack(TakumiItemCore.TAKUMI_TYPE_CORE_DEST, drop), 0f);
                }
            }
        }
        super.onDeath(source);
    }

    @Override
    public boolean getCanSpawnHere() {
        if (this.world.provider.getDimensionType() != TakumiWorldCore.TAKUMI_WORLD) {
            return super.getCanSpawnHere();
        } else {
            return this.world.loadedEntityList.size() < 500 && this.world.getDifficulty() != EnumDifficulty.PEACEFUL &&
                    this.world.getBlockState((new BlockPos(this)).down()).canEntitySpawn(this);
        }
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return this.world.provider.getDimensionType() == TakumiWorldCore.TAKUMI_WORLD ? 50 : 5;
    }

    @Override
    protected boolean isValidLightLevel() {
        return this.world.provider.getDimensionType() == TakumiWorldCore.TAKUMI_WORLD || super.isValidLightLevel();
    }

    @Override
    public boolean isImmuneToExplosions() {
        return this.getAttackTarget() instanceof EntityAttackBlock && super.isImmuneToExplosions();
    }
}
