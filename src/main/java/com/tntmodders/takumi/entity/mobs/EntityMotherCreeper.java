package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityMotherCreeper extends EntityTakumiAbstractCreeper {

    int coolTimer;

    public EntityMotherCreeper(World worldIn) {
        super(worldIn);
        this.setSize(1.2F, 3.4F);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(128);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(80);
    }

    @Override
    public boolean getCanSpawnHere() {
        return this.rand.nextBoolean() && super.getCanSpawnHere();
    }

    @Override
    public double getSizeAmp() {
        return 2.0;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.coolTimer--;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (!this.world.isRemote && source.getTrueSource() instanceof EntityPlayer && this.coolTimer < 0) {
            this.coolTimer = 100;
            EntityCreeper creeper = new EntityCreeper(this.world);
            creeper.setHealth(10);
            creeper.copyLocationAndAnglesFrom(source.getTrueSource());
            spawnChild(creeper);
        }
        return super.attackEntityFrom(source, amount);
    }

    private void spawnChild(EntityCreeper creeper) {
        creeper.setPosition(creeper.posX - 4 + this.rand.nextInt(9), creeper.posY,
                creeper.posZ - 4 + this.rand.nextInt(9));
        this.world.spawnEntity(creeper);
        this.world.createExplosion(creeper, creeper.posX, creeper.posY, creeper.posZ, 0f, false);
    }

    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            for (int i = 0; i < (this.getPowered() ? 8 : 5); i++) {
                EntityCreeper creeper = new EntityCreeper(this.world);
                creeper.copyLocationAndAnglesFrom(this);
                spawnChild(creeper);
            }
        }
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.NORMAL;
    }

    @Override
    public int getExplosionPower() {
        return 0;
    }

    @Override
    public int getSecondaryColor() {
        return 0x009900;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "mothercreeper";
    }

    @Override
    public int getRegisterID() {
        return 77;
    }
}
