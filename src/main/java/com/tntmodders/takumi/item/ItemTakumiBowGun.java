package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiItemCore;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTakumiBowGun extends Item {
    public ItemTakumiBowGun() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "takumibowgun");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("takumibowgun");
        this.maxStackSize = 1;
        this.setMaxDamage(255);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer) {
            ItemStack itemstack = entityLiving.getHeldItemMainhand();
            ItemStack arrow = entityLiving.getHeldItemOffhand();
            boolean flg = false;
            if (arrow.getItem() == TakumiItemCore.TAKUMI_ARROW_HA) {
                EntityArrow takumiArrow = TakumiItemCore.TAKUMI_ARROW_HA.createArrow(worldIn, arrow, entityLiving);
                takumiArrow.setAim(entityLiving, entityLiving.rotationPitch, entityLiving.rotationYaw, 0.0F, 5 * 3.0F,
                        1.0F);
                takumiArrow.setIsCritical(true);
                takumiArrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
                flg = worldIn.spawnEntity(takumiArrow);
                entityLiving.knockBack(entityLiving, 1, takumiArrow.motionX, takumiArrow.motionZ);
            }
            if (flg) {
                if (!(entityLiving instanceof EntityPlayer && ((EntityPlayer) entityLiving).isCreative())) {
                    arrow.shrink(1);
                    itemstack.damageItem(1, entityLiving);
                }
                ((EntityPlayer) entityLiving).getCooldownTracker().setCooldown(this, 20);
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        playerIn.setActiveHand(handIn);
        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }
}
