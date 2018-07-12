package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.client.render.RenderCannonCreeper;
import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.ai.EntityAIAttackRangedCannon;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class EntityCannonCreeper extends EntityTakumiAbstractCreeper implements IRangedAttackMob {

    private final EntityAIAttackRangedCannon<EntityCannonCreeper> aiArrowAttack =
            new EntityAIAttackRangedCannon<>(this, 1.0D, 20, 15.0F);
    private final EntityAIAttackMelee aiAttackOnCollide = new EntityAIAttackMelee(this, 1.2D, false) {
        @Override
        public void startExecuting() {
            super.startExecuting();
            EntityCannonCreeper.this.setSwingingArms(true);
        }

        @Override
        public void resetTask() {
            super.resetTask();
            EntityCannonCreeper.this.setSwingingArms(false);
        }
    };

    public EntityCannonCreeper(World worldIn) {
        super(worldIn);
        this.setSize(1F, 1F);
        this.setCombatTask();
    }

    /**
     * sets this entity's combat AI.
     */
    public void setCombatTask() {
        if (this.world != null && !this.world.isRemote) {
            this.tasks.removeTask(this.aiAttackOnCollide);
            this.tasks.removeTask(this.aiArrowAttack);
            ItemStack itemstack = this.getHeldItemMainhand();
            int i = 20;
            this.aiArrowAttack.setAttackCooldown(i);
            this.tasks.addTask(1, this.aiArrowAttack);
        }
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAICreeperSwell(this));
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIRestrictSun(this));
        this.tasks.addTask(3, new EntityAIFleeSun(this, 1.0D));
        this.tasks.addTask(3, new EntityAIAvoidEntity<>(this, EntityWolf.class, 6.0F, 1.0D, 1.2D));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setCombatTask();
    }


    @SideOnly(Side.CLIENT)
    @Override
    public Object getRender(RenderManager manager) {
        return new RenderCannonCreeper<>(manager);
    }

    /**
     * Handles updating while riding another entity
     */
    @Override
    public void updateRidden() {
        super.updateRidden();

        if (this.getRidingEntity() instanceof EntityCreature) {
            EntityCreature entitycreature = (EntityCreature) this.getRidingEntity();
            this.renderYawOffset = entitycreature.renderYawOffset;
        }
    }

    /**
     * Attack the specified entity using a ranged attack.
     */
    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
        EntityArrow entityarrow = this.getArrow(distanceFactor);
        double d0 = target.posX - this.posX;
        double d1 = target.getEntityBoundingBox().minY + (double) (target.height / 3.0F) - entityarrow.posY;
        double d2 = target.posZ - this.posZ;
        double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
        entityarrow.setThrowableHeading(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F,
                (float) (14 - this.world.getDifficulty().getDifficultyId() * 4));
        this.world.createExplosion(this, this.posX, this.posY, this.posZ, 0f, false);
        this.world.spawnEntity(entityarrow);
    }

    protected EntityArrow getArrow(float v) {
        return TakumiItemCore.TAKUMI_ARROW_SAN.createArrow(this.world, new ItemStack(TakumiItemCore.TAKUMI_ARROW_SAN),
                this);
    }


    @Override
    public void setSwingingArms(boolean swingingArms) {
    }


    /**
     * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
     * when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
     */
    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty,
            @Nullable
                    IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setCombatTask();
        return livingdata;
    }

    @Override
    public void takumiExplode() {
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL;
    }

    @Override
    public int getExplosionPower() {
        return 3;
    }

    @Override
    public int getSecondaryColor() {
        return 0x00ffff;
    }

    @Override
    public boolean isCustomSpawn() {
        return true;
    }

    @Override
    public String getRegisterName() {
        return "cannoncreeper";
    }

    @Override
    public int getRegisterID() {
        return 61;
    }
}
