package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.util.Random;

public class EntityEnderCreeper extends EntityTakumiAbstractCreeper {

    public EntityEnderCreeper(World worldIn) {
        super(worldIn);
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
        return EnumTakumiType.GROUND_M;
    }

    @Override
    public int getExplosionPower() {
        return 3;
    }

    @Override
    public int getSecondaryColor() {
        return 4128831;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "endercreeper";
    }

    @Override
    public int getRegisterID() {
        return 9;
    }

    @Override
    public void additionalSpawn() {
        EntityRegistry.addSpawn(this.getClass(), this.takumiRank().getSpawnWeight(), 10, 10, EnumCreatureType.MONSTER,
                Biomes.SKY);
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
                IBlockState block = entity.world.getBlockState(new BlockPos(xInt, yInt - 1, zInt));
                if (block.getMaterial().blocksMovement()) {
                    foundGround = true;
                } else {
                    --entity.posY;
                    --yInt;
                }
            }

            if (foundGround) {
                entity.setPosition(entity.posX, entity.posY, entity.posZ);
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
}
