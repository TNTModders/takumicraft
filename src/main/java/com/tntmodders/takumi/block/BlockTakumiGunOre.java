package com.tntmodders.takumi.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockTakumiGunOre extends BlockAbstractTakumiBomb {
    
    public BlockTakumiGunOre() {
        super("gunore", 5f, Material.ROCK, MapColor.STONE);
        this.setHarvestLevel("pickaxe", 1);
    }
    
    @Override
    float getPower() {
        return 1.5f;
    }
    
    @Override
    public int quantityDropped(Random random) {
        return random.nextInt(1) * random.nextInt(5);
    }
    
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.GUNPOWDER;
    }
    
    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
        // ドロップする経験値の設定。(他の鉱石の数値はBlockOre参照。ここではダイヤと同じ)
        Random rand = new Random();
        return rand.nextInt(5) + 3;
    }
    
    // 幸運でドロップする量の設定。(幸運で掘った時にドロップする量をランダムにできる)
    @Override
    public int quantityDroppedWithBonus(int level, Random random) {
        if (level > 0 && Item.getItemFromBlock(this) != this.getItemDropped(this.getDefaultState(), random, level)) {
            int j = random.nextInt(level + 2) - 1;
            if (j < 0) {
                j = 0;
            }
            return this.quantityDropped(random) * (j + 1);
        } else {
            return this.quantityDropped(random);
        }
    }
    
    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random) {
        Random rand = new Random();
        return rand.nextInt(5) + 3;
    }
    
    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return true;
    }
}
