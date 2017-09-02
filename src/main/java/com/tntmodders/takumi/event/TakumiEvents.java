package com.tntmodders.takumi.event;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiEnchantmentCore;
import com.tntmodders.takumi.core.TakumiEntityCore;
import com.tntmodders.takumi.entity.ITakumiEntity;
import com.tntmodders.takumi.entity.item.EntityTakumiArrow;
import com.tntmodders.takumi.entity.mobs.EntityHuskCreeper;
import com.tntmodders.takumi.entity.mobs.EntitySlimeCreeper;
import com.tntmodders.takumi.entity.mobs.EntityZombieCreeper;
import com.tntmodders.takumi.entity.mobs.EntityZombieVillagerCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Biomes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
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
        if (event.getEntityLiving() instanceof EntityCreeper && !((EntityCreeper) event.getEntityLiving()).getPowered()
                && ((EntityCreeper) event.getEntityLiving()).world.isThundering()) {
            TakumiUtils.takumiSetPowered(((EntityCreeper) event.getEntityLiving()), true);
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

    @SubscribeEvent
    public void checkSpawn(LivingSpawnEvent.CheckSpawn e) {
        if (!e.getWorld().isRemote) {
            if (e.getEntityLiving().getRNG().nextInt(10) == 0 && e.getEntityLiving() instanceof EntitySlime) {
                EntitySlimeCreeper slimeCreeper = new EntitySlimeCreeper(e.getWorld());
                slimeCreeper.copyLocationAndAnglesFrom(e.getEntityLiving());
                slimeCreeper.setSlimeSize(e.getEntityLiving().getRNG().nextBoolean() ? 1 : e.getEntityLiving().getRNG().nextBoolean() ? 2 : 4, false);
                e.getWorld().spawnEntity(slimeCreeper);
                e.setResult(Event.Result.DENY);
            } else if ((e.getEntityLiving().getClass() == EntityZombieCreeper.class || e.getEntityLiving().getClass() == EntityZombieVillagerCreeper.class) &&
                    (e.getWorld().getBiome(e.getEntityLiving().getPosition()) == Biomes.DESERT || e.getWorld().getBiome(e.getEntityLiving().getPosition()) == Biomes.DESERT_HILLS ||
                            e.getWorld().getBiome(e.getEntityLiving().getPosition()) == Biomes.MUTATED_DESERT)) {
                EntityHuskCreeper huskCreeper = new EntityHuskCreeper(e.getWorld());
                huskCreeper.copyLocationAndAnglesFrom(e.getEntityLiving());
                e.getWorld().spawnEntity(huskCreeper);
                e.setResult(Event.Result.DENY);
            }
        }
    }

    @SubscribeEvent
    public void damage(LivingAttackEvent event) {
        if (event.getSource().isExplosion() && event.getSource().getTrueSource() instanceof EntityTakumiArrow &&
                ((EntityTakumiArrow) event.getSource().getTrueSource()).shootingEntity == event.getEntity()) {
            TakumiCraftCore.LOGGER.info("info");
        } else if (!event.getSource().isMagicDamage() && event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof EntityLivingBase) {
            ItemStack stack = ((EntityLivingBase) event.getSource().getTrueSource()).getHeldItemMainhand();
            if (!EnchantmentHelper.getEnchantments(stack).isEmpty() && EnchantmentHelper.getEnchantments(stack).containsKey(TakumiEnchantmentCore.ANTI_POWERED) &&
                    event.getEntityLiving() instanceof EntityCreeper && ((EntityCreeper) event.getEntityLiving()).getPowered()) {
                event.getEntityLiving().attackEntityFrom(DamageSource.causeMobDamage(((EntityLivingBase) event.getSource().getTrueSource())).setMagicDamage(), 20f);
                TakumiUtils.takumiSetPowered(((EntityCreeper) event.getEntityLiving()), false);
                if (event.getSource().getTrueSource() instanceof EntityPlayerMP) {
                    TakumiUtils.giveAdvancementImpossible(((EntityPlayerMP) event.getSource().getTrueSource()),
                            new ResourceLocation(TakumiCraftCore.MODID, "creeperbomb"),
                            new ResourceLocation(TakumiCraftCore.MODID, "disarmament"));
                }
            }
        }
    }

    @SubscribeEvent
    public void onKillEntity(LivingDeathEvent event) {
        if ((event.getEntityLiving() instanceof ITakumiEntity || event.getEntityLiving() instanceof EntityCreeper) &&
                event.getSource().getTrueSource() instanceof EntityPlayerMP) {
            boolean isOK = true;
            for (ITakumiEntity takumiEntity : TakumiEntityCore.entityList) {
                if (!TakumiUtils.getAdvancementUnlocked(new ResourceLocation(TakumiCraftCore.MODID, "slay_" + takumiEntity.getRegisterName()))
                        && takumiEntity.getClass() != event.getEntityLiving().getClass()) {
                    isOK = false;
                    break;
                }
            }
            if (isOK && event.getSource().getTrueSource() instanceof EntityPlayerMP) {
                TakumiUtils.giveAdvancementImpossible(((EntityPlayerMP) event.getSource().getTrueSource()), new ResourceLocation(TakumiCraftCore.MODID, "creeperbomb"),
                        new ResourceLocation(TakumiCraftCore.MODID, "allcomplete"));
            }
        }
    }
}
