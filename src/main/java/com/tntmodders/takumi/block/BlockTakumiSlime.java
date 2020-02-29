package com.tntmodders.takumi.block;

import com.tntmodders.takumi.TakumiCraftCore;
import net.minecraft.block.BlockSlime;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class BlockTakumiSlime extends BlockSlime {
    public BlockTakumiSlime() {
        super();
        this.setRegistryName(TakumiCraftCore.MODID, "creeperslimeblock");
        this.setCreativeTab(TakumiCraftCore.TAB_CREEPER);
        this.setUnlocalizedName("creeperslimeblock");
        this.setSoundType(SoundType.SLIME);
        this.setResistance(10000000f);
    }

    @Override
    public void onLanded(World worldIn, Entity entityIn) {
        entityIn.fallDistance = 0;
        if (entityIn.isSneaking()) {
            super.onLanded(worldIn, entityIn);
        } else if (entityIn.motionY < 0.0D) {
            entityIn.motionY = -entityIn.motionY * 2;
            if (entityIn.motionY > 10) {
                entityIn.motionY = 10;
            }
            if (!(entityIn instanceof EntityLivingBase)) {
                entityIn.motionY *= 0.8D;
            } else if (entityIn.motionY > 1) {
                worldIn.createExplosion(entityIn, entityIn.posX, entityIn.posY - 0.5, entityIn.posZ, 0f, false);
            }
        }
    }
}
