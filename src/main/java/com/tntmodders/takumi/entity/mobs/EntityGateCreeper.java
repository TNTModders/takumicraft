package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.ai.EntityAIAttackRangedTNT;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

public class EntityGateCreeper extends EntityTakumiAbstractCreeper implements IRangedAttackMob {

    public EntityGateCreeper(World worldIn) {
        super(worldIn);
    }


    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAIAttackRangedTNT<>(this, 1, 20, 15));
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
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityIronGolem.class, true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.1D);
    }

    /**
     * Attack the specified entity using a ranged attack.
     */
    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
        for (int i = 0; i < (this.getPowered() ? 2 : 1); i++) {
            EntityTNTPrimed tntPrimed = new EntityTNTPrimed(this.world);
            tntPrimed.setPosition(this.posX + MathHelper.nextFloat(this.rand, -3, 3), this.posY + 2,
                    this.posZ + MathHelper.nextFloat(this.rand, -3, 3));
            double d0 = target.posX - this.posX;
            double d1 = target.getEntityBoundingBox().minY + (double) (target.height / 3.0F) - tntPrimed.posY;
            double d2 = target.posZ - this.posZ;
            double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
            this.setThrowableHeading(tntPrimed, d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F,
                    (float) (14 - this.world.getDifficulty().getDifficultyId() * 4));
            this.world.spawnEntity(tntPrimed);
        }
    }

    private void setThrowableHeading(EntityTNTPrimed tntPrimed, double x, double y, double z, float velocity,
            float inaccuracy) {
        float f = MathHelper.sqrt(x * x + y * y + z * z);
        x = x / (double) f;
        y = y / (double) f;
        z = z / (double) f;
        x = x + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
        y = y + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
        z = z + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
        x = x * (double) velocity;
        y = y * (double) velocity;
        z = z * (double) velocity;
        tntPrimed.motionX = x;
        tntPrimed.motionY = y;
        tntPrimed.motionZ = z;
        float f1 = MathHelper.sqrt(x * x + z * z);
        tntPrimed.rotationYaw = (float) (MathHelper.atan2(x, z) * (180D / Math.PI));
        tntPrimed.rotationPitch = (float) (MathHelper.atan2(y, (double) f1) * (180D / Math.PI));
        tntPrimed.prevRotationYaw = tntPrimed.rotationYaw;
        tntPrimed.prevRotationPitch = tntPrimed.rotationPitch;
    }

    @Override
    public void setSwingingArms(boolean swingingArms) {
    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            for (int i = 0; i < 10; i++) {
                this.dropItemWithOffset(Items.GUNPOWDER, this.rand.nextInt(16) + 1, 2);
            }
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.FIRE;
    }

    @Override
    public int getExplosionPower() {
        return 3;
    }

    @Override
    public int getSecondaryColor() {
        return 0xff2200;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "gatecreeper";
    }

    @Override
    public int getRegisterID() {
        return 263;
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        event.getAffectedEntities().removeIf(entity -> entity instanceof EntityItem);
        return true;
    }
}
