package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.client.render.RenderPiglinCreeper;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityPiglinCreeper extends EntityTakumiAbstractCreeper {

    public EntityPiglinCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.6);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(100);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1000);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(0, new EntityAIBase() {
            private EntityItem item;
            private EntityPiglinCreeper creeper;
            private double movePosX;
            private double movePosY;
            private double movePosZ;

            @Override
            public boolean shouldExecute() {
                this.creeper = null;
                if (!EntityPiglinCreeper.this.world.getEntitiesWithinAABB(EntityItem.class, EntityPiglinCreeper.this.getEntityBoundingBox().grow(10)).isEmpty()) {
                    EntityPiglinCreeper.this.world.getEntitiesWithinAABB(EntityItem.class, EntityPiglinCreeper.this.getEntityBoundingBox().grow(10)).forEach(entityItem -> {
                        if (entityItem.getItem().getItem() == Items.GOLD_INGOT || entityItem.getItem().getItem() == Item.getItemFromBlock(Blocks.GOLD_BLOCK)) {
                            this.movePosX = entityItem.posX;
                            this.movePosY = entityItem.posY;
                            this.movePosZ = entityItem.posZ;
                            this.creeper = EntityPiglinCreeper.this;
                        }
                    });
                }
                return this.creeper != null;
            }

            @Override
            public boolean shouldContinueExecuting() {
                return !this.creeper.getNavigator().noPath();
            }

            @Override
            public void startExecuting() {
                this.creeper.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, 1);
            }
        });
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            this.world.loadedEntityList.forEach(entity -> {
                if (entity instanceof EntityPigZombie && entity.getDistanceSqToEntity(this) < 1000 &&
                        this.getAttackTarget() != null) {
                    entity.attackEntityFrom(DamageSource.causeMobDamage(this.getAttackTarget()), 0);
                    ((EntityLiving) entity).setAttackTarget(this.getAttackTarget());
                }
            });
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.FIRE_M;
    }

    @Override
    public int getExplosionPower() {
        return 6;
    }

    @Override
    public int getSecondaryColor() {
        return 0x668866;
    }

    @Override
    public boolean isCustomSpawn() {
        return true;
    }

    @Override
    public String getRegisterName() {
        return "piglincreeper";
    }

    @Override
    public int getRegisterID() {
        return 307;
    }

    @Override
    public void customSpawn() {
        EntityRegistry.addSpawn(this.getClass(), this.takumiRank().getSpawnWeight() * 2, 3, 10,
                EnumCreatureType.MONSTER, Biomes.HELL);
    }

    @Override
    public int getPrimaryColor() {
        return 0x002200;
    }

    @Override
    public Object getRender(RenderManager manager) {
        return new RenderPiglinCreeper<>(manager);
    }

    @Override
    public void onDeath(DamageSource source) {
        if (!this.world.isRemote && this.world.getGameRules().getBoolean("doMobLoot")) {
            this.dropItem(Item.getItemFromBlock(Blocks.GOLD_BLOCK), 1);
            if (!this.getHeldItemMainhand().isEmpty()) {
                this.entityDropItem(this.getHeldItemMainhand(), 0f);
            }
        }
        super.onDeath(source);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.getAttackTarget() != null && this.getAttackTarget() != this.getLastAttackedEntity()) {
            this.getAttackTarget().getArmorInventoryList().forEach(itemStack -> {
                if (itemStack.getItem() instanceof ItemArmor && ((ItemArmor) itemStack.getItem()).getArmorMaterial() == ItemArmor.ArmorMaterial.GOLD) {
                    EntityPiglinCreeper.this.setAttackTarget(null);
                    EntityPiglinCreeper.this.setCreeperState(-2);
                    if (!this.world.isRemote) {
                        EntityPiglinCreeper.this.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS));
                    }
                }
            });
        }
        if (!this.world.getEntitiesWithinAABB(EntityItem.class, this.getEntityBoundingBox().grow(10)).isEmpty()) {
            this.world.getEntitiesWithinAABB(EntityItem.class, this.getEntityBoundingBox().grow(10)).forEach(entityItem -> {
                if (entityItem.getItem().getItem() == Items.GOLD_INGOT || entityItem.getItem().getItem() == Item.getItemFromBlock(Blocks.GOLD_BLOCK)) {
                    EntityPiglinCreeper.this.setAttackTarget(null);
                    EntityPiglinCreeper.this.setCreeperState(-2);
                    /*this.getMoveHelper().setMoveTo(entityItem.posX, entityItem.posY, entityItem.posZ, 1f);
                    this.getNavigator().tryMoveToXYZ(entityItem.posX, entityItem.posY, entityItem.posZ, 1f);*/
                    if (!this.world.isRemote) {
                        EntityPiglinCreeper.this.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS));
                    }
                    if (this.getDistanceSqToEntity(entityItem) < 1.5 * 1.5) {
                        if (this.getHeldItemMainhand().isEmpty()) {
                            this.setHeldItem(EnumHand.MAIN_HAND, entityItem.getItem());
                        }
                        entityItem.setDead();
                    }
                }
            });
        }
        if (this.getActivePotionEffect(MobEffects.BLINDNESS) != null) {
            this.setCreeperState(-2);
            this.setAttackTarget(null);
        }
    }

    @Override
    public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState
            blockStateIn) {
        return blockStateIn.getBlockHardness(worldIn, pos) == -1 ? 10000000f : super.getExplosionResistance(explosionIn, worldIn, pos, blockStateIn) / 3;
    }
}
