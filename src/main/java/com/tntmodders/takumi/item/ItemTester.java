package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.entity.mobs.noncreeper.EntityDarkVillager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemTester extends Item {
    public ItemTester() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "tester");
        this.setUnlocalizedName("tester");
    }

/*
    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer playerIn, Entity entity) {
        if (!entity.world.isRemote && playerIn.isCreative() && entity instanceof EntityMinecart) {
            entity.setInvisible(true);
        }
        return true;
    }
*/


    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (playerIn.getName().equals("tomkate")) {
            if (!worldIn.getEntitiesWithinAABB(EntityDarkVillager.class, playerIn.getEntityBoundingBox().grow(5)).isEmpty()) {
                worldIn.getEntitiesWithinAABB(EntityDarkVillager.class, playerIn.getEntityBoundingBox().grow(5)).forEach(entityDarkVillager -> {
                    entityDarkVillager.setHealth(0.1f);
                    entityDarkVillager.setDead();
                });
                return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
            }
        } else if (!playerIn.isCreative()) {
            Explosion explosion = worldIn.createExplosion(playerIn, playerIn.posX, playerIn.posY, playerIn.posZ, 3f, false);
            playerIn.attackEntityFrom(DamageSource.causeExplosionDamage(explosion).setDamageIsAbsolute(), 1000f);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add("匠を創りし者のみ扱える錫杖。余人は振るうこと能わず、其の身は爆発四散する。");
    }
}
