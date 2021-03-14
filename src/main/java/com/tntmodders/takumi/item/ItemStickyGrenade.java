package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.entity.item.EntityStickyGrenade;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemStickyGrenade extends Item {

    public ItemStickyGrenade() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "stickygrenade");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("stickygrenade");
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
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
        //KnuckleCluster
        if (false) {
            EntityStickyGrenade dummy = new EntityStickyGrenade(player.world, player);
            dummy.setPosition(dummy.posX - 0.3 * Math.cos(Math.toRadians(player.rotationYaw)), dummy.posY, dummy.posZ - 0.3 * Math.sin(Math.toRadians(player.rotationYaw)));
            dummy.isDummy = true;
            float f = 1f;
            dummy.setAim(player, player.rotationPitch, player.rotationYaw, 0f, f * 1.5f, 0f);
            double dx = dummy.motionX;
            double dy = dummy.motionY;
            double dz = dummy.motionZ;
            double prevX = dummy.posX;
            double prevY = dummy.posY;
            double prevZ = dummy.posZ;
            for (int i = 0; i < 1200; i++) {
/*            double x = prevX + dx;
            double y = prevY + dy - 0.05000000074505806D;
            double z = prevZ + dz;
            if (FMLCommonHandler.instance().getSide().isClient()) {
                this.spawnParticle(player.world, x, y, z);
            }
            prevX = x;
            prevY = y;
            prevZ = z;
            dx = dx * 0.99;
            dy = dy * 0.99;
            dz = dz * 0.99;*/
                dummy.onUpdate();
                //this.spawnParticle(dummy.world, dummy.posX, dummy.posY, dummy.posZ);
            }
        }
    }

/*    @SideOnly(Side.CLIENT)
    private void spawnParticle(World world, double x, double y, double z) {
        Minecraft.getMinecraft().effectRenderer.addEffect(new EntityStickyGrenade.ParticleClusterDummy(world, x, y, z));
    }*/


    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        EntityPlayer player = (EntityPlayer) entityLiving;
        int i = this.getMaxItemUseDuration(stack) - timeLeft;
        float f = 1f;
        if ((double) f >= 0.1D) {
            if (!worldIn.isRemote) {
                EntityStickyGrenade grenade = new EntityStickyGrenade(worldIn, player);
                grenade.setPosition(grenade.posX - 0.3 * Math.cos(Math.toRadians(player.rotationYaw)), grenade.posY, grenade.posZ - 0.3 * Math.sin(Math.toRadians(player.rotationYaw)));
                grenade.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
                grenade.setAim(player, player.rotationPitch, player.rotationYaw, 0f, f * 1.5f, 0f);
                worldIn.spawnEntity(grenade);
            }
            worldIn.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
            player.addStat(StatList.getObjectUseStats(this));
            if (!player.isCreative()) {
                stack.shrink(1);
                if (stack.isEmpty()) {
                    player.inventory.deleteStack(stack);
                }
            }
        }
    }
}
