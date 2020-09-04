package com.tntmodders.takumi.entity.item;

import com.tntmodders.takumi.core.TakumiBlockCore;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityAlterDummy extends EntityLivingBase {
    public EntityAlterDummy(World worldIn) {
        super(worldIn);
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return null;
    }

    @Override
    public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
        return null;
    }

    @Override
    public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {

    }

    @Override
    public EnumHandSide getPrimaryHand() {
        return null;
    }

    @Override
    public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn) {
        return pos.getY() == 0 ? 1000000000 : (worldIn.getBlockState(pos).getBlock() == Blocks.BEDROCK ||
                worldIn.getBlockState(pos).getBlock() == TakumiBlockCore.TAKUMI_SUPERPOWERED) ? 0 :
                worldIn.getBlockState(pos).getBlockHardness(worldIn, pos) > 0 ? 0 :
                        super.getExplosionResistance(explosionIn, worldIn, pos, blockStateIn);
    }
}
