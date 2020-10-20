package com.tntmodders.takumi.event;

import com.tntmodders.asm.TakumiASMNameMap;
import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.block.BlockTakumiAcid;
import com.tntmodders.takumi.core.*;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.ITakumiEntity;
import com.tntmodders.takumi.entity.ai.EntityAIFollowCatCreeper;
import com.tntmodders.takumi.entity.item.*;
import com.tntmodders.takumi.entity.mobs.*;
import com.tntmodders.takumi.entity.mobs.boss.EntityWitherCreeper;
import com.tntmodders.takumi.entity.mobs.noncreeper.EntityBoneDummy;
import com.tntmodders.takumi.item.*;
import com.tntmodders.takumi.network.MessageFrozenEffect;
import com.tntmodders.takumi.tileentity.TileEntityTakumiForceField;
import com.tntmodders.takumi.utils.TakumiUtils;
import com.tntmodders.takumi.world.TakumiExplosion;
import com.tntmodders.takumi.world.gen.TakumiMapGenDarkShrine;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.*;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.world.DimensionType;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameType;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.minecart.MinecartUpdateEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.entity.player.PlayerContainerEvent.Close;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.lang.reflect.Field;
import java.util.*;

public class TakumiEvents {

    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof ItemTypeSword &&
                event.getRight().getItem() instanceof ItemTypeCoreSP) {
            event.setCost(10);
            event.setMaterialCost(1);
            ItemStack stack = new ItemStack(event.getLeft().getItem(), 1);
            if (event.getLeft().hasDisplayName()) {
                stack.setStackDisplayName(event.getLeft().getDisplayName());
            }
            event.setOutput(stack);
            if (event.getLeft().isItemEnchanted()) {
                for (Map.Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(
                        event.getLeft()).entrySet()) {
                    event.getOutput().addEnchantment(entry.getKey(), entry.getValue());
                }
            }
            if (event.getRight().getItem() == TakumiItemCore.TAKUMI_TYPE_CORE_DEST &&
                    !EnchantmentHelper.getEnchantments(event.getOutput()).containsKey(
                            TakumiEnchantmentCore.TYPE_DEST)) {
                event.getOutput().addEnchantment(TakumiEnchantmentCore.TYPE_DEST, 1);
            }
            if (event.getRight().getItem() == TakumiItemCore.TAKUMI_TYPE_CORE_MAGIC &&
                    !EnchantmentHelper.getEnchantments(event.getOutput()).containsKey(
                            TakumiEnchantmentCore.TYPE_MAGIC)) {
                event.getOutput().addEnchantment(TakumiEnchantmentCore.TYPE_MAGIC, 1);
            }
        } else if (event.getLeft().getItem() instanceof ItemElytra &&
                (event.getRight().getItem() instanceof ItemTypeCore || event.getRight().getItem() instanceof ItemTypeCoreSP) &&
                event.getRight().getCount() == 64 &&
                !event.getLeft().isItemEnchanted()) {
            event.setCost(15);
            event.setMaterialCost(64);
            ItemStack stack = new ItemStack(event.getLeft().getItem(), 1);
            if (event.getLeft().hasDisplayName()) {
                stack.setStackDisplayName(event.getLeft().getDisplayName());
            }
            event.setOutput(stack);
            if (event.getLeft().isItemEnchanted()) {
                for (Map.Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(
                        event.getLeft()).entrySet()) {
                    event.getOutput().addEnchantment(entry.getKey(), entry.getValue());
                }
            }
            event.getOutput().addEnchantment(TakumiEnchantmentCore.ROCKET_ELYTRA, 1);
        } else if ((event.getLeft().getItem().isEnchantable(event.getLeft()) && event.getLeft().isItemEnchanted()) || (!event.getLeft().hasTagCompound())) {
            if (event.getLeft().getCount() == 1 && event.getRight().getItem() == TakumiItemCore.CHAMP_CORE) {
                event.setCost(30);
                event.setMaterialCost(1);
                ItemStack stack = new ItemStack(event.getLeft().getItem(), event.getLeft().getCount(), event.getLeft().getMetadata());
                if (event.getLeft().hasDisplayName()) {
                    stack.setStackDisplayName(event.getLeft().getDisplayName());
                }
                event.setOutput(stack);
                int i = 10;
                if (event.getLeft().isItemEnchanted()) {
                    for (Map.Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(
                            event.getLeft()).entrySet()) {
                        if (entry.getKey() == TakumiEnchantmentCore.ITEM_PROTECTION) {
                            i += entry.getValue();
                        } else {
                            event.getOutput().addEnchantment(entry.getKey(), entry.getValue());
                        }
                    }
                }
                event.getOutput().addEnchantment(TakumiEnchantmentCore.ITEM_PROTECTION, Math.min(i, 10));
            } else if (event.getLeft().getCount() == 1 && event.getRight().getItem() == TakumiItemCore.PARALYSIS_CORE) {
                event.setCost(10);
                event.setMaterialCost(1);
                ItemStack stack = new ItemStack(event.getLeft().getItem(), event.getLeft().getCount(), event.getLeft().getMetadata());
                if (event.getLeft().hasDisplayName()) {
                    stack.setStackDisplayName(event.getLeft().getDisplayName());
                }
                event.setOutput(stack);
                int i = 1;
                if (event.getLeft().isItemEnchanted()) {
                    for (Map.Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(
                            event.getLeft()).entrySet()) {
                        if (entry.getKey() == TakumiEnchantmentCore.ITEM_PROTECTION) {
                            i += entry.getValue();
                        } else {
                            event.getOutput().addEnchantment(entry.getKey(), entry.getValue());
                        }
                    }
                }
                event.getOutput().addEnchantment(TakumiEnchantmentCore.ITEM_PROTECTION, Math.min(i, 10));
            }
        }
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
    public void attackEntity(AttackEntityEvent event) {
        if (event.getEntityLiving().world.getDifficulty() == EnumDifficulty.HARD && TakumiConfigCore.TakumiHard &&
                event.getEntityLiving() instanceof EntityPlayer) {
            event.setCanceled(true);
            if (!event.getEntityLiving().world.isRemote &&
                    !event.getEntityLiving().isPotionActive(MobEffects.SLOWNESS)) {
                event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 30, 334));
                event.getEntityLiving().playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0F, 0.5F);
            }
        }
    }

    @SubscribeEvent
    public void onClickBlock(BlockEvent.BreakEvent event) {

        if (event.getPlayer().world.getDifficulty() == EnumDifficulty.HARD && TakumiConfigCore.TakumiHard &&
                event.getPlayer() instanceof EntityPlayer) {
            event.setCanceled(true);
            if (!event.getPlayer().world.isRemote && !event.getPlayer().isPotionActive(MobEffects.SLOWNESS)) {
                event.getPlayer().addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 30, 334));
                event.getPlayer().playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0F, 0.5F);
            }
        }
    }

    @SubscribeEvent
    public void playerLoggedOut(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent e) {
        if (TakumiConfigCore.inEventServer && e.player.getWorldScoreboard().removePlayerFromTeams(e.player.getName())) {
            Scoreboard scoreboard = e.player.getWorldScoreboard();
            e.player.setGameType(GameType.SPECTATOR);
            e.player.setSpawnPoint(null, true);
            String s = e.player.getName();
            scoreboard.removeObjectiveFromEntity(s, null);
            e.player.getTags().clear();
        }
    }

    @SubscribeEvent
    public void onUpdate(LivingUpdateEvent event) {
        if (TakumiConfigCore.inEventServer && event.getEntityLiving() instanceof EntityVillager) {
            if (event.getEntityLiving().getTags().contains("V1")) {
                event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY));
            }
        }
        //匠式ハードモード
        if (event.getEntityLiving().world.getDifficulty() == EnumDifficulty.HARD && TakumiConfigCore.TakumiHard &&
                event.getEntityLiving() instanceof EntityPlayer) {
            if (event.getEntityLiving().getActivePotionEffect(MobEffects.SLOWNESS) != null) {
                PotionEffect effect = event.getEntityLiving().getActivePotionEffect(MobEffects.SLOWNESS);
                if (effect.getAmplifier() == 334 && effect.getDuration() <= 1 &&
                        !event.getEntityLiving().world.isRemote) {
                    Explosion explosion = event.getEntityLiving().world.createExplosion(event.getEntityLiving(),
                            event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ,
                            3f, true);
                    event.getEntityLiving().attackEntityFrom(DamageSource.causeExplosionDamage(explosion), 6);
                }
            }
        }
        if (event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == TakumiItemCore.MAKEUP) {
            if (!event.getEntityLiving().world.isRemote) {
                event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.SPEED, 100));
                event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 100));
                event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 100, 1));
            }
            event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.HEAD).attemptDamageItem(1,
                    event.getEntityLiving().world.rand, null);
            if (event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItemDamage() >
                    event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.HEAD).getMaxDamage() - 5) {
                event.getEntityLiving().setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack.EMPTY);
            }
        }
