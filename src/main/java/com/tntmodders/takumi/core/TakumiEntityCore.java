package com.tntmodders.takumi.core;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.client.render.*;
import com.tntmodders.takumi.core.client.TakumiClientCore;
import com.tntmodders.takumi.entity.EntityTakumiLightningBolt;
import com.tntmodders.takumi.entity.ITakumiEntity;
import com.tntmodders.takumi.entity.ITakumiEvoEntity;
import com.tntmodders.takumi.entity.item.*;
import com.tntmodders.takumi.entity.mobs.*;
import com.tntmodders.takumi.entity.mobs.boss.*;
import com.tntmodders.takumi.entity.mobs.noncreeper.EntityBoneDummy;
import com.tntmodders.takumi.entity.mobs.noncreeper.EntityDarkVillager;
import com.tntmodders.takumi.entity.mobs.noncreeper.EntityOddDummyGhast;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.init.Biomes;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeOcean;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.io.*;
import java.net.URL;
import java.util.*;

public class TakumiEntityCore {

    //public static final EnumCreatureType CREATURE_TAKUMI = EnumCreatureType.MONSTER;
    /*            EnumHelper.addCreatureType("creature_takumi", IMob.class, TakumiConfigCore.spawnWeightAnimal, Material.AIR,
                        false, false);*/
    public static final EnumCreatureType WATER_TAKUMI = EnumCreatureType.WATER_CREATURE;
    /*            EnumHelper.addCreatureType("water_takumi", IMob.class, 30, Material.WATER, false, false);*/
    public static final List<Class<? extends ITakumiEntity>> CLASS_LIST = new ArrayList<>();
    private static final List<ITakumiEntity> ENTITY_LIST = new ArrayList<>();
    public static List<Biome> biomes = new ArrayList<>();

