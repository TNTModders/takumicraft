package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.client.render.RenderLayerCreeper;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityLayerCreeper extends EntityTakumiAbstractCreeper {

    public EntityLayerCreeper(World worldIn) {
        super(worldIn);
        this.setSize(1, 1);
    }

    @Override
    protected void setRotation(float yaw, float pitch) {
    }

    @Override
    public void move(MoverType type, double x, double y, double z) {
    }

    @Override
    public boolean isEntityInsideOpaqueBlock() {
        return false;
    }

    @Override
    public void applyEntityCollision(Entity entityIn) {
        if (!this.isRidingSameEntity(entityIn)) {
            if (!entityIn.noClip && !this.noClip) {
                double d0 = entityIn.posX - this.posX;
                double d1 = entityIn.posZ - this.posZ;
                double d2 = MathHelper.absMax(d0, d1);

                if (d2 >= 0.009999999776482582D) {
                    d2 = (double) MathHelper.sqrt(d2);
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
                    d0 = d0 * (double) (1.0F - this.entityCollisionReduction);
                    d1 = d1 * (double) (1.0F - this.entityCollisionReduction);

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
    public boolean canBePushed() {
        return false;
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
        return EnumTakumiType.WATER;
    }

    @Override
    public int getExplosionPower() {
        return 4;
    }

    @Override
    public int getSecondaryColor() {
        return 0xaaaaaa;
    }

    @Override
    public boolean isCustomSpawn() {
        return true;
    }

    @Override
    public void customSpawn() {
        EntityRegistry.addSpawn(this.getClass(), this.takumiRank().getSpawnWeight(), 1, 3, EnumCreatureType.MONSTER,
                Biomes.COLD_BEACH, Biomes.COLD_TAIGA, Biomes.COLD_TAIGA_HILLS, Biomes.MUTATED_TAIGA_COLD,
                Biomes.FROZEN_RIVER, Biomes.ICE_PLAINS, Biomes.ICE_MOUNTAINS, Biomes.MUTATED_ICE_FLATS,
                Biomes.EXTREME_HILLS);
    }

    @Override
    public String getRegisterName() {
        return "layercreeper";
    }

    @Override
    public int getRegisterID() {
        return 83;
    }

    @Override
    public int getPrimaryColor() {
        return 0xaaaaff;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Object getRender(RenderManager manager) {
        return new RenderLayerCreeper(manager);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        this.rotationYawHead = 0;
    }

    @Override
    public boolean getCanSpawnHere() {
        return this.posY > 64 && super.getCanSpawnHere();
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(3);
    }
}
