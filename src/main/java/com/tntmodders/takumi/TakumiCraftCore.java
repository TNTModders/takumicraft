package com.tntmodders.takumi;

import com.tntmodders.takumi.client.gui.TakumiGuiHandler;
import com.tntmodders.takumi.core.*;
import com.tntmodders.takumi.core.client.TakumiModelCore;
import com.tntmodders.takumi.event.TakumiEvents;
import com.tntmodders.takumi.utils.TakumiRecipeHolder;
import com.tntmodders.takumi.world.gen.TakumiGunOreGenerator;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionType;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = TakumiCraftCore.MODID, version = TakumiCraftCore.VERSION, acceptedMinecraftVersions = "[1.12.1]", name = "匠Craft [ Takumi Craft ]")
public class TakumiCraftCore {
    //初期設定
    public static final String MODID = "takumicraft";
    public static final String VERSION = "2.0_Dev_1";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static final CreativeTabs TAB_CREEPER = new TakumiCreativeTab();
    public static final CreativeTabs TAB_EGGS = new EggCreativeTab();
    public static final TakumiRecipeHolder HOLDER = new TakumiRecipeHolder();
    @Mod.Instance(MODID)
    public static TakumiCraftCore TakumiInstance;
    @Mod.Metadata(MODID)
    public static ModMetadata metadata;

    @EventHandler
    public void construct(FMLConstructionEvent event) {
        TakumiModInfoCore.load(metadata);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new TakumiEvents());
        TakumiFluidCore.register();
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void registerModels(ModelRegistryEvent event) {
        TakumiModelCore.register();
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        TakumiBlockCore.register(event.getRegistry());
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        TakumiItemCore.register(event.getRegistry());
    }

    @SubscribeEvent
    public void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        TakumiEntityCore.register();
    }

    @SubscribeEvent
    public void registerEnchantments(RegistryEvent.Register<Enchantment> event) {
        TakumiEnchantmentCore.register(event.getRegistry());
    }

    @SubscribeEvent
    public void registerPotions(RegistryEvent.Register<Potion> event) {
        TakumiPotionCore.register(event.getRegistry());
    }

    @SubscribeEvent
    public void registerPotionType(RegistryEvent.Register<PotionType> event) {
        TakumiPotionCore.registerPotionType(event.getRegistry());
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        GameRegistry.registerWorldGenerator(new TakumiGunOreGenerator(), 5);
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new TakumiGuiHandler());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        HOLDER.register();
    }
}
