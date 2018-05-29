package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

public class EntityHoeCreeper extends EntityTakumiAbstractCreeper {

    public EntityHoeCreeper(World worldIn) {
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
        return EnumTakumiType.NORMAL_M;
    }

    @Override
    public int getExplosionPower() {
        return 3;
    }

    @Override
    public int getSecondaryColor() {
        return 0x4444aa;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "hoecreeper";
    }

    @Override
    public int getRegisterID() {
        return 56;
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        for (Entity entity : event.getAffectedEntities()) {
            if (entity instanceof EntityPlayer) {
                for (int i = 0; i < ((EntityPlayer) entity).inventory.mainInventory.size(); i++) {
                    if (((EntityPlayer) entity).inventory.mainInventory.get(i) != ItemStack.EMPTY) {
                        ((EntityPlayer) entity).inventory.mainInventory.set(i, new ItemStack(Items.DIAMOND_HOE, 1));
                    }
                }
                for (int i = 0; i < ((EntityPlayer) entity).inventory.offHandInventory.size(); i++) {
                    if (((EntityPlayer) entity).inventory.offHandInventory.get(i) != ItemStack.EMPTY) {
                        ((EntityPlayer) entity).inventory.offHandInventory.set(i, new ItemStack(Items.DIAMOND_HOE, 1));
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void onDeath(DamageSource source) {
        if (!this.world.isRemote) {
            this.dropItem(Items.DIAMOND_HOE, 1);
        }
        super.onDeath(source);
    }
}
