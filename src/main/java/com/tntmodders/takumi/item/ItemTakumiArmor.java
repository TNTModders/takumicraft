package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.client.model.ModelTakumiArmor;
import com.tntmodders.takumi.core.TakumiEnchantmentCore;
import com.tntmodders.takumi.item.material.TakumiArmorMaterial;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemTakumiArmor extends ItemArmor {

    public ItemTakumiArmor(EntityEquipmentSlot equipmentSlotIn) {
        super(TakumiArmorMaterial.CREEPER, 5, equipmentSlotIn);
        this.setRegistryName(TakumiCraftCore.MODID, "takumiarmor_" + equipmentSlotIn.getName());
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("takumiarmor_" + equipmentSlotIn.getName());
    }

    @Override
    @SideOnly(Side.CLIENT)
    @Nullable
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        switch (armorSlot) {
            case HEAD: {
                return new ModelTakumiArmor.ModelTakumiArmor_Head();
            }
            case CHEST: {
                return new ModelTakumiArmor.ModelTakumiArmor_Chest();
            }
            case LEGS: {
                return new ModelTakumiArmor.ModelTakumiArmor_Legs();
            }
            case FEET: {
                return new ModelTakumiArmor.ModelTakumiArmor_Feet();
            }
            default: {
                return null;
            }
        }
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean isHeld) {
        if (!itemStack.isItemEnchanted()) {
            itemStack.addEnchantment(Enchantments.UNBREAKING, 10);
            itemStack.addEnchantment(TakumiEnchantmentCore.EXPLOSION_PROTECTION, 10);
            itemStack.addEnchantment(Enchantments.MENDING, 10);
            switch (this.armorType) {
                case HEAD:
                    itemStack.addEnchantment(Enchantments.THORNS, 10);
                    break;

                case CHEST:
                    itemStack.addEnchantment(Enchantments.PROJECTILE_PROTECTION, 10);
                    break;

                case LEGS:
                    itemStack.addEnchantment(Enchantments.FIRE_PROTECTION, 10);
                    break;

                case FEET:
                    itemStack.addEnchantment(Enchantments.FEATHER_FALLING, 10);
            }
        }
    }

    @Override
    public void onCreated(ItemStack itemStack, World p_77622_2_, EntityPlayer p_77622_3_) {
        if (!itemStack.isItemEnchanted()) {
            itemStack.addEnchantment(Enchantments.UNBREAKING, 10);
            itemStack.addEnchantment(TakumiEnchantmentCore.EXPLOSION_PROTECTION, 10);
            itemStack.addEnchantment(Enchantments.MENDING, 10);
            switch (this.armorType) {
                case HEAD:
                    itemStack.addEnchantment(Enchantments.THORNS, 10);
                    break;

                case CHEST:
                    itemStack.addEnchantment(Enchantments.PROJECTILE_PROTECTION, 10);
                    break;

                case LEGS:
                    itemStack.addEnchantment(Enchantments.FIRE_PROTECTION, 10);
                    break;

                case FEET:
                    itemStack.addEnchantment(Enchantments.FEATHER_FALLING, 10);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack,
                               @Nullable
                                       World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TakumiUtils.takumiTranslate("takumicraft.message.armor"));
        TakumiUtils.addSpiltInfo(stack, tooltip);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack itemStack) {
        return true;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            ItemStack itemStack = new ItemStack(this, 1);
            if (!itemStack.isItemEnchanted()) {
                itemStack.addEnchantment(Enchantments.UNBREAKING, 10);
                itemStack.addEnchantment(TakumiEnchantmentCore.EXPLOSION_PROTECTION, 10);
                itemStack.addEnchantment(Enchantments.MENDING, 10);
                switch (this.armorType) {
                    case HEAD:
                        itemStack.addEnchantment(Enchantments.THORNS, 10);
                        break;

                    case CHEST:
                        itemStack.addEnchantment(Enchantments.PROJECTILE_PROTECTION, 10);
                        break;

                    case LEGS:
                        itemStack.addEnchantment(Enchantments.FIRE_PROTECTION, 10);
                        break;

                    case FEET:
                        itemStack.addEnchantment(Enchantments.FEATHER_FALLING, 10);
                }
            }
            items.add(itemStack);
        }
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if (!world.isRemote) {
            //ヘルメット
            if (this.armorType == EntityEquipmentSlot.HEAD) {
                player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 1210, 0, true, false));
            }

            //チェストプレート
            if (this.armorType == EntityEquipmentSlot.CHEST) {
                player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 1210, 0, true, false));
            }

            //レギンス
            if (this.armorType == EntityEquipmentSlot.LEGS && !player.isPotionActive(MobEffects.INSTANT_HEALTH)) {
                player.heal(0.5f);
                player.addPotionEffect(new PotionEffect(MobEffects.INSTANT_HEALTH, 1210, 0, true, false));
            }

            //ブーツ
            if (this.armorType == EntityEquipmentSlot.FEET) {
                player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 1210, 0, true, false));
            }
        }
        boolean flg = true;
        for (ItemStack stack : player.getArmorInventoryList()) {
            if (!(stack.getItem() instanceof ItemTakumiArmor)) {
                flg = false;
                break;
            }
        }
        //player.setInvisible(flg);
        if (!itemStack.isItemEnchanted()) {
            itemStack.addEnchantment(Enchantments.UNBREAKING, 10);
            itemStack.addEnchantment(TakumiEnchantmentCore.EXPLOSION_PROTECTION, 10);
            itemStack.addEnchantment(Enchantments.MENDING, 10);
            switch (this.armorType) {
                case HEAD:
                    itemStack.addEnchantment(Enchantments.THORNS, 10);
                    break;

                case CHEST:
                    itemStack.addEnchantment(Enchantments.PROJECTILE_PROTECTION, 10);
                    break;

                case LEGS:
                    itemStack.addEnchantment(Enchantments.FIRE_PROTECTION, 10);
                    break;

                case FEET:
                    itemStack.addEnchantment(Enchantments.FEATHER_FALLING, 10);
            }
        }
    }
}
