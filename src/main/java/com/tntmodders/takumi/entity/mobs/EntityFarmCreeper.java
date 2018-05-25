package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.core.TakumiEntityCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.ITakumiEntity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EntityFarmCreeper extends EntityTakumiAbstractCreeper {

    public EntityFarmCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            List<ITakumiEntity> list =
                    TakumiEntityCore.getEntityList().stream().filter(ITakumiEntity :: isAnimal).collect(
                            Collectors.toList());
            if (!list.isEmpty()) {
                for (int i = 0; i < (this.getPowered() ? 20 : 10); i++) {
                    Class aClass = ((EntityTakumiAbstractCreeper) list.get(this.rand.nextInt(list.size()))).getClass();
                    try {
                        EntityTakumiAbstractCreeper creeper =
                                ((EntityTakumiAbstractCreeper) aClass.getConstructor(World.class).newInstance(
                                        this.world));
                        creeper.copyLocationAndAnglesFrom(this);
                        this.world.spawnEntity(creeper);
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.GROUND;
    }

    @Override
    public int getExplosionPower() {
        return 10;
    }

    @Override
    public int getSecondaryColor() {
        return 0x00ff00;
    }

    @Override
    public int getPrimaryColor() {
        return 0xaa4400;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "farmcreeper";
    }

    @Override
    public int getRegisterID() {
        return 54;
    }

    @Override
    public boolean takumiExplodeEvent(Detonate event) {
        List<BlockPos> posList = new ArrayList<>();
        for (BlockPos pos : event.getAffectedBlocks()) {
            if (pos.getY() <= this.posY + 1) {
                posList.add(pos);
            }
        }
        event.getAffectedBlocks().removeAll(posList);

        for (int y = 0; y <= 1; y++) {
            for (int x = -5; x <= 5; x++) {
                for (int z = -5; z <= 5; z++) {
                    if (x == -5 || x == 5 || z == -5 || z == 5) {
                        this.world.setBlockState(this.getPosition().add(x, y, z), Blocks.OAK_FENCE.getDefaultState());
                    } else {
                        this.world.setBlockToAir(this.getPosition().add(x, y, z));
                    }
                }
            }
        }
        event.getAffectedEntities().clear();
        return true;
    }
}
