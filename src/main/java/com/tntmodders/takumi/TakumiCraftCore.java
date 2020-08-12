package com.tntmodders.takumi;

import com.tntmodders.takumi.client.gui.TakumiGuiHandler;
import com.tntmodders.takumi.core.*;
import com.tntmodders.takumi.core.client.TakumiClientCore;
import com.tntmodders.takumi.event.TakumiClientEvents;
import com.tntmodders.takumi.event.TakumiEvents;
import com.tntmodders.takumi.utils.TakumiRecipeHolder;
import com.tntmodders.takumi.world.gen.TakumiGunOreGenerator;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.Metadata;
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

@Mod(modid = TakumiCraftCore.MODID, version = TakumiCraftCore.VERSION, acceptedMinecraftVersions = "[1.12.2]", name = "匠Craft [ Takumi Craft ]", guiFactory = "com.tntmodders.takumi.core.client.TakumiGuiFactory", updateJSON = "https://raw.githubusercontent.com/TNTModders/takumicraft/master/version/version.json")
public class TakumiCraftCore {

    //初期設定
    public static final String MODID = "takumicraft";
    public static final String VERSION = "2.0.0-β.1.8-tcs22";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static final CreativeTabs TAB_CREEPER = new TakumiCreativeTab();
    public static final CreativeTabs TAB_EGGS = new EggCreativeTab();
    public static final TakumiRecipeHolder HOLDER = new TakumiRecipeHolder();
    @Instance(MODID)
    public static TakumiCraftCore TakumiInstance;
    @Metadata(MODID)
    public static ModMetadata metadata;

    @EventHandler
    public void construct(FMLConstructionEvent event) {
        TakumiModInfoCore.load(metadata);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new TakumiEvents());
        TakumiFluidCore.register();
        TakumiTileEntityCore.register();
        TakumiWorldCore.registerMapGen();
        TakumiLootTableCore.register();
        TakumiEnumCore.register();
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void registerModels(ModelRegistryEvent event) {
        TakumiClientCore.register();
    }

    @SubscribeEvent
    public void registerBlocks(Register<Block> event) {
        TakumiBlockCore.register(event.getRegistry());
    }

    @SubscribeEvent
    public void registerItems(Register<Item> event) {
        TakumiItemCore.register(event.getRegistry());
    }

    @SubscribeEvent
    public void registerEntities(Register<EntityEntry> event) {
        TakumiEntityCore.register();
    }

    @SubscribeEvent
    public void registerEnchantments(Register<Enchantment> event) {
        TakumiEnchantmentCore.register(event.getRegistry());
    }

    @SubscribeEvent
    public void registerPotions(Register<Potion> event) {
        TakumiPotionCore.register(event.getRegistry());
    }

    @SubscribeEvent
    public void registerPotionType(Register<PotionType> event) {
        TakumiPotionCore.registerPotionType(event.getRegistry());
    }

    @SubscribeEvent
    public void registerBiome(Register<Biome> event) {
        TakumiBiomeCore.register(event.getRegistry());
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        TakumiWorldCore.register();
        TakumiConfigCore.loadConfig(event);
        if (FMLCommonHandler.instance().getSide().isClient()) {
            MinecraftForge.EVENT_BUS.register(new TakumiClientEvents());
            TakumiClientCore.registerKey();
        }
        GameRegistry.registerWorldGenerator(new TakumiGunOreGenerator(), 15);
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new TakumiGuiHandler());
        TakumiPacketCore.register();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        HOLDER.register();
    }

    @SubscribeEvent
    public void registerRecipe(Register<IRecipe> event){TakumiRecipeCore.register(event.getRegistry());}
}
