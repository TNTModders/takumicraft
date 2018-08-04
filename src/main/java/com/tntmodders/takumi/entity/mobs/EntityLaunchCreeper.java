package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.item.EntityTakumiLauncher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

public class EntityLaunchCreeper extends EntityTakumiAbstractCreeper {

    public EntityLaunchCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.WIND_M;
    }

    @Override
    public int getExplosionPower() {
        return 3;
    }

    @Override
    public int getSecondaryColor() {
        return 0xaaaaff;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "launchcreeper";
    }

    @Override
    public int getRegisterID() {
        return 265;
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        EntityTakumiLauncher launcher = new EntityTakumiLauncher(this.world);
        launcher.setPosition(this.posX, this.posY, this.posZ);
        this.world.spawnEntity(launcher);
        event.getAffectedEntities().forEach(entity -> {
            if (entity instanceof EntityPlayer) {
                entity.setPosition(this.posX, this.posY, this.posZ);
                entity.startRiding(launcher, true);
            }
        });
        event.getAffectedEntities().clear();
        return true;
    }

    @Override
    public int getPrimaryColor() {
        return 0xddddff;
    }
}
