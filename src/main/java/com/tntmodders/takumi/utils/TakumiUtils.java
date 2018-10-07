package com.tntmodders.takumi.utils;

import com.tntmodders.asm.TakumiASMNameMap;
import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiWorldCore;
import com.tntmodders.takumi.world.TakumiExplosion;
import net.minecraft.advancements.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientAdvancementManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.server.FMLServerHandler;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class TakumiUtils {

    public static boolean canSpawnElementBoss(World world) {
        return world.provider.getDimensionType().getId() == TakumiWorldCore.TAKUMI_WORLD.getId() ||
                (world.playerEntities.stream().anyMatch(player -> player instanceof EntityPlayerMP &&
                        ((EntityPlayerMP) player).getAdvancements().getProgress(
                                player.getServer().getAdvancementManager().getAdvancement(
                                        new ResourceLocation(TakumiCraftCore.MODID, "creeperbomb"))).hasProgress()));
    }

    public static void setBlockStateProtected(World world, BlockPos pos, IBlockState state) {
        if (FMLCommonHandler.instance().getSide().isServer() && world.getMinecraftServer() != null) {
            try {
                BlockPos blockpos = world.getSpawnPoint();
                int i = MathHelper.abs(pos.getX() - blockpos.getX());
                int j = MathHelper.abs(pos.getZ() - blockpos.getZ());
                int k = Math.max(i, j);
                if (k > world.getMinecraftServer().getSpawnProtectionSize()) {
                    world.setBlockState(pos, state);
                }
            } catch (Exception e) {
                world.setBlockState(pos, state);
            }
        } else {
            world.setBlockState(pos, state);
        }
    }

    public static boolean isApril(World world) {
        return world.getCurrentDate().get(Calendar.MONTH) + 1 == 4 && world.getCurrentDate().get(Calendar.DATE) == 1;
        //return true;
    }

    public static String takumiTranslate(String s) {
        return I18n.translateToLocal(s);
    }

    public static void giveAdvancementImpossible(EntityPlayerMP playerMP, ResourceLocation parent,
            ResourceLocation child) {
        try {
            if (playerMP.getAdvancements().getProgress(
                    playerMP.getServer().getAdvancementManager().getAdvancement(parent)).hasProgress()) {
                playerMP.getAdvancements().grantCriterion(
                        playerMP.getServer().getAdvancementManager().getAdvancement(child), "impossible");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SideOnly(Side.CLIENT)
    public static boolean getAdvancementUnlocked(ResourceLocation location) {
        try {
            ClientAdvancementManager manager = Minecraft.getMinecraft().player.connection.getAdvancementManager();
            Field field = TakumiASMNameMap.getField(ClientAdvancementManager.class, "advancementToProgress");
            field.setAccessible(true);
            Map<Advancement, AdvancementProgress> advancementToProgress =
                    (Map<Advancement, AdvancementProgress>) field.get(manager);
            if (advancementToProgress.containsKey(manager.getAdvancementList().getAdvancement(location))) {
                return advancementToProgress.get(manager.getAdvancementList().getAdvancement(location)).isDone();
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    @SideOnly(Side.SERVER)
    public static boolean getAdvancementUnlockedServer(ResourceLocation location,EntityPlayerMP playerMP) {
        try {
            PlayerAdvancements advancements = playerMP.getAdvancements();
            Field field = TakumiASMNameMap.getField(PlayerAdvancements.class, "progress");
            field.setAccessible(true);
            Map<Advancement, AdvancementProgress> advancementToProgress =
                    (Map<Advancement, AdvancementProgress>) field.get(advancements);
            field = TakumiASMNameMap.getField(AdvancementManager.class, "ADVANCEMENT_LIST");
            field.setAccessible(true);
            AdvancementList list = (AdvancementList) field.get(null);
            if (advancementToProgress.containsKey(list.getAdvancement(location))) {
                return advancementToProgress.get(list.getAdvancement(location)).isDone();
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    public static World getDummyWorld() {
        return FMLCommonHandler.instance().getSide().isClient() ? getClientWorld() : getServerWorld();
    }

    @SideOnly(Side.CLIENT)
    private static World getClientWorld() {
        return Minecraft.getMinecraft().world;
    }

    @SideOnly(Side.SERVER)
    private static World getServerWorld() {
        return FMLServerHandler.instance().getServer().getWorld(0);
    }

    public static float takumiGetBlockResistance(Entity entity, IBlockState state, BlockPos pos) {
        float f = entity.getExplosionResistance(null, entity.world, pos, state);
        if (f >= 1200) {
            return -1;
        }
        if (f < 0) {
            return 0;
        }
        return f;
    }

    public static void takumiSetPowered(EntityCreeper entity, boolean flg) {
        if (entity.getPowered() != flg) {
            try {
                Field field = TakumiASMNameMap.getField(EntityCreeper.class, "POWERED");
                field.setAccessible(true);
                DataParameter<Boolean> parameter = (DataParameter<Boolean>) field.get(entity);
                entity.getDataManager().set(parameter, flg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static List<File> getListFile(String path) {
        List<File> files = new ArrayList<>();
        ClassLoader loader = TakumiCraftCore.class.getClassLoader();
        URL url = loader.getResource(path);
        if (Objects.equals(url.getProtocol(), "jar")) {
            // TakumiCraftCore.LOGGER.info("urlpath : " + url.getPath());
/*            String[] strings = url.getPath().split(":/");
            String leadPath = strings[strings.length - 2] + ":/" + strings[strings.length - 1].split("!")[0];*/
            String leadPath = url.getPath().split("!")[0];
            //leadPath = leadPath.replaceAll("%20", " ");
/*            if (leadPath.contains("file:/") && ! leadPath.matches("file:/*:/")) {
                TakumiCraftCore.LOGGER.info("tester");
                leadPath = leadPath.replaceAll("file", "file:/C");
            }*/
            TakumiCraftCore.LOGGER.info("leadpath : " + leadPath);
            File f = new File(leadPath);
            JarFile jarFile;
            try {
                jarFile = new JarFile(f);
                Enumeration<JarEntry> enumeration = jarFile.entries();
                while (enumeration.hasMoreElements()) {
                    JarEntry entry = enumeration.nextElement();
                    String s = entry.getName();
                    if (s != null && s.startsWith(path) && (s.endsWith(".class") || s.endsWith(".json"))) {
                        files.add(new File(loader.getResource(s).getPath()));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            File packFile = FMLCommonHandler.instance().findContainerFor(TakumiCraftCore.TakumiInstance).getSource();
            File newFile = new File(packFile.toURI().getPath() + path);
            return Arrays.asList(newFile.listFiles());
        }
        return files;
    }

    public static void takumiCreateExplosion(World world, Entity entity, double x, double y, double z, float power,
            boolean fire, boolean destroy) {
        takumiCreateExplosion(world, entity, x, y, z, power, fire, destroy, 1);
    }

    public static void takumiCreateExplosion(World world, Entity entity, double x, double y, double z, float power,
            boolean fire, boolean destroy, double amp) {
        boolean flg = world instanceof WorldServer;
        TakumiExplosion explosion = new TakumiExplosion(world, entity, x, y, z, power, fire, destroy, amp);
        if (ForgeEventFactory.onExplosionStart(world, explosion)) {
            return;
        }
        explosion.doExplosionA();
        explosion.doExplosionB(!flg);
        if (flg) {
            if (!fire) {
                explosion.clearAffectedBlockPositions();
            }

            for (EntityPlayer entityplayer : world.playerEntities) {
                if (entityplayer.getDistanceSq(x, y, z) < 4096.0D) {
                    ((EntityPlayerMP) entityplayer).connection.sendPacket(
                            new SPacketExplosion(x, y, z, power, explosion.getAffectedBlockPositions(),
                                    explosion.getPlayerKnockbackMap().get(entityplayer)));
                }
            }
        }
    }
}
