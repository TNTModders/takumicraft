package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.entity.ITakumiEvoEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemEvoCore extends Item {

    public ItemEvoCore() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "evocore");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("evocore");
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.UNCOMMON;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        if (!playerIn.world.isRemote && target instanceof ITakumiEvoEntity && !((ITakumiEvoEntity) target).isEvo()) {
            try {
                Entity entity = ((Entity) ((ITakumiEvoEntity) target).getEvoCreeper().getClass().getConstructor(World.class).newInstance(playerIn.world));
                entity.copyLocationAndAnglesFrom(target);
                target.setDead();
                playerIn.world.spawnEntity(entity);
                if (!playerIn.isCreative()) {
                    stack.shrink(1);
                }
                return true;
            } catch (Exception ignored) {
            }
        }
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
