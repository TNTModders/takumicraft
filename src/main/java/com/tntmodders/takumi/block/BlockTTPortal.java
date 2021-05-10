package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.client.particle.ParticleTakumiPortal;
import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.tileentity.TileEntityTTPortal;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockTTPortal extends BlockContainer implements IBlockTT {
    protected static final AxisAlignedBB END_PORTAL_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);

    public BlockTTPortal() {
        super(Material.PORTAL);
        this.setLightLevel(1.0F);
        this.setRegistryName(TakumiCraftCore.MODID, "ttportal");
        this.setUnlocalizedName("ttportal");
        this.setBlockUnbreakable();
        this.setResistance(10000000f);
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityTTPortal();
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return END_PORTAL_AABB;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.DOWN && super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    /**
     * Called When an Entity Collided with the Block
     */
    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (entityIn instanceof EntityPlayer && !worldIn.isRemote && !entityIn.isRiding()
                && !entityIn.isBeingRidden() && entityIn.isNonBoss() && entityIn.getEntityBoundingBox().intersects(state.getBoundingBox(worldIn, pos).offset(pos))) {
            boolean flg = true;
            for (int x = -35; x <= 35; x++) {
                for (int z = -35; z <= 35; z++) {
                    BlockPos pos1 = pos.add(x, 9, z);
                    if (flg && worldIn.getBlockState(pos1).getBlock() == TakumiBlockCore.TT_CREEPERCORE) {
                        flg = false;
                        break;
                    }
                }
            }
            if (flg) {
                if (entityIn.ticksExisted % 200 == 0) {
                    entityIn.sendMessage(new TextComponentTranslation("takumicraft.message.ttportal.true"));
                    //entityIn.changeDimension(1);
                    if(FMLCommonHandler.instance().getSide().isClient()){
                        proxyShowGuiToast();
                    }
                }
            } else {
                if (entityIn.ticksExisted % 200 == 0) {
                    entityIn.sendMessage(new TextComponentTranslation("takumicraft.message.ttportal.false"));
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void proxyShowGuiToast() {
        Minecraft.getMinecraft().getToastGui().add(new SystemToast(SystemToast.Type.TUTORIAL_HINT, new TextComponentTranslation("tile.ttportal.info.01"),
                new TextComponentTranslation("tile.ttportal.info.02")));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        for (int i = 0; i < 20; i++) {
            double d0 = (float) pos.getX() + rand.nextFloat();
            double d1 = (float) pos.getY() + 0.8F;
            double d2 = (float) pos.getZ() + rand.nextFloat();
            double d3 = rand.nextFloat() - 0.5;
            double d4 = rand.nextFloat();
            double d5 = rand.nextFloat() - 0.5;
            Minecraft.getMinecraft().effectRenderer.addEffect(
                    new ParticleTakumiPortal(worldIn, d0, d1, d2, d3 * 3, d4 * 10, d5 * 3));
        }
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return ItemStack.EMPTY;
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     */
    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return MapColor.BLACK;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
        return BlockFaceShape.UNDEFINED;
    }
}
