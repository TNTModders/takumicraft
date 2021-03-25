package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiEnchantmentCore;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemTakumiDiamond extends Item {
    public ItemTakumiDiamond() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "takumidiamond");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("takumidiamond");
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (EnchantmentHelper.getEnchantmentLevel(TakumiEnchantmentCore.EXPLOSION_PROTECTION, stack) > 0) {
            tooltip.add(TakumiUtils.takumiTranslate("takumicraft.message.takumidiamond"));
            TakumiUtils.addSpiltInfo(stack, tooltip);
        }
    }
}
