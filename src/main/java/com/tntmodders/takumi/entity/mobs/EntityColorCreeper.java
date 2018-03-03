package com.tntmodders.takumi.entity.mobs;

import com.google.common.collect.Lists;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

import java.util.ArrayList;

public class EntityColorCreeper extends EntityTakumiAbstractCreeper {

    public EntityColorCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        for (BlockPos pos : event.getAffectedBlocks()) {
            IBlockState state = this.world.getBlockState(pos);
            ArrayList<IProperty> list = Lists.newArrayList(state.getPropertyKeys());
            if (!list.isEmpty()) {
                for (IProperty property : list) {
                    if (property instanceof PropertyBool) {
                        state = state.withProperty(property, this.rand.nextBoolean());
                    } else if (property instanceof PropertyInteger) {
                        state = state.withProperty(property,
                                (int) property.getAllowedValues().toArray(new Integer[0])[this.rand
                                        .nextInt(property.getAllowedValues().size())]);
                    } else if (property instanceof PropertyEnum) {
                        state = state.withProperty(property, (Enum) property.getAllowedValues().toArray()[this.rand
                                .nextInt(property.getAllowedValues().size())]);
                    }
                }
            }
            this.world.setBlockState(pos, state);
        }
        event.getAffectedBlocks().removeAll(event.getAffectedBlocks());
        return true;
    }

    @Override
    public int getPrimaryColor() {
        return 0xff8888;
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
        return EnumTakumiType.GROUND_M;
    }

    @Override
    public int getExplosionPower() {
        return 3;
    }

    @Override
    public int getSecondaryColor() {
        return 0x66ff66;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "colorcreeper";
    }

    @Override
    public int getRegisterID() {
        return 37;
    }
}
