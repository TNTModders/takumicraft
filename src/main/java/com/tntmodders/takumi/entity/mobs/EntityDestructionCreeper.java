package com.tntmodders.takumi.entity.mobs;

import com.google.common.collect.Lists;
import com.tntmodders.takumi.client.render.RenderDestructionCreeper;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import com.tntmodders.takumi.world.TakumiExplosion;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityDestructionCreeper extends EntityTakumiAbstractCreeper {
    public EntityDestructionCreeper(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.95F);
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        if (!this.world.isRemote) {
            float power = this.getPowered() ? 8f : 4f;
            if (event.getExplosion() instanceof TakumiExplosion) {
                power = ((TakumiExplosion) event.getExplosion()).getSize();
            }
            if (power > 0.5) {
                for (BlockPos pos : event.getAffectedBlocks()) {
                    if (!this.world.isRemote && this.world.getBlockState(pos).getBlock().hasTileEntity(this.world.getBlockState(pos)) &&
                            this.world.getBlockState(pos).getBlock().createTileEntity(this.world, this.world.getBlockState(pos)) instanceof IInventory &&
                            TakumiUtils.takumiGetBlockResistance(this, this.world.getBlockState(pos), pos) != -1) {
                        //this.world.setBlockToAir(pos);
                        TakumiUtils.takumiCreateExplosion(this.world, this, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, power - 0.15f, false, true);
                    }
                }
            }
            Map<IBlockState, IRecipe> map = new HashMap<>();
            Map<IBlockState, Integer> count = new HashMap<>();
            for (BlockPos pos : event.getAffectedBlocks()) {
                IBlockState state = this.world.getBlockState(pos);
                if (state.getMaterial() != Material.AIR) {
                    ItemStack stack = state.getBlock().getPickBlock(state, null, this.world, pos, null);
                    boolean flg = false;
                    for (IRecipe recipe : Lists.newArrayList(CraftingManager.REGISTRY.iterator())) {
                        if (recipe.getRecipeOutput().getItem() == stack.getItem() && recipe.getRecipeOutput().getMetadata() == stack.getMetadata()) {
                            if (!map.containsValue(recipe)) {
                                map.put(state, recipe);
                            }
                            flg = true;
                        }
                    }
                    if (flg) {
                        if (count.containsKey(state)) {
                            count.replace(state, count.get(state) + 1);
                        } else {
                            count.put(state, 1);
                        }
                    }
                }
                this.world.setBlockToAir(pos);
            }
            if (!map.isEmpty()) {
                for (Map.Entry<IBlockState, IRecipe> entry : Lists.newArrayList(map.entrySet())) {
                    IRecipe recipe = entry.getValue();
                    int i = count.get(entry.getKey());
                    List<ItemStack> stackList = new ArrayList<>();
                    for (Ingredient ingredient : recipe.getIngredients()) {
                        for (ItemStack stack : ingredient.getMatchingStacks()) {
                            if (!stackList.contains(stack)) {
                                stackList.add(stack);
                            }
                        }
                    }
                    for (ItemStack stack : stackList) {
                        stack.setCount(i);
                        this.world.spawnEntity(new EntityItem(this.world, posX, posY, posZ, stack));
                    }
                }
            }
            //event.getAffectedBlocks().clear();
            event.getAffectedEntities().clear();
        }
        return true;
    }

    @Override
    public RenderLiving getRender(RenderManager manager) {
        return new RenderDestructionCreeper(manager);
    }

    @Override
    public void takumiExplode() {
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.GROUND_D;
    }

    @Override
    public int getExplosionPower() {
        return 4;
    }

    @Override
    public int getSecondaryColor() {
        return 0x005500;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "destructioncreeper";
    }

    @Override
    public int getRegisterID() {
        return 217;
    }
}
