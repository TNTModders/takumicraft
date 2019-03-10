package com.tntmodders.takumi.entity.mobs.noncreeper;

import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityBoneDummy extends EntityCreeper {
    String name;

    public EntityBoneDummy(World worldIn) {
        super(worldIn);
        this.setSize(1, 1);
        this.setCustomNameTag(TakumiUtils.takumiTranslate("entity.bonecreeperdummy.tag"));
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return null;
    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }

    @Override
    protected void initEntityAI() {
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.setGlowing(true);
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        this.rotationYawHead = 0;
        EntityPlayer entity = this.world.getNearestPlayerNotCreative(this, 5);
        if (entity != null && entity.getHealth() > 0) {
            NBTTagCompound compound = new NBTTagCompound();
            this.writeEntityToNBT(compound);
            compound.setByte("ExplosionRadius", ((byte) 8));
            this.readEntityFromNBT(compound);
            this.setCreeperState(1);
            this.setCustomNameTag(TakumiUtils.takumiTranslate("entity.bonecreeper.name"));
        }
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    protected void setRotation(float yaw, float pitch) {
    }

    @Override
    public void move(MoverType type, double x, double y, double z) {
    }

    @Override
    public void applyEntityCollision(Entity entityIn) {
        if (!this.isRidingSameEntity(entityIn)) {
            if (!entityIn.noClip && !this.noClip) {
                double d0 = entityIn.posX - this.posX;
                double d1 = entityIn.posZ - this.posZ;
                double d2 = MathHelper.absMax(d0, d1);

                if (d2 >= 0.009999999776482582D) {
                    d2 = MathHelper.sqrt(d2);
                    d0 = d0 / d2;
                    d1 = d1 / d2;
                    double d3 = 1.0D / d2;

                    if (d3 > 1.0D) {
                        d3 = 1.0D;
                    }

                    d0 = d0 * d3;
                    d1 = d1 * d3;
                    d0 = d0 * 0.05000000074505806D;
                    d1 = d1 * 0.05000000074505806D;
                    d0 = d0 * (1.0F - this.entityCollisionReduction);
                    d1 = d1 * (1.0F - this.entityCollisionReduction);

/*                    if (!this.isBeingRidden()) {
                        //this.addVelocity(-d0, 0.0D, -d1);
                    }*/

                    if (!entityIn.isBeingRidden()) {
                        entityIn.addVelocity(d0, 0.0D, d1);
                    }
                }
            }
        }
    }

    @Override
    public boolean isPushedByWater() {
        return false;
    }

    @Override
    public EnumPushReaction getPushReaction() {
        return EnumPushReaction.IGNORE;
    }
}