/*        if (TakumiUtils.isApril(event.getEntity().world)) {
            if (event.getEntityLiving() instanceof EntityMob) {
                ((EntityMob) event.getEntityLiving()).setAttackTarget(null);
                if (event.getEntityLiving() instanceof EntityCreeper) {
                    if (((EntityCreeper) event.getEntityLiving()).getCreeperState() > 0 &&
                            event.getEntityLiving().world.isRemote) {
                        for (int i = 0; i < 5; i++) {
                            event.getEntityLiving().world.spawnParticle(EnumParticleTypes.HEART,
                                    event.getEntityLiving().posX + event.getEntityLiving().getRNG().nextDouble(),
                                    event.getEntityLiving().posY + event.getEntityLiving().getRNG().nextDouble(),
                                    event.getEntityLiving().posZ + event.getEntityLiving().getRNG().nextDouble(), 0, 0,
                                    0);
                        }
                    }
                    ((EntityCreeper) event.getEntityLiving()).setCreeperState(-1);
                }
            } else if (event.getEntityLiving() instanceof EntityPlayer &&
                    event.getEntityLiving().getActivePotionEffect(MobEffects.SLOWNESS) != null &&
                    event.getEntityLiving().getActivePotionEffect(MobEffects.SLOWNESS).getAmplifier() == 100 &&
                    event.getEntityLiving().getActivePotionEffect(MobEffects.SLOWNESS).getDuration() <= 1) {
                event.getEntityLiving().world.createExplosion(event.getEntityLiving(), event.getEntityLiving().posX,
                        event.getEntityLiving().posY, event.getEntityLiving().posZ, 3f, true);
                event.getEntityLiving().attackEntityFrom(
                        DamageSource.causeMobDamage(event.getEntityLiving()).setExplosion(), 20f);
            }
        }*/
        if (event.getEntityLiving().getActivePotionEffect(TakumiPotionCore.CREEPERED) != null &&
                event.getEntityLiving().getActivePotionEffect(TakumiPotionCore.CREEPERED).getDuration() <= 1) {
            if (!event.getEntityLiving().world.isRemote) {
                event.getEntityLiving().attackEntityFrom(DamageSource.causeExplosionDamage(
                        event.getEntityLiving().world.createExplosion(event.getEntityLiving(),
                                event.getEntityLiving().posX, event.getEntityLiving().posY,
                                event.getEntityLiving().posZ, 3f, true)), 20f);
            }
        }
        if (event.getEntityLiving().getActivePotionEffect(TakumiPotionCore.EP) != null &&
                event.getEntityLiving().getActivePotionEffect(TakumiPotionCore.EP).getDuration() <= 1) {
            if (!event.getEntityLiving().world.isRemote) {
                event.getEntityLiving().attackEntityFrom(DamageSource.causeExplosionDamage(
                        event.getEntityLiving().world.createExplosion(event.getEntityLiving(),
                                event.getEntityLiving().posX, event.getEntityLiving().posY,
                                event.getEntityLiving().posZ, 3f, true)), 20f);
            }
        }
        if (event.getEntityLiving().getActivePotionEffect(TakumiPotionCore.CLOCK) != null) {
            if (event.getEntityLiving().ticksExisted % 20 == 0) {
                event.getEntityLiving().world.playSound(event.getEntityLiving().posX, event.getEntityLiving().posY,
                        event.getEntityLiving().posZ, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.AMBIENT, 50f, 1,
                        true);
            }

            if (!event.getEntityLiving().world.isRemote &&
                    event.getEntityLiving().getActivePotionEffect(TakumiPotionCore.CLOCK).getDuration() <= 1) {
                event.getEntityLiving().attackEntityFrom(DamageSource.causeExplosionDamage(
                        event.getEntityLiving().world.createExplosion(event.getEntityLiving(),
                                event.getEntityLiving().posX, event.getEntityLiving().posY,
                                event.getEntityLiving().posZ, 7f, true)), 20f);
            }
        }
        if (event.getEntityLiving().getActivePotionEffect(TakumiPotionCore.VIRUS) != null &&
                event.getEntityLiving().getActivePotionEffect(TakumiPotionCore.VIRUS).getDuration() <= 1) {
            if (!event.getEntityLiving().world.isRemote) {
                event.getEntityLiving().attackEntityFrom(DamageSource.causeExplosionDamage(
                        event.getEntityLiving().world.createExplosion(event.getEntityLiving(),
                                event.getEntityLiving().posX, event.getEntityLiving().posY,
                                event.getEntityLiving().posZ, 3f, true)), 20f);
            }
        }
        if (event.getEntityLiving().getActivePotionEffect(TakumiPotionCore.ANTI_SWELLING) != null &&
                event.getEntityLiving() instanceof EntityCreeper && event.getEntityLiving().isNonBoss()) {
            if (event.getEntityLiving().getActivePotionEffect(TakumiPotionCore.ANTI_SWELLING).getDuration() > 1) {
                try {
                    Field field = TakumiASMNameMap.getField(EntityCreeper.class, "timeSinceIgnited");
                    field.setAccessible(true);
                    field.set(event.getEntityLiving(), 1);
                    ((EntityCreeper) event.getEntityLiving()).setCreeperState(-1);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (event.getEntityLiving() instanceof EntityCreeper &&
                !((EntityCreeper) event.getEntityLiving()).getPowered() &&
                (event.getEntityLiving().world.isThundering() ||
                        event.getEntity().world.provider.getDimension() == TakumiWorldCore.TAKUMI_WORLD.getId())) {
            TakumiUtils.takumiSetPowered((EntityCreeper) event.getEntityLiving(), true);
        }
        if (event.getEntityLiving() instanceof EntityParrot) {
            if (event.getEntityLiving().getEntityData().hasKey("creeper") &&
                    event.getEntityLiving().getEntityData().getBoolean("creeper")) {
                event.getEntityLiving().world.createExplosion(event.getEntityLiving(),
                        event.getEntityLiving().posX, event.getEntityLiving().posY,
                        event.getEntityLiving().posZ, 4f, true);
                event.getEntityLiving().setDead();
            }
        }
        if (event.getEntityLiving().getActivePotionEffect(TakumiPotionCore.EXP_JUMP) != null &&
                event.getEntityLiving().onGround) {
            this.jump(event.getEntityLiving());
            if (!event.getEntityLiving().world.isRemote) {
                event.getEntityLiving().world.createExplosion(event.getEntityLiving(), event.getEntityLiving().posX,
                        event.getEntityLiving().posY - 0.5, event.getEntityLiving().posZ, 0f, false);
            } else {
                event.getEntityLiving().world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE,
                        event.getEntityLiving().posX, event.getEntityLiving().posY - 0.5, event.getEntityLiving().posZ,
                        0, 0, 0);
            }
        }

        if (EnchantmentHelper.getEnchantments(event.getEntityLiving().getHeldItem(EnumHand.MAIN_HAND)).containsKey(
                TakumiEnchantmentCore.TYPE_MAGIC)) {
            if (!event.getEntityLiving().world.isRemote) {
                event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 20, 3, true, false));
                List<Potion> potions = new ArrayList<>();
                event.getEntityLiving().getActivePotionEffects().forEach(potionEffect -> {
                    if (potionEffect.getPotion().isBadEffect() && potionEffect.getPotion() != TakumiPotionCore.INVERSION) {
                        potions.add(potionEffect.getPotion());
                    }
                });
                potions.forEach(potion -> event.getEntityLiving().removePotionEffect(potion));
            } else {
                for (int i = 0; i < 5; i++) {
                    event.getEntityLiving().world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE,
                            event.getEntityLiving().posX + (event.getEntityLiving().getRNG().nextDouble() - 0.5D) *
                                    event.getEntityLiving().width * 6, event.getEntityLiving().posY +
                                    event.getEntityLiving().getRNG().nextDouble() * event.getEntityLiving().height * 2,
                            event.getEntityLiving().posZ + (event.getEntityLiving().getRNG().nextDouble() - 0.5D) *
                                    event.getEntityLiving().width * 6, 0.0D, 0.0D, 0.0D);
                }
            }
        }
        if (EnchantmentHelper.getEnchantments(event.getEntityLiving().getHeldItem(EnumHand.MAIN_HAND)).containsKey(
                TakumiEnchantmentCore.TYPE_DEST)) {
            if (!event.getEntityLiving().world.isRemote) {
                event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 5, 4, true, false));
                //event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.SATURATION, 5, 0, true, false));
            } else {
                for (int i = 0; i < 20; ++i) {
                    event.getEntityLiving().world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL,
                            event.getEntityLiving().posX + (event.getEntityLiving().getRNG().nextDouble() - 0.5D) *
                                    event.getEntityLiving().width * 6, event.getEntityLiving().posY +
                                    event.getEntityLiving().getRNG().nextDouble() * event.getEntityLiving().height * 2,
                            event.getEntityLiving().posZ + (event.getEntityLiving().getRNG().nextDouble() - 0.5D) *
                                    event.getEntityLiving().width * 6, 0.0D, 0.0D, 0.0D);
                }
            }
        }
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = ((EntityPlayer) event.getEntityLiving());
            if (player.isElytraFlying() && !EnchantmentHelper.getEnchantments(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST)).isEmpty() &&
                    EnchantmentHelper.getEnchantments(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST)).containsKey(TakumiEnchantmentCore.ROCKET_ELYTRA)) {
                //event.getEntityLiving().world.createExplosion(player, player.posX, player.posY, player.posZ, 0f, false);
                event.getEntity().world.playSound(null, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS,
                        0.3F, (1.0F + (event.getEntity().world.rand.nextFloat() - event.getEntity().world.rand.nextFloat()) * 0.2F) * 0.7F);

                event.getEntity().world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, event.getEntity().posX - event.getEntity().motionX * 0.1,
                        event.getEntity().posY - event.getEntity().motionX * 0.1, event.getEntity().posZ - event.getEntity().motionX * 0.1, 0, 0, 0);
                if (FMLCommonHandler.instance().getSide().isClient() && player.getTicksElytraFlying() > 20) {
                    float red, green, blue;
                    if (player.getTeam() != null && player.getTeam().getColor() != null && player.getTeam().getColor().isColor()) {
                        int color = TakumiUtils.getColorFromText(player.getTeam().getColor());
                        red = (color >> 16) / 255f;
                        green = (color >> 8 & 255) / 255f;
                        blue = (color & 255) / 255f;
                    } else {
                        red = 0;
                        green = 1;
                        blue = 0;
                    }
                    for (int i = 0; i < 50; i++) {
                        Random random = new Random();
                        double x = (random.nextDouble() - 0.5) / 10 - event.getEntity().motionX * 0.8 / 50 * i;
                        double y = (random.nextDouble() - 0.5) / 10 - event.getEntity().motionY * 0.8 / 50 * i;
                        double z = (random.nextDouble() - 0.5) / 10 - event.getEntity().motionZ * 0.8 / 50 * i;

                        TakumiUtils.spawnColoredParticle(event.getEntity().world, event.getEntity().posX + x, event.getEntity().posY + y
                                , event.getEntity().posZ + z, x / 2, y / 2, z / 2, red, green, blue, 200 + random.nextInt(100));
                    }
                }
                if (!player.world.isRemote && player.ticksExisted % 40 == 0) {
                    EntityFireworkRocket entityfireworkrocket = new EntityFireworkRocket(player.world, new ItemStack(Items.FIREWORKS), player);
                    entityfireworkrocket.setSilent(true);
                    entityfireworkrocket.setInvisible(true);
                    player.world.spawnEntity(entityfireworkrocket);
                    Enchantment.getEnchantmentID(TakumiEnchantmentCore.ROCKET_ELYTRA);
                }
            }
        }
    }

    protected void jump(EntityLivingBase entity) {
        entity.motionY = 1f;

        if (entity.isPotionActive(MobEffects.JUMP_BOOST)) {
            entity.motionY +=
                    (float) (entity.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F;
        }

        if (entity.isSprinting()) {
            float f = entity.rotationYaw * 0.017453292F;
            entity.motionX -= MathHelper.sin(f) * 0.2F;
            entity.motionZ += MathHelper.cos(f) * 0.2F;
        }

        entity.isAirBorne = true;
        net.minecraftforge.common.ForgeHooks.onLivingJump(entity);
    }

    @SubscribeEvent
    public void onExplosion(Detonate event) {
        if (TakumiConfigCore.inTCPVP) {
            if (event.getExplosion().getPosition().y < 50) {
                event.getAffectedEntities().clear();
                event.getAffectedBlocks().clear();
            } else {
                event.getAffectedEntities().removeIf(entity -> entity.posY < 49);
                event.getAffectedBlocks().removeIf(pos -> pos.getY() < 50);
            }
            return;
        }
        if (event.getWorld().tickableTileEntities != null && !event.getWorld().tickableTileEntities.isEmpty() &&
                event.getWorld().tickableTileEntities.stream().anyMatch(tileEntity -> tileEntity instanceof TileEntityTakumiForceField)) {
            event.getWorld().tickableTileEntities.forEach(tileEntity -> {
                if (tileEntity instanceof TileEntityTakumiForceField) {
                    event.getAffectedEntities().removeIf(entity -> tileEntity.getDistanceSq(entity.posX + 0.5, entity.posY + 0.5, entity.posZ + 0.5) < 100);
                    event.getAffectedBlocks().removeIf(pos -> tileEntity.getDistanceSq(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) < 100);
                }
            });
        }
        if (FMLCommonHandler.instance().getSide().isServer() && event.getWorld().getMinecraftServer() != null) {
            try {
                List<BlockPos> list = new ArrayList<>();
                event.getAffectedBlocks().forEach(pos -> {
                    BlockPos blockpos = event.getWorld().getSpawnPoint();
                    int i = MathHelper.abs(pos.getX() - blockpos.getX());
                    int j = MathHelper.abs(pos.getZ() - blockpos.getZ());
                    int k = Math.max(i, j);
                    if (k <= event.getWorld().getMinecraftServer().getSpawnProtectionSize()) {
                        list.add(pos);
                    }
                });
                event.getAffectedBlocks().removeAll(list);
            } catch (Exception ignored) {
            }
        }
        event.getAffectedEntities().removeIf(entity -> {
            if (entity instanceof EntityItem) {
                if (((EntityItem) entity).getItem().getRarity() == EnumRarity.EPIC || ((EntityItem) entity).getItem().getItem() == Items.NETHER_STAR) {
                    return true;
                } else if (EnchantmentHelper.getEnchantments(((EntityItem) entity).getItem()).containsKey(TakumiEnchantmentCore.ITEM_PROTECTION)) {
                    if (!event.getWorld().isRemote) {
                        ItemStack stack = ((EntityItem) entity).getItem();
                        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
                        int lv = map.get(TakumiEnchantmentCore.ITEM_PROTECTION);
                        map.remove(TakumiEnchantmentCore.ITEM_PROTECTION);
                        if (lv > 1) {
                            int i = lv;
                            if (entity.world.rand.nextInt(lv + 1) == 0) {
                                i = i - 1;
                            }
                            map.put(TakumiEnchantmentCore.ITEM_PROTECTION, i);
                        }
                        EnchantmentHelper.setEnchantments(map, stack);
                        ((EntityItem) entity).setItem(stack);
                    }
                    return true;
                } else if (((EntityItem) entity).getItem().getItem() instanceof ItemFood && !entity.isDead) {
                    ItemFood food = ItemTakumiSpecialMeat.getSpecializedMeat(((EntityItem) entity).getItem().getItem(), false);
                    if (food != null && entity.world.rand.nextInt(10) == 0) {
                        EntityItem item = new EntityItem(entity.world);
                        item.copyLocationAndAnglesFrom(entity);
                        item.setItem(new ItemStack(food, ((EntityItem) entity).getItem().getCount()));
                        entity.world.spawnEntity(item);
                        entity.setDead();
                        return true;
                    }
                }
            }
            if (entity instanceof EntityPainting && ItemTakumiPainting.isPaintingAntiExplosion(((EntityPainting) entity))) {
                return true;
            }
            if (entity instanceof EntityItemFrame && ItemTakumiItemFrame.isItemFrameAntiExplosion(((EntityItemFrame) entity))) {
                return true;
            }
            if (entity instanceof EntityLivingBase) {
                if (((EntityLivingBase) entity).getActiveItemStack().getItem() instanceof IItemAntiExplosion) {
                    ((EntityLivingBase) entity).getActiveItemStack().damageItem(1, ((EntityLivingBase) entity));
                    return true;
                }

                boolean flg = false;
                for (ItemStack itemStack : entity.getArmorInventoryList()) {
                    if (!EnchantmentHelper.getEnchantments(itemStack).isEmpty() &&
                            EnchantmentHelper.getEnchantments(itemStack).containsKey(
                                    TakumiEnchantmentCore.EXPLOSION_PROTECTION)) {
                        if (((EntityLivingBase) entity).getRNG().nextInt(12) == 0) {
                            itemStack.damageItem(1 + ((EntityLivingBase) entity).getRNG().nextInt(
                                    1 + event.getWorld().getDifficulty().getDifficultyId()),
                                    ((EntityLivingBase) entity));
                        }
                        flg = true;
                    }
                }
                return flg;
            }
            if (entity instanceof EntityPlayer) {
                return entity.getRidingEntity() != null && (entity.getRidingEntity() instanceof EntityXMS || entity.getRidingEntity() instanceof EntityYMS);
            }
            return false;
        });
        if (event.getExplosion().getExplosivePlacedBy() instanceof EntityKingDummy) {
            switch (((EntityKingDummy) event.getExplosion().getExplosivePlacedBy()).id) {
                default: {
                    event.getAffectedEntities().forEach(entity -> {
                        if (entity instanceof EntityLivingBase) {
                            entity.getEquipmentAndArmor().forEach(itemStack -> {
                                if (itemStack != ItemStack.EMPTY && entity.world.rand.nextInt(20) == 0) {
                                    itemStack.shrink(1);
                                }
                            });
                            entity.attackEntityFrom(DamageSource.causeMobDamage(
                                    event.getExplosion().getExplosivePlacedBy()).setMagicDamage(), 10);
                        }
                    });
                }
            }
        }
        if (!event.getWorld().isRemote) {
            if (event.getExplosion().getExplosivePlacedBy() != null &&
                    event.getExplosion().getExplosivePlacedBy().isPotionActive(TakumiPotionCore.VIRUS)) {
                event.getAffectedEntities().forEach(entity -> {
                    if (entity instanceof EntityLivingBase && entity != event.getExplosion().getExplosivePlacedBy()) {
                        ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(TakumiPotionCore.VIRUS, 100));
                    }
                });
            }
            if (event.getWorld().getBlockState(new BlockPos(event.getExplosion().getPosition())).getBlock() ==
                    TakumiBlockCore.ACID_BLOCK) {
                IBlockState state = event.getWorld().getBlockState(new BlockPos(event.getExplosion().getPosition()));
                int i = state.getValue(BlockTakumiAcid.META) + 1;
                event.getAffectedBlocks().forEach(pos -> {
                    if (i < 16) {
                        for (EnumFacing facing : EnumFacing.values()) {
                            BlockPos blockPos = pos.offset(facing);
                            if (!event.getAffectedBlocks().contains(blockPos) &&
                                    !event.getWorld().isAirBlock(blockPos) &&
                                    event.getWorld().getBlockState(blockPos).getBlockHardness(event.getWorld(),
                                            blockPos) != -1 &&
                                    event.getWorld().getBlockState(blockPos).getBlock().getExplosionResistance(event.getWorld(), blockPos, null, null) < 2000 &&
                                    event.getWorld().getBlockState(blockPos).getBlock() != TakumiBlockCore.ACID_BLOCK) {
                                TakumiUtils.setBlockStateProtected(event.getWorld(), blockPos,
                                        TakumiBlockCore.ACID_BLOCK.getDefaultState().withProperty(BlockTakumiAcid.META, i));
                            }
                        }
                    }
                    event.getWorld().setBlockToAir(pos);
                });
                event.getAffectedBlocks().clear();
            }
        }
        if (event.getExplosion() instanceof TakumiExplosion) {
            if (((TakumiExplosion) event.getExplosion()).getExploder() instanceof EntityWindTypeLance) {
                event.getAffectedEntities().removeIf(entity -> !(entity instanceof EntityMob));
                event.getAffectedEntities().forEach(entity -> {
                    if (entity instanceof EntityMob) {
                        entity.attackEntityFrom(DamageSource.causeExplosionDamage(event.getExplosion()), 4f);
                    }
                });
            }
            if (((TakumiExplosion) event.getExplosion()).getExploder() instanceof EntityWitherCreeperSkull) {
                event.getAffectedEntities().removeIf(entity -> entity instanceof EntityWitherCreeper);
            }
            if (((TakumiExplosion) event.getExplosion()).getExploder() instanceof EntityTakumiLauncher) {
                event.getAffectedEntities().clear();
            }
            if (((TakumiExplosion) event.getExplosion()).getExploder() instanceof AbstractEntityTakumiGrenade) {
                AbstractEntityTakumiGrenade grenade =
                        (AbstractEntityTakumiGrenade) ((TakumiExplosion) event.getExplosion()).getExploder();
                if (grenade instanceof EntityTakumiTitanMeteor) {
                    event.getAffectedEntities().forEach(entity -> {
                        if (entity instanceof EntityLivingBase) {
                            entity.setFire(30);
                        } else if (entity instanceof EntityItem) {
                            entity.setDead();
                        }
                    });
                    event.getAffectedEntities().clear();
                }
                if (grenade.getThrower() != null && !(grenade instanceof EntityTakumiThrowGrenede)) {
                    event.getAffectedEntities().remove(grenade.getThrower());
                }
                if (grenade instanceof EntityTakumiThrowGrenede) {
                    event.getAffectedEntities().removeIf(entity -> entity instanceof EntityMob);
                    if (TakumiConfigCore.inEventServer && grenade.getThrower() != null && grenade instanceof EntityTakumiThrowGrenede_SP) {
                        event.getAffectedEntities().removeIf(entity -> {
                            if (entity instanceof EntityPlayer && entity != grenade.getThrower() && entity.getTeam() != null) {
                                switch (entity.getTeam().getCollisionRule()) {
                                    case NEVER: {
                                        return true;
                                    }
                                    case HIDE_FOR_OTHER_TEAMS: {
                                        return entity.isOnSameTeam(grenade.getThrower());
                                    }
                                    case HIDE_FOR_OWN_TEAM: {
                                        return !entity.isOnSameTeam(grenade.getThrower());
                                    }
                                    default: {
                                        return false;
                                    }
                                }
                            }
                            return false;
                        });
                    }
                }
                if (grenade instanceof EntityTakumiKnifeGun) {
                    event.getAffectedEntities().removeIf(entity -> entity instanceof EntityPlayer || (entity instanceof EntityTameable && ((EntityTameable) entity).isTamed())
                            || entity instanceof EntityItem || entity instanceof EntityHanging);
                }
            }
            if (((TakumiExplosion) event.getExplosion()).getExploder() instanceof EntityTakumiArrow) {
                EntityTakumiArrow takumiArrow =
                        (EntityTakumiArrow) ((TakumiExplosion) event.getExplosion()).getExploder();
                if (takumiArrow.shootingEntity != null) {
                    event.getAffectedEntities().remove(takumiArrow.shootingEntity);
                }
                if (takumiArrow.shootingEntity instanceof EntityStrayCreeper) {
                    PotionType type = PotionUtils.getPotionFromItem(
                            ((EntityLivingBase) takumiArrow.shootingEntity).getHeldItem(EnumHand.OFF_HAND));
                    if (!event.getWorld().isRemote) {
                        for (Entity entity : event.getAffectedEntities()) {
                            if (entity instanceof EntityLivingBase && entity != takumiArrow.shootingEntity) {
                                PotionEffect effect = new PotionEffect(type.getEffects().get(0).getPotion(), 400);
                                ((EntityLivingBase) entity).addPotionEffect(effect);
                            }
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
            if (((TakumiExplosion) event.getExplosion()).getExploder() instanceof EntityTransHomingBomb) {
                if (!event.getWorld().isRemote) {
                    event.getAffectedEntities().forEach(entity -> {
                        if (entity instanceof EntityPlayer) {
                            ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 150));
                            ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(TakumiPotionCore.INVERSION, 150));
                        }
                    });
                }
            }
        }
        if (event.getExplosion().getExplosivePlacedBy() instanceof EntityAlterDummy) {
            event.getAffectedBlocks().removeIf(pos -> pos.getY() < event.getExplosion().getPosition().y - 1);
            event.getAffectedEntities().clear();
        }
        if (event.getExplosion().getExplosivePlacedBy() instanceof EntityIceologerCreeperSpell
                && ((EntityIceologerCreeperSpell) event.getExplosion().getExplosivePlacedBy()).canFreezeEntity()) {
            event.getAffectedEntities().forEach(entity -> {
                if (entity instanceof EntityLivingBase && !entity.isImmuneToExplosions()) {
                    PotionEffect effect = new PotionEffect(TakumiPotionCore.FROZEN, 200, 0, true, false);
                    ((EntityLivingBase) entity).addPotionEffect(effect);
                    DamageSource source = DamageSource.causeMobDamage(event.getExplosion().getExplosivePlacedBy()).setMagicDamage();
                    if (!entity.isEntityInvulnerable(source)) {
                        entity.attackEntityFrom(source, 3f + entity.world.getDifficulty().getDifficultyId());
                    }
                    if (!entity.world.isRemote) {
                        TakumiPacketCore.INSTANCE.sendToAll(new MessageFrozenEffect(entity.getEntityId(), effect, false));
                    }
                }
            });
            event.getAffectedEntities().clear();
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
            } else if (!e.getWorld().isRemote && e.getEntityLiving().getRNG().nextInt(10) == 0) {
                try {
                    int rnd = e.getEntityLiving().getRNG().nextInt(10);
                    for (int i = 0; i < 1 + rnd; i++) {
                        EntityLivingBase entity = ((EntityLivingBase) EntityRegistry.getEntry(e.getEntityLiving().getClass()).newInstance(e.getWorld()));
                        entity.copyLocationAndAnglesFrom(entity);
                        e.getWorld().spawnEntity(entity);
                    }
                } catch (Exception ignored) {
                }
            }
        }
        try {
            if (e.getWorld().playerEntities.stream().noneMatch(entityPlayer -> {
                if (FMLCommonHandler.instance().getSide().isClient()) {
                    return TakumiUtils.getAdvancementUnlocked(
                            new ResourceLocation(TakumiCraftCore.MODID, "creeperbomb"));
                } else if (entityPlayer instanceof EntityPlayerMP) {
                    return TakumiUtils.getAdvancementUnlockedServer(
                            new ResourceLocation(TakumiCraftCore.MODID, "creeperbomb"),
                            ((EntityPlayerMP) entityPlayer));
                }
                return false;
            })) {
                if (e.getEntityLiving() instanceof EntityTakumiAbstractCreeper &&
                        ((EntityTakumiAbstractCreeper) e.getEntityLiving()).takumiRank() !=
                                ITakumiEntity.EnumTakumiRank.LOW || e.getWorld().rand.nextInt(3) == 0) {
                    if (!e.getWorld().isThundering()) {
                        e.setResult(Result.DENY);
                    }
                }
            } else if (e.getWorld().getTotalWorldTime() < 48000) {
                if (!e.getWorld().isThundering() && e.getWorld().rand.nextInt(3) == 0) {
                    e.setResult(Result.DENY);
                }
            }
        } catch (Exception ignored) {
        }
        if (e.getEntityLiving().getClass() == EntityCreeper.class) {
            ((EntityLiving) e.getEntityLiving()).tasks.addTask(0,
                    new EntityAIFollowCatCreeper((EntityCreeper) e.getEntityLiving()));
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
            } else if (e.getEntityLiving() instanceof EntitySquidCreeper && e.getEntityLiving().world.provider.getDimensionType() == TakumiWorldCore.TAKUMI_WORLD) {
                if (e.getEntityLiving().getRNG().nextInt(10) == 0) {
                    EntitySeaGuardianCreeper seaGuardianCreeper = new EntitySeaGuardianCreeper(e.getWorld());
                    seaGuardianCreeper.copyLocationAndAnglesFrom(e.getEntityLiving());
                    e.getWorld().spawnEntity(seaGuardianCreeper);
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
                    if (e.getEntityLiving().getRNG().nextInt(10) == 0) {
                        batCreeper = new EntityBigBatCreeper(e.getWorld());
                        batCreeper.copyLocationAndAnglesFrom(e.getEntityLiving());
                    }
                    e.getWorld().spawnEntity(batCreeper);
                    e.setResult(Result.DENY);
                }
            }
        }
    }

    @SubscribeEvent
    public void hurt(LivingHurtEvent event) {
/*        if (event.getSource().getTrueSource() instanceof EntityTakumiArrow && event.getSource().isExplosion() &&
                event.getSource().getImmediateSource() == event.getEntity()) {
            event.setCanceled(true);
        }*/
/*        if (TakumiUtils.isApril(event.getEntityLiving().world) && event.getEntityLiving() instanceof EntityPlayer) {
            event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 30, 100));
            event.getEntityLiving().playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0F, 0.5F);
        }*/
        if (event.getEntityLiving().getActivePotionEffect(TakumiPotionCore.EXP_JUMP) != null &&
                event.getSource() == DamageSource.FALL) {
            event.setCanceled(true);
        }
        if (event.getEntityLiving().getActivePotionEffect(TakumiPotionCore.ANTI_EXPLOSION) != null &&
                event.getSource().isExplosion()) {
            event.setCanceled(event.getAmount() < 20);
        }
    }

    @SubscribeEvent
    public void damage(LivingAttackEvent event) {
        if (event.getSource().isExplosion() && event.getSource().getImmediateSource() instanceof EntityTakumiThrowGrenede /*&&
                ((((EntityTakumiThrowGrenede) event.getSource().getImmediateSource()).getThrower() != null &&
                        ((EntityTakumiThrowGrenede) event.getSource().getImmediateSource()).getThrower().getClass() == event.getEntityLiving().getClass()) ||
                        TakumiConfigCore.inEventServer)*/) {
            event.setCanceled(true);
        } else if (event.getSource().isExplosion() && event.getSource().getTrueSource() instanceof EntityTakumiArrow) {
            if (TakumiConfigCore.inEventServer) {
                event.setCanceled(true);
            }
            if (((EntityTakumiArrow) event.getSource().getTrueSource()).shootingEntity != null &&
                    ((EntityTakumiArrow) event.getSource().getTrueSource()).shootingEntity.getClass() == event.getEntityLiving().getClass()) {
                event.setCanceled(true);
            }
        } else if (event.getSource().isExplosion() && event.getSource().getTrueSource() instanceof EntityChaseCreeper) {
            event.setCanceled(true);
        }
        if (event.getSource().isExplosion() && event.getSource().getTrueSource() instanceof EntityRoboCreeper) {
            event.setCanceled(true);
        }
        if (!event.getSource().isMagicDamage() && event.getSource().getTrueSource() != null &&
                event.getSource().getTrueSource() instanceof EntityLivingBase) {
            ItemStack stack = ((EntityLivingBase) event.getSource().getTrueSource()).getHeldItemMainhand();
            if (!EnchantmentHelper.getEnchantments(stack).isEmpty() &&
                    EnchantmentHelper.getEnchantments(stack).containsKey(TakumiEnchantmentCore.ANTI_POWERED) &&
                    event.getEntityLiving() instanceof EntityCreeper &&
                    ((EntityCreeper) event.getEntityLiving()).getPowered() &&
                    !event.getSource().isMagicDamage()) {
                event.getEntityLiving().attackEntityFrom(DamageSource.causeMobDamage(
                        (EntityLivingBase) event.getSource().getTrueSource()).setMagicDamage(), 20f);
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
                event.getEntityLiving().attackEntityFrom(DamageSource.GENERIC.setExplosion().setDamageIsAbsolute(),
                        event.getAmount());
            }
        }
    }

    @SubscribeEvent
    public void knockbackEvent(LivingKnockBackEvent event) {
        if (event.getAttacker() instanceof EntityLivingBase &&
                ((EntityLivingBase) event.getAttacker()).getHeldItemMainhand().getItem() ==
                        TakumiItemCore.TAKUMI_TYPE_SWORD_WATER) {
            event.setCanceled(true);
        }
    }

