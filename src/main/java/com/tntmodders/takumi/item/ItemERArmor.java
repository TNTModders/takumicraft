package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.item.material.TakumiArmorMaterial;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemERArmor extends ItemArmor {
    public ItemERArmor(EntityEquipmentSlot equipmentSlotIn) {
        super(TakumiArmorMaterial.ERA, 5, equipmentSlotIn);
        this.setRegistryName(TakumiCraftCore.MODID, "takumiera_" + equipmentSlotIn.getName());
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("takumiera_" + equipmentSlotIn.getName());
        this.setMaxDamage(7);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (stack.getItemDamage() >= stack.getMaxDamage() && !worldIn.isRemote) {
            worldIn.createExplosion(null, entityIn.posX, entityIn.posY, entityIn.posZ, 5f, true);
            stack.shrink(1);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack itemStack) {
        return true;
    }

    @Override
    public int getItemEnchantability() {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TakumiUtils.takumiTranslate("takumicraft.message.takumiera"));
        TakumiUtils.addSpiltInfo(stack, tooltip);
    }
}
