package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

import java.util.List;

public class EntityCryCreeper extends EntityTakumiAbstractCreeper {

    public EntityCryCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
        for (int i = 0; i < (this.getPowered() ? 8 : 4); i++) {
            BlockPos pos = new BlockPos(this.posX + this.rand.nextInt(20) - 10, this.posY + this.rand.nextInt(3), this.posZ + this.rand.nextInt(20) - 10);
            EntityLightningBolt bolt = new EntityLightningBolt(this.world, pos.getX(), pos.getY(), pos.getZ(), false);
            //this.world.addWeatherEffect(bolt);
            this.world.spawnEntity(bolt);
            if (!this.world.isRemote) {
                EntityPigZombie ghast = new EntityPigZombie(this.world);
                ghast.setPosition(pos.getX(), pos.getY(), pos.getZ());
                this.world.spawnEntity(ghast);
            }
        }

        List<EntityPig> pigList = this.world.getEntities(EntityPig.class, entity -> (entity instanceof EntityPig && entity.getDistanceSqToEntity(this) < (this.getPowered() ? 40 * 40 : 20 * 20)));
        if (pigList != null && pigList.size() > 0) {
            pigList.forEach(entityPig -> {
                EntityLightningBolt bolt = new EntityLightningBolt(this.world, entityPig.posX, entityPig.posY, entityPig.posZ, false);
                //this.world.addWeatherEffect(bolt);
                this.world.spawnEntity(bolt);
            });
        }
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        event.getAffectedEntities().removeIf(entity -> entity instanceof EntityPigZombie);
        return super.takumiExplodeEvent(event);
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
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
        return 0xff8888;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "crycreeper";
    }

    @Override
    public int getRegisterID() {
        return 287;
    }

    @Override
    public int getPrimaryColor() {
        return 0xbbbbbb;
    }
}
