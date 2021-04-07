package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiEnchantmentCore;
import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.ITakumiEntity;
import com.tntmodders.takumi.entity.item.EntityWaterTypeForce;
import com.tntmodders.takumi.entity.item.EntityWindTypeLance;
import com.tntmodders.takumi.item.material.TakumiToolMaterial;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

import static net.minecraft.item.ItemBow.getArrowVelocity;

public class ItemTypeSword extends ItemSword {
    private final ITakumiEntity.EnumTakumiType type;

    public ItemTypeSword(ITakumiEntity.EnumTakumiType type) {
        super(type == ITakumiEntity.EnumTakumiType.NORMAL ? TakumiToolMaterial.LIGHTSABER_MATERIAL :
                TakumiToolMaterial.TAKUMI_MATERIAL);
        this.type = type;
        this.setRegistryName(TakumiCraftCore.MODID, "typesword_" + type.getName());
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("typesword_" + type.getName());
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (target instanceof EntityTakumiAbstractCreeper) {
            ITakumiEntity.EnumTakumiType targetType = ((EntityTakumiAbstractCreeper) target).takumiType();
            float f = EntityTakumiAbstractCreeper.getTypeMatchFactor(this.type, targetType);
            f = ((float) Math.pow(f, 8));
            f *= 5;
            target.attackEntityFrom(DamageSource.causeMobDamage(attacker), f);
        }
        return this.performEffect(target, attacker) && super.hitEntity(stack, target, attacker);
    }

