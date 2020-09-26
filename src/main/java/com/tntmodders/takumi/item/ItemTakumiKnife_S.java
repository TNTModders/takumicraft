package com.tntmodders.takumi.item;

import com.google.common.collect.Multimap;
import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiEnchantmentCore;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class ItemTakumiKnife_S extends Item {
    protected static final UUID SWORD_HEALTH_MODIFIER = UUID.fromString("7ABE2D17-F418-4EC4-BFC2-E7B2A1AB89B2");

    public ItemTakumiKnife_S() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "takumiknife");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("takumiknife");
        this.setMaxStackSize(1);
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);
        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
                    new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 25, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(),
                    new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", 1d, 0));
            multimap.put(SharedMonsterAttributes.MAX_HEALTH.getName(),
                    new AttributeModifier(SWORD_HEALTH_MODIFIER, "Health modifier", 20f, 0));
        }
        return multimap;
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        return super.getAttributeModifiers(slot, stack);
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
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!EnchantmentHelper.getEnchantments(stack).containsKey(TakumiEnchantmentCore.ANTI_POWERED)) {
            try {
                stack.addEnchantment(TakumiEnchantmentCore.ANTI_POWERED, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        if (!EnchantmentHelper.getEnchantments(stack).containsKey(TakumiEnchantmentCore.ANTI_POWERED)) {
            try {
                stack.addEnchantment(TakumiEnchantmentCore.ANTI_POWERED, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            ItemStack itemStack = new ItemStack(this, 1);
            itemStack.addEnchantment(TakumiEnchantmentCore.ANTI_POWERED, 1);
            items.add(itemStack);
        }
    }

    @Override
    public boolean canDisableShield(ItemStack stack, ItemStack shield, EntityLivingBase entity, EntityLivingBase attacker) {
        return true;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        if (entity instanceof EntityLivingBase) {
            player.heal(2f);
            player.getFoodStats().addStats(1, 0.2f);
            for (int i = 0; i < 10; i++) {
                double x = player.world.rand.nextDouble() - 0.5;
                double y = player.world.rand.nextDouble() * 1.5;
                double z = player.world.rand.nextDouble() - 0.5;
                player.world.spawnParticle(EnumParticleTypes.HEART, player.posX + x, player.posY + y, player.posZ + z, x, y, z);
            }
            if (!player.world.isRemote) {
                NBTTagCompound compound = stack.getTagCompound() == null ? new NBTTagCompound() : stack.getTagCompound();
                if (compound.hasKey("Power")) {
                    int i = Math.min(10, compound.getInteger("Power") + 1);
                    compound.setInteger("Power", i);
                } else {
                    compound.setInteger("Power", 1);
                }
                stack.setTagCompound(compound);
            }
        }
        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int i = 0;
        if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("Power")) {
            i = stack.getTagCompound().getInteger("Power");
        }
        tooltip.add(TakumiUtils.takumiTranslate("item.takumiknife.tooltip") + " " + i);
    }
}
