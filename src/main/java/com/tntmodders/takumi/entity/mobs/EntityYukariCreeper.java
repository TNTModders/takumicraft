package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

import java.util.ArrayList;
import java.util.List;

public class EntityYukariCreeper extends EntityTakumiAbstractCreeper {
    public EntityYukariCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        List<BlockPos> posList = new ArrayList<>();
        for (BlockPos pos : event.getAffectedBlocks()) {
            if (pos.getY() > this.posY) {
                this.world.setBlockToAir(pos);
            } else if (this.world.isAirBlock(pos) || this.world.getBlockState(pos).getMaterial().isLiquid()) {
                this.world.setBlockState(pos, this.world.getBlockState(this.getPosition().down()));
            }
        }
        event.getAffectedBlocks().clear();
        return true;
    }

    @Override
    public ResourceLocation getArmor() {
        return new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/yukaricreeper_armor.png");
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            int i = this.getPowered() ? 40 : 25;
            for (int x = -i; x <= i; x++) {
                for (int z = -i; z <= i; z++) {
                    if (x * x + z * z < i * i) {
                        this.world.createExplosion(this, this.posX + x, this.posY, this.posZ + z, (float) Math.sqrt(i - Math.sqrt(x * x + z * z) + 1) * 1.75f, true);
                    }
                }
            }
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.HIGH;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.GROUND_MD;
    }

    @Override
    public int getExplosionPower() {
        return 10;
    }

    @Override
    public int getSecondaryColor() {
        return 0xff00ff;
    }

    @Override
    public boolean isCustomSpawn() {
        return true;
    }

    @Override
    public String getRegisterName() {
        return "yukaricreeper";
    }

    @Override
    public int getRegisterID() {
        return 505;
    }

    @Override
    public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn) {
        return blockStateIn.getBlockHardness(worldIn, pos) == -1 ? 10000000f : 1f;
    }
}
