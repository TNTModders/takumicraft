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
import java.util.UUID;

public class ItemBattleShield extends ItemShield implements IItemAntiExplosion {
    public static final ResourceLocation SHIELD_TEXTURE =
            new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/shield_battle.png");
    protected static final UUID SHIELD_ARMOR_MODIFIER = UUID.fromString("7ABE2D17-F418-4EC4-BFC2-E7B2A1AB89B8");
    protected static final UUID SHIELD_SPEED_MODIFIER = UUID.fromString("7ABE2D17-F418-4EC4-BFC2-E7B2A1AB89B9");
    private final boolean isPowered;

    public ItemBattleShield(boolean flg) {
        super();
        this.isPowered = flg;
        String s = flg ? "battleshield_powered" : "battleshield";
        this.setRegistryName(TakumiCraftCore.MODID, s);
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName(s);
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
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (!attacker.world.isRemote) {
            attacker.world.createExplosion(attacker, target.posX, target.posY, target.posZ, 0f, false);
            double d1 = attacker.posX - target.posX;
            double d0;

            for (d0 = attacker.posZ - target.posZ; d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D) {
                d1 = (Math.random() - Math.random()) * 0.01D;
            }

            target.knockBack(attacker, 1.5F, d1, d0);
        }
        stack.damageItem(1, attacker);
        return true;
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
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
                    new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.isPowered ? 29 : 9, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(),
                    new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -3.5d, 0));
            multimap.put(SharedMonsterAttributes.ARMOR.getName(),
                    new AttributeModifier(SHIELD_ARMOR_MODIFIER, "Armor modifier", 10f, 0));
            multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(),
                    new AttributeModifier(SHIELD_SPEED_MODIFIER, "Speed modifier", -0.025f, 0));
        } else if (equipmentSlot == EntityEquipmentSlot.OFFHAND) {
            multimap.put(SharedMonsterAttributes.ARMOR.getName(),
                    new AttributeModifier(SHIELD_ARMOR_MODIFIER, "Armor modifier", 10f, 0));
            multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(),
                    new AttributeModifier(SHIELD_SPEED_MODIFIER, "Speed modifier", -0.025f, 0));
        }
        return multimap;
    }

    @Override
    public boolean isShield(ItemStack stack,
                            @Nullable
                                    EntityLivingBase entity) {
        return true;
    }
}
