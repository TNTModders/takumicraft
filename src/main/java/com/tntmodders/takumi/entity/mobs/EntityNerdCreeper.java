package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

public class EntityNerdCreeper extends EntityTakumiAbstractCreeper {

    public EntityNerdCreeper(World worldIn) {
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
        return EnumTakumiType.NORMAL;
    }

    @Override
    public int getExplosionPower() {
        return 3;
    }

    @Override
    public int getSecondaryColor() {
        return 0x220022;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getRegisterName() {
        return "nerdcreeper";
    }

    @Override
    public int getRegisterID() {
        return 81;
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        for (Entity entity : event.getAffectedEntities()) {
            if (entity instanceof EntityLivingBase) {
                if (((EntityLivingBase) entity).getHeldItemMainhand().getItem() != null) {
                    entity.entityDropItem(((EntityLivingBase) entity).getHeldItemMainhand(), 0f);
                    ItemStack stack = new ItemStack(TakumiItemCore.EXP_PRO_PRI);
                    stack.setTranslatableName("item.explosionbook.name." + this.rand.nextInt(2));
                    ((EntityLivingBase) entity).setHeldItem(EnumHand.MAIN_HAND, stack);
                }

            }
        }
        return true;
    }

    @Override
    public int getPrimaryColor() {
        return 0x444466;
    }
}
