package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemTakumiItemFrame extends Item {
    public static final String KEY = "creeperframe";

    public ItemTakumiItemFrame() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "creeperframe");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("creeperframe");
    }

    public static boolean isItemFrameAntiExplosion(EntityItemFrame frame) {
        if (frame.hasNoGravity()) {
            return true;
        }
        NBTTagCompound nbt = frame.getEntityData();
        return nbt.hasKey(KEY) && nbt.getBoolean(KEY);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            EntityItemFrame painting = new EntityItemFrame(worldIn, pos.offset(facing), facing);
            NBTTagCompound nbt = new NBTTagCompound();
            NBTTagCompound data = painting.getEntityData();
            data.setBoolean(KEY, true);
            nbt = painting.writeToNBT(nbt);
            nbt.setTag("ForgeData", data);
            painting.readFromNBT(nbt);
            painting.setNoGravity(true);
            boolean flg = worldIn.spawnEntity(painting);
            if (flg && !player.isCreative()) {
                player.getHeldItem(hand).shrink(1);
            }
        }
        return EnumActionResult.SUCCESS;
    }
}
