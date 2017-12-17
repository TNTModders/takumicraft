package com.tntmodders.takumi.item;

import com.google.common.collect.Sets;
import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiEnchantmentCore;
import com.tntmodders.takumi.item.material.TakumiToolMaterial;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ItemTakumiMineSweeperTool extends ItemTool {
    
    private static final Set <Block> EFFECTIVE_ON_PICK = Sets.newHashSet(Blocks.ACTIVATOR_RAIL, Blocks.COAL_ORE, Blocks.COBBLESTONE, Blocks
            .DETECTOR_RAIL, Blocks.DIAMOND_BLOCK, Blocks.DIAMOND_ORE, Blocks.DOUBLE_STONE_SLAB, Blocks.GOLDEN_RAIL, Blocks.GOLD_BLOCK, Blocks
            .GOLD_ORE, Blocks.ICE, Blocks.IRON_BLOCK, Blocks.IRON_ORE, Blocks.LAPIS_BLOCK, Blocks.LAPIS_ORE, Blocks.LIT_REDSTONE_ORE, Blocks
            .MOSSY_COBBLESTONE, Blocks.NETHERRACK, Blocks.PACKED_ICE, Blocks.RAIL, Blocks.REDSTONE_ORE, Blocks.SANDSTONE, Blocks.RED_SANDSTONE,
            Blocks.STONE, Blocks.STONE_SLAB, Blocks.STONE_BUTTON, Blocks.STONE_PRESSURE_PLATE);
    
    private static final Set <Block> EFFECTIVE_ON_SHOVEL = Sets.newHashSet(Blocks.CLAY, Blocks.DIRT, Blocks.FARMLAND, Blocks.GRASS, Blocks.GRAVEL,
            Blocks.MYCELIUM, Blocks.SAND, Blocks.SNOW, Blocks.SNOW_LAYER, Blocks.SOUL_SAND, Blocks.GRASS_PATH, Blocks.CONCRETE_POWDER);
    
    private static final Set <Block> EFFECTIVE_ON_AXE = Sets.newHashSet(Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST,
            Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER, Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE);
    
    
    private static final Map <EnumTakumiTool, Set <Block>> TOOL_SET_MAP = new HashMap <>();
    
    static {
        TOOL_SET_MAP.put(EnumTakumiTool.AXE, EFFECTIVE_ON_AXE);
        TOOL_SET_MAP.put(EnumTakumiTool.PICKAXE, EFFECTIVE_ON_PICK);
        TOOL_SET_MAP.put(EnumTakumiTool.SHOVEL, EFFECTIVE_ON_SHOVEL);
    }
    
    private final EnumTakumiTool enumTakumiTool;
    
    public ItemTakumiMineSweeperTool(EnumTakumiTool enumTakumiToolIn) {
        super(TakumiToolMaterial.TAKUMI_MATERIAL, TOOL_SET_MAP.get(enumTakumiToolIn));
        this.enumTakumiTool = enumTakumiToolIn;
        this.setRegistryName(TakumiCraftCore.MODID, "takumiminetool_" + this.enumTakumiTool.getName());
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("takumiminetool_" + this.enumTakumiTool.getName());
        this.attackSpeed = -3.0f;
    }
    
    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        switch (this.enumTakumiTool) {
            case AXE: {
                Material material = state.getMaterial();
                return material != Material.WOOD && material != Material.PLANTS && material != Material.VINE ? super.getStrVsBlock(stack, state) :
                       this.efficiencyOnProperMaterial;
            }
            case PICKAXE:
                Material material = state.getMaterial();
                return material != Material.IRON && material != Material.ANVIL && material != Material.ROCK ? super.getStrVsBlock(stack, state) :
                       this.efficiencyOnProperMaterial;
        }
        return super.getStrVsBlock(stack, state);
    }
    
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY,
            float hitZ) {
        if (this.enumTakumiTool == EnumTakumiTool.SHOVEL) {
            ItemStack itemstack = player.getHeldItem(hand);
            
            if (!player.canPlayerEdit(pos.offset(facing), facing, itemstack)) {
                return EnumActionResult.FAIL;
            } else {
                IBlockState iblockstate = worldIn.getBlockState(pos);
                Block block = iblockstate.getBlock();
                
                if (facing != EnumFacing.DOWN && worldIn.getBlockState(pos.up()).getMaterial() == Material.AIR && block == Blocks.GRASS) {
                    IBlockState iblockstate1 = Blocks.GRASS_PATH.getDefaultState();
                    worldIn.playSound(player, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    
                    if (!worldIn.isRemote) {
                        worldIn.setBlockState(pos, iblockstate1, 11);
                        itemstack.damageItem(1, player);
                    }
                    
                    return EnumActionResult.SUCCESS;
                } else {
                    return EnumActionResult.PASS;
                }
            }
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
    
    @Override
    public boolean canHarvestBlock(IBlockState blockIn) {
        switch (this.enumTakumiTool) {
            case SHOVEL: {
                Block block = blockIn.getBlock();
                return block == Blocks.SNOW_LAYER || block == Blocks.SNOW;
            }
            case PICKAXE:
                Block block = blockIn.getBlock();
                
                if (block == Blocks.OBSIDIAN) {
                    return this.toolMaterial.getHarvestLevel() > 3;
                } else if (block != Blocks.DIAMOND_BLOCK && block != Blocks.DIAMOND_ORE) {
                    if (block != Blocks.EMERALD_ORE && block != Blocks.EMERALD_BLOCK) {
                        if (block != Blocks.GOLD_BLOCK && block != Blocks.GOLD_ORE) {
                            if (block != Blocks.IRON_BLOCK && block != Blocks.IRON_ORE) {
                                if (block != Blocks.LAPIS_BLOCK && block != Blocks.LAPIS_ORE) {
                                    if (block != Blocks.REDSTONE_ORE && block != Blocks.LIT_REDSTONE_ORE) {
                                        Material material = blockIn.getMaterial();
                                        return material == Material.ROCK || material == Material.IRON || material == Material.ANVIL;
                                    } else {
                                        return this.toolMaterial.getHarvestLevel() >= 2;
                                    }
                                } else {
                                    return this.toolMaterial.getHarvestLevel() >= 1;
                                }
                            } else {
                                return this.toolMaterial.getHarvestLevel() >= 1;
                            }
                        } else {
                            return this.toolMaterial.getHarvestLevel() >= 2;
                        }
                    } else {
                        return this.toolMaterial.getHarvestLevel() >= 2;
                    }
                } else {
                    return this.toolMaterial.getHarvestLevel() >= 2;
                }
        }
        return super.canHarvestBlock(blockIn);
    }
    
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!stack.isItemEnchanted()) {
            try {
                stack.addEnchantment(TakumiEnchantmentCore.MINESWEEPER, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        if (!stack.isItemEnchanted()) {
            try {
                stack.addEnchantment(TakumiEnchantmentCore.MINESWEEPER, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return true;
    }
    
    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList <ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            ItemStack itemStack = new ItemStack(this, 1);
            itemStack.addEnchantment(TakumiEnchantmentCore.MINESWEEPER, 1);
            items.add(itemStack);
        }
    }
    
    public enum EnumTakumiTool {
        PICKAXE("pickaxe"), SHOVEL("shovel"), AXE("axe");
        
        private final String name;
        
        EnumTakumiTool(String name) {
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
    }
}
