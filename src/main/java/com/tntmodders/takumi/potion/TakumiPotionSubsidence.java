package com.tntmodders.takumi.potion;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TakumiPotionSubsidence extends Potion {
    public TakumiPotionSubsidence() {
        super(true, 551559);
        this.setRegistryName(TakumiCraftCore.MODID, "takumisubsidence");
        this.setPotionName("takumisubsidence");
    }

    public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier) {
        World world = entityLivingBaseIn.world;
        BlockPos pos = new BlockPos(entityLivingBaseIn).down();
        if (world.getBlockState(pos).getBlockHardness(world, pos) < 0 || world.getBlockState(pos).getBlock() == Blocks.BEDROCK ||
                world.getBlockState(pos.down()).getBlockHardness(world, pos.down()) < 0 || world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK) {
            entityLivingBaseIn.noClip = false;
        } else {
            entityLivingBaseIn.noClip = true;
            entityLivingBaseIn.motionY *= 0.55;
        }
        super.performEffect(entityLivingBaseIn, amplifier);
    }

    public void removeAttributesModifiersFromEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier) {
        entityLivingBaseIn.noClip = false;
        super.removeAttributesModifiersFromEntity(entityLivingBaseIn, attributeMapIn, amplifier);
    }

    public boolean isReady(int duration, int amplifier) {
        return true;
    }
}
