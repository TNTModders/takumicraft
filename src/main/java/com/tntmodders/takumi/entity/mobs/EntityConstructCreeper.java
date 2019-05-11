package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class EntityConstructCreeper extends EntityTakumiAbstractCreeper {

    public EntityConstructCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                if (x != -3 && x != 3 && z != -3 && z != 3) {
                    TakumiUtils.setBlockStateProtected(this.world, this.getPosition().add(x, 0, z),
                            Blocks.PLANKS.getDefaultState());
                    if (x == -2 || x == 2 || z == -2 || z == 2) {
                        for (int y = 1; y < 4; y++) {
                            TakumiUtils.setBlockStateProtected(this.world, this.getPosition().add(x, y, z),
                                    Blocks.PLANKS.getDefaultState());
                        }
                    }
                }
                for (int y = 3; y <= 6; y++) {
                    if (x <= 6 - y && x >= y - 6) {
                        if (x == 6 - y || x == y - 6 || (z != -3 && z != 3)) {
                            TakumiUtils.setBlockStateProtected(this.world, this.getPosition().add(x, y, z),
                                    Blocks.PLANKS.getDefaultState());
                        }
                    }
                }
                if (x >= -1 && x <= 1 && z >= -1 && z <= 1) {
                    TakumiUtils.setBlockStateProtected(this.world, this.getPosition().add(x, 3, z),
                            Blocks.AIR.getDefaultState());
                }
            }
        }
        TakumiUtils.setBlockStateProtected(this.world, this.getPosition().add(0, 1, 2), Blocks.AIR.getDefaultState());
        TakumiUtils.setBlockStateProtected(this.world, this.getPosition().add(0, 2, 2), Blocks.AIR.getDefaultState());
        TakumiUtils.setBlockStateProtected(this.world, this.getPosition().add(0, 1, 2),
                Blocks.OAK_DOOR.getDefaultState());
        TakumiUtils.setBlockStateProtected(this.world, this.getPosition().add(0, 2, 2),
                Blocks.OAK_DOOR.getDefaultState().withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER));
        TakumiUtils.setBlockStateProtected(this.world, this.getPosition().add(2, 2, 0), Blocks.AIR.getDefaultState());
        TakumiUtils.setBlockStateProtected(this.world, this.getPosition().add(-2, 2, 0), Blocks.AIR.getDefaultState());
        TakumiUtils.setBlockStateProtected(this.world, this.getPosition().add(-1, 1, -1),
                Blocks.CRAFTING_TABLE.getDefaultState());
        TakumiUtils.setBlockStateProtected(this.world, this.getPosition().add(0, 1, -1),
                Blocks.LIT_FURNACE.getDefaultState());
        TakumiUtils.setBlockStateProtected(this.world, this.getPosition().add(1, 1, 0),
                Blocks.BED.getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.NORTH));
        TakumiUtils.setBlockStateProtected(this.world, this.getPosition().add(1, 1, -1),
                Blocks.BED.getDefaultState().withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD).withProperty(
                        BlockHorizontal.FACING, EnumFacing.NORTH));
        if (this.world.getTileEntity(this.getPosition().add(1, 1, -1)) instanceof TileEntityBed) {
            ((TileEntityBed) this.world.getTileEntity(this.getPosition().add(1, 1, -1))).setColor(EnumDyeColor.GREEN);
        }
        if (this.world.getTileEntity(this.getPosition().add(1, 1, 0)) instanceof TileEntityBed) {
            ((TileEntityBed) this.world.getTileEntity(this.getPosition().add(1, 1, 0))).setColor(EnumDyeColor.GREEN);
        }
        TakumiUtils.setBlockStateProtected(this.world, this.getPosition().add(1, 0, -1),
                Blocks.CHEST.getDefaultState());
        if (this.world.getTileEntity(this.getPosition().add(1, 0, -1)) instanceof TileEntityChest) {
            for (int i = 0; i < 27; i++) {
                ((TileEntityChest) this.world.getTileEntity(this.getPosition().add(1, 0, -1))).setInventorySlotContents(
                        i, new ItemStack(Items.GUNPOWDER));
            }
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL;
    }

    @Override
    public int getExplosionPower() {
        return 0;
    }

    @Override
    public int getSecondaryColor() {
        return 0xff8800;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "constructcreeper";
    }

    @Override
    public int getRegisterID() {
        return 78;
    }
}
