package com.tntmodders.tutorial;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@Mod(modid = "aluminiummod", version = "1.0", name = "AluminiumMod")
public class AluminiumMod {
    @Mod.Instance("aluminiummod")
    public static AluminiumMod aluminiumInstance;

    public static final Item ALUMINIUM = new ItemAluminium();
    public static final Block ALUMINIUM_BLOCK = new BlockAluminium();
    public static final Item COLOR_ALUMINIUM = new ItemColorAluminium();
    public static final AluminiumRecipeHolder HOLDER = new AluminiumRecipeHolder();

    @Mod.EventHandler
    public void construct(FMLConstructionEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(ALUMINIUM);
        event.getRegistry().register(COLOR_ALUMINIUM);
        event.getRegistry().register(new ItemBlock(ALUMINIUM_BLOCK).setRegistryName("aluminiummod", "aluminium_block"));
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(ALUMINIUM_BLOCK);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void registerModels(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(ALUMINIUM, 0, new ModelResourceLocation(new ResourceLocation("aluminiummod", "aluminium"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ALUMINIUM_BLOCK), 0, new ModelResourceLocation(new ResourceLocation("aluminiummod", "aluminium_block"), "inventory"));
        //メタデータの分だけモデルを登録する。
        for(int i = 0; i < 16; i++){
            ModelLoader.setCustomModelResourceLocation(COLOR_ALUMINIUM, i, new ModelResourceLocation(new ResourceLocation("aluminiummod", "color_aluminium_" + i), "inventory"));
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        AluminiumMod.HOLDER.register();
    }

    @SubscribeEvent
    public void onPickupItem(EntityItemPickupEvent event) {
        this.aluminiumUnlockRecipes(event.getItem().getItem(), event.getEntityPlayer());
    }

    @SubscribeEvent
    public void onCloseContainer(PlayerContainerEvent.Close event) {
        for (ItemStack itemStack : event.getEntityPlayer().inventoryContainer.getInventory()) {
            this.aluminiumUnlockRecipes(itemStack, event.getEntityPlayer());
        }
    }

    private void aluminiumUnlockRecipes(ItemStack stack, EntityPlayer player) {
        if (FMLCommonHandler.instance().getSide().isClient()) {
            Item item = stack.getItem();
            int meta = stack.getMetadata();
            ItemStack itemStack = new ItemStack(item, 1, meta);
            if (!AluminiumMod.HOLDER.map.isEmpty() && AluminiumMod.HOLDER.map.containsKey(itemStack)) {
                List<ResourceLocation> list = AluminiumMod.HOLDER.map.get(itemStack);
                player.unlockRecipes(list.toArray(new ResourceLocation[list.size()]));
            }
        }
    }
}
