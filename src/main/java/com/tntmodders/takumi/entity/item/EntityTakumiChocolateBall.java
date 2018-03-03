package com.tntmodders.takumi.entity.item;

import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.entity.mobs.EntityShootingCreeper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityTakumiChocolateBall extends EntityThrowable {

    public EntityTakumiChocolateBall(World worldIn) {
        super(worldIn);
    }

    public EntityTakumiChocolateBall(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
    }

    public EntityTakumiChocolateBall(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy) {
        super.setThrowableHeading(x, y, z, velocity, inaccuracy * 5);
    }

    @Override
    public void onUpdate() {
        for (int i = 0; i < 4; ++i) {
            this.world.spawnParticle(EnumParticleTypes.HEART, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
        }
        super.onUpdate();
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    @Override
    protected void onImpact(RayTraceResult result) {
        if (result.entityHit != null && !(result.entityHit instanceof EntityShootingCreeper)) {
            if (result.entityHit instanceof EntityLivingBase) {
                EntityLivingBase entityLivingBase = (EntityLivingBase) result.entityHit;
                if (result.entityHit instanceof EntityLiving) {
                    if (result.entityHit instanceof EntityCreeper && !this.world.isRemote) {
                        EntityCreeper entityCreeper =
                                (EntityCreeper) EntityRegistry.getEntry(result.entityHit.getClass())
                                                              .newInstance(this.world);
                        entityCreeper.copyLocationAndAnglesFrom(result.entityHit);
                        result.entityHit.setDead();
                        this.world.spawnEntity(entityCreeper);
                        entityLivingBase = entityCreeper;
                    }
                    ((EntityLiving) entityLivingBase).setAttackTarget(null);
                }
                if (!(entityLivingBase instanceof EntityPlayer)) {
                    double d1 = this.posX - entityLivingBase.posX;
                    double d0;

                    for (d0 = this.posZ - entityLivingBase.posZ; d1 * d1 + d0 * d0 < 1.0E-4D;
                         d0 = (Math.random() - Math.random()) * 0.01D) {
                        d1 = (Math.random() - Math.random()) * 0.01D;
                    }
                    entityLivingBase.knockBack(this, 2f, d1, d0);
                } else if (this.thrower instanceof EntityShootingCreeper && !this.world.isRemote) {
                    EntityItem item = new EntityItem(this.world, entityLivingBase.posX, entityLivingBase.posY,
                            entityLivingBase.posZ, new ItemStack(TakumiItemCore.TAKUMI_CHOCO_BALL, 1));
                    this.world.spawnEntity(item);
                }
                entityLivingBase.heal(1f);
            }
        }

        if (!this.world.isRemote) {
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, 0, false);
            this.world.setEntityState(this, (byte) 3);
            this.setDead();
        }
    }

    /**
     * Handler for {@link World#setEntityState}
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 3) {
            for (int i = 0; i < 15; ++i) {
                this.world.spawnParticle(EnumParticleTypes.HEART, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}
