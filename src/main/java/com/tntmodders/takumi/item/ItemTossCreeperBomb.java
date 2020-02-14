package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemTossCreeperBomb extends Item {
    public ItemTossCreeperBomb() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "tosscreeperbomb");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("tosscreeperbomb");
        this.setMaxDamage(200);
        this.setMaxStackSize(1);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
        if (entityIn instanceof EntityLivingBase) {
            stack.damageItem(1, ((EntityLivingBase) entityIn));
        }
        if (stack.getItemDamage() == 1) {
            entityIn.playSound(SoundEvents.ENTITY_TNT_PRIMED, 0.2f, 0.5f);
        }
        if (stack.getItemDamage() >= stack.getMaxDamage() && !worldIn.isRemote) {
            worldIn.createExplosion(null, entityIn.posX, entityIn.posY, entityIn.posZ, 5f, true);
            stack.shrink(1);
        }
    }
}
