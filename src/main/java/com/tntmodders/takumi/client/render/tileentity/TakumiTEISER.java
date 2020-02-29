package com.tntmodders.takumi.client.render.tileentity;

import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;

public class TakumiTEISER extends TileEntityItemStackRenderer {
    @Override
    public void renderByItem(ItemStack itemStackIn) {
       /* if (itemStackIn.getItem() == Item.getItemFromBlock(TakumiBlockCore.CREEPER_SHULKER_BOX)) {
            RenderTakumiShulkerBox.instance.renderAsItem();
        }*/
    }
}