    static {
        CLASS_LIST.addAll(Arrays.asList(EntityAcidCreeper.class, EntityAnnivCreeper.class, EntityAntinomyCreeper.class,
                EntityArrowCreeper.class, EntityArtCreeper.class, EntityBangCreeper.class, EntityBatCreeper.class,
                EntityBedCreeper.class, EntityBigFishCreeper.class, EntityBirdCreeper.class, EntityBlazeCreeper.class,
                EntityBoltCreeper.class, EntityCallCreeper.class, EntityCatCreeper.class, EntityCeruleanCreeper.class,
                EntityChestCreeper.class, EntityChunkCreeper.class, EntityColorCreeper.class, EntityCowCreeper.class,
                EntityCreativeCreeper.class, EntityDarkCreeper.class, EntityDemonCreeper.class,
                EntityDestructionCreeper.class, EntityDiamondCreeper.class, EntityDirtCreeper.class,
                EntityEarthCreeper.class, EntityEmeraldCreeper.class, EntityEnderCreeper.class,
                EntityEndermiteCreeper.class, EntityFallCreeper.class, EntityFighterCreeper.class,
                EntityFireCreeper.class, EntityFireworksCreeper.class, EntityFishCreeper.class,
                EntityFrostCreeper.class, EntityGhastCreeper.class, EntityGiantCreeper.class, EntityGlassCreeper.class,
                EntityGlowStoneCreeper.class, EntityGorgeousCreeper.class, EntityGunoreCreeper.class,
                EntityHappinessCreeper.class, EntityHorseCreeper.class, EntityHotSpringCreeper.class,
                EntityHuskCreeper.class, EntityIceCreeper.class, EntityInversionCreeper.class, EntityIronCreeper.class,
                EntityKingCreeper.class, EntityLapisCreeper.class, EntityLavaCreeper.class, EntityLeadCreeper.class,
                EntityLightCreeper.class, EntityLostCreeper.class, EntityLuckCreeper.class, EntityMagmaCreeper.class,
                EntityMiniSpiderCreeper.class, EntityMusicCreeper.class, EntityNaturalCreeper.class,
                EntityObjetCreeper.class, EntityOfalenCreeper.class, EntityOneCreeper.class, EntityParrotCreeper.class,
                EntityPierceCreeper.class, EntityPigCreeper.class, EntityRabbitCreeper.class, EntityRareCreeper.class,
                EntityRedStoneCreeper.class, EntityReverseCreeper.class, EntityRewriteCreeper.class,
                EntityRoundCreeper.class, EntityRushCreeper.class, EntitySandCreeper.class,
                EntitySandstormCreeper.class, EntitySeaGuardianCreeper.class, EntitySheepCreeper.class,
                EntitySilentCreeper.class, EntitySkeletonCreeper.class, EntitySlimeCreeper.class,
                EntitySnowCreeper.class, EntitySpiderCreeper.class, EntitySquidCreeper.class, EntityStoneCreeper.class,
                EntityStrayCreeper.class, EntitySubsidenceCreeper.class, EntityTNTCreeper.class,
                EntityTreeCreeper.class, EntityUpperCreeper.class, EntityVoidCreeper.class, EntityWaterCreeper.class,
                EntityWeatherCreeper.class, EntityWitchCreeper.class, EntityWitherSkeletonCreeper.class,
                EntityWolfCreeper.class, EntityWoodCreeper.class, EntityWrylyCreeper.class, EntityYukariCreeper.class,
                EntityZombieCreeper.class, EntityZombieVillagerCreeper.class, EntityCraftsmanCreeper.class,
                EntityLlamaCreeper.class, EntityPolarBearCreeper.class, EntityShulkerCreeper.class,
                EntityConcreteCreeper.class, EntityVindicatorCreeper.class, EntityEvokerCreeper.class,
                EntityVexCreeper.class, EntityIllusionerCreeper.class, EntityPigmanCreeper.class,
                EntityKillerCreeper.class, EntityExperienceCreeper.class, EntitySinobiCreeper.class,
                EntityStraightCreeper.class, EntityCrossCreeper.class, EntityTransCreeper.class,
                EntityShootingCreeper.class, EntityShadowCreeper.class, EntityFallingBombCreeper.class,
                EntityFallingSlimeCreeper.class, EntityTerracottaCreeper.class, EntityWearCreeper.class,
                EntityBlackCreeper.class, EntityAngryCreeper.class, EntityJumpCreeper.class, EntityBigCreeper.class,
                EntityRoboCreeper.class, EntityDanceCreeper.class, EntityFarmCreeper.class, EntityGateCreeper.class,
                EntityDashCreeper.class, EntityNoobCreeper.class, EntityHoeCreeper.class, EntityLaunchCreeper.class,
                EntityStingyCreeper.class, EntityInvisibleCreeper.class, EntityReturnCreeper.class,
                EntityAnvilCreeper.class, EntityRandCreeper.class, EntityEPCreeper.class, EntityVirusCreeper.class,
                EntityCannonCreeper.class, EntityCactusCreeper.class, EntityGrassCreeper.class, EntityBoxCreeper.class,
                EntityPoweredCreeper.class, EntityUnlimitedCreeper.class, EntityBigSpiderCreeper.class,
                EntityMineCreeper.class, EntityBalloonCreeper.class, EntityOceanCreeper.class,
                EntityForestCreeper.class, EntityOddCreeper.class, EntityFloatCreeper.class, EntityCrystalCreeper.class,
                EntityDeteriorationCreeper.class, EntitySleeperCreeper.class, EntityTriZombieCreeper.class,
                EntityDisorderCreeper.class, EntityTransCreeper_2.class, EntityNetherCreeper.class,
                EntityFesCreeper.class, EntityBoneCreeper.class, EntityMasterCreeper.class, EntitySpongeCreeper.class,
                EntityBigBatCreeper.class, EntityClockCreeper.class, EntityLevelUpCreeper.class,
                EntityBeetrootCreeper.class, EntityMirrorCreeper.class, EntityFastCreeper.class,
                EntityWhiteEyesCreeper.class, EntityHyperspaceCreeper.class, EntityMotherCreeper.class,
                EntityConstructCreeper.class, EntityShockCreeper.class, EntityDoubleCreeper.class,
                EntityMeatCreeper.class, EntityLineCreeper.class, EntityMakeupCreeper.class, EntityTitanCreeper.class,
                EntityNerdCreeper.class, EntityDaimonjiCreeper.class, EntityTearCreeper.class, EntityCryCreeper.class, EntityOpaqueCreeper.class,
                EntityLayerCreeper.class, EntityChaseCreeper.class, EntityBoarCreeper.class, EntityMouseCreeper.class, EntityEvolutionCreeper.class,
                EntityObsidianCreeper.class, EntityWitherCreeper.class, EntityAngelCreeper.class, EntityParalysisCreeper.class, EntityKeepCreeper.class,
                EntitySlipCreeper.class, EntityEdgyCreeper.class, EntityPsychicCreeper.class, EntityShrinkCreeper.class, EntityTossCreeper.class, EntityWonderCreeper.class,
                EntityGemCreeper.class, EntityFakeCreeper.class, EntityPhantomCreeper.class, EntityPoisonCreeper.class, EntityMyceliumCreeper.class,
                EntityIceologerCreeper.class, EntityExtremeCreeper.class, EntityPiglinCreeper.class, EntityCorruptionCreeper.class, EntityEvaporationCreeper.class,
                EntityPulseCreeper.class, EntitySmokeCreeper.class, EntityNightmareCreeper.class, EntityVergerCreeper.class));
    }

