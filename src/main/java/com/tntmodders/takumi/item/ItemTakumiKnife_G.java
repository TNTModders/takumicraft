package com.tntmodders.takumi.item;

import com.google.common.collect.Multimap;
import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiEnchantmentCore;
import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.entity.item.EntityTakumiKnifeGun;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class ItemTakumiKnife_G extends Item {
    protected static final UUID SWORD_HEALTH_MODIFIER = UUID.fromString("7ABE2D17-F418-4EC4-BFC2-E7B2A1AB89B2");

    public ItemTakumiKnife_G() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "takumiknife_gun");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("takumiknife_gun");
        this.setMaxStackSize(1);
        this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter() {
            @Override
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                if (entityIn == null) {
                    return 0.0F;
                }
                return entityIn.getActiveItemStack().getItem() != TakumiItemCore.TAKUMI_KNIFE_GUN ? 0.0F :
                        (float) (stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / 40.0F;
            }
        });
        this.addPropertyOverride(new ResourceLocation("pulling"), new IItemPropertyGetter() {
            @Override
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);
        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
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
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TakumiUtils.takumiTranslate("takumicraft.message.takumiknife"));
        TakumiUtils.addSpiltInfo(stack, tooltip);
        int i = 0;
        if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("Power")) {
            i = stack.getTagCompound().getInteger("Power");
        }
        tooltip.add(TakumiUtils.takumiTranslate("item.takumiknife.tooltip") + " " + i);
    }


    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer) {
            if (true) {
                int i = this.getMaxItemUseDuration(stack) - timeLeft;
                if (i > 40) {
                    if (entityLiving.getActiveItemStack() == stack) {
                        ItemStack itemStack = new ItemStack(TakumiItemCore.TAKUMI_KNIFE);
                        itemStack.setTagCompound(stack.getTagCompound());
                        entityLiving.setHeldItem(entityLiving.getActiveHand(), itemStack);
                    }
                }
            }
        }
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        playerIn.setActiveHand(handIn);
        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
        int i = this.getMaxItemUseDuration(stack) - count;
        if (player.world.isRemote && i % 16 == 1 && i < 40) {
            player.playSound(SoundEvents.BLOCK_ANVIL_USE, 0.25f, 1f);
        } else
            super.onUsingTick(stack, player, count);
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
        if (entityLiving instanceof EntityPlayer) {
            if (!((EntityPlayer) entityLiving).getCooldownTracker().hasCooldown(stack.getItem())) {
                if (!entityLiving.world.isRemote) {
                    NBTTagCompound compound = stack.getTagCompound() == null ? new NBTTagCompound() : stack.getTagCompound();
                    boolean flg = ((EntityPlayer) entityLiving).isCreative();
                    if (compound.hasKey("Power")) {
                        int i = compound.getInteger("Power");
                        if (i > 0 || flg) {
                            if (!flg) {
                                compound.setInteger("Power", i - 1);
                            }
                            EntityTakumiKnifeGun grenede = new EntityTakumiKnifeGun(entityLiving.world, entityLiving);
                            grenede.setThrowableHeading(entityLiving.getLookVec().x, entityLiving.getLookVec().y, entityLiving.getLookVec().z, 5f, 0);
                            entityLiving.world.spawnEntity(grenede);
                            stack.setTagCompound(compound);
                            ((EntityPlayer) entityLiving).getCooldownTracker().setCooldown(this, 10);
                        }
                    } else {
                        compound.setInteger("Power", 0);
                        stack.setTagCompound(compound);
                    }
                }
                return true;
            }
            return false;
        }
        return true;
    }
}
