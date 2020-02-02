package com.tntmodders.takumi.entity.item;

import com.tntmodders.takumi.core.TakumiItemCore;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityTakumiMinecart extends EntityMinecartEmpty {
    public EntityTakumiMinecart(World worldIn) {
        super(worldIn);
    }

    public EntityTakumiMinecart(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    public Type getType() {
        return Type.RIDEABLE;
    }

    @Override
    public ItemStack getCartItem() {
        return new ItemStack(TakumiItemCore.TAKUMI_MINECART);
    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }

    public static EntityMinecart create(World worldIn, double x, double y, double z, EntityMinecart.Type typeIn) {
        return new EntityTakumiMinecart(worldIn, x, y, z);
    }

    @Override
    public void killMinecart(DamageSource source) {
        this.setDead();

        if (this.world.getGameRules().getBoolean("doEntityDrops")) {
            ItemStack itemstack = new ItemStack(TakumiItemCore.TAKUMI_MINECART, 1);
            if (this.hasCustomName()) {
                itemstack.setStackDisplayName(this.getCustomNameTag());
            }
            this.entityDropItem(itemstack, 0.0F);
        }
    }
}
