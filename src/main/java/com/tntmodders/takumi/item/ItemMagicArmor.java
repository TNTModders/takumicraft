package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.item.material.TakumiArmorMaterial;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemMagicArmor extends ItemArmor {

    public ItemMagicArmor(EntityEquipmentSlot equipmentSlotIn) {
        super(TakumiArmorMaterial.MAGIC, 5, equipmentSlotIn);
        this.setRegistryName(TakumiCraftCore.MODID, "magicarmor_" + equipmentSlotIn.getName());
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("magicarmor_" + equipmentSlotIn.getName());
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean isHeld) {
        if (!itemStack.isItemEnchanted()) {
            itemStack.addEnchantment(Enchantments.UNBREAKING, 10);
            itemStack.addEnchantment(Enchantments.MENDING, 10);
            itemStack.addEnchantment(Enchantments.VANISHING_CURSE, 10);
            itemStack.addEnchantment(Enchantments.BINDING_CURSE, 10);
        }
    }

    @Override
    public void onCreated(ItemStack itemStack, World p_77622_2_, EntityPlayer p_77622_3_) {
        if (!itemStack.isItemEnchanted()) {
            itemStack.addEnchantment(Enchantments.UNBREAKING, 10);
            itemStack.addEnchantment(Enchantments.MENDING, 10);
            itemStack.addEnchantment(Enchantments.VANISHING_CURSE, 10);
            itemStack.addEnchantment(Enchantments.BINDING_CURSE, 10);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack itemStack) {
        return true;
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if (!itemStack.isItemEnchanted()) {
            itemStack.addEnchantment(Enchantments.UNBREAKING, 10);
            itemStack.addEnchantment(Enchantments.MENDING, 10);
            itemStack.addEnchantment(Enchantments.VANISHING_CURSE, 10);
            itemStack.addEnchantment(Enchantments.BINDING_CURSE, 10);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TakumiUtils.takumiTranslate("takumicraft.message.magicarmor"));
        tooltip.add(TakumiUtils.takumiTranslate("takumicraft.message.spilt"));
    }
}
