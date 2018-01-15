package com.tntmodders.takumi.world.gen;


import com.tntmodders.takumi.core.TakumiBlockCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import java.util.Random;

public class WorldGenTakumiObjet extends WorldGenAbstractTree {
    
    protected final int minHeight;
    protected final Block blockTakumi1;
    protected final Block blockTakumi2;
    protected final int metaTakumi;
    
    public WorldGenTakumiObjet(boolean par1) {
        this(par1, 5, TakumiBlockCore.CREEPER_IRON, TakumiBlockCore.CREEPER_BRICK, 0);
    }
    
    public WorldGenTakumiObjet(boolean par1, int height, Block Takumi1, Block Takumi2, int meta) {
        super(par1);
        this.minHeight = height;
        this.blockTakumi1 = Takumi1;
        this.blockTakumi2 = Takumi2;
        this.metaTakumi = meta;
    }
    
    @Override
    public boolean generate(World par1World, Random par2Random, BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        
        int var6 = par2Random.nextInt(3) + this.minHeight;
        boolean var7 = true;
        
        if (y >= 1 && y + var6 + 1 <= 256) {
            byte var9;
            int var11;
            IBlockState var12;
            
            for (int var8 = y; var8 <= y + 1 + var6; ++var8) {
                var9 = 1;
                
                if (var8 == y) {
                    var9 = 0;
                }
                
                if (var8 >= y + 1 + var6 - 2) {
                    var9 = 2;
                }
                
                for (int var10 = x - var9; var10 <= x + var9 && var7; ++var10) {
                    for (var11 = z - var9; var11 <= z + var9 && var7; ++var11) {
                        if (var8 >= 0 && var8 < 256) {
                            var12 = par1World.getBlockState(new BlockPos(var10, var8, var11));
                            if (var12.getMaterial() != Material.AIR) {
                                var7 = false;
                            }
                        } else {
                            var7 = false;
                        }
                    }
                }
            }
            
            if (!var7) {
                return false;
            }
            Block var8 = par1World.getBlockState(pos.down()).getBlock();
            
            if (y < 256 - var6 - 1) {
                this.buildTakumi(x, y, z, var6, par1World, par2Random);
                return true;
            }
            return false;
        }
        return false;
    }
    
    protected void buildTakumi(int ox, int oy, int oz, int height, World par1World, Random par2Random) {
        Block blockTakumi;
        Random r = new Random();
        if (r.nextBoolean()) {
            blockTakumi = this.blockTakumi1;
        } else {
            blockTakumi = this.blockTakumi2;
        }
        int blockY, radius, blockX, relX;
        radius = 1 + height / 2;
        
        for (blockY = oy; blockY <= oy + height; ++blockY) {
            for (blockX = ox - radius; blockX <= ox + radius; ++blockX) {
                relX = blockX - ox;
                
                for (int blockZ = oz - radius; blockZ <= oz + radius; ++blockZ) {
                    int relZ = blockZ - oz;
                    
                    if (blockY == oy) {
                        for (int y = oy - 1; y > 0; y--) {
                            Block blockId = par1World.getBlockState(new BlockPos(blockX, y, blockZ)).getBlock();
                            if (blockId == Blocks.AIR) {
                                if (par2Random.nextBoolean()) {
                                    this.setBlockAndNotifyAdequately(par1World, new BlockPos(blockX, y, blockZ), blockTakumi.getDefaultState());
                                }
                            } else {
                                break;
                            }
                        }
                    }
                    Random rand = new Random();
                    if (rand.nextBoolean()) {
                        this.setBlockAndNotifyAdequately(par1World, new BlockPos(blockX, blockY, blockZ), blockTakumi.getDefaultState());
                    }
                }
            }
        }
    }
}

