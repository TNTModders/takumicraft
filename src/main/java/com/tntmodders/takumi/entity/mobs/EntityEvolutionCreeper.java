package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.ITakumiEntity;
import com.tntmodders.takumi.entity.ITakumiEvoEntity;
import com.tntmodders.takumi.entity.mobs.evo.EntityEvolutionCreeper_Evo;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

public class EntityEvolutionCreeper extends EntityTakumiAbstractCreeper implements ITakumiEvoEntity {

    public EntityEvolutionCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        if (!this.world.isRemote) {
            event.getAffectedEntities().forEach(entity -> {
                if (entity instanceof ITakumiEvoEntity) {
                    try {
                        Entity evoEntity = ((Entity) ((ITakumiEvoEntity) entity).getEvoCreeper().getClass().getConstructor(World.class).newInstance(this.world));
                        evoEntity.copyLocationAndAnglesFrom(entity);
                        entity.setDead();
                        this.world.spawnEntity(evoEntity);
                    } catch (Exception ignored) {
                    }
                }
            });
        }
        return true;
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL;
    }

    @Override
    public int getExplosionPower() {
        return 3;
    }

    @Override
    public int getSecondaryColor() {
        return 0xff00aa;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "evolutioncreeper";
    }

    @Override
    public int getRegisterID() {
        return 299;
    }

    @Override
    public int getPrimaryColor() {
        return 0x00ff00;
    }

    @Override
    public ITakumiEntity getEvoCreeper() {
        return new EntityEvolutionCreeper_Evo(null);
    }

    @Override
    public boolean isEvo() {
        return false;
    }

    @Override
    public void onDeath(DamageSource source) {
        if (!this.world.isRemote) {
            this.dropItem(TakumiItemCore.EVO_CORE, this.rand.nextInt(3));
        }
        super.onDeath(source);
    }
}
