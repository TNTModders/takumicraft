package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTakumiBoltStone extends Item {

    public ItemTakumiBoltStone() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "boltstone");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("boltstone");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        Vec3d vec3d = playerIn.getLookVec();
        vec3d = vec3d.normalize().scale(5);
        EntityLightningBolt bolt = new EntityLightningBolt(worldIn, playerIn.posX + vec3d.x, playerIn.posY + vec3d.y,
                playerIn.posZ + vec3d.z, false);
        worldIn.addWeatherEffect(bolt);
        worldIn.spawnEntity(bolt);
        if (!playerIn.isCreative()) {
            playerIn.getHeldItem(handIn).shrink(1);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        Vec3d vec3d = entityLiving.getLookVec();
        vec3d = vec3d.normalize().scale(5);
        EntityLightningBolt bolt =
                new EntityLightningBolt(worldIn, entityLiving.posX + vec3d.x, entityLiving.posY + vec3d.y,
                        entityLiving.posZ + vec3d.z, false);
        worldIn.addWeatherEffect(bolt);
        worldIn.spawnEntity(bolt);
        if (entityLiving instanceof EntityPlayer && !((EntityPlayer) entityLiving).isCreative()) {
            stack.shrink(1);
        }
        return stack;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        EntityLightningBolt bolt =
                new EntityLightningBolt(attacker.world, target.posX, target.posY, target.posZ, false);
        attacker.world.addWeatherEffect(bolt);
        attacker.world.spawnEntity(bolt);
        if (!(attacker instanceof EntityPlayer) || !((EntityPlayer) attacker).isCreative()) {
            stack.shrink(1);
        }
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        if (entityItem instanceof EntityItem) {
            entityItem.setEntityInvulnerable(entityItem.ticksExisted < 1200);
        }
        return super.onEntityItemUpdate(entityItem);
    }
}
