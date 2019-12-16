package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemExpProcPri extends ItemWrittenBook {

    public ItemExpProcPri() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "explosionbook");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("explosionbook");
        this.setMaxStackSize(1);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return super.getItemStackDisplayName(stack);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (!worldIn.isRemote) {
            worldIn.createExplosion(null, playerIn.posX, playerIn.posY, playerIn.posZ, 3f, true);
        }
        if (!playerIn.isCreative()) {
            playerIn.setHeldItem(handIn, ItemStack.EMPTY);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, EntityPlayer playerIn) {
        if (!playerIn.world.isRemote) {
            playerIn.world.createExplosion(null, playerIn.posX, playerIn.posY, playerIn.posZ, 3f, true);
        }

        item.shrink(1);

        return false;
    }
}
