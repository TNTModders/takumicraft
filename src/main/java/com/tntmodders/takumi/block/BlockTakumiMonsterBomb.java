package com.tntmodders.takumi.block;

import com.tntmodders.takumi.entity.mobs.EntityBoltCreeper;
import com.tntmodders.takumi.item.ItemTakumiMonsterBomb;
import com.tntmodders.takumi.tileentity.TileEntityMonsterBomb;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.Random;

public class BlockTakumiMonsterBomb extends BlockAbstractTakumiBomb implements ITileEntityProvider, ITakumiItemBlock {

    public final TileEntityMonsterBomb tileEntityMonsterBomb;
    private final Class<? extends EntityCreeper> entityClass;
    private final String name;
    private final String locName;

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    public BlockTakumiMonsterBomb(Class<? extends EntityCreeper> entityClass, String name) {
        super("monsterbomb_" + name, 0.1f, Material.TNT, MapColor.GREEN);
        this.setLightLevel(1f);
        this.entityClass = entityClass;
        this.name = name;
        String locName = "textures/entity/" + name + ".png";
        if (Objects.equals(name, "destructioncreeper")) {
            locName = "textures/entity/destb.png";
        }
        if (Objects.equals(name, "creativecreeper")) {
            locName = "textures/entity/creab.png";
        }
        this.locName = locName;
        this.tileEntityMonsterBomb = new TileEntityMonsterBomb(locName);
        this.isBlockContainer = true;
        this.setUnlocalizedName("monsterbomb");
    }

    public Class<? extends EntityCreeper> getEntityClass() {
        return entityClass;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
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
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state,
            @Nullable
                    TileEntity te, ItemStack stack) {
        if (te instanceof IWorldNameable && ((IWorldNameable) te).hasCustomName()) {
            player.addStat(StatList.getBlockStats(this));
            player.addExhaustion(0.005F);

            if (worldIn.isRemote) {
                return;
            }

            int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
            Item item = this.getItemDropped(state, worldIn.rand, i);

            if (item == Items.AIR) {
                return;
            }

            ItemStack itemstack = new ItemStack(item, this.quantityDropped(worldIn.rand));
            itemstack.setStackDisplayName(((IWorldNameable) te).getName());
            spawnAsEntity(worldIn, pos, itemstack);
        } else {
            super.harvestBlock(worldIn, player, pos, state, null, stack);
        }
    }

    @Override
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
        super.eventReceived(state, worldIn, pos, id, param);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(id, param);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager) {
        return true;
    }

    @Override
    public void explode(World world, int x, int y, int z) {
        try {
            if (!world.isRemote) {
                EntityCreeper creeper = this.entityClass.getConstructor(World.class).newInstance(world);
                creeper.setPosition(x + 0.5, y + 0.5, z + 0.5);
                NBTTagCompound compound = new NBTTagCompound();
                creeper.writeEntityToNBT(compound);
                compound.setShort("Fuse", (short) 1);
                creeper.readEntityFromNBT(compound);
                if (creeper instanceof EntityBoltCreeper || world.isThundering()) {
                    creeper.onStruckByLightning(null);
                }
                creeper.setInvisible(true);
                creeper.ignite();
                world.spawnEntity(creeper);
                creeper.onUpdate();
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    float getPower() {
        return 0;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityMonsterBomb(this.locName);
    }

    @Override
    public ItemBlock getItem() {
        return new ItemTakumiMonsterBomb(this);
    }
}
