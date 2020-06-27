package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ItemTakumiShield extends ItemShield implements IItemAntiExplosion {

    public static final ResourceLocation SHIELD_TEXTURE =
            new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/shield_base.png");

    public ItemTakumiShield() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "takumishield");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("takumishield");
        this.setMaxDamage(520);
    }

    @Override
    public boolean isShield(ItemStack stack,
                            @Nullable
                                    EntityLivingBase entity) {
        return true;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return TakumiUtils.takumiTranslate(this.getUnlocalizedName() + ".name");
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        if (repair.getItem() == Item.getItemFromBlock(Blocks.PLANKS)) {
            return false;
        }
        return repair.getItem() == Item.getItemFromBlock(TakumiBlockCore.CREEPER_IRON) ||
                super.getIsRepairable(toRepair, repair);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (stack.getItemDamage() >= stack.getMaxDamage() && !worldIn.isRemote) {
            stack.shrink(1);
            worldIn.createExplosion(null, entityIn.posX, entityIn.posY, entityIn.posZ, 5f, true);
        }
    }
}
