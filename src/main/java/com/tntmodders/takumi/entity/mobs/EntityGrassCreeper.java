package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class EntityGrassCreeper extends EntityTakumiAbstractCreeper {

    public EntityGrassCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        event.getAffectedBlocks().forEach(pos -> {
            for (int i = 0; i < 3 + this.rand.nextInt(3); i++) {
                ItemDye.applyBonemeal(
                        new ItemStack(Items.DYE, this.rand.nextInt(4) + 1, EnumDyeColor.WHITE.getMetadata()),
                        this.world, pos);
            }
            if (FMLCommonHandler.instance().getSide().isClient()) {
                ItemDye.spawnBonemealParticles(this.world, pos, 3);
            }
        });
        event.getAffectedBlocks().clear();
        return true;
    }

    @Override
    public void takumiExplode() {
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.GRASS;
    }

    @Override
    public int getExplosionPower() {
        return 5;
    }

    @Override
    public int getSecondaryColor() {
        return 0xaaffaa;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "grasscreeper";
    }

    @Override
    public int getRegisterID() {
        return 63;
    }

    @Override
    public int getPrimaryColor() {
        return 0x77aa99;
    }
}
