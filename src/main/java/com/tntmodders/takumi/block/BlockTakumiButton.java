package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockButton;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockTakumiButton extends BlockButton {
    private final boolean wooden;

    public BlockTakumiButton(String s, boolean wooden) {
        super(wooden);
        this.setRegistryName(TakumiCraftCore.MODID, s);
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName(s);
        this.setHardness(0.5f);
        this.setResistance(10000000f);
        this.setSoundType(wooden ? SoundType.WOOD : SoundType.METAL);
        this.wooden = wooden;
    }

    @Override
    protected void playClickSound(@Nullable EntityPlayer player, World worldIn, BlockPos pos) {
        worldIn.playSound(player, pos, this.wooden ? SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON : SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON,
                SoundCategory.BLOCKS, 0.3F, 0.6F);
    }

    @Override
    protected void playReleaseSound(World worldIn, BlockPos pos) {
        worldIn.playSound(null, pos, this.wooden ? SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF : SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF,
                SoundCategory.BLOCKS, 0.3F, 0.5F);
    }
}
