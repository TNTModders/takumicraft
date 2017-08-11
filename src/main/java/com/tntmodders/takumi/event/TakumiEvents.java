package com.tntmodders.takumi.event;

import com.tntmodders.takumi.entity.ITakumiEntity;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TakumiEvents {
    @SubscribeEvent
    public void onPickupItem(EntityItemPickupEvent event) {
        TakumiUtils.takumiUnlockRecipes(event.getItem().getItem(), event.getEntityPlayer());
    }

    @SubscribeEvent
    public void onCloseContainer(PlayerContainerEvent.Close event) {
        for (ItemStack itemStack : event.getEntityPlayer().inventoryContainer.getInventory()) {
            TakumiUtils.takumiUnlockRecipes(itemStack, event.getEntityPlayer());
        }
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof EntityCreeper && !((EntityCreeper) event.getEntityLiving()).getPowered() && ((EntityCreeper) event.getEntityLiving()).world.isThundering()) {
            TakumiUtils.takumiSetPowered(event.getEntityLiving());
        }
    }

    @SubscribeEvent
    public void onExplosion(ExplosionEvent.Detonate event) {
        if (event.getExplosion().getExplosivePlacedBy() instanceof ITakumiEntity) {
            boolean flg = ((ITakumiEntity) event.getExplosion().getExplosivePlacedBy()).takumiExplodeEvent(event);
            if (!flg) {
                event.setCanceled(true);
            }
        }
    }

}
