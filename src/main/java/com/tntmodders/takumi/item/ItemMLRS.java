package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.entity.item.EntityTakumiMLRS;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemMLRS extends Item {
    public ItemMLRS() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "takumimlrs");
        //this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("takumimlrs");
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
            EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            EntityTakumiMLRS mlrs = new EntityTakumiMLRS(worldIn);
            mlrs.setPosition(pos.getX() + 0.5 + facing.getFrontOffsetX(), pos.getY() + facing.getFrontOffsetY(),
                    pos.getZ() + 0.5 + facing.getFrontOffsetZ());
            boolean flg = worldIn.spawnEntity(mlrs);
            if (flg && !player.isCreative()) {
                player.getHeldItem(hand).shrink(1);
            }
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
}
