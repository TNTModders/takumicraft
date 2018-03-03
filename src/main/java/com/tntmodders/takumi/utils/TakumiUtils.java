package com.tntmodders.takumi.utils;

import com.tntmodders.asm.TakumiASMNameMap;
import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.world.TakumiExplosion;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
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

    public static String takumiTranslate(String s) {
        return I18n.translateToLocal(s);
    }

    public static void giveAdvancementImpossible(EntityPlayerMP playerMP, ResourceLocation parent,
            ResourceLocation child) {
        try {
            if (playerMP.getAdvancements()
                        .getProgress(playerMP.getServer().getAdvancementManager().getAdvancement(parent))
                        .hasProgress()) {
                playerMP.getAdvancements()
                        .grantCriterion(playerMP.getServer().getAdvancementManager().getAdvancement(child),
                                "impossible");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SideOnly(Side.CLIENT)
    public static boolean getAdvancementUnlocked(ResourceLocation location) {
        ClientAdvancementManager manager = Minecraft.getMinecraft().player.connection.getAdvancementManager();
        try {
            Field field = TakumiASMNameMap.getField(ClientAdvancementManager.class, "advancementToProgress");
            field.setAccessible(true);
            Map<Advancement, AdvancementProgress> advancementToProgress =
                    (Map<Advancement, AdvancementProgress>) field.get(manager);
            if (advancementToProgress.containsKey(manager.getAdvancementList().getAdvancement(location))) {
                return advancementToProgress.get(manager.getAdvancementList().getAdvancement(location)).isDone();
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
    
   /* public static void takumiUnlockRecipes(ItemStack stack, EntityPlayer player) {
        if (FMLCommonHandler.instance().getSide().isClient()) {
            //レシピはjsonをスキャンして結果からリストでif判定できるように!
            Item item = stack.getItem();
            int meta = stack.getMetadata();
            ItemStack itemStack = new ItemStack(item, 1, meta);
            if (!TakumiRecipeHolder.map.isEmpty() && TakumiRecipeHolder.map.containsKey(itemStack)) {
                List <ResourceLocation> list = TakumiRecipeHolder.map.get(itemStack);
                try {
                    if (Block.getBlockFromItem(item) instanceof ITakumiItemBlock) {
                        NonNullList <ItemStack> list1 = NonNullList.create();
                        Block.getBlockFromItem(item).getSubBlocks(null, list1);
                        for (ItemStack aList1 : list1) {
                            list.addAll(TakumiRecipeHolder.map.get(aList1));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                player.unlockRecipes(list.toArray(new ResourceLocation[list.size()]));
            }
        }
    }*/

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

    /* public static List<Class> getListClass(String path) {
         List<Class> files = new ArrayList<>();
         ClassLoader loader = TakumiCraftCore.class.getClassLoader();
         URL url = loader.getResource(path);
         if (url.getProtocol().equals("jar")) {
             String[] strings = url.getPath().split(":");
             String leadPath = strings[strings.length - 1].split("!")[0];
             File f = new File(leadPath);
             JarFile jarFile;
             try {
                 Set<ClassPath.ClassInfo> set = ClassPath.from(loader).getAllClasses();
                 List<ClassPath.ClassInfo> list = new ArrayList<>();
                 TakumiCraftCore.LOGGER.info("takumiclassesoutput");
                 for (ClassPath.ClassInfo classInfo : set) {
                     try {
                         if (classInfo.getName().contains("com.tntmodders")) {
                             TakumiCraftCore.LOGGER.info(classInfo.getName());
                             files.add(classInfo.load());
                         }
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 }
             } catch (Exception e) {
                 e.printStackTrace();
             }
         } else {
             File packFile = FMLCommonHandler.instance().findContainerFor(TakumiCraftCore.TakumiInstance).getSource();
             File newFile = new File(packFile.toURI().getPath() + path);
             for (File file : newFile.listFiles()) {
                 ClassLoader loader0 = TakumiCraftCore.class.getClassLoader();
                 Class clazz = null;
                 try {
                     clazz = loader0.loadClass("com.tntmodders.takumi.entity.mobs." + file.getName().replaceAll(".class", ""));
                 } catch (ClassNotFoundException e) {
                     e.printStackTrace();
                 }
                 files.add(clazz);
             }
         }
         return files;
     }*/
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

   /* public static boolean canUseTheVersion() {
        try {
            String title;
            BufferedReader reader = new BufferedReader(new InputStreamReader(getHttpsInputStream()));
            StringBuilder lines = new StringBuilder(4096);
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                TakumiCraftCore.LOGGER.info(line);
                if (line.contains("version")) {
                    line = line.replaceAll("\"", "").replaceAll("version", "")
                            .replaceAll(":", "").replaceAll(",", "").trim();
                    if (!line.equalsIgnoreCase(TakumiCraftCore.VERSION)) {
                        return false;
                    }
                } else if (line.contains("can_use")) {
                    line = line.replaceAll("\"can_use\":", "").trim();
                    if (!line.equalsIgnoreCase("true")) {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private static InputStream getHttpsInputStream() throws Exception {
        URL url = new URL("https://www.tntmodders.com/takumicraft_json/version.json");
        // ホスト名の検証を行わない
        HostnameVerifier hv = (s, ses) -> {
            TakumiCraftCore.LOGGER.info("[WARN] Hostname is not matched.");
            return true;
        };
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
        // 証明書の検証を行わない
        KeyManager[] km = null;
        TrustManager[] tm = {new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        }};
        SSLContext sslcontext = SSLContext.getInstance("SSL");
        sslcontext.init(km, tm, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        TakumiCraftCore.LOGGER.info(conn.getContentEncoding());
        return conn.getInputStream();
    }*/
}
