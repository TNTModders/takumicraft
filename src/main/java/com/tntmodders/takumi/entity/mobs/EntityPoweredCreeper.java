package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

public class EntityPoweredCreeper extends EntityTakumiAbstractCreeper {

    public EntityPoweredCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            int flg = this.rand.nextInt(3);
            for (int i = 0; i < (this.getPowered() ? 20 : 10); i++) {
                EntityCreeper creeper = new EntityCreeper(this.world);
                creeper.setPosition(this.posX, this.posY, this.posZ);
                TakumiUtils.takumiSetPowered(creeper, true);
                NBTTagCompound compound = new NBTTagCompound();
                creeper.writeEntityToNBT(compound);
                compound.setByte("ExplosionRadius", ((byte) 8));
                switch (flg) {
                    case 0: {
                        compound.setFloat("Health", 100);
                        break;
                    }
                    case 1: {
                        compound.setFloat("AbsorptionAmount", 1);
                        break;
                    }
                    case 2: {
                        compound.setShort("Fuse", ((short) 20));
                        break;
                    }
                }
                creeper.readEntityFromNBT(compound);
                this.world.spawnEntity(creeper);
            }
        }
    }

    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        event.getAffectedEntities().removeIf(entity -> entity instanceof EntityCreeper);
        return true;
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.MID;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL;
    }

    @Override
    public int getExplosionPower() {
        return 4;
    }

    @Override
    public int getSecondaryColor() {
        return 0x66ff44;
    }

    @Override
    public int getPrimaryColor() {
        return 0x00ff00;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }


    @Override
    public String getRegisterName() {
        return "poweredcreeper";
    }

    @Override
    public int getRegisterID() {
        return 269;
    }
}
