package com.tntmodders.takumi.core.client;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.block.BlockTakumiDoor;
import com.tntmodders.takumi.block.BlockTakumiFenceGate;
import com.tntmodders.takumi.client.render.tileentity.*;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.core.TakumiItemCore;
import com.tntmodders.takumi.entity.ITakumiEntity;
import com.tntmodders.takumi.tileentity.*;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.item.*;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Field;

@SideOnly(Side.CLIENT)
public class TakumiClientCore {

    public static KeyBinding keyBindingTakumiBook;
    public static KeyBinding keyBindingYMS;
    public static KeyBinding keyBindingTHMDetonate;

    public static void registerKey() {
        keyBindingTakumiBook = new KeyBinding("takumicraft.takumibook.key", Keyboard.KEY_I, TakumiCraftCore.MODID);
        ClientRegistry.registerKeyBinding(keyBindingTakumiBook);
        keyBindingYMS = new KeyBinding("takumicraft.xms.key", Keyboard.KEY_G, TakumiCraftCore.MODID);
        ClientRegistry.registerKeyBinding(keyBindingYMS);
        keyBindingTHMDetonate = new KeyBinding("takumicraft.thm.key", Keyboard.KEY_Z, TakumiCraftCore.MODID);
        ClientRegistry.registerKeyBinding(keyBindingTHMDetonate);
    }

