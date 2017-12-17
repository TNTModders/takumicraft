package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.PooledMutableBlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityLapisCreeper extends EntityTakumiAbstractCreeper {
    
    public EntityLapisCreeper(World worldIn) {
        super(worldIn);
        this.moveHelper = new AIMoveControl(this);
    }
    
    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAICreeperSwell(this));
        this.tasks.addTask(2, new AIMoveRandom());
        this.tasks.addTask(3, new EntityAIAvoidEntity <>(this, EntityOcelot.class, 6.0F, 1.0D, 1.2D));
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget <>(this, EntityPlayer.class, true));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.15);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(128);
    }
    
    @Override
    public void onUpdate() {
        this.noClip = true;
        super.onUpdate();
        this.noClip = false;
        this.setNoGravity(true);
    }
    
    @Override
    public void onDeath(DamageSource source) {
        if (!this.world.isRemote) {
            this.dropItem(Item.getItemFromBlock(Blocks.LAPIS_BLOCK), 1);
        }
        super.onDeath(source);
    }
    
    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        if (damageSrc != DamageSource.FALL) {
            super.damageEntity(damageSrc, damageAmount);
        }
    }
    
    @Override
    public int getPrimaryColor() {
        return 0x9090a0;
    }
    
    @Override
    public void move(MoverType type, double x, double y, double z) {
        super.move(type, x, y, z);
        this.doBlockCollisions();
    }
    
    @Override
    protected void doBlockCollisions() {
        AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
        PooledMutableBlockPos blockpos$pooledmutableblockpos = PooledMutableBlockPos.retain(axisalignedbb.minX + 0.001D, axisalignedbb.minY +
                0.001D, axisalignedbb.minZ + 0.001D);
        PooledMutableBlockPos blockpos$pooledmutableblockpos1 = PooledMutableBlockPos.retain(axisalignedbb.maxX - 0.001D, axisalignedbb.maxY -
                0.001D, axisalignedbb.maxZ - 0.001D);
        PooledMutableBlockPos blockpos$pooledmutableblockpos2 = PooledMutableBlockPos.retain();
        
        if (this.world.isAreaLoaded(blockpos$pooledmutableblockpos, blockpos$pooledmutableblockpos1)) {
            for (int i = blockpos$pooledmutableblockpos.getX(); i <= blockpos$pooledmutableblockpos1.getX(); ++i) {
                for (int j = blockpos$pooledmutableblockpos.getY(); j <= blockpos$pooledmutableblockpos1.getY(); ++j) {
                    for (int k = blockpos$pooledmutableblockpos.getZ(); k <= blockpos$pooledmutableblockpos1.getZ(); ++k) {
                        blockpos$pooledmutableblockpos2.setPos(i, j, k);
                        IBlockState iblockstate = this.world.getBlockState(blockpos$pooledmutableblockpos2);
                        
                        try {
                            iblockstate.getBlock().onEntityCollidedWithBlock(this.world, blockpos$pooledmutableblockpos2, iblockstate, this);
                            this.onInsideBlock(iblockstate);
                        } catch (Throwable throwable) {
                            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Colliding entity with block");
                            CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being collided with");
                            CrashReportCategory.addBlockInfo(crashreportcategory, blockpos$pooledmutableblockpos2, iblockstate);
                            throw new ReportedException(crashreport);
                        }
                    }
                }
            }
        }
        
        blockpos$pooledmutableblockpos.release();
        blockpos$pooledmutableblockpos1.release();
        blockpos$pooledmutableblockpos2.release();
    }
    
    @Override
    public void onLivingUpdate() {
        if (this.world.isRemote) {
            for (int i = 0; i < 2; ++i) {
                this.world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width,
                        this.posY - 1 + this.rand.nextDouble() * (double) this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this
                                .width, 0.0D, 0.0D, 0.0D);
            }
        }
        super.onLivingUpdate();
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
        return EnumTakumiType.WIND;
    }
    
    @Override
    public int getExplosionPower() {
        return 5;
    }
    
    @Override
    public int getSecondaryColor() {
        return 0x7777ff;
    }
    
    @Override
    public boolean isCustomSpawn() {
        return false;
    }
    
    @Override
    public String getRegisterName() {
        return "lapiscreeper";
    }
    
    @Override
    public int getRegisterID() {
        return 214;
    }
    
    class AIMoveControl extends EntityMoveHelper {
        
        public AIMoveControl(EntityLapisCreeper vex) {
            super(vex);
        }
        
        @Override
        public void onUpdateMoveHelper() {
            if (this.action == Action.MOVE_TO) {
                double d0 = this.posX - EntityLapisCreeper.this.posX;
                double d1 = this.posY - EntityLapisCreeper.this.posY;
                double d2 = this.posZ - EntityLapisCreeper.this.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                d3 = (double) MathHelper.sqrt(d3);
                
                if (d3 < EntityLapisCreeper.this.getEntityBoundingBox().getAverageEdgeLength()) {
                    this.action = Action.WAIT;
                    EntityLapisCreeper.this.motionX *= 0.5D;
                    EntityLapisCreeper.this.motionY *= 0.5D;
                    EntityLapisCreeper.this.motionZ *= 0.5D;
                } else {
                    EntityLapisCreeper.this.motionX += d0 / d3 * 0.05D * this.speed * 0.5;
                    EntityLapisCreeper.this.motionY += d1 / d3 * 0.05D * this.speed * 0.5;
                    EntityLapisCreeper.this.motionZ += d2 / d3 * 0.05D * this.speed * 0.5;
                    
                    if (EntityLapisCreeper.this.getAttackTarget() == null) {
                        EntityLapisCreeper.this.rotationYaw = -((float) MathHelper.atan2(EntityLapisCreeper.this.motionX, EntityLapisCreeper.this
                                .motionZ)) * (180F / (float) Math.PI);
                        EntityLapisCreeper.this.renderYawOffset = EntityLapisCreeper.this.rotationYaw;
                    } else {
                        double d4 = EntityLapisCreeper.this.getAttackTarget().posX - EntityLapisCreeper.this.posX;
                        double d5 = EntityLapisCreeper.this.getAttackTarget().posZ - EntityLapisCreeper.this.posZ;
                        EntityLapisCreeper.this.rotationYaw = -((float) MathHelper.atan2(d4, d5)) * (180F / (float) Math.PI);
                        EntityLapisCreeper.this.renderYawOffset = EntityLapisCreeper.this.rotationYaw;
                    }
                }
            }
        }
    }
    
    
    class AIMoveRandom extends EntityAIBase {
        
        public AIMoveRandom() {
            this.setMutexBits(1);
        }
        
        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        @Override
        public boolean shouldExecute() {
            return !EntityLapisCreeper.this.getMoveHelper().isUpdating() && EntityLapisCreeper.this.rand.nextInt(7) == 0;
        }
        
        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        @Override
        public boolean shouldContinueExecuting() {
            return false;
        }
        
        /**
         * Keep ticking a continuous task that has already been started
         */
        @Override
        public void updateTask() {
            BlockPos blockpos = new BlockPos(EntityLapisCreeper.this);
            for (int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.add(EntityLapisCreeper.this.rand.nextInt(15) - 7, EntityLapisCreeper.this.rand.nextInt(11) - 5,
                        EntityLapisCreeper.this.rand.nextInt(15) - 7);
                if (EntityLapisCreeper.this.world.isAirBlock(blockpos1)) {
                    EntityLapisCreeper.this.moveHelper.setMoveTo((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double)
                            blockpos1.getZ() + 0.5D, 0.25D);
                    if (EntityLapisCreeper.this.getAttackTarget() == null) {
                        EntityLapisCreeper.this.getLookHelper().setLookPosition((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D,
                                (double) blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    
                    break;
                }
            }
        }
    }
}
