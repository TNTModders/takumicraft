package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.item.ItemTakumiBed;
import com.tntmodders.takumi.tileentity.TileEntityTakumiBed;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockTakumiBed extends BlockBed implements ITakumiItemBlock {
    public BlockTakumiBed() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "creeperbed");
        this.setUnlocalizedName("creeperbed");
        this.setHardness(0.8f);
        this.setResistance(10000000f);
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setLightLevel(1);
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        if (state.getValue(PART) == BlockBed.EnumPartType.HEAD) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            EnumDyeColor enumdyecolor = tileentity instanceof TileEntityBed ? ((TileEntityBed) tileentity).getColor() : EnumDyeColor.RED;
            spawnAsEntity(worldIn, pos, new ItemStack(Item.getItemFromBlock(TakumiBlockCore.CREEPER_BED), 1));
        }
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        BlockPos blockpos = pos;

        if (state.getValue(PART) == BlockBed.EnumPartType.FOOT) {
            blockpos = pos.offset(state.getValue(FACING));
        }

        TileEntity tileentity = worldIn.getTileEntity(blockpos);
        EnumDyeColor enumdyecolor = tileentity instanceof TileEntityBed ? ((TileEntityBed) tileentity).getColor() : EnumDyeColor.RED;
        return new ItemStack(Item.getItemFromBlock(TakumiBlockCore.CREEPER_BED), 1);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return state.getValue(PART) == BlockBed.EnumPartType.FOOT ? Items.AIR : Item.getItemFromBlock(TakumiBlockCore.CREEPER_BED);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityTakumiBed();
    }

    @Override
    public ItemBlock getItem() {
        return new ItemTakumiBed(this);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (playerIn.getHeldItemMainhand().getItem() instanceof ItemBlock) {
            Block block = ((ItemBlock) playerIn.getHeldItemMainhand().getItem()).getBlock();
            IBlockState blockState = block.getStateFromMeta(playerIn.getHeldItemMainhand().getMetadata());
            if (blockState.isFullCube() && worldIn.getTileEntity(pos) instanceof TileEntityTakumiBed) {
                ((TileEntityTakumiBed) worldIn.getTileEntity(pos)).setItemValues(playerIn.getHeldItemMainhand());
                worldIn.notifyNeighborsRespectDebug(pos, this, true);
                if (!worldIn.isRemote) {
                    worldIn.getMinecraftServer().getPlayerList().sendPacketToAllPlayers(worldIn.getTileEntity(pos).getUpdatePacket());
                }

                EnumFacing offset = state.getValue(FACING);
                if (!this.isBedFoot(worldIn, pos)) {
                    offset = offset.getOpposite();
                }
                if (worldIn.getTileEntity(pos.offset(offset)) instanceof TileEntityTakumiBed) {
                    ((TileEntityTakumiBed) worldIn.getTileEntity(pos.offset(offset))).setItemValues(playerIn.getHeldItemMainhand());
                    worldIn.notifyNeighborsRespectDebug(pos, this, true);
                    if (!worldIn.isRemote) {
                        worldIn.getMinecraftServer().getPlayerList().sendPacketToAllPlayers(worldIn.getTileEntity(pos.offset(offset)).getUpdatePacket());
                    }
                }
                return true;
            }
        } else if (playerIn.getHeldItemMainhand().isEmpty()) {
            if (worldIn.getTileEntity(pos) instanceof TileEntityTakumiBed) {
                ((TileEntityTakumiBed) worldIn.getTileEntity(pos)).clear();
                worldIn.notifyNeighborsRespectDebug(pos, this, true);
                if (!worldIn.isRemote) {
                    worldIn.getMinecraftServer().getPlayerList().sendPacketToAllPlayers(worldIn.getTileEntity(pos).getUpdatePacket());
                }

                EnumFacing offset = state.getValue(FACING);
                if (!this.isBedFoot(worldIn, pos)) {
                    offset = offset.getOpposite();
                }
                if (worldIn.getTileEntity(pos.offset(offset)) instanceof TileEntityTakumiBed) {
                    ((TileEntityTakumiBed) worldIn.getTileEntity(pos.offset(offset))).clear();
                    worldIn.notifyNeighborsRespectDebug(pos, this, true);
                    if (!worldIn.isRemote) {
                        worldIn.getMinecraftServer().getPlayerList().sendPacketToAllPlayers(worldIn.getTileEntity(pos.offset(offset)).getUpdatePacket());
                    }
                }
                return true;
            }
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public boolean isBed(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable Entity player) {
        return true;
    }

    @Override
    public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager) {
        return true;
    }

    @Override
    public boolean addHitEffects(IBlockState state, World worldObj, RayTraceResult target, ParticleManager manager) {
        return true;
    }

    @Override
    public boolean addLandingEffects(IBlockState state, WorldServer worldObj, BlockPos blockPosition, IBlockState iblockstate, EntityLivingBase entity, int numberOfParticles) {
        return true;
    }

    @Override
    public boolean addRunningEffects(IBlockState state, World world, BlockPos pos, Entity entity) {
        return true;
    }
}
