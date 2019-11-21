package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameType;
import net.minecraft.world.World;

public class ItemTester extends Item {
    public ItemTester() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "tester");
        this.setUnlocalizedName("tester");
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer playerIn, Entity entity) {
        return false;
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        playerIn.setGameType(GameType.SPECTATOR);
        if (!worldIn.isRemote) {
            TakumiCraftCore.LOGGER.info("strt");
            Explosion e = worldIn.createExplosion(playerIn, playerIn.posX, 128, playerIn.posZ, 1000, true);
            TakumiCraftCore.LOGGER.info("fnshd");
            /* for (int x = -10; x <= 10; x++) {
                for (int z = -10; z <= 10; z++) {
                    if (x * x + z * z <= 100) {
                        EntityVillager villager = new EntityVillager(worldIn);
                        villager.setPosition(playerIn.getPosition().getX() + x, playerIn.getPosition().getY(), playerIn.getPosition().getZ() + z);
                        villager.setHealth(20);
                        villager.setNoAI(true);
                        if (!(x == 0 && z == 0)) {
                            worldIn.spawnEntity(villager);
                        }
                    }
                }
            }*/
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