    public static void register() {
        Class clazz = TakumiItemCore.class;
        for (Field field : clazz.getFields()) {
            try {
                if (field.get(TakumiItemCore.INSTANCE) instanceof Item) {
                    Item item = (Item) field.get(TakumiItemCore.INSTANCE);
                    String s = item.getUnlocalizedName().substring(5);
                    if (item.getHasSubtypes()) {
                        NonNullList<ItemStack> stacks = NonNullList.create();
                        item.getSubItems(TakumiCraftCore.TAB_CREEPER, stacks);
                        for (int i = 0; i < stacks.size(); i++) {
                            ModelLoader.setCustomModelResourceLocation(item, i,
                                    new ModelResourceLocation(new ResourceLocation(TakumiCraftCore.MODID, s + "_" + i),
                                            "inventory"));
                            TakumiCraftCore.LOGGER.info(
                                    "Registered item model with metadata" + i + " : " + s + "_" + i);
                        }
                    } else {
                        ModelLoader.setCustomModelResourceLocation(item, 0,
                                new ModelResourceLocation(new ResourceLocation(TakumiCraftCore.MODID, s), "inventory"));
                        TakumiCraftCore.LOGGER.info("Registered item model: " + s);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        for (Item item : TakumiItemCore.itemBlocks) {
            String s = item.getUnlocalizedName().substring(5);
            if (item.getHasSubtypes()) {
                NonNullList<ItemStack> stacks = NonNullList.create();
                item.getSubItems(TakumiCraftCore.TAB_CREEPER, stacks);
                for (int i = 0; i < stacks.size(); i++) {
                    if (item instanceof ItemCloth) {
                        if (((ItemBlock) item).getBlock() == TakumiBlockCore.CREEPER_STAINED_GLASS_PANE) {
                            ModelLoader.setCustomStateMapper(((ItemBlock) item).getBlock(), new StateMapperBase() {
                                @Override
                                protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                                    return new ModelResourceLocation(new ResourceLocation(TakumiCraftCore.MODID,
                                            "creeperstainedglasspane_" +
                                                    state.getValue(BlockStainedGlassPane.COLOR).getName()), "normal");
                                }
                            });
                        }
                        ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(
                                new ResourceLocation(TakumiCraftCore.MODID,
                                        EnumDyeColor.byMetadata(i).getName() + "_" + s), "inventory"));
                        TakumiCraftCore.LOGGER.info(
                                "Registered block model with color " + EnumDyeColor.byMetadata(i).getName() + " : " +
                                        EnumDyeColor.byMetadata(i).getName() + "_" + s);
                    } else {
                        ModelLoader.setCustomModelResourceLocation(item, i,
                                new ModelResourceLocation(new ResourceLocation(TakumiCraftCore.MODID, s + "_" + i),
                                        "inventory"));
                        TakumiCraftCore.LOGGER.info("Registered block model with metadata " + i + " : " + s + "_" + i);
                    }
                }
            } else {
                ModelLoader.setCustomModelResourceLocation(item, 0,
                        new ModelResourceLocation(new ResourceLocation(TakumiCraftCore.MODID, s), "inventory"));
                TakumiCraftCore.LOGGER.info("Registered block model: " + s);
            }
        }
        registerStatesModel();
    }

    private static void registerStatesModel() {
        ModelLoader.setCustomStateMapper(TakumiBlockCore.TAKUMI_DIRT, new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return new ModelResourceLocation(new ResourceLocation(TakumiCraftCore.MODID, "takumidirt"), "normal");
            }
        });
        ModelLoader.setCustomStateMapper(TakumiBlockCore.CREEPER_FENCEGATE, new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return new ModelResourceLocation(new ResourceLocation(TakumiCraftCore.MODID, "creeperfence_gate"),
                        "facing=" + state.getValue(BlockTakumiFenceGate.FACING).getName() + ",in_wall="
                                + state.getValue(BlockTakumiFenceGate.IN_WALL) + ",open=" + state.getValue(BlockTakumiFenceGate.OPEN));
            }
        });
        ModelLoader.setCustomStateMapper(TakumiBlockCore.CREEPER_IRON_DOOR, new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return new ModelResourceLocation(new ResourceLocation(TakumiCraftCore.MODID, "creeperiron_door"),
                        "facing=" + state.getValue(BlockTakumiDoor.FACING).getName() + ",half="
                                + state.getValue(BlockTakumiDoor.HALF) + ",hinge=" + state.getValue(BlockTakumiDoor.HINGE) + ",open=" + state.getValue(BlockTakumiDoor.OPEN));
            }
        });
        ModelLoader.setCustomStateMapper(TakumiBlockCore.CREEPER_PLANKS_DOOR, new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return new ModelResourceLocation(new ResourceLocation(TakumiCraftCore.MODID, "creeperplanks_door"),
                        "facing=" + state.getValue(BlockTakumiDoor.FACING).getName() + ",half="
                                + state.getValue(BlockTakumiDoor.HALF) + ",hinge=" + state.getValue(BlockTakumiDoor.HINGE) + ",open=" + state.getValue(BlockTakumiDoor.OPEN));
            }
        });
    }

    public static void registerEntityRender(Class<Entity> clazz, ITakumiEntity entity) {
        RenderingRegistry.registerEntityRenderingHandler(clazz, new TakumiRenderFactory() {
            @Override
            public Render createRenderFor(RenderManager manager) {
                return (Render) entity.getRender(manager);
            }
        });
    }

    public static void registerTileRender() {
        ClientRegistry.registerTileEntity(TileEntityAcidBlock.class, TakumiCraftCore.MODID + ":acidblock",
                new RenderAcidBlock<>());
        ClientRegistry.registerTileEntity(TileEntityMonsterBomb.class, TakumiCraftCore.MODID + ":monsterbomb",
                new RenderMonsterBomb<>());
        ClientRegistry.registerTileEntity(TileEntityTakumiBlock.class, TakumiCraftCore.MODID + ":takumiblock",
                new RenderTakumiBlock<>());
        ClientRegistry.registerTileEntity(TileEntityTakumiCreepered.class, TakumiCraftCore.MODID + ":takumicreepered",
                new RenderTakumiCreepered<>());
        ClientRegistry.registerTileEntity(TileEntityDarkBoard.class, TakumiCraftCore.MODID + ":takumidarkboard",
                new RenderDarkBoard<>());
        ClientRegistry.registerTileEntity(TileEntityVault.class, TakumiCraftCore.MODID + ":creepervault",
                new RenderVault());
        ClientRegistry.registerTileEntity(TileEntityTakumiBed.class, TakumiCraftCore.MODID + ":creeperbed",
                new RenderTakumiBed());
    }
}
