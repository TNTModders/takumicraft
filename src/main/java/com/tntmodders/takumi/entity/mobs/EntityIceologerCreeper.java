package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.client.render.RenderIceologerCreeper;
import com.tntmodders.takumi.entity.item.EntityIceologerCreeperSpell;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityIceologerCreeper extends EntityAbstractSpellCreeper {

    private EntitySheep wololoTarget;

    public EntityIceologerCreeper(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.95F);
        this.experienceValue = 10;
    }

    public static void summonIceologerSpell(World world, double x, double y, double z) {
        EntityIceologerCreeperSpell spell = new EntityIceologerCreeperSpell(world, x, y, z, 10);
        world.spawnEntity(spell);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();

        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityPlayer.class, 8.0F, 0.6D, 1.0D));
        this.tasks.addTask(5, new AIAttackSpell());
        this.tasks.addTask(8, new EntityAIWander(this, 0.6D));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, EntityIceologerCreeper.class));
        this.targetTasks.addTask(2,
                new EntityAINearestAttackableTarget(this, EntityPlayer.class, true).setUnseenMemoryTicks(300));
        this.targetTasks.addTask(3,
                new EntityAINearestAttackableTarget(this, EntityVillager.class, false).setUnseenMemoryTicks(300));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, false));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.1D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(12.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(12.0D);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_EVOCATION_ILLAGER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.EVOCATION_ILLAGER_DEATH;
    }

    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_EVOCATION_ILLAGER;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        super.onUpdate();
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
    }

    @Override
    protected SoundEvent getSpellSound() {
        return SoundEvents.EVOCATION_ILLAGER_CAST_SPELL;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_EVOCATION_ILLAGER_AMBIENT;
    }

    @Override
    public void takumiExplode() {
    }

    @Override
    public int getExplosionPower() {
        return 0;
    }

    @Override
    public int getSecondaryColor() {
        return 0x008888;
    }

    @Override
    public int getPrimaryColor() {
        return 0x004422;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "iceologercreeper";
    }

    @Override
    public int getRegisterID() {
        return 306;
    }

    @Override
    public Object getRender(RenderManager manager) {
        return new RenderIceologerCreeper(manager);
    }

    class AIAttackSpell extends AIUseSpell {

        private AIAttackSpell() {
            super();
        }

        @Override
        protected int getCastingTime() {
            return 20;
        }

        @Override
        protected int getCastingInterval() {
            return 300;
        }

        @Override
        protected void castSpell() {
            EntityLivingBase entitylivingbase = EntityIceologerCreeper.this.getAttackTarget();
            double d0 = Math.min(entitylivingbase.posY, EntityIceologerCreeper.this.posY);
            double d1 = Math.max(entitylivingbase.posY, EntityIceologerCreeper.this.posY) + 1.0D;
            float f = (float) MathHelper.atan2(entitylivingbase.posZ - EntityIceologerCreeper.this.posZ,
                    entitylivingbase.posX - EntityIceologerCreeper
                            .this.posX);

            if (EntityIceologerCreeper.this.getDistanceSqToEntity(entitylivingbase) < 16.0D) {
                for (int i = 0; i < 5; ++i) {
                    float f1 = f + (float) i * (float) Math.PI * 0.4F;
                    this.spawnFangs(EntityIceologerCreeper.this.posX + (double) MathHelper.cos(f1) * 3D,
                            EntityIceologerCreeper.this.posZ + (double) MathHelper.sin(f1) * 3D, d0, d1, f1, 0);
                }

                for (int k = 0; k < 8; ++k) {
                    float f2 = f + (float) k * (float) Math.PI * 2.0F / 8.0F + (float) Math.PI * 2F / 5F;
                    this.spawnFangs(EntityIceologerCreeper.this.posX + (double) MathHelper.cos(f2) * 7D,
                            EntityIceologerCreeper.this.posZ + (double) MathHelper.sin(f2) * 7D, d0, d1, f2, 3);
                }
            } else {
                for (int l = 0; l < 16; ++l) {
                    double d2 = 2D * (double) (l + 1);
                    this.spawnFangs(EntityIceologerCreeper.this.posX + (double) MathHelper.cos(f) * d2,
                            EntityIceologerCreeper.this.posZ + (double) MathHelper.sin(f) * d2, d0, d1, f, l / 2);
                }
            }
        }

        private void spawnFangs(double p_190876_1_, double p_190876_3_, double p_190876_5_, double p_190876_7_,
                                float p_190876_9_, int p_190876_10_) {
            BlockPos blockpos = new BlockPos(p_190876_1_, p_190876_7_, p_190876_3_);
            boolean flag = false;
            double d0 = 0.0D;

            while (true) {
                if (!EntityIceologerCreeper.this.world.isBlockNormalCube(blockpos, true) &&
                        EntityIceologerCreeper.this.world.isBlockNormalCube(blockpos.down(), true)) {
                    if (!EntityIceologerCreeper.this.world.isAirBlock(blockpos)) {
                        IBlockState iblockstate = EntityIceologerCreeper.this.world.getBlockState(blockpos);
                        AxisAlignedBB axisalignedbb =
                                iblockstate.getCollisionBoundingBox(EntityIceologerCreeper.this.world, blockpos);

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
/*                EntityEvokerCreeperFangs EntityEvokerCreeperfangs =
                        new EntityEvokerCreeperFangs(EntityIceologerCreeper.this.world, p_190876_1_,
                                (double) blockpos.getY() + d0, p_190876_3_, p_190876_9_, p_190876_10_,
                                EntityIceologerCreeper.this);
                EntityIceologerCreeper.this.world.spawnEntity(EntityEvokerCreeperfangs);*/
                EntityIceologerCreeperSpell spell = new EntityIceologerCreeperSpell(EntityIceologerCreeper.this.world, p_190876_1_,
                        (double) blockpos.getY() + d0 + 5, p_190876_3_, p_190876_10_ * 10 + 20);
                EntityIceologerCreeper.this.world.spawnEntity(spell);
            }
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOCATION_ILLAGER_PREPARE_ATTACK;
        }

        @Override
        protected SpellType getSpellType() {
            return SpellType.ICE;
        }
    }
}
