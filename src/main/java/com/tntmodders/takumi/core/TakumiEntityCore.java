package com.tntmodders.takumi.core;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.client.render.RenderTakumiTNTPrimed;
import com.tntmodders.takumi.core.client.TakumiModelCore;
import com.tntmodders.takumi.entity.ITakumiEntity;
import com.tntmodders.takumi.entity.item.*;
import com.tntmodders.takumi.entity.mobs.*;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.projectile.EntityArrow;
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

import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;

public class TakumiEntityCore {
    
    /*public static final EnumCreatureType CREATURE_TAKUMI = EnumHelper.addCreatureType("creature_takumi", IMob.class, 100, Material.AIR, false, false);
    public static final EnumCreatureType WATER_TAKUMI = EnumHelper.addCreatureType("water_takumi", IMob.class, 50, Material.WATER, false, false);*/
    private static final List <Class <? extends ITakumiEntity>> CLASS_LIST = new ArrayList <>();
    public static List <Biome> biomes = new ArrayList <>();
    private static List <ITakumiEntity> entityList = new ArrayList <>();
    
    static {
        CLASS_LIST.addAll(Arrays.asList(EntityAcidCreeper.class, EntityAnnivCreeper.class, EntityAntinomyCreeper.class, EntityArrowCreeper.class,
                EntityArtCreeper.class, EntityBangCreeper.class, EntityBatCreeper.class, EntityBedCreeper.class, EntityBigFishCreeper.class,
                EntityBirdCreeper.class, EntityBlazeCreeper.class, EntityBoltCreeper.class, EntityCallCreeper.class, EntityCatCreeper.class,
                EntityCeruleanCreeper.class, EntityChestCreeper.class, EntityChunkCreeper.class, EntityColorCreeper.class, EntityCowCreeper.class,
                EntityCreativeCreeper.class, EntityDarkCreeper.class, EntityDemonCreeper.class, EntityDestructionCreeper.class,
                EntityDiamondCreeper.class, EntityDirtCreeper.class, EntityEarthCreeper.class, EntityEmeraldCreeper.class, EntityEnderCreeper
                        .class, EntityEndermiteCreeper.class, EntityFallCreeper.class, EntityFighterCreeper.class, EntityFireCreeper.class,
                EntityFireworksCreeper.class, EntityFishCreeper.class, EntityFrostCreeper.class, EntityGhastCreeper.class, EntityGiantCreeper
                        .class, EntityGlassCreeper.class, EntityGlowStoneCreeper.class, EntityGorgeousCreeper.class, EntityGunoreCreeper.class,
                EntityHappinessCreeper.class, EntityHorseCreeper.class, EntityHotSpringCreeper.class, EntityHuskCreeper.class, EntityIceCreeper
                        .class, EntityInversionCreeper.class, EntityIronCreeper.class, EntityKingCreeper.class, EntityLapisCreeper.class,
                EntityLavaCreeper.class, EntityLeadCreeper.class, EntityLightCreeper.class, EntityLostCreeper.class, EntityLuckCreeper.class,
                EntityMagmaCreeper.class, EntityMiniSpiderCreeper.class, EntityMusicCreeper.class, EntityNaturalCreeper.class, EntityObjetCreeper
                        .class, EntityOfalenCreeper.class, EntityOneCreeper.class, EntityParrotCreeper.class, EntityPierceCreeper.class,
                EntityPigCreeper.class, EntityRabbitCreeper.class, EntityRareCreeper.class, EntityRedStoneCreeper.class, EntityReverseCreeper
                        .class, EntityRewriteCreeper.class, EntityRoundCreeper.class, EntityRushCreeper.class, EntitySandCreeper.class,
                EntitySandstormCreeper.class, EntitySeaGuardianCreeper.class, EntitySheepCreeper.class, EntitySilentCreeper.class,
                EntitySkeletonCreeper.class, EntitySlimeCreeper.class, EntitySnowCreeper.class, EntitySpiderCreeper.class, EntitySquidCreeper
                        .class, EntityStoneCreeper.class, EntityStrayCreeper.class, EntitySubsidenceCreeper.class, EntityTNTCreeper.class,
                EntityTreeCreeper.class, EntityUpperCreeper.class, EntityVoidCreeper.class, EntityWaterCreeper.class, EntityWeatherCreeper.class,
                EntityWitchCreeper.class, EntityWitherSkeletonCreeper.class, EntityWolfCreeper.class, EntityWoodCreeper.class, EntityWrylyCreeper
                        .class, EntityYukariCreeper.class, EntityZombieCreeper.class, EntityZombieVillagerCreeper.class));
    }
    
    public static List <ITakumiEntity> getEntityList() {
        return entityList;
    }
    
