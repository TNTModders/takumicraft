package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.client.render.RenderEndermiteCreeper;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.Random;

public class EntityEndermiteCreeper extends EntityTakumiAbstractCreeper {

    public EntityEndermiteCreeper(World worldIn) {
        super(worldIn);
        this.setSize(0.4F, 0.3F);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAICreeperSwell(this));
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(3, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_ENDERMITE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ENDERMITE_DEATH;
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_ENDERMITE;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ENDERMITE_AMBIENT;
    }

    @Nullable
    @Override
    protected Item getDropItem() {
        return Items.ENDER_EYE;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(SoundEvents.ENTITY_ENDERMITE_STEP, 0.15F, 1.0F);
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public double getYOffset() {
        return 0.1D;
    }

    @Override
    public float getEyeHeight() {
        return 0.1F;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (this.world.isRemote) {
            for (int i = 0; i < 2; ++i) {
                this.world.spawnParticle(EnumParticleTypes.PORTAL,
                        this.posX + (this.rand.nextDouble() - 0.5D) * this.width,
                        this.posY + this.rand.nextDouble() * this.height,
                        this.posZ + (this.rand.nextDouble() - 0.5D) * this.width,
                        (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(),
                        (this.rand.nextDouble() - 0.5D) * 2.0D);
            }
        }
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.ARTHROPOD;
    }

    @Override
    public void setRenderYawOffset(float offset) {
        this.rotationYaw = offset;
        super.setRenderYawOffset(offset);
    }

    @Override
    public void takumiExplode() {
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.WIND_M;
    }

    @Override
    public int getExplosionPower() {
        return 4;
    }

    @Override
    public int getSecondaryColor() {
        return 0x440066;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "endermitecreeper";
    }

    @Override
    public int getRegisterID() {
        return 241;
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        for (Entity entity : event.getAffectedEntities()) {
            if (entity instanceof EntityLivingBase) {
                boolean done = false;
                for (int i = 0; i < 50 && !done; i++) {
                    done = this.teleportTo((EntityLivingBase) entity);
                }
            }
        }
        return true;
    }

    protected boolean teleportTo(EntityLivingBase entity) {
        Random rand = new Random();
        int distance = 128;
        double x = entity.posX + (rand.nextDouble() - 0.5D) * distance;
        double y = entity.posY + rand.nextInt(distance + 1) - distance / 2;
        double z = entity.posZ + (rand.nextDouble() - 0.5D) * distance;
        EnderTeleportEvent event = new EnderTeleportEvent(entity, x, y, z, 0);

        double d3 = entity.posX;
        double d4 = entity.posY;
        double d5 = entity.posZ;
        entity.posX = event.getTargetX();
        entity.posY = event.getTargetY();
        entity.posZ = event.getTargetZ();

        int xInt = MathHelper.floor(entity.posX);
        int yInt = MathHelper.floor(entity.posY);
        int zInt = MathHelper.floor(entity.posZ);

        boolean flag = false;
        if (entity.world.isAirBlock(new BlockPos(xInt, yInt, zInt))) {

            boolean foundGround = false;
            while (!foundGround && yInt > 0) {
                BlockPos pos = new BlockPos(xInt, yInt - 1, zInt);
                IBlockState block = entity.world.getBlockState(pos);
                if (block.getMaterial().blocksMovement() && entity.world.isAirBlock(pos.up(20)) &&
                        entity.world.isAirBlock(pos.up(21))) {
                    foundGround = true;
                } else {
                    --entity.posY;
                    --yInt;
                }
            }
            boolean flg = true;
            try {
                Method method = EntityLivingBase.class.getDeclaredMethod("isMovementBlocked");
                method.setAccessible(true);
                flg = !(Boolean) method.invoke(entity);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (foundGround && flg) {
                entity.setPosition(entity.posX, entity.posY + 20, entity.posZ);
                if (entity.world.getCollisionBoxes(entity, entity.getEntityBoundingBox()).isEmpty() &&
                        !entity.world.containsAnyLiquid(entity.getEntityBoundingBox())) {
                    flag = true;
                }
            }
        }

        if (!flag) {
            entity.setPosition(d3, d4, d5);
            return false;
        }

        entity.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);

        short short1 = 128;
        for (int l = 0; l < short1; ++l) {
            double d6 = l / (short1 - 1.0D);
            float f = (rand.nextFloat() - 0.5F) * 0.2F;
            float f1 = (rand.nextFloat() - 0.5F) * 0.2F;
            float f2 = (rand.nextFloat() - 0.5F) * 0.2F;
            double d7 = d3 + (entity.posX - d3) * d6 + (rand.nextDouble() - 0.5D) * entity.width * 2.0D;
            double d8 = d4 + (entity.posY - d4) * d6 + rand.nextDouble() * entity.height;
            double d9 = d5 + (entity.posZ - d5) * d6 + (rand.nextDouble() - 0.5D) * entity.width * 2.0D;
            entity.world.spawnParticle(EnumParticleTypes.PORTAL, d7, d8, d9, f, f1, f2);
        }

        entity.world.playSound(null, entity.prevPosX, entity.prevPosY, entity.prevPosZ,
                SoundEvents.ENTITY_ENDERMEN_TELEPORT, entity.getSoundCategory(), 1.0F, 1.0F);
        entity.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
        return true;

    }

    @Override
    public int getPrimaryColor() {
        return 4128831;
    }

    @Override
    public Object getRender(RenderManager manager) {
        return new RenderEndermiteCreeper<>(manager);
    }

    @Override
    public void onUpdate() {
        this.renderYawOffset = this.rotationYaw;
        super.onUpdate();
    }
}
