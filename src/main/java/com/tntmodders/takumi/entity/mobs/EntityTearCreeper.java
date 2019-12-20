package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

public class EntityTearCreeper extends EntityTakumiAbstractCreeper {

    public EntityTearCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {

        for (int i = 0; i < (this.getPowered() ? 10 : 5); i++) {
            BlockPos pos = new BlockPos(this.posX + this.rand.nextInt(20) - 10, this.posY + this.rand.nextInt(20), this.posZ + this.rand.nextInt(20) - 10);
            EntityLightningBolt bolt = new EntityLightningBolt(this.world, pos.getX(), pos.getY(), pos.getZ(), false);
            this.world.addWeatherEffect(bolt);
            this.world.spawnEntity(bolt);
            if (!this.world.isRemote) {
                EntityGhast ghast = new EntityGhast(this.world);
                ghast.setPosition(pos.getX(), pos.getY(), pos.getZ());
                this.world.spawnEntity(ghast);
            }
        }
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        event.getAffectedEntities().removeIf(entity -> entity instanceof EntityGhast);
        return super.takumiExplodeEvent(event);
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.FIRE;
    }

    @Override
    public int getExplosionPower() {
        return 3;
    }

    @Override
    public int getSecondaryColor() {
        return 0xffcccc;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "tearcreeper";
    }

    @Override
    public int getRegisterID() {
        return 286;
    }

    @Override
    public int getPrimaryColor() {
        return 0xbbbbbb;
    }
}
