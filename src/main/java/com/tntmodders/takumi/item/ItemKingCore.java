package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.entity.item.EntityYMS;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemKingCore extends Item {

    public ItemKingCore() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "kingcore");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("kingcore");
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
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if(!worldIn.isRemote){
            EntityYMS ghastSP = new EntityYMS(worldIn);
            ghastSP.setPosition(playerIn.posX,playerIn.posY,playerIn.posZ);
            worldIn.spawnEntity(ghastSP);
            playerIn.startRiding(ghastSP,true);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