    public static List<ITakumiEntity> getEntityList() {
        return ENTITY_LIST;
    }

    public static void register() {
        //CREATURE_TAKUMI = EnumHelper.addCreatureType("creature_takumi", EntityTakumiAbstractCreeper.class, 100, Material.AIR, false, true);
        //WATER_TAKUMI = EnumHelper.addCreatureType("water_takumi", EntityTakumiAbstractCreeper.class, 25, Material.WATER, false, false);
        Biome.REGISTRY.iterator().forEachRemaining(biome -> biomes.add(biome));
        biomes.remove(Biomes.HELL);
        biomes.remove(Biomes.VOID);
        biomes.remove(Biomes.SKY);

        /*List<Class> files = TakumiUtils.getListClass("com/tntmodders/takumi/entity/mobs/");*/
        ArrayList<EntityHolder> entityHolders = new ArrayList<>();
        for (Class clazz : CLASS_LIST) {
            try {
                /*ClassLoader loader = TakumiCraftCore.class.getClassLoader();
                //Class clazz = loader.loadClass("com.tntmodders.takumi.entity.mobs." + file.getName().replaceAll(".class", ""));*/
                ITakumiEntity entity = (ITakumiEntity) clazz.getConstructor(World.class).newInstance((World) null);
                entityHolders.add(new EntityHolder(clazz, entity));
                if (entity instanceof ITakumiEvoEntity && !((ITakumiEvoEntity) entity).isEvo()) {
                    ITakumiEntity evoEntity = ((ITakumiEvoEntity) entity).getEvoCreeper();
                    entityHolders.add(new EntityHolder(evoEntity.getClass(), evoEntity));
                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
        //List <File> files = TakumiUtils.getListFile("com/tntmodders/takumi/entity/mobs/");
        /*for (File file : files) {
            try {
                ClassLoader loader = TakumiCraftCore.class.getClassLoader();
                Class clazz = loader.loadClass("com.tntmodders.takumi.entity.mobs." + file.getName().replaceAll(".class", ""));
                ITakumiEntity entity = (ITakumiEntity) clazz.getConstructor(World.class).newInstance((World) null);
                entityHolders.add(new EntityHolder(clazz, entity));
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }*/
        entityHolders.sort(new EntityComparator());
        for (EntityHolder holder : entityHolders) {
            Class clazz = holder.clazz;
            ITakumiEntity entity = holder.entity;
            if (!entity.canRegister()) {
                continue;
            }
            ResourceLocation location = new ResourceLocation(TakumiCraftCore.MODID, entity.getRegisterName());
            EntityRegistry.registerModEntity(location, clazz, location.getResourcePath(), entity.getRegisterID(),
                    TakumiCraftCore.TakumiInstance, 64, 2, true, entity.getPrimaryColor(), entity.getSecondaryColor());
            if (entity.isCustomSpawn()) {
                try {
                    entity.customSpawn();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (entity.takumiRank().getSpawnWeight() != 0 && !entity.isAnimal() && !(entity instanceof ITakumiEvoEntity && ((ITakumiEvoEntity) entity).isEvo())) {
                Biome.REGISTRY.iterator().forEachRemaining(biome -> {
                    if (!(biome instanceof BiomeOcean) && biome != Biomes.HELL && biome != Biomes.VOID &&
                            biome != Biomes.SKY) {
                        int mp = entity.takumiRank() == ITakumiEntity.EnumTakumiRank.LOW ? 2 : 1;
                        EntityRegistry.addSpawn(clazz, entity.takumiRank().getSpawnWeight(), 10 * mp, 20 * mp,
                                EnumCreatureType.MONSTER, biome);
                    }
                });
                entity.additionalSpawn();
                //TakumiBiomeCore.BIOMES.forEach(biome -> EntityRegistry.addSpawn(clazz, 100, 10, 501, EnumCreatureType.MONSTER, biome));
            }
            if (FMLCommonHandler.instance().getSide().isClient()) {
                TakumiClientCore.registerEntityRender(clazz, entity);
            }
            ENTITY_LIST.add(entity);
            TakumiCraftCore.LOGGER.info(
                    "Registered entity on ID " + entity.getRegisterID() + " : " + location.getResourcePath() + " , " +
                            entity.takumiRank().name() + " and " + entity.takumiType().name());

            File packFile = FMLCommonHandler.instance().findContainerFor(TakumiCraftCore.TakumiInstance).getSource();
            File oldFile = null;
            String assetS = "assets/takumicraft/advancements/slay";
            for (File f : TakumiUtils.getListFile(assetS)) {
                if (f.getName().contains("slay_.json")) {
                    oldFile = f;
                    break;
                }
            }
            if (oldFile != null) {
                ClassLoader loader = TakumiCraftCore.class.getClassLoader();
                URL url = loader.getResource(assetS);
                if (!Objects.equals(url.getProtocol(), "jar")) {
                    String[] strings = {oldFile.getAbsolutePath().replaceAll(".json", ""),
                            oldFile.getAbsolutePath().split("out")[0] + "src" +
                                    oldFile.getAbsolutePath().split("out")[1].replaceAll("production",
                                            "main").replaceAll("minecraft", "resources").replaceAll(".json", "")};
                    for (String sPath : strings) {
                        String sResource = sPath + entity.getRegisterName() + ".json";
                        File file = new File(sResource);
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        FileReader h_fr;
                        String buf = "";

                        String h_s;
                        try {
                            h_fr = new FileReader(oldFile);
                            BufferedReader h_br = new BufferedReader(h_fr);
                            while (true) {
                                h_s = h_br.readLine();
                                if (h_s == null) {
                                    break;
                                }

                                h_s = h_s.replaceAll("minecraft:creeper", "takumicraft:" + entity.getRegisterName());
                                h_s = h_s.replaceAll("creeper_hoge", entity.getRegisterName());
                                buf = buf + h_s;
                            }
                            h_fr.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            FileWriter writer = new FileWriter(file);
                            writer.write(buf);
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        TakumiCraftCore.LOGGER.info("Registered all " + ENTITY_LIST.size() + " entites.");
        itemRegister();
        if (FMLCommonHandler.instance().getSide().isClient()) {
            renderRegister();
        }
    }

    private static void itemRegister() {
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "takumiarrow"),
                EntityTakumiArrow.class, "takumiarrow", 900, TakumiCraftCore.TakumiInstance, 64, 2, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "takumisnowball"),
                EntityTakumiSnowBall.class, "takumiasnowball", 901, TakumiCraftCore.TakumiInstance, 64, 2, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "takumipotion"),
                EntityTakumiPotion.class, "takumipotion", 902, TakumiCraftCore.TakumiInstance, 64, 2, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "takumiexpegg"),
                EntityTakumiExpEgg.class, "takumiexpegg", 903, TakumiCraftCore.TakumiInstance, 64, 2, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "takumitnt"),
                EntityTakumiTNTPrimed.class, "takumitnt", 904, TakumiCraftCore.TakumiInstance, 64, 2, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "llamacreeperspit"),
                EntityLlamaCreeperSpit.class, "llamacreeperspit", 905, TakumiCraftCore.TakumiInstance, 64, 2, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "takumichocolateball"),
                EntityTakumiChocolateBall.class, "takumichocolateball", 906, TakumiCraftCore.TakumiInstance, 64, 2,
                true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "fallingbomb"),
                EntityFallingBomb.class, "fallingbomb", 907, TakumiCraftCore.TakumiInstance, 64, 2, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "darkvillager"),
                EntityDarkVillager.class, "darkvillager", 908, TakumiCraftCore.TakumiInstance, 64, 2, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "transhomingbomb"),
                EntityTransHomingBomb.class, "transhomingbomb", 909, TakumiCraftCore.TakumiInstance, 64, 2, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "attackblock"),
                EntityAttackBlock.class, "attackblock", 910, TakumiCraftCore.TakumiInstance, 1024, 2, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "takumiXMS"), EntityXMS.class,
                "takumiXMS", 911, TakumiCraftCore.TakumiInstance, 64, 1, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "msrazer"), EntityMSRazer.class,
                "msrazer", 912, TakumiCraftCore.TakumiInstance, 64, 2, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "takumiYMS"), EntityYMS.class,
                "takumiYMS", 913, TakumiCraftCore.TakumiInstance, 64, 1, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "takumilauncher"),
                EntityTakumiLauncher.class, "takumilauncher", 914, TakumiCraftCore.TakumiInstance, 64, 2, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "waterforce"),
                EntityWaterTypeForce.class, "waterforce", 915, TakumiCraftCore.TakumiInstance, 64, 1, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "windlance"),
                EntityWindTypeLance.class, "windlance", 916, TakumiCraftCore.TakumiInstance, 64, 1, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "bighomingbomb"),
                EntityBigHomingBomb.class, "bighomingbomb", 917, TakumiCraftCore.TakumiInstance, 64, 2, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "odddummyghast"),
                EntityOddDummyGhast.class, "odddummyghast", 918, TakumiCraftCore.TakumiInstance, 64, 2, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "dummyking"),
                EntityKingDummy.class, "dummyking", 919, TakumiCraftCore.TakumiInstance, 64, 2, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "takumimlrs"),
                EntityTakumiMLRS.class, "takumimlrs", 920, TakumiCraftCore.TakumiInstance, 64, 2, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "bonecreeperdummy"),
                EntityBoneDummy.class, "bonecreeperdummy", 921, TakumiCraftCore.TakumiInstance, 64, 2, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "takumithrowgrenede"),
                EntityTakumiThrowGrenede.class, "takumithrowgrenede", 922, TakumiCraftCore.TakumiInstance, 64, 2,
                true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "withercreeperskull"),
                EntityWitherCreeperSkull.class, "withercreeperskull", 923, TakumiCraftCore.TakumiInstance, 64, 2,
                true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "titanmeteor"),
                EntityTakumiTitanMeteor.class, "titanmeteor", 924, TakumiCraftCore.TakumiInstance, 64, 2,
                true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "creeperminecart"),
                EntityTakumiMinecart.class, "creeperminecart", 925, TakumiCraftCore.TakumiInstance, 64, 2,
                true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "creeperboat"),
                EntityTakumiBoat.class, "creeperboat", 926, TakumiCraftCore.TakumiInstance, 64, 2,
                true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "creeperbolt"),
                EntityTakumiBoat.class, "creeperbolt", 927, TakumiCraftCore.TakumiInstance, 160, 2,
                false);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "creeperlaser"),
                EntityTakumiLaser.class, "creeperlaser", 928, TakumiCraftCore.TakumiInstance, 160, 2,
                false);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "takumithrowgrenede_sp"),
                EntityTakumiThrowGrenede_SP.class, "takumithrowgrenede", 929, TakumiCraftCore.TakumiInstance, 64, 2,
                true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "takumiepicitem"),
                EntityItemEpicDrop.class, "takumiepicitem", 930, TakumiCraftCore.TakumiInstance, 64, 2,
                true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "bigcreeperdummy"),
                EntityBigCreeperDummy.class, "bigcreeperdummy", 931, TakumiCraftCore.TakumiInstance, 1024, 2, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "destgolem"),
                EntityDestGolem.class, "destgolem", 932, TakumiCraftCore.TakumiInstance, 64, 2, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "takumiparachute"),
                EntityTakumiParachute.class, "takumiparachute", 933, TakumiCraftCore.TakumiInstance, 64, 2, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "takumirandomchest"),
                EntityTakumiRandomChest.class, "takumirandomchest", 934, TakumiCraftCore.TakumiInstance, 64, 2, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "takumiknifegungrenede"),
                EntityTakumiKnifeGun.class, "takumiknifegungrenede", 935, TakumiCraftCore.TakumiInstance, 64, 2, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "iceologercreeperspell"),
                EntityIceologerCreeperSpell.class, "iceologercreeperspell", 936, TakumiCraftCore.TakumiInstance, 64, 2, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "takumicannon"),
                EntityTakumiCannon.class, "takumicannon", 937, TakumiCraftCore.TakumiInstance, 64, 2, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "takumicannonball"),
                EntityTakumiCannonBall.class, "takumicannonball", 938, TakumiCraftCore.TakumiInstance, 64, 2, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "kingcreeperspellblock"),
                EntityKingBlock.class, "kingcreeperspellblock", 939, TakumiCraftCore.TakumiInstance, 64, 2, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "kingcreeperstorm"),
                EntityKingStorm.class, "kingcreeperstorm", 940, TakumiCraftCore.TakumiInstance, 64, 2, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "takumikingtoolarrow"),
                EntityTakumiKingToolArrow.class, "takumikingtoolarrow", 941, TakumiCraftCore.TakumiInstance, 64, 2, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "smokeparticle"),
                EntitySmokeParticle.class, "smokeparticle", 942, TakumiCraftCore.TakumiInstance, 64, 2, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "vergerball"),
                EntityVergerBall.class, "vergerball", 943, TakumiCraftCore.TakumiInstance, 64, 2, true);
    }

    @SideOnly(Side.CLIENT)
    private static void renderRegister() {
        RenderingRegistry.registerEntityRenderingHandler(EntityTakumiArrow.class,
                manager -> new RenderArrow<EntityArrow>(manager) {
                    @Override
                    protected ResourceLocation getEntityTexture(EntityArrow entity) {
                        return new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/item/carrow.png");
                    }
                });
        RenderingRegistry.registerEntityRenderingHandler(EntityTakumiKingToolArrow.class,
                manager -> new RenderArrow<EntityArrow>(manager) {
                    @Override
                    protected ResourceLocation getEntityTexture(EntityArrow entity) {
                        return new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/item/carrow.png");
                    }
                });
        RenderingRegistry.registerEntityRenderingHandler(EntityTakumiSnowBall.class,
                manager -> new RenderSnowball<>(manager, Items.SNOWBALL, Minecraft.getMinecraft().getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(EntityTakumiExpEgg.class,
                manager -> new RenderSnowball<>(manager, Items.EGG, Minecraft.getMinecraft().getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(EntityTakumiTNTPrimed.class, RenderTakumiTNTPrimed::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityLlamaCreeperSpit.class, RenderLlamaCreeperSpit::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTakumiChocolateBall.class,
                manager -> new RenderSnowball<>(manager, TakumiItemCore.TAKUMI_CHOCO_BALL,
                        Minecraft.getMinecraft().getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(EntityDarkVillager.class, RenderDarkVillager::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTransHomingBomb.class,
                manager -> new RenderShulkerBullet(manager) {
                    @Override
                    public void doRender(EntityShulkerBullet entity, double x, double y, double z, float entityYaw,
                                         float partialTicks) {
                    }
                });
        RenderingRegistry.registerEntityRenderingHandler(EntityBigHomingBomb.class,
                manager -> new RenderShulkerBullet(manager) {
                    @Override
                    public void doRender(EntityShulkerBullet entity, double x, double y, double z, float entityYaw,
                                         float partialTicks) {
                    }
                });
        RenderingRegistry.registerEntityRenderingHandler(EntityAttackBlock.class, RenderAttackBlock::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityBigCreeperDummy.class, RenderBigCreeperDummy::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityXMS.class, RenderXMS::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityMSRazer.class, RenderMSRazer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityYMS.class, RenderYMS::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTakumiLauncher.class,
                manager -> new Render<EntityTakumiLauncher>(manager) {
                    @Nullable
                    @Override
                    protected ResourceLocation getEntityTexture(EntityTakumiLauncher entity) {
                        return null;
                    }
                });
        RenderingRegistry.registerEntityRenderingHandler(EntityWaterTypeForce.class,
                manager -> new Render<EntityWaterTypeForce>(manager) {
                    @Nullable
                    @Override
                    protected ResourceLocation getEntityTexture(EntityWaterTypeForce entity) {
                        return null;
                    }
                });
        RenderingRegistry.registerEntityRenderingHandler(EntityWindTypeLance.class,
                manager -> new Render<EntityWindTypeLance>(manager) {
                    @Nullable
                    @Override
                    protected ResourceLocation getEntityTexture(EntityWindTypeLance entity) {
                        return null;
                    }
                });
        RenderingRegistry.registerEntityRenderingHandler(EntityOddDummyGhast.class,
                manager -> new Render<EntityOddDummyGhast>(manager) {
                    @Nullable
                    @Override
                    protected ResourceLocation getEntityTexture(EntityOddDummyGhast entity) {
                        return null;
                    }
                });
        RenderingRegistry.registerEntityRenderingHandler(EntityKingDummy.class,
                manager -> new Render<EntityKingDummy>(manager) {
                    @Nullable
                    @Override
                    protected ResourceLocation getEntityTexture(EntityKingDummy entity) {
                        return null;
                    }
                });

        RenderingRegistry.registerEntityRenderingHandler(EntityBoneDummy.class, RenderBoneDummy::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTakumiThrowGrenede.class,
                manager -> new RenderSnowball<>(manager, TakumiItemCore.THROW_GRENEDE,
                        Minecraft.getMinecraft().getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(EntityTakumiTitanMeteor.class, RenderTitanMeteor::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTakumiMinecart.class, manager -> new RenderMinecart<EntityTakumiMinecart>(manager) {
            @Override
            protected ResourceLocation getEntityTexture(EntityTakumiMinecart entity) {
                return new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/creeperminecart.png");
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(EntityTakumiBoat.class, manager -> new RenderBoat(manager) {
            @Override
            protected ResourceLocation getEntityTexture(EntityBoat entity) {
                return new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/creeperboat.png");
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(EntityPainting.class, RenderTakumiPainting::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityItemFrame.class, manager -> new RenderTakumiItemFrame(manager, Minecraft.getMinecraft().getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(EntityTakumiLightningBolt.class, RenderTakumiLightningBolt::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTakumiLaser.class, RenderTakumiLaser::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityDestGolem.class, manager -> new RenderIronGolem(manager) {
            @Override
            protected ResourceLocation getEntityTexture(EntityIronGolem entity) {
                return new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/destgolem.png");
            }

            @Override
            protected void preRenderCallback(EntityIronGolem entitylivingbaseIn, float partialTickTime) {
                super.preRenderCallback(entitylivingbaseIn, partialTickTime);
                GlStateManager.scale(0.5f, 0.5f, 0.5f);
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(EntityTakumiParachute.class, RenderTakumiParachute::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTakumiRandomChest.class, RenderTakumiRandomChest::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTakumiKnifeGun.class, RenderTakumiKnifeGun::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityIceologerCreeperSpell.class, RenderIceologerCreeperSpell::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTakumiCannon.class, RenderTakumiCannon::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTakumiCannonBall.class,
                manager -> new RenderSnowball<>(manager, TakumiItemCore.THROW_GRENEDE, Minecraft.getMinecraft().getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(EntityKingBlock.class, manager -> new RenderIceologerCreeperSpell(manager, TakumiBlockCore.CREEPER_ALTAR));
        RenderingRegistry.registerEntityRenderingHandler(EntityKingStorm.class, manager -> new RenderLiving<EntityKingStorm>(manager, new ModelCreeper(), 0f) {
            @Override
            protected ResourceLocation getEntityTexture(EntityKingStorm entity) {
                return null;
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(EntityVergerBall.class, manager -> new Render<EntityVergerBall>(manager) {
            @Nullable
            @Override
            protected ResourceLocation getEntityTexture(EntityVergerBall entity) {
                return null;
            }
        });
    }

    static class EntityComparator implements Comparator<EntityHolder> {

        @Override
        public int compare(EntityHolder o1, EntityHolder o2) {
            return Integer.compare(o1.entity.getRegisterID(), o2.entity.getRegisterID());
        }
    }

    static class EntityHolder {

        final Class clazz;
        final ITakumiEntity entity;

        EntityHolder(Class cls, ITakumiEntity ent) {
            this.clazz = cls;
            this.entity = ent;
        }
    }
}
