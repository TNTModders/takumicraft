package com.tntmodders.takumi.event;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.block.BlockTakumiAcid;
import com.tntmodders.takumi.core.*;
import com.tntmodders.takumi.entity.ITakumiEntity;
import com.tntmodders.takumi.entity.ai.EntityAIFollowCatCreeper;
import com.tntmodders.takumi.entity.item.AbstractEntityTakumiGrenade;
import com.tntmodders.takumi.entity.item.EntityTakumiArrow;
import com.tntmodders.takumi.entity.item.EntityTakumiPotion;
import com.tntmodders.takumi.entity.mobs.*;
import com.tntmodders.takumi.utils.TakumiUtils;
import com.tntmodders.takumi.world.TakumiExplosion;
import com.tntmodders.takumi.world.gen.TakumiMapGenDarkShrine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.DimensionType;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent.Close;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class TakumiEvents {

    @SubscribeEvent
    public void onPickupItem(EntityItemPickupEvent event) {
    }

    @SubscribeEvent
    public void onConfigChanged(OnConfigChangedEvent event) {
        // コンフィグが変更された時に呼ばれる。
        if (Objects.equals(event.getModID(), TakumiCraftCore.MODID)) {
            TakumiConfigCore.syncConfig();
        }
    }

    @SubscribeEvent
    public void onCloseContainer(Close event) {
    }

    @SubscribeEvent
    public void onUpdate(LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof EntityCreeper &&
                !((EntityCreeper) event.getEntityLiving()).getPowered() &&
                (((EntityCreeper) event.getEntityLiving()).world.isThundering() ||
                        event.getEntity().world.provider.getDimension() == TakumiWorldCore.TAKUMI_WORLD.getId())) {
            TakumiUtils.takumiSetPowered((EntityCreeper) event.getEntityLiving(), true);
        }
        if (event.getEntityLiving() instanceof EntityParrot) {
            if (event.getEntityLiving().getEntityData().hasKey("creeper") &&
                    event.getEntityLiving().getEntityData().getBoolean("creeper")) {
                ((EntityParrot) event.getEntityLiving()).world
                        .createExplosion(event.getEntityLiving(), ((EntityParrot) event.getEntityLiving()).posX,
                                ((EntityParrot) event.getEntityLiving()).posY,
                                ((EntityParrot) event.getEntityLiving()).posZ, 4f, true);
                event.getEntityLiving().setDead();
            }
        }
    }

    @SubscribeEvent
    public void onExplosion(Detonate event) {
        if (!event.getWorld().isRemote) {
            event.getAffectedEntities().removeIf(entity -> entity instanceof EntityLivingBase &&
                    ((EntityLivingBase) entity).getActiveItemStack().getItem() == TakumiItemCore.TAKUMI_SHIELD);
            if (event.getWorld().getBlockState(new BlockPos(event.getExplosion().getPosition())).getBlock() ==
                    TakumiBlockCore.ACID_BLOCK) {
                IBlockState state = event.getWorld().getBlockState(new BlockPos(event.getExplosion().getPosition()));
                int i = state.getValue(BlockTakumiAcid.META) + 1;
                event.getAffectedBlocks().forEach(pos -> {
                    if (i < 16) {
                        for (EnumFacing facing : EnumFacing.values()) {
                            BlockPos blockPos = pos.offset(facing);
                            if (!event.getAffectedBlocks().contains(blockPos) &&
                                    !event.getWorld().isAirBlock(blockPos) && event.getWorld().getBlockState(blockPos)
                                                                                   .getBlockHardness(event.getWorld(),
                                                                                           blockPos) != -1 &&
                                    event.getWorld().getBlockState(blockPos).getBlock() != TakumiBlockCore.ACID_BLOCK) {
                                event.getWorld().setBlockState(blockPos, TakumiBlockCore.ACID_BLOCK.getDefaultState()
                                                                                                   .withProperty(
                                                                                                           BlockTakumiAcid.META,
                                                                                                           i));
                            }
                        }
                    }
                    event.getWorld().setBlockToAir(pos);
                });
                event.getAffectedBlocks().clear();
            }
        }
        if (event.getExplosion() instanceof TakumiExplosion) {
            if (((TakumiExplosion) event.getExplosion()).getExploder() instanceof AbstractEntityTakumiGrenade) {
                AbstractEntityTakumiGrenade grenade =
                        (AbstractEntityTakumiGrenade) ((TakumiExplosion) event.getExplosion()).getExploder();
                if (grenade.getThrower() != null) {
                    event.getAffectedEntities().remove(grenade.getThrower());
                }
            }
            if (((TakumiExplosion) event.getExplosion()).getExploder() instanceof EntityTakumiArrow) {
                EntityTakumiArrow takumiArrow =
                        (EntityTakumiArrow) ((TakumiExplosion) event.getExplosion()).getExploder();
                if (takumiArrow.shootingEntity instanceof EntityStrayCreeper) {
                    PotionType type = PotionUtils.getPotionFromItem(
                            ((EntityLivingBase) takumiArrow.shootingEntity).getHeldItem(EnumHand.OFF_HAND));
                    for (Entity entity : event.getAffectedEntities()) {
                        if (entity instanceof EntityLivingBase && entity != takumiArrow.shootingEntity) {
                            PotionEffect effect = new PotionEffect(type.getEffects().get(0).getPotion(), 400);
                            ((EntityLivingBase) entity).addPotionEffect(effect);
                        }
                    }
                }
            }
            if (((TakumiExplosion) event.getExplosion()).getExploder() instanceof EntityTakumiPotion) {
                for (Entity entity : event.getAffectedEntities()) {
                    if (entity instanceof EntityLivingBase) {
                        List<PotionEffect> effects = PotionUtils.getEffectsFromStack(
                                ((EntityPotion) ((TakumiExplosion) event.getExplosion()).getExploder()).getPotion());
                        for (PotionEffect effect : effects) {
                            ((EntityLivingBase) entity).addPotionEffect(effect);
                        }
                    }
                }
            }
        }
        if (event.getExplosion().getExplosivePlacedBy() instanceof ITakumiEntity) {
            boolean flg = ((ITakumiEntity) event.getExplosion().getExplosivePlacedBy()).takumiExplodeEvent(event);
            if (!flg) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void checkSpawn(LivingSpawnEvent.CheckSpawn e) {
        if (e.getWorld().provider.getDimensionType() == TakumiWorldCore.TAKUMI_WORLD) {
            if (!(e.getEntityLiving() instanceof ITakumiEntity)) {
                e.setResult(Result.DENY);
            } else {
                TakumiCraftCore.LOGGER.info(e.getEntityLiving());
            }
        }
        if (e.getEntityLiving().getClass() == EntityCreeper.class) {
            ((EntityLiving) e.getEntityLiving()).tasks
                    .addTask(0, new EntityAIFollowCatCreeper((EntityCreeper) e.getEntityLiving()));
        }
        if (!e.getWorld().isRemote) {
            if (e.getEntityLiving().getRNG().nextInt(5) == 0 && e.getEntityLiving() instanceof EntitySlime) {
                EntitySlimeCreeper slimeCreeper = new EntitySlimeCreeper(e.getWorld());
                slimeCreeper.copyLocationAndAnglesFrom(e.getEntityLiving());
                slimeCreeper.setSlimeSize(e.getEntityLiving().getRNG().nextBoolean() ? 1 :
                        e.getEntityLiving().getRNG().nextBoolean() ? 2 : 4, false);
                if (slimeCreeper.getCanSpawnHere()) {
                    e.getWorld().spawnEntity(slimeCreeper);
                    e.setResult(Result.DENY);
                }
            }
            if (e.getEntityLiving().getRNG().nextInt(10) == 0 && e.getEntityLiving() instanceof EntityFishCreeper) {
                EntityBigFishCreeper bigFishCreeper = new EntityBigFishCreeper(e.getWorld());
                bigFishCreeper.copyLocationAndAnglesFrom(e.getEntityLiving());
                if (bigFishCreeper.getCanSpawnHere()) {
                    e.getWorld().spawnEntity(bigFishCreeper);
                    e.setResult(Result.DENY);
                }
            } else if (e.getEntityLiving().getRNG().nextInt(10) == 0 && e.getEntityLiving() instanceof EntitySquid) {
                EntitySquidCreeper squidCreeper = new EntitySquidCreeper(e.getWorld());
                squidCreeper.copyLocationAndAnglesFrom(e.getEntityLiving());
                if (squidCreeper.getCanSpawnHere()) {
                    e.getWorld().spawnEntity(squidCreeper);
                    e.setResult(Result.DENY);
                }
            } else if ((e.getEntityLiving().getClass() == EntityZombieCreeper.class ||
                    e.getEntityLiving().getClass() == EntityZombieVillagerCreeper.class) &&
                    (e.getWorld().getBiome(e.getEntityLiving().getPosition()) == Biomes.DESERT ||
                            e.getWorld().getBiome(e.getEntityLiving().getPosition()) == Biomes.DESERT_HILLS ||
                            e.getWorld().getBiome(e.getEntityLiving().getPosition()) == Biomes.MUTATED_DESERT)) {
                EntityHuskCreeper huskCreeper = new EntityHuskCreeper(e.getWorld());
                huskCreeper.copyLocationAndAnglesFrom(e.getEntityLiving());
                if (huskCreeper.getCanSpawnHere()) {
                    e.getWorld().spawnEntity(huskCreeper);
                    e.setResult(Result.DENY);
                }
            } else if (e.getEntityLiving().getClass() == EntitySkeletonCreeper.class &&
                    (e.getWorld().getBiome(e.getEntityLiving().getPosition()) == Biomes.ICE_MOUNTAINS ||
                            e.getWorld().getBiome(e.getEntityLiving().getPosition()) == Biomes.ICE_PLAINS ||
                            e.getWorld().getBiome(e.getEntityLiving().getPosition()) == Biomes.COLD_BEACH ||
                            e.getWorld().getBiome(e.getEntityLiving().getPosition()) == Biomes.COLD_TAIGA ||
                            e.getWorld().getBiome(e.getEntityLiving().getPosition()) == Biomes.COLD_TAIGA_HILLS ||
                            e.getWorld().getBiome(e.getEntityLiving().getPosition()) == Biomes.FROZEN_OCEAN ||
                            e.getWorld().getBiome(e.getEntityLiving().getPosition()) == Biomes.FROZEN_RIVER)) {
                EntityStrayCreeper strayCreeper = new EntityStrayCreeper(e.getWorld());
                strayCreeper.copyLocationAndAnglesFrom(e.getEntityLiving());
                if (strayCreeper.getCanSpawnHere()) {
                    e.getWorld().spawnEntity(strayCreeper);
                    e.setResult(Result.DENY);
                }
            } else if (e.getEntityLiving().getRNG().nextInt(5) == 0 && e.getEntityLiving() instanceof EntityBat) {
                EntityBatCreeper batCreeper = new EntityBatCreeper(e.getWorld());
                batCreeper.copyLocationAndAnglesFrom(e.getEntityLiving());
                if (batCreeper.getCanSpawnHere()) {
                    e.getWorld().spawnEntity(batCreeper);
                    e.setResult(Result.DENY);
                }
            }
        }
    }

    @SubscribeEvent
    public void hurt(LivingHurtEvent event) {
        if (event.getSource().getTrueSource() instanceof EntityTakumiArrow && event.getSource().isExplosion() &&
                event.getSource().getImmediateSource() == event.getEntity()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void damage(LivingAttackEvent event) {
/*        if (event.getSource().isExplosion() && event.getSource().getTrueSource() instanceof EntityTakumiArrow &&
                event.getSource().getImmediateSource() == event.getEntity()) {
            event.setCanceled(true);
        } else*/
        if (!event.getSource().isMagicDamage() && event.getSource().getTrueSource() != null &&
                event.getSource().getTrueSource() instanceof EntityLivingBase) {
            ItemStack stack = ((EntityLivingBase) event.getSource().getTrueSource()).getHeldItemMainhand();
            if (!EnchantmentHelper.getEnchantments(stack).isEmpty() &&
                    EnchantmentHelper.getEnchantments(stack).containsKey(TakumiEnchantmentCore.ANTI_POWERED) &&
                    event.getEntityLiving() instanceof EntityCreeper &&
                    ((EntityCreeper) event.getEntityLiving()).getPowered()) {
                event.getEntityLiving().attackEntityFrom(
                        DamageSource.causeMobDamage((EntityLivingBase) event.getSource().getTrueSource())
                                    .setMagicDamage(), 20f);
                TakumiUtils.takumiSetPowered((EntityCreeper) event.getEntityLiving(), false);
                if (event.getSource().getTrueSource() instanceof EntityPlayerMP) {
                    TakumiUtils.giveAdvancementImpossible((EntityPlayerMP) event.getSource().getTrueSource(),
                            new ResourceLocation(TakumiCraftCore.MODID, "creeperbomb"),
                            new ResourceLocation(TakumiCraftCore.MODID, "disarmament"));
                }
            }
        }
        if (event.getSource().isExplosion() && event.getSource() != DamageSource.GENERIC &&
                event.getEntityLiving().getActiveItemStack().getItem() instanceof ItemShield) {
            if (event.getEntityLiving().getActiveItemStack().getItem() != TakumiItemCore.TAKUMI_SHIELD) {
                event.getEntityLiving()
                     .attackEntityFrom(DamageSource.GENERIC.setExplosion().setDamageIsAbsolute(), event.getAmount());
            }
        }
    }

    @SubscribeEvent
    public void onKillEntity(LivingDeathEvent event) {
        if (!event.getEntityLiving().world.isRemote && event.getSource().getTrueSource() instanceof EntityPlayer &&
                TakumiBlockCore.BOMB_MAP.containsKey(event.getEntityLiving().getClass()) &&
                event.getEntityLiving().getRNG().nextInt(10) == 0) {
            event.getEntityLiving()
                 .dropItem(Item.getItemFromBlock(TakumiBlockCore.BOMB_MAP.get(event.getEntityLiving().getClass())), 1);
        }
        if (!event.getEntityLiving().world.isRemote) {
            Calendar calendar = event.getEntityLiving().world.getCurrentDate();
            int month = calendar.get(Calendar.MONTH) + 1;
            int date = calendar.get(Calendar.DATE);
            if (month == 12 && (date == 24 || date == 25)) {
                event.getEntityLiving().dropItem(Item.getItemFromBlock(Blocks.CHEST), 1);
            } else if (month == 1 && date < 8) {
                event.getEntityLiving().entityDropItem(new ItemStack(Items.SKULL, 1, 4)
                        .setStackDisplayName(TakumiUtils.takumiTranslate("takumicraft.newyear.item.name")), 0.1f);
            }
        }
        if (FMLCommonHandler.instance().getSide().isClient() && (event.getEntityLiving() instanceof ITakumiEntity ||
                event.getEntityLiving() instanceof EntityCreeper) &&
                event.getSource().getTrueSource() instanceof EntityPlayerMP) {
            boolean isOK = true;
            for (ITakumiEntity takumiEntity : TakumiEntityCore.getEntityList()) {
                if (!TakumiUtils.getAdvancementUnlocked(
                        new ResourceLocation(TakumiCraftCore.MODID, "slay/slay_" + takumiEntity.getRegisterName())) &&
                        takumiEntity.getClass() != event.getEntityLiving().getClass()) {
                    isOK = false;
                    break;
                }
            }
            if (isOK && event.getSource().getTrueSource() instanceof EntityPlayerMP) {
                TakumiUtils.giveAdvancementImpossible((EntityPlayerMP) event.getSource().getTrueSource(),
                        new ResourceLocation(TakumiCraftCore.MODID, "creeperbomb"),
                        new ResourceLocation(TakumiCraftCore.MODID, "allcomplete"));
            }
        }
    }

    @SubscribeEvent
    public void onChunckPopulate(PopulateChunkEvent.Pre event) {
        if (event.getWorld().provider.getDimensionType() == DimensionType.OVERWORLD) {
            TakumiMapGenDarkShrine mapGenDarkShrine;
            mapGenDarkShrine = new TakumiMapGenDarkShrine();
            mapGenDarkShrine.generate(event.getWorld(), event.getChunkX(), event.getChunkZ(), null);
            mapGenDarkShrine.generateStructure(event.getWorld(), event.getRand(),
                    new ChunkPos(event.getChunkX(), event.getChunkZ()));

        }
    }
}
