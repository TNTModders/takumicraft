package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.TakumiCraftCore;
import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityCrystalCreeper extends EntityTakumiAbstractCreeper {
    private static final DataParameter<Boolean> EXPLODED =
            EntityDataManager.createKey(EntityGiantCreeper.class, DataSerializers.BOOLEAN);
    private boolean dead;

    public EntityCrystalCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void onUpdate() {
        if (this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
            this.setDead();
        }
        if (this.getDataManager().get(EXPLODED)) {
            if (!this.isInvisible()) {
                this.setInvisible(true);
            }
            if (this.world.isRemote) {
                for (double x = -5; x <= 5; x += 0.5) {
                    for (double y = -5; y <= 5; y += 0.5) {
                        for (double z = -5; z <= 5; z += 0.5) {
                            if (x < -4.5 || x > 4.5 || y < -4.5 || y > 4.5 || z < -4.5 || z > 4.5) {
                                if (this.rand.nextInt(4) != 3) {
                                    this.world.spawnParticle(EnumParticleTypes.CRIT_MAGIC,
                                            this.posX + x + this.rand.nextDouble() / 10 - 0.05,
                                            this.posY + y + this.rand.nextDouble() / 10 - 0.05,
                                            this.posZ + z + this.rand.nextDouble() / 10 - 0.05, 0, 0, 0);
                                }
                            }
                        }
                    }
                }
            }
            this.world.getEntities(EntityLivingBase.class, entity -> entity.getDistanceSqToEntity(this) < 25).forEach(
                    entity -> {
                        entity.motionX = (this.posX - entity.posX) / 2;
                        entity.motionY = (this.posY - entity.posY) / 2;
                        entity.motionZ = (this.posZ - entity.posZ) / 2;
                    });
            if (this.ticksExisted > 100) {
                if (!this.world.isRemote) {
                    this.world.createExplosion(this, this.posX, this.posY, this.posZ, 10, true);
                }
                this.getDataManager().set(EXPLODED, false);
                this.dead = true;
                this.setDead();
            }
        } else {
            if (!this.getPowered()) {
                TakumiUtils.takumiSetPowered(this, true);
            }
            super.onUpdate();
        }
    }

    @Override
    public void setDead() {
        if (this.world.getDifficulty() == EnumDifficulty.PEACEFUL || !this.getDataManager().get(EXPLODED) ||
                this.dead) {
            super.setDead();
        }
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(EXPLODED, Boolean.FALSE);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        if (this.dataManager.get(EXPLODED)) {
            compound.setBoolean("exploded", true);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.dataManager.set(EXPLODED, compound.getBoolean("exploded"));
    }

    @Override
    public void takumiExplode() {
        this.setInvisible(true);
        this.ticksExisted = 0;
        this.getDataManager().set(EXPLODED, true);
    }

    @Override
    public int getPrimaryColor() {
        return 0x88aaff;
    }

    @Override
    public ResourceLocation getArmor() {
        return new ResourceLocation(TakumiCraftCore.MODID, "textures/entity/crystal_creeper_armor.png");
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL_MD;
    }

    @Override
    public int getExplosionPower() {
        return 0;
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
        return "crystalcreeper";
    }

    @Override
    public int getRegisterID() {
        return 65;
    }
}