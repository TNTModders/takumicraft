package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiConfigCore;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTakumiBook extends Item {

    public ItemTakumiBook() {
        this.setRegistryName(TakumiCraftCore.MODID, "takumibook");
        //this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("takumibook");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (TakumiConfigCore.inEventServer) {
            return super.onItemRightClick(worldIn, playerIn, handIn);
        }
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        playerIn.openGui(TakumiCraftCore.TakumiInstance, 0, worldIn, (int) playerIn.posX, (int) playerIn.posY,
                (int) playerIn.posZ);
        playerIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (TakumiConfigCore.inEventServer) {
            if (!target.world.isRemote) {
                TakumiUtils.takumiCreateExplosion(target.world, attacker, attacker.posX, attacker.posY, attacker.posZ, 2f, false, false, 5, false);
            }
            return false;
        }
        return super.hitEntity(stack, target, attacker);
    }
}