/*    @SubscribeEvent
    public void rightClick(PlayerInteractEvent.RightClickBlock event) {
        if (!event.getWorld().isRemote) {
            if (event.getHitVec() != null && event.getWorld().getTileEntity(new BlockPos(event.getHitVec())) instanceof TileEntityCommandBlock) {
                TileEntityCommandBlock commandBlock = ((TileEntityCommandBlock) event.getWorld().getTileEntity(new BlockPos(event.getHitVec())));
                if (commandBlock.getCommandBlockLogic() != null) {
                    TakumiCraftCore.LOGGER.info("tssystem:" + commandBlock.getCommandBlockLogic().getCommand());
                }
            }
        }
    }*/

    @SubscribeEvent
    public void onKillEntity(LivingDeathEvent event) {
        if (!event.getEntityLiving().world.isRemote && TakumiBlockCore.BOMB_MAP.containsKey(event.getEntityLiving().getClass()) &&
                event.getEntityLiving().getRNG().nextInt(10) == 0) {
            int i = event.getEntityLiving().getRNG().nextInt(3);
            if (i > 0) {
                event.getEntityLiving().dropItem(Item.getItemFromBlock(TakumiBlockCore.BOMB_MAP.get(event.getEntityLiving().getClass())), i);
            }
        }
        if (event.getEntityLiving() instanceof EntityPlayer &&
                event.getSource().getTrueSource() instanceof EntityBoneCreeper && event.getSource().isExplosion()) {
            EntityBoneDummy dummy = new EntityBoneDummy(event.getEntityLiving().world);
            //EntityCreeper dummy = new EntityWrylyCreeper(event.getEntityLiving().world);
            dummy.setPosition(event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ);
            event.getEntityLiving().world.spawnEntity(dummy);
        }
        if (!event.getEntityLiving().world.isRemote && event.getEntityLiving() instanceof EntityCreeper) {
            Calendar calendar = event.getEntityLiving().world.getCurrentDate();
            int month = calendar.get(Calendar.MONTH) + 1;
            int date = calendar.get(Calendar.DATE);
            if (month == 12 && (date == 24 || date == 25) && event.getEntityLiving().getRNG().nextInt(4) == 0) {
                Item[] xmasItems = {Item.getItemFromBlock(TakumiBlockCore.CREEPER_BOMB), Item.getItemFromBlock(Blocks.TNT), Items.GUNPOWDER, TakumiItemCore.EXP_PRO_PRI};
                ItemStack stack = new ItemStack(xmasItems[event.getEntityLiving().getRNG().nextInt(xmasItems.length)], 1);
                if (stack.getItem() != TakumiItemCore.EXP_PRO_PRI) {
                    stack.setStackDisplayName(TakumiUtils.takumiTranslate("takumicraft.xmas.item.name"));
                }
                if (stack.getItem() != TakumiItemCore.EXP_PRO_PRI || event.getEntityLiving().getRNG().nextInt(5) == 0) {
                    event.getEntityLiving().entityDropItem(stack, 0.1f);
                }
            } else if (month == 1 && date < 8) {
                event.getEntityLiving().entityDropItem(new ItemStack(Items.SKULL, 1, 4).setStackDisplayName(
                        TakumiUtils.takumiTranslate("takumicraft.newyear.item.name")), 0.1f);
            }
        }
        if (event.getSource().getTrueSource() instanceof EntityPlayer && (event.getEntityLiving() instanceof ITakumiEntity ||
                event.getEntityLiving() instanceof EntityCreeper)) {
            if (FMLCommonHandler.instance().getSide().isServer()) {
                if (TakumiConfigCore.rangeTakumiBookSync > 0) {
                    try {
                        int range = event.getEntityLiving().isNonBoss() ? TakumiConfigCore.rangeTakumiBookSync : TakumiConfigCore.rangeTakumiBookSync * 5;
                        String name = event.getEntityLiving().getClass() == EntityCreeper.class ? "" : ((ITakumiEntity) event.getEntityLiving()).getRegisterName();
                        List<EntityPlayerMP> players = event.getEntityLiving().world.getPlayers(EntityPlayerMP.class, input ->
                                input.getDistanceToEntity(event.getEntityLiving()) < range);
                        players.forEach(playerMP -> playerMP.getAdvancements().grantCriterion(
                                playerMP.getServer().getAdvancementManager().getAdvancement(
                                        new ResourceLocation(TakumiCraftCore.MODID, "slay/slay_" + name)), name));
                        if (event.getEntityLiving().getClass() == EntitySheepCreeper.class && ((EntitySheepCreeper) event.getEntityLiving()).getRainbow()) {
                            players.forEach(playerMP -> TakumiUtils.giveAdvancementImpossible(playerMP,
                                    new ResourceLocation(TakumiCraftCore.MODID, "disarmament"),
                                    new ResourceLocation(TakumiCraftCore.MODID, "rainbowsheep")));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (event.getSource().getTrueSource() instanceof EntityPlayerMP) {
                    boolean isOK = true;
                    for (ITakumiEntity takumiEntity : TakumiEntityCore.getEntityList()) {
                        if (!TakumiUtils.getAdvancementUnlockedServer(
                                new ResourceLocation(TakumiCraftCore.MODID, "slay/slay_" + takumiEntity.getRegisterName()),
                                ((EntityPlayerMP) event.getSource().getTrueSource())) && takumiEntity.getClass() != event.getEntityLiving().getClass()) {
                            isOK = false;
                            break;
                        }
                    }
                    if (isOK && event.getSource().getTrueSource() instanceof EntityPlayerMP) {
                        TakumiUtils.giveAdvancementImpossible((EntityPlayerMP) event.getSource().getTrueSource(),
                                new ResourceLocation(TakumiCraftCore.MODID, "root"),
                                new ResourceLocation(TakumiCraftCore.MODID, "allcomplete"));
                    }
                }
            }

        }
        if (!event.getEntity().world.isRemote && event.getEntity() instanceof ITakumiEntity && event.getEntity().isGlowing() && event.getEntity().world.loadedEntityList != null &&
                event.getEntity().world.loadedEntityList.stream().anyMatch(entity -> entity instanceof EntityAttackBlock)) {
            event.getEntity().dropItem(TakumiItemCore.ENERGY_CORE, event.getEntity().world.rand.nextInt(3));
        }
        if (TakumiConfigCore.useTP) {
            ScoreObjective objective = event.getEntityLiving().world.getScoreboard().getObjective("tp");
            if (objective != null) {
                if (event.getEntityLiving() instanceof EntityCreeper &&
                        event.getSource().getTrueSource() instanceof EntityPlayer) {
                    int point = event.getEntityLiving() instanceof EntityTakumiAbstractCreeper ?
                            ((EntityTakumiAbstractCreeper) event.getEntityLiving()).takumiRank().getPoint() : 10;
                    if (event.getEntityLiving().world.isThundering() ||
                            ((EntityCreeper) event.getEntityLiving()).getPowered()) {
                        point = ((int) (point * 1.5));
                    }
                    event.getEntityLiving().world.getScoreboard().getOrCreateScore(
                            event.getSource().getTrueSource().getName(), objective).increaseScore(point);
                }
                if (event.getEntityLiving() instanceof EntityPlayer &&
                        !(event.getSource().getTrueSource() instanceof EntityPlayer) /*&&
                        !(event.getSource().getTrueSource() instanceof EntityAttackBlock)*/) {
                    event.getEntityLiving().world.getScoreboard().getOrCreateScore(event.getEntityLiving().getName(),
                            objective).setScorePoints(0);
                }
            }
        }
    }

    @SubscribeEvent
    public void onItemPickUp(EntityItemPickupEvent event) {
        if (event.getItem().getItem().getItem() == Item.getItemFromBlock(TakumiBlockCore.ANVIL_CREEPER)) {
            if (!event.getEntityLiving().world.isRemote) {
                event.getEntityLiving().world.createExplosion(null, event.getItem().posX, event.getItem().posY,
                        event.getItem().posZ, 2, true);
            }
            event.setCanceled(true);
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
/*        if (event.getWorld().provider.getDimensionType() == TakumiWorldCore.TAKUMI_WORLD && event.getChunkX() == 0 &&
                event.getChunkZ() == 0) {
            TakumiMapGenTower_F takumiMapGenTower_f;
            takumiMapGenTower_f = new TakumiMapGenTower_F();
            takumiMapGenTower_f.generate(event.getWorld(), event.getChunkX(), event.getChunkZ(), null);
            takumiMapGenTower_f.generateStructure(event.getWorld(), event.getRand(),
                    new ChunkPos(event.getChunkX(), event.getChunkZ()));
        }*/
    }

    @SubscribeEvent
    public void onPlayerUpdate(LivingUpdateEvent event) {
        if (TakumiConfigCore.useTP && event.getEntityLiving() instanceof EntityPlayer) {
            Scoreboard scoreboard = event.getEntityLiving().world.getScoreboard();
            ScoreObjective objective = scoreboard.getObjective("tp") == null ?
                    scoreboard.addScoreObjective("tp", new ScoreCriteria("tp_crit")) : scoreboard.getObjective("tp");
            Score score = scoreboard.getOrCreateScore(event.getEntityLiving().getName(), objective);
            scoreboard.setObjectiveInDisplaySlot(1, objective);
        }
    }

    @SubscribeEvent
    public void onEntityDrop(LivingDropsEvent event) {
        if (TakumiConfigCore.useTP && event.getEntityLiving() instanceof EntityCreeper &&
                event.getSource().getTrueSource() instanceof EntityPlayer) {
            ScoreObjective objective = event.getEntityLiving().world.getScoreboard().getObjective("tp");
            if (objective != null) {
                int point = 0;
                for (EntityItem entityItem : event.getDrops()) {
                    ItemStack item = entityItem.getItem();
                    if (item.getItem() == Items.GUNPOWDER) {
                        point += 1;
                    } else if (item.getItem() == Items.SKULL && item.getMetadata() == 4) {
                        point += 5;
                    } else if (item.getItem() == Item.getItemFromBlock(TakumiBlockCore.CREEPER_BOMB)) {
                        point += 10;
                    }
                    event.getEntityLiving().world.getScoreboard().getOrCreateScore(
                            event.getSource().getTrueSource().getName(), objective).increaseScore(point);
                }
            }
        }
    }

    @SubscribeEvent
    public void minecartUpdate(MinecartUpdateEvent event) {
        if (!event.getMinecart().world.isRemote) {
            int k = MathHelper.floor(event.getMinecart().posX);
            int l = MathHelper.floor(event.getMinecart().posY);
            int i1 = MathHelper.floor(event.getMinecart().posZ);
            if (BlockRailBase.isRailBlock(event.getMinecart().world, new BlockPos(k, l - 1, i1))) {
                --l;
            }
            BlockPos pos = new BlockPos(k, l, i1);
            IBlockState iblockstate = event.getMinecart().world.getBlockState(pos);
            if (event.getMinecart().canUseRail() && BlockRailBase.isRailBlock(iblockstate)) {
                if (iblockstate.getBlock() == TakumiBlockCore.CREEPER_RAIL_ACTIVATOR) {
                    event.getMinecart().onActivatorRailPass(k, l, i1, iblockstate.getValue(BlockRailPowered.POWERED));
                }
                if (iblockstate.getBlock() == TakumiBlockCore.CREEPER_RAIL_POWERED || iblockstate.getBlock() == TakumiBlockCore.CREEPER_RAIL_EXPLOSIVE) {
                    boolean flag;
                    boolean flag1;
                    flag = iblockstate.getValue(BlockRailPowered.POWERED);
                    flag1 = !flag;
                    if (iblockstate.getBlock() == TakumiBlockCore.CREEPER_RAIL_EXPLOSIVE) {
                        flag1 = false;
                    }
                    Entity entity = event.getMinecart().getPassengers().isEmpty() ? null : event.getMinecart().getPassengers().get(0);
                    BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = ((BlockRailBase) iblockstate.getBlock()).getRailDirection(event.getMinecart().world, pos, iblockstate, event.getMinecart());
                    if (entity instanceof EntityLivingBase) {
                        double d6 = ((EntityLivingBase) entity).moveForward;

                        if (d6 > 0.0D) {
                            double d7 = -Math.sin(entity.rotationYaw * 0.017453292F);
                            double d8 = Math.cos(entity.rotationYaw * 0.017453292F);
                            double d9 = event.getMinecart().motionX * event.getMinecart().motionX + event.getMinecart().motionZ * event.getMinecart().motionZ;

                            if (d9 < 0.01D) {
                                flag1 = false;
                            }
                        }
                    }
                    if (flag1 && event.getMinecart().shouldDoRailFunctions()) {
                        double d17 = Math.sqrt(event.getMinecart().motionX * event.getMinecart().motionX + event.getMinecart().motionZ * event.getMinecart().motionZ);

                        if (d17 < 0.03D) {
                            event.getMinecart().motionX *= 0.0D;
                            event.getMinecart().motionY *= 0.0D;
                            event.getMinecart().motionZ *= 0.0D;
                        } else {
                            event.getMinecart().motionX *= 0.5D;
                            event.getMinecart().motionY *= 0.0D;
                            event.getMinecart().motionZ *= 0.5D;
                        }
                    }
                    if (flag && event.getMinecart().shouldDoRailFunctions()) {
                        double d15 = Math.sqrt(event.getMinecart().motionX * event.getMinecart().motionX + event.getMinecart().motionZ * event.getMinecart().motionZ);

                        if (d15 > 0.01D) {
                            double d16 = 0.06D;
                            event.getMinecart().motionX += event.getMinecart().motionX / d15 * 0.06D;
                            event.getMinecart().motionZ += event.getMinecart().motionZ / d15 * 0.06D;
                        } else if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.EAST_WEST) {
                            if (event.getMinecart().world.getBlockState(pos.west()).isNormalCube()) {
                                event.getMinecart().motionX = 0.02D;
                            } else if (event.getMinecart().world.getBlockState(pos.east()).isNormalCube()) {
                                event.getMinecart().motionX = -0.02D;
                            }
                        } else if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.NORTH_SOUTH) {
                            if (event.getMinecart().world.getBlockState(pos.north()).isNormalCube()) {
                                event.getMinecart().motionZ = 0.02D;
                            } else if (event.getMinecart().world.getBlockState(pos.south()).isNormalCube()) {
                                event.getMinecart().motionZ = -0.02D;
                            }
                        }

                        if (iblockstate.getBlock() == TakumiBlockCore.CREEPER_RAIL_EXPLOSIVE) {
                            if (event.getMinecart().motionX > 0.1 || event.getMinecart().motionZ > 0.1) {
                                event.getMinecart().motionX = event.getMinecart().motionX * 5;
                                event.getMinecart().motionZ = event.getMinecart().motionZ * 5;
                                event.getMinecart().motionY = 3;
                                event.getMinecart().onGround = false;
                                event.getMinecart().move(MoverType.SELF, event.getMinecart().motionX, event.getMinecart().motionY, event.getMinecart().motionZ);
                                if (!event.getMinecart().world.isRemote) {
                                    Explosion explosion = event.getMinecart().world.createExplosion(event.getMinecart(),
                                            event.getPos().getX(), event.getPos().getY() + 0.5, event.getPos().getZ(), 0f, false);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void respawn(PlayerEvent.Clone event) {
        if (!event.getEntityPlayer().world.loadedEntityList.isEmpty() && event.isWasDeath() && event.getOriginal().getLastDamageSource() != null &&
                event.getOriginal().getLastDamageSource().isExplosion() && event.getOriginal().getLastDamageSource().getTrueSource() instanceof EntityKeepCreeper) {
            event.getEntityPlayer().world.loadedEntityList.forEach(entity -> {
                if (entity instanceof EntityItem && ((EntityItem) entity).getOwner() != null && ((EntityItem) entity).getOwner().equals(event.getOriginal().getName())) {
                    event.getEntityPlayer().addItemStackToInventory(((EntityItem) entity).getItem());
                    entity.setDead();
                }
            });
        }
    }

    @SubscribeEvent
    public void playerDrop(PlayerDropsEvent event) {
        if (!event.getDrops().isEmpty() && event.getSource().getTrueSource() instanceof EntityKeepCreeper && event.getSource().isExplosion()) {
            event.getDrops().forEach(entityItem -> entityItem.setOwner(event.getEntityPlayer().getName()));
        }
    }

    @SubscribeEvent
    public void breakSpeed(PlayerEvent.BreakSpeed event) {
        if (event.getState().getBlock() == TakumiBlockCore.CREEPER_WOOL || event.getState().getBlock() == TakumiBlockCore.CREEPER_CARPET) {
            if (event.getEntityPlayer().getHeldItemMainhand().getItem() == Items.SHEARS) {
                event.setNewSpeed(5f);
            }
        }
    }

    @SubscribeEvent
    public void serverChat(ServerChatEvent event) {
        if (TakumiConfigCore.inEventServer && TakumiConfigCore.SPEC_CHAT) {
            if (event.getPlayer().isSpectator()) {
                //[spec]->takumicraft.tcs.spec, compString -> compTranslation
                Style style = new Style();
                style.setColor(TextFormatting.DARK_PURPLE);
                style.setBold(true);
                style.setClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "tcs_spec"));
                ITextComponent comp = new TextComponentTranslation("takumicraft.tcs.spec");
                comp.setStyle(style);
                comp.appendText(" ");
                ITextComponent message = comp.appendSibling(event.getComponent().setStyle(new Style().setColor(TextFormatting.RESET).setBold(false)));
                event.setComponent(message);
            }
        }
    }

    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
        if (event.getSource().getTrueSource() instanceof EntityLivingBase &&
                ((EntityLivingBase) event.getSource().getTrueSource()).getHeldItemMainhand().getItem() == TakumiItemCore.TAKUMI_KNIFE &&
                (event.getSource().getDamageType().equals("player") || event.getSource().isCreativePlayer())) {
            event.getDrops().replaceAll(entityItem -> {
                if (ItemTakumiSpecialMeat.getSpecializedMeat(entityItem.getItem().getItem(), true) != null) {
                    EntityItem newItem = new EntityItem(entityItem.world);
                    newItem.copyLocationAndAnglesFrom(entityItem);
                    newItem.setItem(new ItemStack(ItemTakumiSpecialMeat.getSpecializedMeat(entityItem.getItem().getItem(), true), entityItem.getItem().getCount()));
                    return newItem;
                }
                return entityItem;
            });
        }
    }

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getEntityPlayer() != null && event.getEntityPlayer().isPotionActive(TakumiPotionCore.FROZEN) && !event.getEntityPlayer().isCreative()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntityPlayer() != null && event.getEntityPlayer().isPotionActive(TakumiPotionCore.FROZEN) && !event.getEntityPlayer().isCreative()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getEntityPlayer() != null && event.getEntityPlayer().isPotionActive(TakumiPotionCore.FROZEN) && !event.getEntityPlayer().isCreative()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {
        if (event.getEntityPlayer() != null && event.getEntityPlayer().isPotionActive(TakumiPotionCore.FROZEN) && !event.getEntityPlayer().isCreative()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onUseItem(LivingEntityUseItemEvent.Start event) {
        if (event.getEntityLiving() != null && event.getEntityLiving().isPotionActive(TakumiPotionCore.FROZEN) &&
                !(event.getEntityLiving() instanceof EntityPlayer && ((EntityPlayer) event.getEntityLiving()).isCreative())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if (event.getEntityPlayer() != null && event.getEntityPlayer().isPotionActive(TakumiPotionCore.FROZEN) && !event.getEntityPlayer().isCreative()) {
            event.setCanceled(true);
        } else if (event.getTarget() instanceof EntityLivingBase && ((EntityLivingBase) event.getTarget()).isPotionActive(TakumiPotionCore.FROZEN) && !event.getTarget().world.isRemote) {
            ((EntityLivingBase) event.getTarget()).removePotionEffect(TakumiPotionCore.FROZEN);
            PotionEffect effect = new PotionEffect(TakumiPotionCore.FROZEN, 200, 0, true, false);
            TakumiPacketCore.INSTANCE.sendToAll(new MessageFrozenEffect(event.getTarget().getEntityId(), effect, true));
            event.getTarget().playSound(SoundEvents.BLOCK_GLASS_BREAK, 1f, 1f);
        }
    }

    @SubscribeEvent
    public void onBreak(PlayerEvent.BreakSpeed event) {
        if (event.getEntityPlayer() != null && event.getEntityPlayer().isPotionActive(TakumiPotionCore.FROZEN) && !event.getEntityPlayer().isCreative()) {
            event.setNewSpeed(event.getOriginalSpeed() / 100);
        }
    }

    @SubscribeEvent
    public void onFinishBreak(BlockEvent.BreakEvent event) {
        if (event.getPlayer() != null && event.getPlayer().isPotionActive(TakumiPotionCore.FROZEN) && !event.getPlayer().isCreative()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntityLiving() != null && event.getEntityLiving().isPotionActive(TakumiPotionCore.FROZEN) &&
                !(event.getEntityLiving() instanceof EntityPlayer && ((EntityPlayer) event.getEntityLiving()).isCreative())) {
            event.getEntityLiving().isAirBorne = false;
            event.getEntityLiving().motionX = 0;
            event.getEntityLiving().motionY = 0;
            event.getEntityLiving().motionZ = 0;
        }
    }

    @SubscribeEvent
    public void onKnockBack(LivingKnockBackEvent event) {
        if (event.getEntityLiving() != null && event.getEntityLiving().isPotionActive(TakumiPotionCore.FROZEN)) {
            event.setStrength(event.getOriginalStrength() / 100);
        }
    }
}
