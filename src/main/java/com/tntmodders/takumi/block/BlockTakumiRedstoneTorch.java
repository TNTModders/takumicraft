package com.tntmodders.takumi.block;

import com.google.common.collect.Lists;
import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.client.particle.ParticleTakumiRedstone;
import com.tntmodders.takumi.core.TakumiBlockCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class BlockTakumiRedstoneTorch extends BlockTorch {
    public BlockTakumiRedstoneTorch(boolean isOn) {
        super();
        this.isOn = isOn;
        String name = "creeperredstonetorch_" + (isOn ? "on" : "off");
        this.setTickRandomly(true);
        this.setRegistryName(TakumiCraftCore.MODID, name);
        if (isOn) {
            this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
            this.setLightLevel(0.5f);
        }
        this.setUnlocalizedName(name);
        this.setHardness(0f);
        this.setResistance(10000000f);
        this.setSoundType(SoundType.WOOD);
    }

    private static final Map<World, List<Toggle>> toggles = new java.util.WeakHashMap<>(); // FORGE - fix vanilla MC-101233
    private final boolean isOn;

    private boolean isBurnedOut(World worldIn, BlockPos pos, boolean turnOff) {
        if (!toggles.containsKey(worldIn)) {
            toggles.put(worldIn, Lists.newArrayList());
        }

        List<Toggle> list = toggles.get(worldIn);

        if (turnOff) {
            list.add(new Toggle(pos, worldIn.getTotalWorldTime()));
        }

        int i = 0;

        for (Toggle blockredstonetorch$toggle : list) {
            if (blockredstonetorch$toggle.pos.equals(pos)) {
                ++i;

                if (i >= 8) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * How many world ticks before ticking
     */
    @Override
    public int tickRate(World worldIn) {
        return 2;
    }

    /**
     * Called after the block is set in the Chunk data, but before the Tile Entity is set
     */
    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (this.isOn) {
            for (EnumFacing enumfacing : EnumFacing.values()) {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
            }
        }
    }

    /**
     * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
     */
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (this.isOn) {
            for (EnumFacing enumfacing : EnumFacing.values()) {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
            }
        }
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return this.isOn && blockState.getValue(FACING) != side ? 15 : 0;
    }

    private boolean shouldBeOff(World worldIn, BlockPos pos, IBlockState state) {
        EnumFacing enumfacing = state.getValue(FACING).getOpposite();
        return worldIn.isSidePowered(pos.offset(enumfacing), enumfacing);
    }

    /**
     * Called randomly when setTickRandomly is set to true (used by e.g. crops to grow, etc.)
     */
    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        boolean flag = this.shouldBeOff(worldIn, pos, state);
        List<Toggle> list = toggles.get(worldIn);

        while (list != null && !list.isEmpty() && worldIn.getTotalWorldTime() - (list.get(0)).time > 60L) {
            list.remove(0);
        }

        if (this.isOn) {
            if (flag) {
                worldIn.setBlockState(pos, TakumiBlockCore.CREEPER_REDSTONE_TORCH_OFF.getDefaultState().withProperty(FACING, state.getValue(FACING)), 3);

                if (this.isBurnedOut(worldIn, pos, true)) {
                    worldIn.playSound(null, pos, SoundEvents.BLOCK_REDSTONE_TORCH_BURNOUT, SoundCategory.BLOCKS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);

                    for (int i = 0; i < 5; ++i) {
                        double d0 = (double) pos.getX() + rand.nextDouble() * 0.6D + 0.2D;
                        double d1 = (double) pos.getY() + rand.nextDouble() * 0.6D + 0.2D;
                        double d2 = (double) pos.getZ() + rand.nextDouble() * 0.6D + 0.2D;
                        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D);
                    }

                    worldIn.scheduleUpdate(pos, worldIn.getBlockState(pos).getBlock(), 160);
                }
            }
        } else if (!flag && !this.isBurnedOut(worldIn, pos, false)) {
            worldIn.setBlockState(pos, TakumiBlockCore.CREEPER_REDSTONE_TORCH.getDefaultState().withProperty(FACING, state.getValue(FACING)), 3);
        }
    }

    /**
     * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
     * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
     * block, etc.
     */
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!this.onNeighborChangeInternal(worldIn, pos, state)) {
            if (this.isOn == this.shouldBeOff(worldIn, pos, state)) {
                worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
            }
        }
    }

    @Override
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.DOWN ? blockState.getWeakPower(blockAccess, pos, side) : 0;
    }

    /**
     * Get the Item that this Block should drop when harvested.
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(TakumiBlockCore.CREEPER_REDSTONE_TORCH);
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (this.isOn) {
            double d0 = (double) pos.getX() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
            double d1 = (double) pos.getY() + 0.7D + (rand.nextDouble() - 0.5D) * 0.2D;
            double d2 = (double) pos.getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
            EnumFacing enumfacing = stateIn.getValue(FACING);

            if (enumfacing.getAxis().isHorizontal()) {
                EnumFacing enumfacing1 = enumfacing.getOpposite();
                double d3 = 0.27D;
                d0 += 0.27D * (double) enumfacing1.getFrontOffsetX();
                d1 += 0.22D;
                d2 += 0.27D * (double) enumfacing1.getFrontOffsetZ();
            }

            Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleTakumiRedstone(worldIn, d0, d1, d2, 0.0f, 0.0f, 0.0f));
        }
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(TakumiBlockCore.CREEPER_REDSTONE_TORCH);
    }

    @Override
    public boolean isAssociatedBlock(Block other) {
        return other == TakumiBlockCore.CREEPER_REDSTONE_TORCH_OFF || other == TakumiBlockCore.CREEPER_REDSTONE_TORCH;
    }

    static class Toggle {
        BlockPos pos;
        long time;

        public Toggle(BlockPos pos, long time) {
            this.pos = pos;
            this.time = time;
        }
    }
}
