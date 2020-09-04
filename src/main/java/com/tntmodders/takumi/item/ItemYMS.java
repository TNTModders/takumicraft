package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemYMS extends Item {
    public ItemYMS() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "yms");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("yms");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
/*        if (!worldIn.isRemote) {
            EntityYMS yms = new EntityYMS(worldIn);
            yms.setPosition(playerIn.posX, playerIn.posY, playerIn.posZ);
            boolean flg = worldIn.spawnEntity(yms);
            if (flg && !playerIn.isCreative()) {
                playerIn.getHeldItem(handIn).shrink(1);
            }
        }*/
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