    public boolean performEffect(EntityLivingBase target, EntityLivingBase attacker) {
        switch (this.type) {
            case FIRE: {
                target.setFire(100);
                if (!attacker.world.isRemote) {
                    Vec3d vec3d = attacker.getLookVec().normalize();
                    for (double i = 1.5; i < 9; i += 1d) {
                        attacker.world.newExplosion(attacker, attacker.posX + vec3d.x * i * 2.5,
                                attacker.posY + vec3d.y * i * 2.5, attacker.posZ + vec3d.z * i * 2.5, 2f, true, false);
                    }
                }
                break;
            }
            case WATER: {
                if (!attacker.world.isRemote && target.isNonBoss()) {
                    EntityWaterTypeForce force = new EntityWaterTypeForce(attacker.world);
                    force.setPosition(target.posX, target.posY + 0.5, target.posZ);
                    attacker.world.spawnEntity(force);
                    target.setPosition(force.posX, force.posY, force.posZ);
                    target.startRiding(force, true);
                    target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 100, 10, true, false));
                }
                return true;
            }
            case GRASS: {
                if (!attacker.world.isRemote) {
                    attacker.world.createExplosion(attacker, target.posX, target.posY, target.posZ, 1f, false);
                    int i = 50;
                    while (i > 0) {
                        int j = EntityXPOrb.getXPSplit(i);
                        i -= j;
                        target.world.spawnEntity(
                                new EntityXPOrb(target.world, target.posX, target.posY, target.posZ, j));
                    }
                    attacker.addPotionEffect(new PotionEffect(MobEffects.INSTANT_HEALTH, 1, 5));
                }
                break;
            }
            case WIND: {
                if (!attacker.world.isRemote) {

                }
                break;
            }
            case GROUND: {
                if (!attacker.world.isRemote) {
                    for (int i = 0; i < 9; i++) {
                        double x = attacker.posX + Math.cos(Math.PI * i * 2 / 9) * 5;
                        double z = attacker.posZ + Math.sin(Math.PI * i * 2 / 9) * 5;
                        TakumiUtils.takumiCreateExplosion(attacker.world, attacker, x, attacker.posY, z, 2, false, false, 1.5f);
                    }
                }
                break;
            }
        }
        return true;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (this.type == ITakumiEntity.EnumTakumiType.NORMAL &&
                !EnchantmentHelper.getEnchantments(stack).containsKey(TakumiEnchantmentCore.ANTI_POWERED)) {
            try {
                stack.addEnchantment(TakumiEnchantmentCore.ANTI_POWERED, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (entityIn instanceof EntityLivingBase && ((EntityLivingBase) entityIn).getHeldItemMainhand() == stack) {
            if (EnchantmentHelper.getEnchantments(stack).containsKey(TakumiEnchantmentCore.TYPE_DEST)) {
                if (entityIn.ticksExisted % 20 == 0) {
                    worldIn.getEntities(EntityMob.class,
                            input -> entityIn.getDistanceSqToEntity(input) < 7f).forEach(entityLivingBase -> {
                        if (entityLivingBase.getClass() != entityIn.getClass() && !worldIn.isRemote) {
                            entityLivingBase.attackEntityFrom(
                                    DamageSource.causeMobDamage(((EntityLivingBase) entityIn)).setExplosion().setMagicDamage(), 5f);
                        }
                    });
                }
            }
            if (EnchantmentHelper.getEnchantments(stack).containsKey(TakumiEnchantmentCore.TYPE_MAGIC)) {
                worldIn.getEntities(EntityLivingBase.class,
                        input -> entityIn.getDistanceSqToEntity(input) < 16f).forEach(entityLivingBase -> {
                    if (entityLivingBase.getClass() != entityIn.getClass()) {
                        entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 5, 2, true, false));
                    }
                });
            }
        }
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        if (this.type == ITakumiEntity.EnumTakumiType.NORMAL &&
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
        return true;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return this.type == ITakumiEntity.EnumTakumiType.NORMAL ? EnumRarity.EPIC : EnumRarity.RARE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.type == ITakumiEntity.EnumTakumiType.NORMAL && this.isInCreativeTab(tab)) {
            ItemStack itemStack = new ItemStack(this, 1);
            itemStack.addEnchantment(TakumiEnchantmentCore.ANTI_POWERED, 1);
            items.add(itemStack);
        } else {
            super.getSubItems(tab, items);
        }
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return (toRepair.getItem() != TakumiItemCore.TAKUMI_TYPE_SWORD_NORMAL &&
                repair.getItem() == TakumiItemCore.TAKUMI_TYPE_CORE && repair.getMetadata() + 1 == type.getId()) || super.getIsRepairable(toRepair, repair);
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
        if (this.type == ITakumiEntity.EnumTakumiType.WIND && entityLiving.onGround) {
            performEffect(null, entityLiving);
            if (!(entityLiving instanceof EntityPlayer) || !((EntityPlayer) entityLiving).isCreative()) {
                stack.damageItem(1, entityLiving);
            }
        }
        return super.onEntitySwing(entityLiving, stack);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (stack.getItem() != TakumiItemCore.TAKUMI_TYPE_SWORD_NORMAL) {
            tooltip.add(TakumiUtils.takumiTranslate("takumicraft.message.typesword." + this.type.getName() + ".1"));
            tooltip.add(TakumiUtils.takumiTranslate("takumicraft.message.typesword." + this.type.getName() + ".2"));
        } else {
            tooltip.add(TakumiUtils.takumiTranslate("takumicraft.message.typesword.normal.1"));
            tooltip.add(TakumiUtils.takumiTranslate("takumicraft.message.typesword.normal.2"));
        }
        TakumiUtils.addSpiltInfo(stack, tooltip);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer && stack.getItem() == TakumiItemCore.TAKUMI_TYPE_SWORD_WIND) {
            EntityPlayer entityplayer = (EntityPlayer) entityLiving;
            int i = this.getMaxItemUseDuration(stack) - timeLeft;
            float f = getArrowVelocity(i);
            if ((double) f >= 0.1D) {
                if (!worldIn.isRemote) {
                    EntityWindTypeLance lance = new EntityWindTypeLance(worldIn, entityplayer);
                    lance.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
                    lance.setAim(entityplayer, entityplayer.rotationPitch < 0 ? entityplayer.rotationPitch / 6 : 0, entityplayer.rotationYaw, 10F, 5.0F, 0.2F);
                    worldIn.spawnEntity(lance);
                    entityplayer.setPosition(lance.posX, lance.posY, lance.posZ);
                    entityplayer.startRiding(lance, true);
                    stack.damageItem(1, entityplayer);
                }
                worldIn.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                entityplayer.addStat(StatList.getObjectUseStats(this));
            }
        }
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return stack.getItem() == TakumiItemCore.TAKUMI_TYPE_SWORD_WIND ? 72000 : super.getMaxItemUseDuration(stack);
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return stack.getItem() == TakumiItemCore.TAKUMI_TYPE_SWORD_WIND ? EnumAction.BOW : super.getItemUseAction(stack);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (playerIn.getHeldItem(handIn).getItem() == TakumiItemCore.TAKUMI_TYPE_SWORD_WIND) {
            playerIn.setActiveHand(handIn);
            return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
        } else {
            return super.onItemRightClick(worldIn, playerIn, handIn);
        }
    }
}
