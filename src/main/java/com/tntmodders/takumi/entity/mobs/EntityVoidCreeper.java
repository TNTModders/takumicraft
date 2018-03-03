package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

import java.util.ArrayList;
import java.util.List;

public class EntityVoidCreeper extends EntityTakumiAbstractCreeper {

    public EntityVoidCreeper(World worldIn) {
        super(worldIn);
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
        return EnumTakumiType.GROUND_M;
    }

    @Override
    public int getExplosionPower() {
        return 8;
    }

    @Override
    public int getSecondaryColor() {
        return 0xaa00aa;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "voidcreeper";
    }

    @Override
    public int getRegisterID() {
        return 215;
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        List<BlockPos> posList = new ArrayList<>();
        for (BlockPos pos : event.getAffectedBlocks()) {
            if (pos.getY() < this.posY) {
                posList.add(pos);
            }
        }
        event.getAffectedBlocks().removeAll(posList);
        return true;
    }
}
