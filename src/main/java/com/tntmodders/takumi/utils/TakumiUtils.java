package com.tntmodders.takumi.utils;

import com.tntmodders.asm.TakumiASMNameMap;
import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.block.IBlockTT;
import com.tntmodders.takumi.block.ITakumiSPBomb;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.core.TakumiConfigCore;
import com.tntmodders.takumi.core.TakumiWorldCore;
import com.tntmodders.takumi.tileentity.TileEntityTakumiForceField;
import com.tntmodders.takumi.world.TakumiExplosion;
import net.minecraft.advancements.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientAdvancementManager;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRedstone;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
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

    public static void addSpiltInfo(ItemStack stack, List<String> tooltip){
        if(stack.isItemEnchanted()){
            tooltip.add(TakumiUtils.takumiTranslate("takumicraft.message.spilt"));
        }
    }

    public static ItemStack generateRandomTipsBook(Random random) {
        ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
        EnumTakumiOldBooks books = EnumTakumiOldBooks.randomValueOf(random);
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null) {
            compound = new NBTTagCompound();
        }
        compound.setString("title", TakumiUtils.takumiTranslate("item.oldbook.name") + "_" + books.getId());
        compound.setString("author", "???");
        NBTTagList list = new NBTTagList();
        list.appendTag(new NBTTagString(TakumiUtils.takumiTranslate("item.oldbook.content." + books.getId())));
        compound.setTag("pages", list);
        compound.setInteger("generation", 1);
        stack.setTagCompound(compound);
        return stack;
    }

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
                if (world.tickableTileEntities == null || world.tickableTileEntities.isEmpty()) {
                    world.setBlockState(pos, state);
                } else {
                    boolean flg = true;
                    if (world.tickableTileEntities.stream().anyMatch(tileEntity -> tileEntity instanceof TileEntityTakumiForceField)) {
                        for (TileEntity tileEntity : world.tickableTileEntities) {
                            if (tileEntity instanceof TileEntityTakumiForceField) {
                                if (tileEntity.getDistanceSq(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) < 100) {
                                    flg = false;
                                }
                            }
                        }
                    }
                    if (flg) {
                        BlockPos blockpos = world.getSpawnPoint();
                        int i = MathHelper.abs(pos.getX() - blockpos.getX());
                        int j = MathHelper.abs(pos.getZ() - blockpos.getZ());
                        int k = Math.max(i, j);
                        if (k > world.getMinecraftServer().getSpawnProtectionSize()) {
                            world.setBlockState(pos, state);
                        }
                    }
                }
            } catch (Exception e) {
                world.setBlockState(pos, state);
            }
        } else {
            world.setBlockState(pos, state);
        }
    }

    public static void setBlockStateProtectedFlg(World world, BlockPos pos, IBlockState state, int flag) {
        if (FMLCommonHandler.instance().getSide().isServer() && world.getMinecraftServer() != null) {
            try {
                BlockPos blockpos = world.getSpawnPoint();
                int i = MathHelper.abs(pos.getX() - blockpos.getX());
                int j = MathHelper.abs(pos.getZ() - blockpos.getZ());
                int k = Math.max(i, j);
                if (k > world.getMinecraftServer().getSpawnProtectionSize()) {
                    world.setBlockState(pos, state, flag);
                }
            } catch (Exception e) {
                world.setBlockState(pos, state, flag);
            }
        } else {
            world.setBlockState(pos, state, flag);
        }
    }

    public static boolean isApril(World world) {
        //return world.getCurrentDate().get(Calendar.MONTH) + 1 == 4 && world.getCurrentDate().get(Calendar.DATE) == 1;
        return false;
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
    public static boolean getAdvancementUnlockedServer(ResourceLocation location, EntityPlayerMP playerMP) {
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
        takumiCreateExplosion(world, entity, x, y, z, power, fire, destroy, amp, true);
    }

    public static TakumiExplosion takumiCreateExplosion(World world, Entity entity, double x, double y, double z, float power,
                                                        boolean fire, boolean destroy, double amp, boolean damage) {
        return takumiCreateExplosion(world, entity, x, y, z, power, fire, destroy, amp, damage, true);
    }

    public static TakumiExplosion takumiCreateExplosion(World world, Entity entity, double x, double y, double z, float power,
                                                        boolean fire, boolean destroy, double amp, boolean damage, boolean sound) {
        boolean flg = world instanceof WorldServer;
        TakumiExplosion explosion = new TakumiExplosion(world, entity, x, y, z, power, fire, destroy, amp, damage, sound);
        if (ForgeEventFactory.onExplosionStart(world, explosion)) {
            return explosion;
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
        return explosion;
    }

    public static boolean isExcludedBlockForRewrite(Block block) {
        return block instanceof ITakumiSPBomb || block == TakumiBlockCore.TAKUMI_FORCEFIELD || block instanceof IBlockTT;
    }

    @SideOnly(Side.CLIENT)
    public static void spawnColoredParticle(World world, double x, double y, double z, double speedX, double speedY, double speedZ, float red, float green, float blue, int tick) {
        Particle particle = new ParticleRedstone.Factory().createParticle(EnumParticleTypes.REDSTONE.getParticleID(), world, x, y, z, speedX, speedY, speedZ);
        particle.setRBGColorF(red, green, blue);
        particle.setMaxAge(tick);
        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
    }

    @SideOnly(Side.CLIENT)
    public static boolean isInEventServer(Minecraft mc) {
        return (mc.getCurrentServerData() != null && mc.getCurrentServerData().serverMOTD.contains(":tc_server")) || TakumiConfigCore.inEventServerClient;
    }

    public static int getColorFromText(TextFormatting formatting) {
        switch (formatting) {
            case WHITE:
                return EnumDyeColor.WHITE.getColorValue();
            case GOLD:
                return EnumDyeColor.ORANGE.getColorValue();
            case AQUA:
                return EnumDyeColor.LIGHT_BLUE.getColorValue();
            case BLUE:
                return EnumDyeColor.BLUE.getColorValue();
            case YELLOW:
                return EnumDyeColor.YELLOW.getColorValue();
            case GREEN:
                return EnumDyeColor.LIME.getColorValue();
            case LIGHT_PURPLE:
                return EnumDyeColor.PINK.getColorValue();
            case DARK_GRAY:
                return EnumDyeColor.GRAY.getColorValue();
            case GRAY:
                return EnumDyeColor.SILVER.getColorValue();
            case DARK_AQUA:
                return EnumDyeColor.CYAN.getColorValue();
            case DARK_PURPLE:
                return EnumDyeColor.PURPLE.getColorValue();
            case DARK_BLUE:
                return ((int) (EnumDyeColor.BLUE.getColorValue() * 0.75));
            case DARK_GREEN:
                return EnumDyeColor.GREEN.getColorValue();
            case DARK_RED: {
                int color = EnumDyeColor.RED.getColorValue();
                return (((int) ((color >> 16) * 0.75)) << 16);
            }
            case RED:
                return EnumDyeColor.RED.getColorValue();
            case BLACK:
                return EnumDyeColor.BLACK.getColorValue();
        }
        return EnumDyeColor.BLACK.getColorValue();
    }

    public enum EnumTakumiOldBooks {
        POTION(0),
        WORLD(1),
        KING(2),
        TYPE_01(3),
        TYPE_02(4),
        ENCHANT(5),
        MISC_01(6),
        ITEM_PROTECTION(7),
        MISC_02(8);

        private final int id;

        EnumTakumiOldBooks(int idIn) {
            this.id = idIn;
        }

        public static EnumTakumiOldBooks randomValueOf(Random random) {
            return EnumTakumiOldBooks.values()[random.nextInt(EnumTakumiOldBooks.values().length)];
        }

        public int getId() {
            return id;
        }
    }
}
