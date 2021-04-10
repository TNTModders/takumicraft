package com.tntmodders.takumi.entity;

import com.tntmodders.takumi.entity.mobs.noncreeper.EntityTheUnknown;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityUnknownLay extends Entity {

    public EntityUnknownLay(World worldIn) {
        super(worldIn);
        this.setSize(1f, 1f);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.ticksExisted < 30) {
            for (EntityLivingBase entitylivingbase : this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(0.2D, 0.0D, 0.2D))) {
                if (!(entitylivingbase instanceof EntityTheUnknown)) {
                    entitylivingbase.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 40, 1));
                }
            }
        } else if (this.ticksExisted < 40) {
            if (!this.world.isRemote) {
                for (EntityLivingBase entitylivingbase : this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(0.2D, 0.0D, 0.2D))) {
                    if (!(entitylivingbase instanceof EntityTheUnknown)) {
                        entitylivingbase.attackEntityFrom(DamageSource.OUT_OF_WORLD, 20f);
                    }
                }
            } else {
                for (int i = 0; i < 2; ++i) {
                    this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width, this.posY + this.rand.nextDouble() * (double) this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D);
                }
                this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
            }
        } else {
            this.setDead();
        }
    }


    @Override
    protected void entityInit() {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
    }

    public static class EntityUnknownLayEx extends EntityUnknownLay {

        public EntityUnknownLayEx(World worldIn) {
            super(worldIn);
            this.setSize(5f, 5f);
        }
    }
}