    public static void register() {
        //CREATURE_TAKUMI = EnumHelper.addCreatureType("creature_takumi", EntityTakumiAbstractCreeper.class, 100, Material.AIR, false, true);
        //WATER_TAKUMI = EnumHelper.addCreatureType("water_takumi", EntityTakumiAbstractCreeper.class, 25, Material.WATER, false, false);
        for (Field fileld : Biomes.class.getDeclaredFields()) {
            try {
                biomes.add((Biome) fileld.get(null));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        biomes.remove(Biomes.HELL);
        biomes.remove(Biomes.VOID);
        
        /*List<Class> files = TakumiUtils.getListClass("com/tntmodders/takumi/entity/mobs/");*/
        ArrayList <EntityHolder> entityHolders = new ArrayList <>();
        for (Class clazz : CLASS_LIST) {
            try {
                /*ClassLoader loader = TakumiCraftCore.class.getClassLoader();
                //Class clazz = loader.loadClass("com.tntmodders.takumi.entity.mobs." + file.getName().replaceAll(".class", ""));*/
                ITakumiEntity entity = (ITakumiEntity) clazz.getConstructor(World.class).newInstance((World) null);
                entityHolders.add(new EntityHolder(clazz, entity));
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
                break;
            }
            ResourceLocation location = new ResourceLocation(TakumiCraftCore.MODID, entity.getRegisterName());
            EntityRegistry.registerModEntity(location, clazz, location.getResourcePath(), entity.getRegisterID(), TakumiCraftCore.TakumiInstance,
                    64, 2, true, entity.getPrimaryColor(), entity.getSecondaryColor());
            if (entity.isCustomSpawn()) {
                try {
                    entity.customSpawn();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (entity.takumiRank().getSpawnWeight() != 0) {
                Biome.REGISTRY.iterator().forEachRemaining(biome -> {
                    if (!(biome instanceof BiomeOcean)) {
                        EntityRegistry.addSpawn(clazz, entity.takumiRank().getSpawnWeight(), 5, 20, EnumCreatureType.MONSTER, biome);
                    }
                });
            }
            if (FMLCommonHandler.instance().getSide().isClient()) {
                TakumiModelCore.registerEntityRender(clazz, entity);
            }
            entityList.add(entity);
            TakumiCraftCore.LOGGER.info("Registered entity on ID " + entity.getRegisterID() + " : " + location.getResourcePath() + " , " + entity
                    .takumiRank().name() + " and " + entity.takumiType().name());
            
            File packFile = FMLCommonHandler.instance().findContainerFor(TakumiCraftCore.TakumiInstance).getSource();
            File oldFile = null;
            String assetS = "assets/takumicraft/advancements/";
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
                            oldFile.getAbsolutePath().split("out")[0] + "src" + oldFile.getAbsolutePath().split("out")[1].replaceAll("production",
                                    "main").replaceAll("forge1.12", "resources").replaceAll(".json", "")};
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
        itemRegister();
        if (FMLCommonHandler.instance().getSide().isClient()) {
            renderRegister();
        }
    }
    
    private static void itemRegister() {
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "takumiarrow"), EntityTakumiArrow.class, "takumiarrow", 900,
                TakumiCraftCore.TakumiInstance, 64, 2, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "takumisnowball"), EntityTakumiSnowBall.class,
                "takumiasnowball", 901, TakumiCraftCore.TakumiInstance, 64, 2, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "takumipotion"), EntityTakumiPotion.class, "takumipotion",
                902, TakumiCraftCore.TakumiInstance, 64, 2, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "takumiexpegg"), EntityTakumiExpEgg.class, "takumiexpegg",
                903, TakumiCraftCore.TakumiInstance, 64, 2, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TakumiCraftCore.MODID, "takumitnt"), EntityTakumiTNTPrimed.class, "takumitnt", 904,
                TakumiCraftCore.TakumiInstance, 64, 2, true);
    }
    
    @SideOnly(Side.CLIENT)
    private static void renderRegister() {
        RenderingRegistry.registerEntityRenderingHandler(EntityTakumiArrow.class, manager -> new RenderArrow <EntityArrow>(manager) {
            @Override
            protected ResourceLocation getEntityTexture(EntityArrow entity) {
                return new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/item/carrow.png");
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(EntityTakumiSnowBall.class, manager -> new RenderSnowball <>(manager, Items.SNOWBALL,
                Minecraft.getMinecraft().getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(EntityTakumiExpEgg.class, manager -> new RenderSnowball <>(manager, Items.EGG, Minecraft
                .getMinecraft().getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(EntityTakumiTNTPrimed.class, RenderTakumiTNTPrimed::new);
    }
    
    static class EntityComparator implements Comparator <EntityHolder> {
        
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
