package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.entity.item.EntityTakumiArrow;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTakumiArrow extends ItemArrow {

    public final int power;
    public final int pierce;
    public final boolean destroy;

    public ItemTakumiArrow(String name, int powerIn, int pierceIn, boolean destroyIn) {
        super();
        this.power = powerIn;
        this.pierce = pierceIn;
        this.destroy = destroyIn;
        this.setRegistryName(TakumiCraftCore.MODID, "takumiarrow_" + name);
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("takumiarrow_" + name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return stack.getItem() == TakumiItemCore.TAKUMI_ARROW_BAKU || super.hasEffect(stack);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return stack.getItem() == TakumiItemCore.TAKUMI_ARROW_BAKU ? EnumRarity.EPIC : super.getRarity(stack);
    }

    @Override
    public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
        if (shooter.getActiveItemStack().getItem() != TakumiItemCore.TAKUMI_BOW &&
                shooter.getActiveItemStack().getItem() != TakumiItemCore.TAKUMI_BOWGUN && shooter instanceof EntityPlayer) {
            return ((ItemArrow) Items.ARROW).createArrow(worldIn, stack, shooter);
        }
        if (stack.getItem() == TakumiItemCore.TAKUMI_ARROW_SAN) {
            return new EntityTakumiArrow(worldIn, new ItemStack(this), shooter, EntityTakumiArrow.EnumArrowType.SHOT);
        }
        if (stack.getItem() == TakumiItemCore.TAKUMI_ARROW_KAN) {
            return new EntityTakumiArrow(worldIn, new ItemStack(this), shooter, EntityTakumiArrow.EnumArrowType.PIERCE);
        }
        if (stack.getItem() == TakumiItemCore.TAKUMI_ARROW_BAKU) {
            return new EntityTakumiArrow(worldIn, new ItemStack(this), shooter, EntityTakumiArrow.EnumArrowType.LASER);
        }
        return new EntityTakumiArrow(worldIn, new ItemStack(this), shooter);
    }
}
