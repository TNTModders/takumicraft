package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.core.TakumiPotionCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

public class EntityCorruptionCreeper extends EntityTakumiAbstractCreeper {

    public EntityCorruptionCreeper(World worldIn) {
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
        return EnumTakumiType.NORMAL_D;
    }

    @Override
    public int getExplosionPower() {
        return 0;
    }

    @Override
    public int getSecondaryColor() {
        return 0x002211;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "corruptioncreeper";
    }

    @Override
    public int getRegisterID() {
        return 308;
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        if (!this.world.isRemote) {
            event.getAffectedEntities().forEach(entity -> {
                if (entity instanceof EntityLivingBase) {
                    ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(TakumiPotionCore.CORRUPTION, 100));
                }
            });
        }
        return true;
    }

    @Override
    public int getPrimaryColor() {
        return 0x111111;
    }
}
