package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.List;

public class ItemTakumiSpecialMeat extends ItemFood {
    private final ItemFood itemFood;
    private final Item rawFood;

    public ItemTakumiSpecialMeat(ItemFood itemIn, Item rawItemIn) {
        super(((int) (itemIn.getHealAmount(new ItemStack(itemIn)) * 2.5)), itemIn.getSaturationModifier(new ItemStack(itemIn)) * 2, false);
        this.itemFood = itemIn;
        this.rawFood = rawItemIn;
        String s = "takumispecialmeat_" + itemIn.getRegistryName().getResourcePath();
        this.setRegistryName(TakumiCraftCore.MODID, s);
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("takumispecialmeat");
    }

    public static ItemTakumiSpecialMeat getSpecializedMeat(Item itemIn, boolean allowRawFood) {
        Class<TakumiItemCore> clazz = TakumiItemCore.class;
        for (Field field : clazz.getFields()) {
            try {
                if (field.get(TakumiItemCore.INSTANCE) instanceof ItemTakumiSpecialMeat &&
                        (((ItemTakumiSpecialMeat) field.get(TakumiItemCore.INSTANCE)).getItemFood() == itemIn || (((ItemTakumiSpecialMeat) field.get(TakumiItemCore.INSTANCE)).getRawFood() == itemIn && allowRawFood))) {
                    return ((ItemTakumiSpecialMeat) field.get(TakumiItemCore.INSTANCE));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public ItemFood getItemFood() {
        return itemFood;
    }

    public Item getRawFood() {
        return rawFood;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 16;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TakumiUtils.takumiTranslate(this.itemFood.getUnlocalizedName() + ".name"));
    }
}
