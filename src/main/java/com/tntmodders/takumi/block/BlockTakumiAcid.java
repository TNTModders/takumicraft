package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.tileentity.TileEntityAcidBlock;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class BlockTakumiAcid extends BlockContainer {
    public static final PropertyInteger META = PropertyInteger.create("acidmeta", 0, 15);

    public BlockTakumiAcid() {
        super(Material.TNT);
        this.setDefaultState(this.blockState.getBaseState().withProperty(META, 0));
        this.setRegistryName(TakumiCraftCore.MODID, "acidblock");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("acidblock");
        this.setResistance(0f);
        this.setHardness(0.5f);
        this.setLightLevel(1f);
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
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, META);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityAcidBlock();
    }
}
