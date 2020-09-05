package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockTakumiInstantWall extends Block {
    public static final PropertyInteger META = PropertyInteger.create("wallmeta", 0, 15);
    private Explosion lastblasted;

    public BlockTakumiInstantWall() {
        super(Material.ROCK);
        this.setDefaultState(this.blockState.getBaseState().withProperty(META, 0));
        this.setRegistryName(TakumiCraftCore.MODID, "takumiinstantwall");
        this.setUnlocalizedName("takumiinstantwall");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setHardness(0.1f);
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
        if (world.getBlockState(pos).getBlock() == this) {
            int meta = world.getBlockState(pos).getValue(META);
            if (meta == 0) {
                return 0f;
            } else if (meta == 15) {
                world.setBlockToAir(pos);
                if (!world.isRemote) {
                    world.createExplosion(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1.5f, false);
                }
                return 0f;
            } else {
                if (!world.isUpdateScheduled(pos, this)) {
                    world.scheduleUpdate(pos, this, 20);
                }
                return 10000000f;
            }
        }
        return super.getExplosionResistance(world, pos, exploder, explosion);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (!world.isRemote && world.getBlockState(pos).getBlock() == this) {
            int meta = world.getBlockState(pos).getValue(META);
            if (meta > 0 && meta < 15) {
                world.setBlockState(pos, this.getDefaultState().withProperty(META, meta + 1));
            }
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.getBlockState(pos).getBlock() == this) {
            int meta = worldIn.getBlockState(pos).getValue(META);
            if (meta == 0) {
                if (facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH) {
                    for (int x = -2; x <= 2; x++) {
                        for (int y = 0; y <= 2; y++) {
                            BlockPos blockPos = pos.add(x, y, 0);
                            if (worldIn.isAirBlock(blockPos) || worldIn.getBlockState(blockPos).getBlock() == this || worldIn.getBlockState(blockPos).getBlock().isReplaceable(worldIn, blockPos)) {
                                worldIn.setBlockState(pos.add(x, y, 0), this.getDefaultState().withProperty(META, 11));
                            }
                        }
                    }
                } else if (facing == EnumFacing.EAST || facing == EnumFacing.WEST) {
                    for (int z = -2; z <= 2; z++) {
                        for (int y = 0; y <= 2; y++) {
                            BlockPos blockPos = pos.add(0, y, z);
                            if (worldIn.isAirBlock(blockPos) || worldIn.getBlockState(blockPos).getBlock() == this || worldIn.getBlockState(blockPos).getBlock().isReplaceable(worldIn, blockPos)) {
                                worldIn.setBlockState(pos.add(0, y, z), this.getDefaultState().withProperty(META, 11));
                            }
                        }
                    }
                } else {
                    return false;
                }
                if (!worldIn.isRemote) {
                    worldIn.createExplosion(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0f, false);
                }
                return true;
            }
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (worldIn.getBlockState(pos).getBlock() == this) {
            int meta = worldIn.getBlockState(pos).getValue(META);
            if (meta > 13) {
                double d0 = (double) pos.getX() + rand.nextDouble() * 1.4 - 0.2;
                double d1 = (double) pos.getY() + 0.3 + rand.nextDouble() * 0.5;
                double d2 = (double) pos.getZ() + rand.nextDouble() * 1.4 - 0.2;
                for (int i = 0; i < (meta - 10) * (meta - 10) * 2; i++) {
                    TakumiCraftCore.LOGGER.info("disp");
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(META, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(META);
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, META);
    }
}
