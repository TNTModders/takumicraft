package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.block.BlockFarmland;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

public class EntityBeetrootCreeper extends EntityTakumiAbstractCreeper {

    public EntityBeetrootCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
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
        return 10;
    }

    @Override
    public int getSecondaryColor() {
        return 0x333333;
    }

    @Override
    public int getPrimaryColor() {
        return 0xaa3333;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "beetrootcreeper";
    }

    @Override
    public int getRegisterID() {
        return 73;
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        for (Entity entity : event.getAffectedEntities()) {
            if (entity instanceof EntityPlayer) {
                for (int i = 0; i < ((EntityPlayer) entity).inventory.mainInventory.size(); i++) {
                    if (((EntityPlayer) entity).inventory.mainInventory.get(i) != ItemStack.EMPTY) {
                        ((EntityPlayer) entity).inventory.mainInventory.set(i, new ItemStack(Items.BEETROOT,
                                ((EntityPlayer) entity).inventory.mainInventory.get(i).getCount()));
                    }
                }
                for (int i = 0; i < ((EntityPlayer) entity).inventory.offHandInventory.size(); i++) {
                    if (((EntityPlayer) entity).inventory.offHandInventory.get(i) != ItemStack.EMPTY) {
                        ((EntityPlayer) entity).inventory.offHandInventory.set(i, new ItemStack(Items.BEETROOT,
                                ((EntityPlayer) entity).inventory.mainInventory.get(i).getCount()));
                    }
                }
            }
        }
        event.getAffectedEntities().clear();

        event.getAffectedBlocks().forEach(blockPos -> {
            if (this.world.getBlockState(blockPos).getBlock() == Blocks.DIRT ||
                    this.world.getBlockState(blockPos).getBlock() == Blocks.GRASS) {
                this.world.setBlockState(blockPos,
                        Blocks.FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, 6));
                if (this.world.isAirBlock(blockPos.up())) {
                    this.world.setBlockState(blockPos.up(), Blocks.BEETROOTS.getDefaultState(), 0);
                }
            }
        });
        event.getAffectedBlocks().clear();
        return true;
    }

    @Override
    public void onDeath(DamageSource source) {
        if (!this.world.isRemote) {
            this.dropItem(Items.BEETROOT_SOUP, 1);
        }
        super.onDeath(source);
    }
}
