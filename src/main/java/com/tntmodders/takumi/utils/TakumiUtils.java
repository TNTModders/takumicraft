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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

    public static void giveAdvancementImpossible(EntityPlayerMP playerMP, ResourceLocation parent, ResourceLocation child) {
        try {
            if (playerMP.getAdvancements().getProgress(playerMP.getServer().getAdvancementManager().getAdvancement(parent)).hasProgress()) {
                playerMP.getAdvancements().grantCriterion(playerMP.getServer().getAdvancementManager().getAdvancement(child), "impossible");
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
            Map<Advancement, AdvancementProgress> advancementToProgress = ((Map<Advancement, AdvancementProgress>) field.get(manager));
            if (advancementToProgress.containsKey(manager.getAdvancementList().getAdvancement(location))) {
                return advancementToProgress.get(manager.getAdvancementList().getAdvancement(location)).isDone();
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void takumiUnlockRecipes(ItemStack stack, EntityPlayer player) {
        if (FMLCommonHandler.instance().getSide().isClient()) {
            //レシピはjsonをスキャンして結果からリストでif判定できるように!
            Item item = stack.getItem();
            int meta = stack.getMetadata();
            ItemStack itemStack = new ItemStack(item, 1, meta);
            if (!TakumiRecipeHolder.map.isEmpty() && TakumiRecipeHolder.map.containsKey(itemStack)) {
                List<ResourceLocation> list = TakumiRecipeHolder.map.get(itemStack);
                player.unlockRecipes(list.toArray(new ResourceLocation[list.size()]));
            }
        }
    }

    public static float takumiGetBlockResistance(Entity entity, IBlockState state, BlockPos pos) {
        float f = entity.getExplosionResistance(null, entity.world, pos, state);
        if (f >= 1200) {
            f = -1;
        } else if (f < 0) {
            f = 0;
        }
        return f;
    }

    public static void takumiSetPowered(EntityCreeper entity, boolean flg) {
        if (entity.getPowered() != flg) {
            try {
                Field field = TakumiASMNameMap.getField(EntityCreeper.class, "POWERED");
                field.setAccessible(true);
                DataParameter<Boolean> parameter = ((DataParameter<Boolean>) field.get(entity));
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
        if (url.getProtocol().equals("jar")) {
            String[] strings = url.getPath().split(":");
            String leadPath = strings[strings.length - 1].split("!")[0];
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
            files = Arrays.asList(newFile.listFiles());
        }
        return files;
    }

    public static void takumiCreateExplosion(World world, Entity entity, double x, double y, double z, float power, boolean fire, boolean destroy) {
        boolean flg = world instanceof WorldServer;
        TakumiExplosion explosion = new TakumiExplosion(world, entity, x, y, z, power, fire, destroy);
        if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(world, explosion)) {
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
                    ((EntityPlayerMP) entityplayer).connection.sendPacket(new SPacketExplosion(x, y, z, power, explosion.getAffectedBlockPositions(), explosion.getPlayerKnockbackMap().get(entityplayer)));
                }
            }
        }
    }
}
