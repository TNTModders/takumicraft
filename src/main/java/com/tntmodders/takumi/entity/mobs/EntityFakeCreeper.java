package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

public class EntityFakeCreeper extends EntityTakumiAbstractCreeper {

    public EntityFakeCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
        this.world.createExplosion(this, this.posX, this.posY, this.posZ, this.getPowered() ? 7 : 4, false);
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL;
    }

    @Override
    public int getExplosionPower() {
        return 0;
    }

    @Override
    public int getSecondaryColor() {
        return 0x00ff00;
    }

    @Override
    public int getPrimaryColor() {
        return 0x447722;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "fakecreeper";
    }

    @Override
    public int getRegisterID() {
        return 87;
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        if (this.world.isRemote) {
            event.getAffectedEntities().forEach(entity -> {
                if (entity instanceof EntityPlayer) {
                    ((EntityPlayer) entity).openGui(TakumiCraftCore.TakumiInstance, 1, this.world, (int) entity.posX, (int) entity.posY, (int) entity.posZ);
                }
            });
        }
        event.getAffectedEntities().clear();
        return true;
    }
}
