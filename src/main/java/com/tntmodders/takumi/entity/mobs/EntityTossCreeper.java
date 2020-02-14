package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

public class EntityTossCreeper extends EntityTakumiAbstractCreeper {

    public EntityTossCreeper(World worldIn) {
        super(worldIn);
        this.setSize(0.3F, 0.85F);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.145f);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10);
    }

    @Override
    public double getSizeAmp() {
        return 0.5;
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        event.getAffectedEntities().forEach(entity -> {
            if (entity instanceof EntityPlayer) {
                for (int i = 0; i < 3; i++) {
                    int r = this.rand.nextInt(9);
                    if (((EntityPlayer) entity).inventory.mainInventory.get(r) != ItemStack.EMPTY) {
                        ((EntityPlayer) entity).dropItem(((EntityPlayer) entity).inventory.mainInventory.get(r), false);
                    }
                    ((EntityPlayer) entity).inventory.mainInventory.set(r, new ItemStack(TakumiItemCore.TOSSCREEPER_BOMB));
                }
            }
        });
        return true;
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
        return EnumTakumiType.FIRE_D;
    }

    @Override
    public int getExplosionPower() {
        return 3;
    }

    @Override
    public int getSecondaryColor() {
        return 0xff9900;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "tosscreeper";
    }

    @Override
    public int getRegisterID() {
        return 86;
    }
}
