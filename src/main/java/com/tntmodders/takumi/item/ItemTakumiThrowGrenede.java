package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.entity.item.EntityTakumiCannon;
import com.tntmodders.takumi.entity.item.EntityTakumiCannonBall;
import com.tntmodders.takumi.entity.item.EntityTakumiThrowGrenede;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemTakumiThrowGrenede extends Item {

    public ItemTakumiThrowGrenede() {
        this.setRegistryName(TakumiCraftCore.MODID, "takumithrowgrenede");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("takumithrowgrenede");
    }

    /**
     * Called when the equipped item is right clicked.
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (!playerIn.capabilities.isCreativeMode) {
            itemstack.shrink(1);
        }

        worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_SNOWBALL_THROW,
                SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!worldIn.isRemote) {
            EntityTakumiThrowGrenede grenede = new EntityTakumiThrowGrenede(worldIn, playerIn);
            grenede.setHeadingFromThrower(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 2F,
                    1.25F);
            worldIn.spawnEntity(grenede);
        }

        playerIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
        if (entityLiving instanceof EntityPlayer && entityLiving.isRiding() && entityLiving.getRidingEntity() instanceof EntityTakumiCannon) {
            if (!entityLiving.world.isRemote) {
                EntityTakumiCannonBall grenede = new EntityTakumiCannonBall(entityLiving.world, entityLiving);
                grenede.setHeadingFromThrower(entityLiving, ((EntityTakumiCannon) entityLiving.getRidingEntity()).getFacing().getHorizontalAngle(),
                        entityLiving.getRidingEntity().rotationPitch, 0.0F, 5F, 0.5F);
                entityLiving.world.spawnEntity(grenede);
            }
        }
        return super.onEntitySwing(entityLiving, stack);
    }
}
