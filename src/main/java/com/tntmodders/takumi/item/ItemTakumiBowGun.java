package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.block.BlockTakumiMonsterBomb;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.entity.item.EntityTakumiArrow;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

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
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (handIn != EnumHand.MAIN_HAND) {
            return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
        }
        ItemStack stack = playerIn.getHeldItemOffhand();
        if (!(stack.getItem() instanceof ItemArrow ||
                stack.getItem() == Item.getItemFromBlock(TakumiBlockCore.CREEPER_BOMB) ||
                (stack.getItem() instanceof ItemBlock &&
                        ((ItemBlock) stack.getItem()).getBlock() instanceof BlockTakumiMonsterBomb))) {
            return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
        }
        playerIn.setActiveHand(handIn);
        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer) {
            ItemStack itemstack = entityLiving.getHeldItemMainhand();
            ItemStack arrow = entityLiving.getHeldItemOffhand();
            boolean flg = false;
            EntityArrow takumiArrow = null;
            if (arrow.getItem() == TakumiItemCore.TAKUMI_ARROW_BAKU) {
                takumiArrow = ((ItemArrow) arrow.getItem()).createArrow(worldIn, arrow, entityLiving);
                takumiArrow.setAim(entityLiving, entityLiving.rotationPitch, entityLiving.rotationYaw, 0.0F, 1f * 3.0F,
                        0f);
                takumiArrow.setIsCritical(true);
                takumiArrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
                takumiArrow.shootingEntity = entityLiving;
                flg = worldIn.spawnEntity(takumiArrow);
            } else if (arrow.getItem() instanceof ItemArrow) {
                takumiArrow = ((ItemArrow) arrow.getItem()).createArrow(worldIn, arrow, entityLiving);
                takumiArrow.setAim(entityLiving, entityLiving.rotationPitch, entityLiving.rotationYaw, 0.0F, 5f * 3.0F,
                        0f);
                takumiArrow.setIsCritical(true);
                takumiArrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
                takumiArrow.shootingEntity = entityLiving;
                flg = worldIn.spawnEntity(takumiArrow);
            } else if (arrow.getItem() instanceof ItemBlock &&
                    Block.getBlockFromItem(arrow.getItem()) instanceof BlockTakumiMonsterBomb) {
                BlockTakumiMonsterBomb bomb = ((BlockTakumiMonsterBomb) Block.getBlockFromItem(arrow.getItem()));
                takumiArrow =
                        new EntityTakumiArrow(worldIn, new ItemStack(TakumiItemCore.TAKUMI_ARROW_HA), entityLiving,
                                bomb.getEntityClass());
                takumiArrow.setAim(entityLiving, entityLiving.rotationPitch, entityLiving.rotationYaw, 0.0F, 5f * 3.0F,
                        0f);
                takumiArrow.setIsCritical(true);
                takumiArrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
                takumiArrow.shootingEntity = entityLiving;
                flg = worldIn.spawnEntity(takumiArrow);
            } else if (arrow.getItem() == Item.getItemFromBlock(TakumiBlockCore.CREEPER_BOMB)) {
                takumiArrow = new EntityTakumiArrow(worldIn, entityLiving, 1, 5, true, null,
                        EntityTakumiArrow.EnumArrowType.NORMAL);
                takumiArrow.setAim(entityLiving, entityLiving.rotationPitch, entityLiving.rotationYaw, 0.0F, 5f * 3.0F,
                        1.0F);
                takumiArrow.setIsCritical(true);
                takumiArrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
                takumiArrow.shootingEntity = entityLiving;
                flg = worldIn.spawnEntity(takumiArrow);
            }
            if (flg && takumiArrow != null) {
                if (!worldIn.isRemote) {
                    worldIn.createExplosion(entityLiving, entityLiving.posX, entityLiving.posY, entityLiving.posZ, 0f,
                            false);
                }
                //entityLiving.knockBack(entityLiving, 1, takumiArrow.motionX, takumiArrow.motionZ);
                if (!(entityLiving instanceof EntityPlayer && ((EntityPlayer) entityLiving).isCreative())) {
                    arrow.shrink(1);
                    itemstack.damageItem(1, entityLiving);
                }
                ((EntityPlayer) entityLiving).getCooldownTracker().setCooldown(this, 20);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack,
                               @Nullable
                                       World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TakumiUtils.takumiTranslate("takumicraft.message.bowgun"));
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
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }


    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        if (entityItem instanceof EntityItem) {
            entityItem.setEntityInvulnerable(entityItem.ticksExisted < 1200);
        }
        return super.onEntityItemUpdate(entityItem);
    }
}
