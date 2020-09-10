package com.tntmodders.takumi.entity.item;

import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.core.TakumiPacketCore;
import com.tntmodders.takumi.network.MessageMSMove;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class EntityXMS extends EntityFlying {

    private static final DataParameter<Boolean> ATTACK_MODE =
            EntityDataManager.createKey(EntityXMS.class, DataSerializers.BOOLEAN);
    public int attackModeTick;

    public EntityXMS(World worldIn) {
        super(worldIn);
        this.setSize(4, 2);
    }

    public Boolean getAttackMode() {
        return this.dataManager.get(ATTACK_MODE);
    }

    public void setAttackMode(Boolean flg) {
        this.dataManager.set(ATTACK_MODE, flg);
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
    }

    @Override
    public void travel(float p_191986_1_, float p_191986_2_, float p_191986_3_) {
        if (this.isInWater()) {
            this.moveRelative(p_191986_1_, p_191986_2_, p_191986_3_, 0.02F);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.800000011920929D;
            this.motionY *= 0.800000011920929D;
            this.motionZ *= 0.800000011920929D;
        } else if (this.isInLava()) {
            this.moveRelative(p_191986_1_, p_191986_2_, p_191986_3_, 0.02F);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.5D;
            this.motionY *= 0.5D;
            this.motionZ *= 0.5D;
        } else {
            float f = 0.91F;

/*            if (this.onGround) {
                BlockPos underPos = new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ));
                IBlockState underState = this.world.getBlockState(underPos);
                f = underState.getBlock().getSlipperiness(underState, this.world, underPos, this) * 0.91F;
            }*/

            float f1 = 0.16277136F / (f * f * f);
            this.moveRelative(p_191986_1_, p_191986_2_, p_191986_3_, this.onGround ? 0.1F * f1 : 0.02F);
            f = 0.91F;

            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= f;
            this.motionY *= f;
            this.motionZ *= f;
        }

        this.prevLimbSwingAmount = this.limbSwingAmount;
        double d1 = this.posX - this.prevPosX;
        double d0 = this.posZ - this.prevPosZ;
        float f2 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;

        if (f2 > 1.0F) {
            f2 = 1.0F;
        }

        this.limbSwingAmount += (f2 - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;
    }

    /**
     * returns true if this entity is by a ladder, false otherwise
     */
    @Override
    public boolean isOnLadder() {
        return false;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(ATTACK_MODE, false);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.taskEntries.clear();
        this.targetTasks.taskEntries.clear();
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.getControllingPassenger() instanceof EntityPlayer) {
            if (FMLCommonHandler.instance().getSide().isClient()) {
                if (this.getControllingPassenger() instanceof EntityPlayerSP) {
                    this.clientUpdate();
                }
                if (this.getAttackMode() && this.attackModeTick < 20) {
                    this.attackModeTick++;
                } else if (this.attackModeTick > 0) {
                    this.attackModeTick--;
                }
            }
            this.rotationYaw = this.rotationYawHead = ((EntityPlayer) this.getControllingPassenger()).rotationYawHead;
            this.rotationPitch = this.getControllingPassenger().rotationPitch;
        }
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    public boolean processInteract(EntityPlayer entityPlayer, EnumHand hand) {
        if (!this.getPassengers().isEmpty() && this.getPassengers().stream().anyMatch(entity -> entity == entityPlayer)) {
            if (!this.world.isRemote && this.getAttackMode()) {
                EntityMSRazer razer =
                        new EntityMSRazer(entityPlayer.world, ((EntityXMS) entityPlayer.getRidingEntity()));
                razer.setHeadingFromThrower(entityPlayer.getRidingEntity(),
                        entityPlayer.getRidingEntity().rotationPitch / 2.5f,
                        entityPlayer.getRidingEntity().rotationYaw + 5, 0, 10f, 0f);
                entityPlayer.world.spawnEntity(razer);
                //entityPlayer.world.updateEntities();
                razer = new EntityMSRazer(entityPlayer.world, ((EntityXMS) entityPlayer.getRidingEntity()));
                razer.setHeadingFromThrower(entityPlayer.getRidingEntity(),
                        entityPlayer.getRidingEntity().rotationPitch / 2.5f,
                        entityPlayer.getRidingEntity().rotationYaw - 5, 0, 10f, 0f);
                entityPlayer.world.spawnEntity(razer);
                //entityPlayer.world.updateEntities();
            }
        } else {
            if (!this.world.isRemote && this.getPassengers().isEmpty()) {
                entityPlayer.startRiding(this, true);
            }
        }
        return true;
    }

    @SideOnly(Side.CLIENT)
    private void clientUpdate() {
        if (((EntityPlayerSP) this.getControllingPassenger()).movementInput.forwardKeyDown) {
            TakumiPacketCore.INSTANCE.sendToServer(new MessageMSMove((byte) (this.getAttackMode() ? 0 : 1)));
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.getControllingPassenger() == null && source.getTrueSource() instanceof EntityPlayer &&
                !source.isExplosion()) {
            if (!this.world.isRemote) {
                EntityItem item = new EntityItem(this.world, this.posX, this.posY, this.posZ,
                        new ItemStack(TakumiItemCore.TAKUMI_XMS, 1));
                this.world.spawnEntity(item);
            }
            this.setDead();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onDeath(DamageSource cause) {
        if (!this.world.isRemote) {
            this.world.newExplosion(this, this.posX, this.posY, this.posZ, 8f, true, true);
        }
        super.onDeath(cause);
    }

    @Override
    protected boolean isMovementBlocked() {
        return true;
    }

    @Override
    public void jump() {
        super.jump();
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    protected void doWaterSplashEffect() {
        super.doWaterSplashEffect();
    }

    @Override
    public double getMountedYOffset() {
        return this.height * 0.2;
    }

    @Override
    public boolean shouldDismountInWater(Entity rider) {
        return false;
    }

    @Nullable
    @Override
    public Entity getControllingPassenger() {
        if (this.getPassengers().isEmpty()) {
            return null;
        }
        return this.getPassengers().get(0);
    }

    @Override
    protected float getSoundVolume() {
        return 0f;
    }
}
