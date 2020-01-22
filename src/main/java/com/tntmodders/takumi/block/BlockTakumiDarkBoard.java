package com.tntmodders.takumi.block;

import com.tntmodders.asm.TakumiASMNameMap;
import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.tileentity.TileEntityDarkBoard;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

public class BlockTakumiDarkBoard extends BlockContainer {
    public BlockTakumiDarkBoard() {
        super(Material.ROCK);
        this.setRegistryName(TakumiCraftCore.MODID, "darkboard");
        this.setUnlocalizedName("darkboard");
        this.setBlockUnbreakable();
        this.setResistance(10000000f);
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setLightLevel(0.4f);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityDarkBoard();
    }

    @Override
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
        TakumiBlockCore.BOMB_MAP.values().forEach(blockTakumiMonsterBomb -> {
            if (blockTakumiMonsterBomb.getName().contains(((TileEntityDarkBoard) worldIn.getTileEntity(pos)).name)) {
                try {
                    EntityCreeper creeper =
                            blockTakumiMonsterBomb.getEntityClass().getConstructor(World.class).newInstance(worldIn);
                    creeper.setPosition(pos.getX() + 2.5 + worldIn.rand.nextDouble() - 0.5, pos.getY(),
                            pos.getZ() + 2.5 + worldIn.rand.nextDouble() - 0.5);
                    try {
                        Field field = TakumiASMNameMap.getField(EntityCreeper.class, "fuseTime");
                        field.setAccessible(true);
                        field.set(creeper, 50);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!worldIn.isRemote) {
                        worldIn.spawnEntity(creeper);
                        creeper.ignite();
                    }
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        });
        return super.eventReceived(state, worldIn, pos, id, param);
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos,
            EnumFacing side) {
        return true;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        if (worldIn.getTileEntity(pos) instanceof TileEntityDarkBoard) {
            Random random = new Random();
            random.setSeed(System.currentTimeMillis() + pos.getX() + pos.getZ());
            if (((TileEntityDarkBoard) worldIn.getTileEntity(pos)).name == null ||
                    ((TileEntityDarkBoard) worldIn.getTileEntity(pos)).name.isEmpty()) {
                Class clazz = ((Class) TakumiBlockCore.BOMB_MAP.keySet().toArray()[random.nextInt(
                        TakumiBlockCore.BOMB_MAP.size())]);
                try {
                    EntityTakumiAbstractCreeper creeper =
                            (EntityTakumiAbstractCreeper) clazz.getConstructor(World.class).newInstance(worldIn);
                    ((TileEntityDarkBoard) worldIn.getTileEntity(pos)).name = creeper.getRegisterName();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //TakumiCraftCore.LOGGER.info(((TileEntityDarkBoard) worldIn.getTileEntity(pos)).name);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
            EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityDarkBoard) {
            if (playerIn.getHeldItem(hand).getItem() instanceof ItemBlock &&
                    ((ItemBlock) playerIn.getHeldItem(hand).getItem()).getBlock() instanceof BlockTakumiMonsterBomb &&
                    ((BlockTakumiMonsterBomb) ((ItemBlock) playerIn.getHeldItem(
                            hand).getItem()).getBlock()).getName().contains(
                            ((TileEntityDarkBoard) worldIn.getTileEntity(pos)).name)) {
                worldIn.setBlockState(pos, TakumiBlockCore.DARKBOARD_ON.getDefaultState());
                if (playerIn instanceof EntityPlayerMP) {
                    TakumiUtils.giveAdvancementImpossible((EntityPlayerMP) playerIn,
                            new ResourceLocation(TakumiCraftCore.MODID, "creeperbomb"),
                            new ResourceLocation(TakumiCraftCore.MODID, "darkshrine"));
                }
                worldIn.createExplosion(playerIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3f, true);
            } else {
                worldIn.addBlockEvent(pos, state.getBlock(), 0, 0);
            }
        }
        return true;
    }
}
