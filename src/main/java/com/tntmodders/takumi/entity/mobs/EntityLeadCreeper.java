package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

import java.util.Comparator;
import java.util.List;

public class EntityLeadCreeper extends EntityTakumiAbstractCreeper {

    public EntityLeadCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            List<Entity> entities = this.world.loadedEntityList;
            entities.sort(Comparator.comparingDouble(o -> o.getDistanceSqToEntity(EntityLeadCreeper.this)));
            int i = 0;
            for (Entity entity : entities) {
                if (entity instanceof EntityMob) {
                    entity.setPositionAndUpdate(this.posX, this.posY, this.posZ);
                    i++;
                    if (i > (this.getPowered() ? 40 : 20)) {
                        break;
                    }
                }
            }
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
        return 5;
    }

    @Override
    public int getSecondaryColor() {
        return 0xff8000;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "leadcreeper";
    }

    @Override
    public int getRegisterID() {
        return 226;
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        event.getAffectedEntities().clear();
        return true;
    }
}
