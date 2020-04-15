package com.tntmodders.takumi.entity.item;

import com.tntmodders.takumi.core.TakumiItemCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

public class EntityTakumiMinecart extends EntityMinecartEmpty {
    public EntityTakumiMinecart(World worldIn) {
        super(worldIn);
    }

    public EntityTakumiMinecart(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public static EntityMinecart create(World worldIn, double x, double y, double z, EntityMinecart.Type typeIn) {
        return new EntityTakumiMinecart(worldIn, x, y, z);
    }

    @Override
    public Type getType() {
        return Type.RIDEABLE;
    }

    @Override
    public ItemStack getCartItem() {
        return new ItemStack(TakumiItemCore.TAKUMI_MINECART);
    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }

    @Override
    public void killMinecart(DamageSource source) {
        this.setDead();

        if (this.world.getGameRules().getBoolean("doEntityDrops")) {
            ItemStack itemstack = new ItemStack(TakumiItemCore.TAKUMI_MINECART, 1);
            if (this.hasCustomName()) {
                itemstack.setStackDisplayName(this.getCustomNameTag());
            }
            this.entityDropItem(itemstack, 0.0F);
        }
    }

    @Override
    protected boolean canFitPassenger(Entity passenger) {
        return this.getPassengers().size() < 2;
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        if (player.isSneaking()) {
            return false;
        } else {
            if (!this.world.isRemote) {
                player.startRiding(this);
            }
            return true;
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.world.isRemote) {
            AxisAlignedBB box;
            if (getCollisionHandler() != null) {
                box = getCollisionHandler().getMinecartCollisionBox(this);
            } else {
                box = this.getEntityBoundingBox().grow(0.20000000298023224D, 0.0D, 0.20000000298023224D);
            }
            boolean flag = !this.world.isRemote && !(this.getControllingPassenger() instanceof EntityPlayer);
            List<Entity> list = this.world.getEntitiesInAABBexcluding(this, box, EntitySelectors.getTeamCollisionPredicate(this));

            for (Entity entity : list) {
                if (!entity.isPassenger(this)) {
                    if (flag && this.getPassengers().size() < 2 && !entity.isRiding() && entity.width < this.width && entity instanceof EntityLivingBase && !(entity instanceof EntityPlayer)) {
                        entity.startRiding(this);
                    } else {
                        this.applyEntityCollision(entity);
                    }
                }
            }
        }
    }

    @Override
    public void updatePassenger(Entity passenger) {
        if (this.isPassenger(passenger)) {
            if (this.getPassengers().size() == 1) {
                super.updatePassenger(passenger);
            } else {
                double x = (this.posX - this.lastTickPosX) * 2;
                if (x > 0.25) {
                    x = 0.25;
                } else if (x < -0.25) {
                    x = -0.25;
                }
                double y = (this.posY - this.lastTickPosY) * 2;
                if (y > 0.25) {
                    y = 0.25;
                } else if (y < -0.25) {
                    y = -0.25;
                }
                double z = (this.posZ - this.lastTickPosZ) * 2;
                if (z > 0.25) {
                    z = 0.25;
                } else if (z < -0.25) {
                    z = -0.25;
                }

                if (this.getPassengers().indexOf(passenger) == 0) {
                    passenger.setPosition(this.posX + x, this.posY + y + this.getMountedYOffset() + passenger.getYOffset(), this.posZ + z);
                } else {
                    passenger.setPosition(this.posX - x, this.posY - y + this.getMountedYOffset() + passenger.getYOffset(), this.posZ - z);
                }
            }
        }
    }
}
