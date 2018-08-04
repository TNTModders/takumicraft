package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.core.TakumiEntityCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.ITakumiEntity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class EntityRandCreeper extends EntityTakumiAbstractCreeper {

    private List<ITakumiEntity> entities = new ArrayList<>();

    public EntityRandCreeper(World worldIn) {
        super(worldIn);
        TakumiEntityCore.getEntityList().forEach(iTakumiEntity -> {
            if (iTakumiEntity.takumiRank() == ITakumiEntity.EnumTakumiRank.LOW ||
                    iTakumiEntity.takumiRank() == ITakumiEntity.EnumTakumiRank.MID ||
                    iTakumiEntity.takumiRank() == EnumTakumiRank.HIGH) {
                if (iTakumiEntity.getClass() != EntitySeaGuardianCreeper.class &&
                        iTakumiEntity.getClass() != EntitySquidCreeper.class) {
                    entities.add(iTakumiEntity);
                }
            }
        });
    }

    @Override
    public void setDead() {
        if (!(this.getHealth() <= 0 || this.world.getDifficulty() == EnumDifficulty.PEACEFUL)) {
            if (!this.world.isRemote) {
                EntityCreeper creeper = null;
                if (!entities.isEmpty()) {
                    try {
                        creeper = (EntityCreeper) entities.get(
                                this.rand.nextInt(entities.size())).getClass().getConstructor(World.class).newInstance(
                                this.world);
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
                if (creeper != null) {
                    creeper.copyLocationAndAnglesFrom(this);
                    if (creeper instanceof EntityLiving) {
                        creeper.onInitialSpawn(this.world.getDifficultyForLocation(this.getPosition()), null);
                    }
                    NBTTagCompound compound = new NBTTagCompound();
                    creeper.writeEntityToNBT(compound);
                    compound.setShort("Fuse", (short) 1);
                    creeper.readEntityFromNBT(compound);
                    creeper.setInvisible(true);
                    creeper.ignite();
                    this.world.spawnEntity(creeper);
                    creeper.onUpdate();

                }
            }
        }
        super.setDead();
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
        return EnumTakumiType.NORMAL_M;
    }

    @Override
    public int getExplosionPower() {
        return 0;
    }

    @Override
    public int getSecondaryColor() {
        return 0xffffff;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "randcreeper";
    }

    @Override
    public int getRegisterID() {
        return 59;
    }
}
