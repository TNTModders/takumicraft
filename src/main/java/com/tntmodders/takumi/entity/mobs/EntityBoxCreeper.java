package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.client.render.RenderBoxCreeper;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.EntityAICreeperSwell;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class EntityBoxCreeper extends EntityTakumiAbstractCreeper {

    public EntityBoxCreeper(World worldIn) {
        super(worldIn);
        this.setSize(1, 1);
    }

    @Override
    public Object getRender(RenderManager manager) {
        return new RenderBoxCreeper<>(manager);
    }

    @Override
    public int getPrimaryColor() {
        return 0x00bbbb;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(2, new EntityAICreeperSwell(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        this.rotationYawHead = 0;
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
        return EnumPushReaction.BLOCK;
    }

    @Override
    public void takumiExplode() {
        List<BlockPos> posList = new ArrayList<>();
        this.world.loadedTileEntityList.forEach(tileEntity -> {
            if (tileEntity instanceof IInventory &&
                    tileEntity.getDistanceSq(this.posX, this.posY, this.posZ) < (this.getPowered() ? 2500 : 1000)) {
                posList.add(tileEntity.getPos());
            }
        });
        if (!posList.isEmpty()) {
            posList.forEach(
                    pos -> this.world.createExplosion(this, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                            (this.getPowered() ? this.getExplosionPower() * 2 : this.getExplosionPower()), true));
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.GRASS;
    }

    @Override
    public int getExplosionPower() {
        return 3;
    }

    @Override
    public int getSecondaryColor() {
        return 0x00ffcc;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "boxcreeper";
    }

    @Override
    public int getRegisterID() {
        return 64;
    }
}
