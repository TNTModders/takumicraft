package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.block.material.TakumiMaterial;
import com.tntmodders.takumi.core.TakumiItemCore;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;

public class ItemTakumiBucket extends ItemBucket {

    private final Block containedBlock;

    public ItemTakumiBucket(Block containedBlockIn) {
        super(containedBlockIn);
        this.containedBlock = containedBlockIn;
        String s = "takumibucket_" + containedBlockIn.getRegistryName().getResourcePath();
        this.setRegistryName(TakumiCraftCore.MODID, s);
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName(s);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        boolean flag = this.containedBlock == Blocks.AIR;
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, flag);
        ActionResult<ItemStack> ret = ForgeEventFactory.onBucketUse(playerIn, worldIn, itemstack, raytraceresult);
        if (ret != null) {
            return ret;
        }

        if (raytraceresult == null) {
            return new ActionResult<>(EnumActionResult.PASS, itemstack);
        }
        if (raytraceresult.typeOfHit != Type.BLOCK) {
            return new ActionResult<>(EnumActionResult.PASS, itemstack);
        }
        BlockPos blockpos = raytraceresult.getBlockPos();

        if (!worldIn.isBlockModifiable(playerIn, blockpos)) {
            return new ActionResult<>(EnumActionResult.FAIL, itemstack);
        }
        if (flag) {
            if (!playerIn.canPlayerEdit(blockpos.offset(raytraceresult.sideHit), raytraceresult.sideHit, itemstack)) {
                return new ActionResult<>(EnumActionResult.FAIL, itemstack);
            }
            IBlockState iblockstate = worldIn.getBlockState(blockpos);
            Material material = iblockstate.getMaterial();

            if (material == TakumiMaterial.HOT_SPRING && iblockstate.getValue(BlockLiquid.LEVEL) == 0) {
                worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 11);
                playerIn.addStat(StatList.getObjectUseStats(this));
                playerIn.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
                return new ActionResult<>(EnumActionResult.SUCCESS,
                        this.fillBucket(itemstack, playerIn, TakumiItemCore.TAKUMI_SPRING_BUCKET));
            }
            if (material == TakumiMaterial.TAKUMI_WATER && iblockstate.getValue(BlockLiquid.LEVEL) == 0) {
                playerIn.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
                worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 11);
                playerIn.addStat(StatList.getObjectUseStats(this));
                return new ActionResult<>(EnumActionResult.SUCCESS,
                        this.fillBucket(itemstack, playerIn, TakumiItemCore.TAKUMI_WATER_BUCKET));
            }
            return new ActionResult<>(EnumActionResult.FAIL, itemstack);
        }
        boolean flag1 = worldIn.getBlockState(blockpos).getBlock().isReplaceable(worldIn, blockpos);
        BlockPos blockpos1 =
                flag1 && raytraceresult.sideHit == EnumFacing.UP ? blockpos : blockpos.offset(raytraceresult.sideHit);

        if (!playerIn.canPlayerEdit(blockpos1, raytraceresult.sideHit, itemstack)) {
            return new ActionResult<>(EnumActionResult.FAIL, itemstack);
        }
        if (this.tryPlaceContainedLiquid(playerIn, worldIn, blockpos1)) {
            if (playerIn instanceof EntityPlayerMP) {
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) playerIn, blockpos1, itemstack);
            }

            playerIn.addStat(StatList.getObjectUseStats(this));
            return !playerIn.capabilities.isCreativeMode ?
                    new ActionResult(EnumActionResult.SUCCESS, new ItemStack(TakumiItemCore.TAKUMI_BUCKET)) :
                    new ActionResult(EnumActionResult.SUCCESS, itemstack);
        }
        return new ActionResult<>(EnumActionResult.FAIL, itemstack);
    }

    private ItemStack fillBucket(ItemStack emptyBuckets, EntityPlayer player, Item fullBucket) {
        if (player.capabilities.isCreativeMode) {
            return emptyBuckets;
        }
        emptyBuckets.shrink(1);

        if (emptyBuckets.isEmpty()) {
            return new ItemStack(fullBucket);
        }
        if (!player.inventory.addItemStackToInventory(new ItemStack(fullBucket))) {
            player.dropItem(new ItemStack(fullBucket), false);
        }

        return emptyBuckets;
    }

    @Override
    public boolean tryPlaceContainedLiquid(
            @Nullable
                    EntityPlayer player, World worldIn, BlockPos posIn) {
        if (this.containedBlock == Blocks.AIR) {
            return false;
        }
        IBlockState iblockstate = worldIn.getBlockState(posIn);
        Material material = iblockstate.getMaterial();
        boolean flag = !material.isSolid();
        boolean flag1 = iblockstate.getBlock().isReplaceable(worldIn, posIn);

        if (!worldIn.isAirBlock(posIn) && !flag && !flag1) {
            return false;
        }
        if (worldIn.provider.doesWaterVaporize()) {
            int l = posIn.getX();
            int i = posIn.getY();
            int j = posIn.getZ();
            worldIn.playSound(player, posIn, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F,
                    2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);

            for (int k = 0; k < 8; ++k) {
                worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, l + Math.random(), (double) i + Math.random(),
                        (double) j + Math.random(), 0.0D, 0.0D, 0.0D);
            }
        } else {
            if (!worldIn.isRemote && (flag || flag1) && !material.isLiquid()) {
                worldIn.destroyBlock(posIn, true);
            }

            SoundEvent
                    soundevent = /*this.containedBlock == Blocks.FLOWING_LAVA ? SoundEvents.ITEM_BUCKET_EMPTY_LAVA :*/
                    SoundEvents.ITEM_BUCKET_EMPTY;
            worldIn.playSound(player, posIn, soundevent, SoundCategory.BLOCKS, 1.0F, 1.0F);
            worldIn.setBlockState(posIn, this.containedBlock.getDefaultState(), 11);
        }

        return true;
    }
}
