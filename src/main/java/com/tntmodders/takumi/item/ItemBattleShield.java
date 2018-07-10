package com.tntmodders.takumi.item;

import com.google.common.collect.Multimap;
import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.core.TakumiEnchantmentCore;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemBattleShield extends ItemShield implements IItemAntiExplosion {
    private final boolean isPowered;
    public static final ResourceLocation SHIELD_TEXTURE =
            new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/shield_battle.png");

    public ItemBattleShield(boolean flg) {
        super();
        this.isPowered = flg;
        String s = flg ? "battleshield_powered" : "battleshield";
        this.setRegistryName(TakumiCraftCore.MODID, s);
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName(s);
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
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
                    new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.isPowered ? 19 : 6, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(),
                    new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -3.5d, 0));
        }
        return multimap;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (this.isPowered &&
                !EnchantmentHelper.getEnchantments(stack).containsKey(TakumiEnchantmentCore.ANTI_POWERED)) {
            try {
                stack.addEnchantment(TakumiEnchantmentCore.ANTI_POWERED, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        if (this.isPowered &&
                !EnchantmentHelper.getEnchantments(stack).containsKey(TakumiEnchantmentCore.ANTI_POWERED)) {
            try {
                stack.addEnchantment(TakumiEnchantmentCore.ANTI_POWERED, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return this.isPowered;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return this.isPowered ? EnumRarity.RARE : EnumRarity.UNCOMMON;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            ItemStack itemStack = new ItemStack(this, 1);
            if (this.isPowered) {
                itemStack.addEnchantment(TakumiEnchantmentCore.ANTI_POWERED, 1);
            }
            items.add(itemStack);
        }
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (!attacker.world.isRemote) {
            attacker.world.createExplosion(attacker, target.posX, target.posY, target.posZ, 0f, false);
        }
        stack.damageItem(1, attacker);
        return true;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        if (repair.getItem() == Item.getItemFromBlock(Blocks.PLANKS)) {
            return false;
        }
        return repair.getItem() == Item.getItemFromBlock(TakumiBlockCore.CREEPER_IRON) ||
                super.getIsRepairable(toRepair, repair);
    }
}
