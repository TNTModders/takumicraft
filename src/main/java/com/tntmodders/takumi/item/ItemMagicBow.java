package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.core.TakumiItemCore;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityArrow.PickupStatus;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.*;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemMagicBow extends ItemBow {

    public ItemMagicBow() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "magicbow");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("magicbow");
        this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter() {
            @Override
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack,
                               @Nullable
                                       World worldIn,
                               @Nullable
                                       EntityLivingBase entityIn) {
                if (entityIn == null) {
                    return 0.0F;
                }
                return entityIn.getActiveItemStack().getItem() != TakumiItemCore.MAGIC_BOW ? 0.0F :
                        (float) (stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / 5.0F;
            }
        });
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return repair.getItem() == Item.getItemFromBlock(TakumiBlockCore.MAGIC_BLOCK) || super.getIsRepairable(toRepair, repair);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer) entityLiving;
            boolean flag = entityplayer.capabilities.isCreativeMode ||
                    EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
            ItemStack itemstack = this.findAmmo(entityplayer);

            int i = this.getMaxItemUseDuration(stack) - timeLeft;
            i = ForgeEventFactory.onArrowLoose(stack, worldIn, entityplayer, i, !itemstack.isEmpty() || flag);
            if (i < 0) {
                return;
            }

            if (!itemstack.isEmpty() || flag) {
                if (itemstack.isEmpty()) {
                    itemstack = new ItemStack(Items.ARROW);
                }

                float f = getArrowVelocity(i);

                if ((double) f >= 0.1D) {
                    boolean flag1 = entityplayer.capabilities.isCreativeMode ||
                            itemstack.getItem() instanceof ItemArrow &&
                                    ((ItemArrow) itemstack.getItem()).isInfinite(itemstack, stack, entityplayer);

                    if (!worldIn.isRemote) {
                        for (int i1 = -2; i1 <= 2; i1++) {
                            ItemArrow itemarrow =
                                    (ItemArrow) (itemstack.getItem() instanceof ItemArrow ? itemstack.getItem() :
                                            Items.ARROW);
                            EntityArrow entityarrow = itemarrow.createArrow(worldIn, itemstack, entityplayer);
                            entityarrow.setAim(entityplayer, entityplayer.rotationPitch,
                                    (float) (entityplayer.rotationYaw + 15 * i1 / Math.PI), 0.0F, f * 3.0F, 1.0F);

                            if (f == 1.0F) {
                                entityarrow.setIsCritical(true);
                            }

                            entityarrow.setDamage(entityarrow.getDamage() / 1.25);
                            int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

                            if (j > 0) {
                                entityarrow.setDamage(entityarrow.getDamage() + (double) j * 0.5D + 0.5D);
                            }

                            int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);

                            if (k > 0) {
                                entityarrow.setKnockbackStrength(k);
                            }

                            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
                                entityarrow.setFire(100);
                            }

                            stack.damageItem(1, entityplayer);

                            entityarrow.pickupStatus = PickupStatus.CREATIVE_ONLY;

                            worldIn.spawnEntity(entityarrow);
                        }
                    }

                    worldIn.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ,
                            SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F,
                            1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                    if (!flag1 && !entityplayer.capabilities.isCreativeMode) {
                        itemstack.shrink(1);

                        if (itemstack.isEmpty()) {
                            entityplayer.inventory.deleteStack(itemstack);
                        }
                    }

                    entityplayer.addStat(StatList.getObjectUseStats(this));
                }
            }
        }
    }

    private ItemStack findAmmo(EntityPlayer player) {
        if (this.isArrow(player.getHeldItem(EnumHand.OFF_HAND))) {
            return player.getHeldItem(EnumHand.OFF_HAND);
        }
        if (this.isArrow(player.getHeldItem(EnumHand.MAIN_HAND))) {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        }
        for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
            ItemStack itemstack = player.inventory.getStackInSlot(i);

            if (this.isArrow(itemstack)) {
                return itemstack;
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    protected boolean isArrow(ItemStack stack) {
        return stack.getItem() == Items.ARROW || stack.getItem() == Items.TIPPED_ARROW || stack.getItem() == Items.SPECTRAL_ARROW;
    }
}
