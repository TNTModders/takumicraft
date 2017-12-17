package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

public class EntityLostCreeper extends EntityTakumiAbstractCreeper {
    
    public EntityLostCreeper(World worldIn) {
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
        return 4128831;
    }
    
    @Override
    public boolean isCustomSpawn() {
        return false;
    }
    
    @Override
    public String getRegisterName() {
        return "lostcreeper";
    }
    
    @Override
    public int getRegisterID() {
        return 32;
    }
    
    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        for (Entity entity : event.getAffectedEntities()) {
            if (entity instanceof EntityLivingBase) {
                ((EntityLivingBase) entity).getHeldItemMainhand().shrink(this.rand.nextInt(5) + 1);
                ((EntityLivingBase) entity).getHeldItemOffhand().shrink(this.rand.nextInt(5) + 1);
                if (this.getPowered()) {
                    EntityEquipmentSlot slot = EntityEquipmentSlot.MAINHAND;
                    switch (this.rand.nextInt(4)) {
                        case 0:
                            slot = EntityEquipmentSlot.HEAD;
                            break;
                        case 1:
                            slot = EntityEquipmentSlot.CHEST;
                            break;
                        case 2:
                            slot = EntityEquipmentSlot.LEGS;
                            break;
                        case 3:
                            slot = EntityEquipmentSlot.FEET;
                            break;
                    }
                    entity.setItemStackToSlot(slot, ItemStack.EMPTY);
                }
            }
        }
        return true;
    }
    
    @Override
    public int getPrimaryColor() {
        return 0xaa44ff;
    }
}
