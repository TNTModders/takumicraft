package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

public class EntityMakeupCreeper extends EntityTakumiAbstractCreeper {

    public EntityMakeupCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        event.getAffectedEntities().forEach(entity -> {
            if (entity instanceof EntityLivingBase) {
                if (((EntityLivingBase) entity).getItemStackFromSlot(EntityEquipmentSlot.HEAD) != null &&
                        ((EntityLivingBase) entity).getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() !=
                                TakumiItemCore.MAKEUP) {
                    entity.entityDropItem(((EntityLivingBase) entity).getItemStackFromSlot(EntityEquipmentSlot.HEAD),
                            1f);
                }
                entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(TakumiItemCore.MAKEUP, 1));
            }

        });
        event.getAffectedEntities().removeIf(entity -> !(entity instanceof EntityLivingBase &&
                ((EntityLivingBase) entity).getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() !=
                        TakumiItemCore.MAKEUP));
        return true;
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL_M;
    }

    @Override
    public int getExplosionPower() {
        return 3;
    }

    @Override
    public int getSecondaryColor() {
        return 0x00ffff;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "makeupcreeper";
    }

    @Override
    public int getRegisterID() {
        return 285;
    }
}
