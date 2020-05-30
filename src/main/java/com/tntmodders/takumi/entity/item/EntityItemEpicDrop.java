package com.tntmodders.takumi.entity.item;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityItemEpicDrop extends EntityItem {
    public EntityItemEpicDrop(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        this.isImmuneToFire = true;
    }

    public EntityItemEpicDrop(World worldIn, double x, double y, double z, ItemStack stack) {
        super(worldIn, x, y, z, stack);
        this.isImmuneToFire = true;
    }

    public EntityItemEpicDrop(World worldIn) {
        super(worldIn);
        this.isImmuneToFire = true;
    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }
}
