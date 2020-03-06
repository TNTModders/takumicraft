package com.tntmodders.takumi.core;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class TakumiRecipeCore {
    private static final Map<Item, Block> COLORED = new HashMap<>();

    static {
        COLORED.put(Item.getItemFromBlock(TakumiBlockCore.CREEPER_GLASS), TakumiBlockCore.CREEPER_STAINED_GLASS);
        COLORED.put(Item.getItemFromBlock(TakumiBlockCore.CREEPER_GLASS_PANE), TakumiBlockCore.CREEPER_STAINED_GLASS_PANE);
        COLORED.put(Item.getItemFromBlock(TakumiBlockCore.CREEPER_CARPET), TakumiBlockCore.CREEPER_CARPET);
        COLORED.put(Item.getItemFromBlock(TakumiBlockCore.TAKUMI_CLAY), TakumiBlockCore.TAKUMI_CLAY);
    }

    public static void register(IForgeRegistry<IRecipe> registry) {
        registry.register(new IRecipe() {
            @Override
            public boolean matches(InventoryCrafting inv, World worldIn) {
                ItemStack wool = null, dye = null;
                for (int i = 0; i < inv.getSizeInventory(); i++) {
                    if (inv.getStackInSlot(i).getItem() == Item.getItemFromBlock(TakumiBlockCore.CREEPER_WOOL) && inv.getStackInSlot(i).getMetadata() == 0) {
                        if (wool != null) {
                            return false;
                        }
                        wool = inv.getStackInSlot(i);
                    } else if (inv.getStackInSlot(i).getItem() == Items.DYE) {
                        if (dye != null) {
                            return false;
                        }
                        dye = inv.getStackInSlot(i);
                    }
                }
                return wool != null && dye != null;
            }

            @Override
            public ItemStack getCraftingResult(InventoryCrafting inv) {
                ItemStack dye = null;
                for (int i = 0; i < inv.getSizeInventory(); i++) {
                    if (inv.getStackInSlot(i).getItem() == Items.DYE) {
                        dye = inv.getStackInSlot(i);
                    }
                }
                return dye == null ? ItemStack.EMPTY : new ItemStack(TakumiBlockCore.CREEPER_WOOL, 1, 15 - dye.getMetadata());
            }

            @Override
            public boolean canFit(int width, int height) {
                return width * height > 2;
            }

            @Override
            public ItemStack getRecipeOutput() {
                return ItemStack.EMPTY;
            }

            @Override
            public IRecipe setRegistryName(ResourceLocation name) {
                return this;
            }

            @Nullable
            @Override
            public ResourceLocation getRegistryName() {
                return new ResourceLocation(TakumiCraftCore.MODID, "creeperwool_staining");
            }

            @Override
            public Class<IRecipe> getRegistryType() {
                return IRecipe.class;
            }

            @Override
            public boolean isHidden() {
                return true;
            }
        });

        registry.register(new IRecipe() {

            @Override
            public boolean matches(InventoryCrafting inv, World worldIn) {
                Item item = null;
                int meta = 0;
                for (int i = 0; i < 9; i++) {
                    if (i != 4) {
                        if (!COLORED.containsKey(inv.getStackInSlot(i).getItem())) {
                            return false;
                        }
                        if (item == null) {
                            item = inv.getStackInSlot(i).getItem();
                            meta = inv.getStackInSlot(i).getMetadata();
                            if(item !=Item.getItemFromBlock(TakumiBlockCore.TAKUMI_CLAY) && meta != 0){
                                return false;
                            }
                        } else if (item != inv.getStackInSlot(i).getItem() || meta != inv.getStackInSlot(i).getMetadata()) {
                            return false;
                        }
                    }else {
                        if (inv.getStackInSlot(i).getItem() != Items.DYE) {
                            return false;
                        }
                    }
                }
                return true;
            }

            @Override
            public ItemStack getCraftingResult(InventoryCrafting inv) {
                ItemStack colored = inv.getStackInSlot(0);
                ItemStack dye = inv.getStackInSlot(4);
                return new ItemStack(COLORED.get(colored.getItem()), 8, 15 - dye.getMetadata());
            }

            @Override
            public boolean canFit(int width, int height) {
                return width * height > 4;
            }

            @Override
            public ItemStack getRecipeOutput() {
                return ItemStack.EMPTY;
            }

            @Override
            public IRecipe setRegistryName(ResourceLocation name) {
                return this;
            }

            @Nullable
            @Override
            public ResourceLocation getRegistryName() {
                return new ResourceLocation("colored_staining");
            }

            @Override
            public Class<IRecipe> getRegistryType() {
                return IRecipe.class;
            }

            @Override
            public boolean isHidden() {
                return true;
            }
        });
    }
}
