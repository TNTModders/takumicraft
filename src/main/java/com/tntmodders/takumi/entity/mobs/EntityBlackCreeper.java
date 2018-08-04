package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class EntityBlackCreeper extends EntityTakumiAbstractCreeper {

    public EntityBlackCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            List<EntityBlackCreeper> blackCreepers = new ArrayList<>();
            List<EntityCreeper> creepers = new ArrayList<>();
            this.world.loadedEntityList.forEach(entity -> {
                if (entity instanceof EntityCreeper && !(entity instanceof EntityBlackCreeper) &&
                        this.getDistanceSqToEntity(entity) < (this.getPowered() ? 2500 : 900)) {
                    EntityBlackCreeper blackCreeper = new EntityBlackCreeper(this.world);
                    blackCreeper.copyLocationAndAnglesFrom(entity);
                    if (this.getAttackTarget() != null) {
                        blackCreeper.setAttackTarget(this.getAttackTarget());
                    }
                    blackCreepers.add(blackCreeper);
                    creepers.add(((EntityCreeper) entity));
                }
            });
            blackCreepers.forEach(entityBlackCreeper -> this.world.spawnEntity(entityBlackCreeper));
            creepers.forEach(Entity :: setDead);
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL_M;
    }

    @Override
    public int getExplosionPower() {
        return 4;
    }

    @Override
    public int getSecondaryColor() {
        return 0;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "blackcreeper";
    }

    @Override
    public int getRegisterID() {
        return 258;
    }

    @Override
    public int getPrimaryColor() {
        return 0;
    }

    @Override
    public void onLivingUpdate() {
        if (this.world.isRemote) {
            for (int i = 0; i < 5; ++i) {
                this.world.spawnParticle(EnumParticleTypes.SPELL_MOB,
                        this.posX + (this.rand.nextDouble() - 0.5D) * this.width,
                        this.posY + this.rand.nextDouble() * this.height,
                        this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D);
            }
        }
        super.onLivingUpdate();
    }
}
