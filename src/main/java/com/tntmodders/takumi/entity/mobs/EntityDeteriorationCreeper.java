package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

public class EntityDeteriorationCreeper extends EntityTakumiAbstractCreeper {

    public EntityDeteriorationCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
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
        return 0x330000;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "deteriorationcreeper";
    }

    @Override
    public int getRegisterID() {
        return 66;
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        for (Entity entity : event.getAffectedEntities()) {
            if (entity instanceof EntityLivingBase) {
                if (((EntityLivingBase) entity).getHeldItemMainhand().getItem().isDamageable()) {
                    ((EntityLivingBase) entity).getHeldItemMainhand().setItemDamage(
                            ((EntityLivingBase) entity).getHeldItemMainhand().getMaxDamage() - 1);
                }
                if (((EntityLivingBase) entity).getHeldItemOffhand().getItem().isDamageable()) {
                    ((EntityLivingBase) entity).getHeldItemOffhand().setItemDamage(
                            ((EntityLivingBase) entity).getHeldItemOffhand().getMaxDamage() - 1);
                }
                if (((EntityLivingBase) entity).getItemStackFromSlot(
                        EntityEquipmentSlot.HEAD).getItem().isDamageable()) {
                    ((EntityLivingBase) entity).getItemStackFromSlot(EntityEquipmentSlot.HEAD).setItemDamage(
                            ((EntityLivingBase) entity).getItemStackFromSlot(EntityEquipmentSlot.HEAD).getMaxDamage() -
                                    1);
                }
                if (((EntityLivingBase) entity).getItemStackFromSlot(
                        EntityEquipmentSlot.CHEST).getItem().isDamageable()) {
                    ((EntityLivingBase) entity).getItemStackFromSlot(EntityEquipmentSlot.CHEST).setItemDamage(
                            ((EntityLivingBase) entity).getItemStackFromSlot(EntityEquipmentSlot.CHEST).getMaxDamage() -
                                    1);
                }
                if (((EntityLivingBase) entity).getItemStackFromSlot(
                        EntityEquipmentSlot.LEGS).getItem().isDamageable()) {
                    ((EntityLivingBase) entity).getItemStackFromSlot(EntityEquipmentSlot.LEGS).setItemDamage(
                            ((EntityLivingBase) entity).getItemStackFromSlot(EntityEquipmentSlot.LEGS).getMaxDamage() -
                                    1);
                }
                if (((EntityLivingBase) entity).getItemStackFromSlot(
                        EntityEquipmentSlot.FEET).getItem().isDamageable()) {
                    ((EntityLivingBase) entity).getItemStackFromSlot(EntityEquipmentSlot.FEET).setItemDamage(
                            ((EntityLivingBase) entity).getItemStackFromSlot(EntityEquipmentSlot.FEET).getMaxDamage() -
                                    1);
                }
            }
        }
        return true;
    }

    @Override
    public int getPrimaryColor() {
        return 0x664444;
    }
}
