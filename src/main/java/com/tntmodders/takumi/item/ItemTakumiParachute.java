package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.entity.item.EntityTakumiParachute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class ItemTakumiParachute extends Item {
    public ItemTakumiParachute() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "takumiparachute");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("takumiparachute");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (!worldIn.isRemote && playerIn.getRidingEntity() == null) {
            if (!worldIn.collidesWithAnyBlock(playerIn.getEntityBoundingBox().grow(4)) && worldIn.getWorldBorder().getClosestDistance(playerIn) > 3.5 * 3.5) {
                EntityTakumiParachute parachute = new EntityTakumiParachute(worldIn);
                parachute.setPosition(playerIn.posX, playerIn.posY, playerIn.posZ);
                boolean flg = worldIn.spawnEntity(parachute);
                if (flg) {
                    if (!playerIn.isCreative()) {
                        playerIn.getHeldItem(handIn).shrink(1);
                    }
                    playerIn.startRiding(parachute, true);
                    parachute.initY = playerIn.posY;
                }
            } else {
                playerIn.sendMessage(new TextComponentTranslation("item.takumiparachute.message"));
            }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
