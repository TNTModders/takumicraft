package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiEnchantmentCore;
import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.ITakumiEntity;
import com.tntmodders.takumi.entity.item.EntityWaterTypeForce;
import com.tntmodders.takumi.entity.item.EntityWindLance;
import com.tntmodders.takumi.item.material.TakumiToolMaterial;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
        boolean flg = this.performEffect(target, attacker);
        if (flg && target instanceof EntityTakumiAbstractCreeper) {
            ITakumiEntity.EnumTakumiType targetType = ((EntityTakumiAbstractCreeper) target).takumiType();
            float f = EntityTakumiAbstractCreeper.getTypeMatchFactor(this.type, targetType);
            f = f * f * f * f * f * f * f * f;
            f *= 4;
            target.attackEntityFrom(DamageSource.causeMobDamage(attacker), f);
        }
        return flg && super.hitEntity(stack, target, attacker);
    }

    public boolean performEffect(EntityLivingBase target, EntityLivingBase attacker) {
        switch (this.type) {
            case FIRE: {
                target.setFire(100);
                if (!attacker.world.isRemote) {
                    Vec3d vec3d = attacker.getLookVec().normalize();
                    for (double i = 1.5; i < 7; i += 1d) {
                        attacker.world.newExplosion(attacker, attacker.posX + vec3d.x * i * 2,
                                attacker.posY + vec3d.y * i * 2, attacker.posZ + vec3d.z * i * 2, 2f, true, false);
                    }
                }
                break;
            }
            case WATER: {
                if (!attacker.world.isRemote) {
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
                    for (int t = 0; t < 4; t++) {
                        double x = target.posX + MathHelper.getInt(target.getRNG(), -3, 3);
                        double y = target.posY + 1;
                        double z = target.posZ + MathHelper.getInt(target.getRNG(), -3, 3);
                        attacker.world.createExplosion(attacker, x, y, z, 2f, false);
                    }
                    int i = 20;
                    while (i > 0) {
                        int j = EntityXPOrb.getXPSplit(i);
                        i -= j;
                        target.world.spawnEntity(
                                new EntityXPOrb(target.world, target.posX, target.posY, target.posZ, j));
                    }
                    attacker.addPotionEffect(new PotionEffect(MobEffects.INSTANT_HEALTH, 1, 0));
                }
                break;
            }
            case WIND: {
                if (!attacker.world.isRemote) {
                    EntityWindLance lance = new EntityWindLance(attacker.world, attacker);
                    lance.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
                    lance.setAim(attacker, 0, attacker.rotationYaw, 0.0F, 1.0F, 1.0F);
                    attacker.world.spawnEntity(lance);
                    attacker.setPosition(lance.posX, lance.posY, lance.posZ);
                    attacker.startRiding(lance, true);
                }
                break;
            }
            case GROUND: {
                if (!attacker.world.isRemote) {
                    for (int i = 0; i < 9; i++) {
                        double x = attacker.posX + Math.cos(Math.PI * i * 2 / 9) * 4;
                        double z = attacker.posZ + Math.sin(Math.PI * i * 2 / 9) * 4;
                        attacker.world.createExplosion(attacker, x, attacker.posY, z, 2, false);
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
                                    DamageSource.causeMobDamage(((EntityLivingBase) entityIn)).setExplosion(), 5f);
                        }
                    });
                }
            }
            if (EnchantmentHelper.getEnchantments(stack).containsKey(TakumiEnchantmentCore.TYPE_MAGIC)) {
                worldIn.getEntities(EntityLivingBase.class,
                        input -> entityIn.getDistanceSqToEntity(input) < 16f).forEach(entityLivingBase -> {
                    if (entityLivingBase.getClass() != entityIn.getClass() && !worldIn.isRemote) {
                        entityLivingBase.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 200, 2));
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
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
        if (this.type == ITakumiEntity.EnumTakumiType.WIND && entityLiving.onGround) {
            performEffect(null, entityLiving);
            if (!(entityLiving instanceof EntityPlayer) || !((EntityPlayer) entityLiving).isCreative()) {
                stack.damageItem(1, entityLiving);
            }
        }
        return super.onEntitySwing(entityLiving, stack);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return (toRepair.getItem() != TakumiItemCore.TAKUMI_TYPE_SWORD_NORMAL &&
                repair.getItem() == TakumiItemCore.TAKUMI_TYPE_CORE && repair.getMetadata() + 1 == type.getId()) || super.getIsRepairable(toRepair, repair);
    }
}
