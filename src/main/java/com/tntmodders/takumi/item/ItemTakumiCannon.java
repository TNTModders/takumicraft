package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.entity.item.EntityTakumiCannon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemTakumiCannon extends Item {
    public ItemTakumiCannon() {
        this.maxStackSize = 1;
        this.setRegistryName(TakumiCraftCore.MODID, "takumicannon");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("takumicannon");
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            EntityTakumiCannon cannon = new EntityTakumiCannon(worldIn);
            cannon.setPosition(pos.getX(), pos.getY() + 1, pos.getZ());
            if (!worldIn.collidesWithAnyBlock(cannon.getEntityBoundingBox())) {
                worldIn.spawnEntity(cannon);
                //cannon.setFacing(player.getHorizontalFacing());
            }
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
}
