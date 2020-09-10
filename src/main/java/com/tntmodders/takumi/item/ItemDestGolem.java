package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.entity.item.EntityDestGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemDestGolem extends Item {
    public ItemDestGolem() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "destgolem");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("destgolem");
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
                                      EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            EntityDestGolem destGolem = new EntityDestGolem(worldIn);
            BlockPos blockPos = pos.offset(facing);
            if (worldIn.isAirBlock(blockPos)) {
                destGolem.setPosition(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
                if (worldIn.spawnEntity(destGolem)) {
                    if (!player.isCreative()) {
                        player.getHeldItem(hand).shrink(1);
                    }
                    player.getCooldownTracker().setCooldown(this, 100);
                }
            }
        }
        return EnumActionResult.SUCCESS;
    }
}
