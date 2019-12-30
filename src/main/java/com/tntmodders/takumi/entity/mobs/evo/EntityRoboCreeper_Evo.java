package com.tntmodders.takumi.entity.mobs.evo;

import com.tntmodders.takumi.entity.mobs.EntityRoboCreeper;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityRoboCreeper_Evo extends EntityRoboCreeper {

    public EntityRoboCreeper_Evo(World worldIn) {
        super(worldIn);
    }

    @Override
    public int getExplosionPower() {
        return super.getExplosionPower() * 3;
    }

    @Override
    public boolean isCustomSpawn() {
        return true;
    }

    @Override
    public String getRegisterName() {
        return super.getRegisterName() + "_evo";
    }

    @Override
    public int getRegisterID() {
        return 298;
    }

    @Override
    public boolean isEvo() {
        return true;
    }

    @Override
    protected void laser() {
        RayTraceResult rayTraceResult = this.rayTrace(128);
        if (rayTraceResult.getBlockPos() != null) {
            pos = rayTraceResult.getBlockPos();
        }
        if (this.getAttackTarget() != null &&
                !this.world.getEntitiesWithinAABB(this.getAttackTarget().getClass(),
                        this.getEntityBoundingBox().grow(this.getLookVec().x, this.getLookVec().y,
                                this.getLookVec().z).grow(32)).isEmpty() &&
                this.getLookHelper().getIsLooking()) {
            Entity entity = this.world.getEntitiesWithinAABB(this.getAttackTarget().getClass(),
                    this.getEntityBoundingBox().grow(this.getLookVec().x, this.getLookVec().y,
                            this.getLookVec().z).grow(32)).get(0);
            if (pos == null || this.getDistanceSqToEntity(entity) < this.getDistanceSq(pos)) {
                pos = entity.getPosition();
            }
        }
        if (pos != null) {
            this.world.playSound(null, this.posX, this.posY, this.posZ,
                    SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.WEATHER, 10000.0F,
                    0.8F + this.rand.nextFloat() * 0.2F);
            if (!this.world.isRemote) {
                this.world.newExplosion(this, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 8f,
                        true, true);
            }
        }
    }
}
