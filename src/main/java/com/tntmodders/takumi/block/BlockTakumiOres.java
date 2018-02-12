package com.tntmodders.takumi.block;

import com.tntmodders.takumi.entity.mobs.EntityFishCreeper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockTakumiOres extends BlockTakumiGunOre {
    
    private final Item item;
    
    public BlockTakumiOres(Item item) {
        super("takumiore_" + item.getRegistryName().getResourcePath());
        this.item = item;
        if (item == Items.GLOWSTONE_DUST) {
            this.setLightLevel(1f);
        } else {
            this.setLightLevel(0.25f);
        }
    }
    
    @Override
    float getPower() {
        return 2.5f;
    }
    
    @Override
    public int quantityDropped(Random random) {
        return (random.nextInt(3) + 1) * random.nextInt(5);
    }
    
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return item;
    }
    
    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
        Random rand = new Random();
        return 2 * (rand.nextInt(5) + 3);
    }
    
    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random) {
        return this.quantityDropped(random);
    }
    
    @Override
    public int damageDropped(IBlockState state) {
        if (this.item == Items.DYE) {
            return EnumDyeColor.BLUE.getDyeDamage();
        }
        return 0;
    }
    
    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        super.onBlockHarvested(worldIn, pos, state, player);
        if (worldIn.rand.nextInt(3) == 0 && worldIn.getDifficulty() != EnumDifficulty.PEACEFUL && !worldIn.isRemote) {
            EntityFishCreeper fishCreeper = new EntityFishCreeper(worldIn);
            fishCreeper.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
            worldIn.spawnEntity(fishCreeper);
        }
    }
}
