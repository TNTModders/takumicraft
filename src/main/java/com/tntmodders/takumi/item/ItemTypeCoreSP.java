package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemTypeCoreSP extends Item {

    public ItemTypeCoreSP(boolean isMagic) {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "typecoresp_" + (isMagic ? "magic" : "dest"));
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("typecoresp_" + (isMagic ? "magic" : "dest"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.UNCOMMON;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TakumiUtils.takumiTranslate("takumicraft.message.typecore"));
        tooltip.add(TakumiUtils.takumiTranslate("takumicraft.message.typecore_sp"));
        if (stack.getItem() == TakumiItemCore.TAKUMI_TYPE_CORE_DEST) {
            tooltip.add(TakumiUtils.takumiTranslate("takumicraft.message.typecore_p2"));
        }
        if (stack.getItem() == TakumiItemCore.TAKUMI_TYPE_CORE_MAGIC) {
            tooltip.add(TakumiUtils.takumiTranslate("takumicraft.message.typecore_p3"));
        }
        TakumiUtils.addSpiltInfo(stack, tooltip);
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        if (entityItem instanceof EntityItem) {
            entityItem.setEntityInvulnerable(entityItem.ticksExisted < 1200);
        }
        return super.onEntityItemUpdate(entityItem);
    }
}
