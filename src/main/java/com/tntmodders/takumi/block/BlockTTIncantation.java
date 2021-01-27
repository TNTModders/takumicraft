package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.tileentity.TileEntityTTIncantation;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockTTIncantation extends BlockContainer {
    public static final PropertyEnum<EnumTTIncantationType> INCANTATION = PropertyEnum.create("incantation", EnumTTIncantationType.class);

    public BlockTTIncantation() {
        super(Material.ROCK);
        this.setRegistryName(TakumiCraftCore.MODID, "ttincantation");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setDefaultState(this.blockState.getBaseState().withProperty(INCANTATION, EnumTTIncantationType.MONSTER));
        this.setUnlocalizedName("ttincantation");
        this.setBlockUnbreakable();
        this.setResistance(10000000f);
    }


    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    /**
     * Get the Item that this Block should drop when harvested.
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.AIR;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityTTIncantation();
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(INCANTATION, EnumTTIncantationType.getTypeFromID(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(INCANTATION).getID();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, INCANTATION);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    public enum EnumTTIncantationType implements IStringSerializable {
        MONSTER("monster", 0),
        WATER("water", 1),
        THUNDER("thunder", 2),
        LAVA("lava", 3),
        EX("ex", 4);

        private final String name;
        private final int id;

        EnumTTIncantationType(String s, int id) {
            this.name = s;
            this.id = id;
        }

        public static EnumTTIncantationType getTypeFromID(int id) {
            switch (id) {
                case 1:
                    return WATER;
                case 2:
                    return THUNDER;
                case 3:
                    return LAVA;
                case 4:
                    return EX;
                default:
                    return MONSTER;
            }
        }

        @Override
        public String getName() {
            return name;
        }

        public int getID() {
            return id;
        }
    }
}
