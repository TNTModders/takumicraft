package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.core.TakumiPotionCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

public class EntityVirusCreeper extends EntityTakumiAbstractCreeper {

    public EntityVirusCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
        this.world.createExplosion(this, this.posX, this.posY, this.posZ, this.getPowered() ? 6 : 4, true);
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.WIND;
    }

    @Override
    public int getExplosionPower() {
        return 0;
    }

    @Override
    public int getSecondaryColor() {
        return 0x004400;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "viruscreeper";
    }

    @Override
    public int getRegisterID() {
        return 268;
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        event.getAffectedEntities().forEach(entity -> {
            if (entity instanceof EntityLivingBase) {
                ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(TakumiPotionCore.VIRUS, 100));
            }
        });
        return true;
    }

    @Override
    public int getPrimaryColor() {
        return 0x111111;
    }
}
