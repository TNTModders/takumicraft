package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityLevelUpCreeper extends EntityTakumiAbstractCreeper {

    private int level;

    public EntityLevelUpCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void setDead() {
        if (!(this.getHealth() <= 0 || this.world.getDifficulty() == EnumDifficulty.PEACEFUL)) {
            if (!this.world.isRemote) {
                EntityLevelUpCreeper levelUpCreeper = new EntityLevelUpCreeper(this.world);
                levelUpCreeper.copyLocationAndAnglesFrom(this);
                levelUpCreeper.setHealth(this.getHealth());
                if (this.getPowered()) {
                    TakumiUtils.takumiSetPowered(levelUpCreeper, true);
                }
                levelUpCreeper.level = this.level + 1;
                this.world.spawnEntity(levelUpCreeper);
            }
        }
        super.setDead();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.addPotionEffect(new PotionEffect(MobEffects.SPEED, 10, this.level * 2));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(128);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("level", this.level);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.level = compound.getInteger("level");
    }

    @Override
    protected void outOfWorld() {
        this.setHealth(0);
        super.outOfWorld();
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return !source.isExplosion() && super.attackEntityFrom(source, amount);
    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
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
        return EnumTakumiType.WIND;
    }

    @Override
    public int getExplosionPower() {
        return (int) (this.level * 1.25 + 3);
    }

    @Override
    public int getSecondaryColor() {
        return 8913151;
    }

    @Override
    public int getPrimaryColor() {
        return 0xaaffff;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "levelupcreeper";
    }

    @Override
    public int getRegisterID() {
        return 281;
    }
}
