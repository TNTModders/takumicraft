package com.tntmodders.takumi.item;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemTakumiPainting extends Item {
    public static final String KEY = "creeperpainting";

    public ItemTakumiPainting() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "creeperpainting");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("creeperpainting");
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            EntityPainting painting = new EntityPainting(worldIn, pos.offset(facing), facing);
            NBTTagCompound nbt = new NBTTagCompound();
            NBTTagCompound data = painting.getEntityData();
            data.setBoolean(KEY, true);
            nbt = painting.writeToNBT(nbt);
            nbt.setTag("ForgeData", data);
            painting.readFromNBT(nbt);
            painting.setNoGravity(true);
            boolean flg = worldIn.spawnEntity(painting);
            if(flg && !player.isCreative()){
                player.getHeldItem(hand).shrink(1);
            }
        }
        return EnumActionResult.SUCCESS;
    }

    public static boolean isPaintingAntiExplosion(EntityPainting painting) {
        if (painting.hasNoGravity()) {
            return true;
        }
        NBTTagCompound nbt = painting.getEntityData();
        return nbt.hasKey(KEY) && nbt.getBoolean(KEY);
    }
}
