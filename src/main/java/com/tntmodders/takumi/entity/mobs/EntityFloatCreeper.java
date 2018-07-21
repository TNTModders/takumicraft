package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

public class EntityFloatCreeper extends EntityTakumiAbstractCreeper {

    public EntityFloatCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void onUpdate() {
        if (!this.getPowered()) {
            TakumiUtils.takumiSetPowered(this, true);
        }
        super.onUpdate();
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        event.getAffectedBlocks().forEach(pos -> {
            this.world.setBlockState(pos.up(30), this.world.getBlockState(pos));
            this.world.setBlockToAir(pos);
        });
        event.getAffectedBlocks().clear();
        event.getAffectedEntities().forEach(entity -> {
            entity.setPosition(this.posX, this.posY + 30.5, this.posZ);
        });
        event.getAffectedEntities().clear();
        return true;
    }

    @Override
    public int getPrimaryColor() {
        return 0x00aaff;
    }

    @Override
    public ResourceLocation getArmor() {
        return new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/big_creeper_armor.png");
    }

    @Override
    public void takumiExplode() {
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL_MD;
    }

    @Override
    public int getExplosionPower() {
        return 6;
    }

    @Override
    public int getSecondaryColor() {
        return 0x00ff00;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "floatcreeper";
    }

    @Override
    public int getRegisterID() {
        return 276;
    }

    @Override
    public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn) {
        return blockStateIn.getBlockHardness(worldIn, pos) == -1 ? 10000000f : 0.75f;
    }
}