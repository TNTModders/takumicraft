package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityMusicCreeper extends EntityTakumiAbstractCreeper {
    public EntityMusicCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void takumiExplode() {
        this.world.playRecord(this.getPosition(), SoundEvents.RECORD_13);
    }

    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.LOW;
    }

    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.WIND_M;
    }

    @Override
    public int getExplosionPower() {
        return 3;
    }

    @Override
    public int getSecondaryColor() {
        return 0xffaa00;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "musiccreeper";
    }

    @Override
    public int getRegisterID() {
        return 42;
    }

    @Override
    public void onUpdate() {
        if (this.isEntityAlive() && this.rand.nextInt(10) == 0) {
            this.world.spawnParticle(EnumParticleTypes.NOTE, this.posX + rand.nextFloat() - 0.5, this.posY + rand.nextFloat(), this.posZ + rand.nextFloat() - 0.5,
                    -this.motionX, -this.motionY, -this.motionZ);
        }
        /*try {
            Field field = EntityCreeper.class.getDeclaredField("timeSinceIgnited");
            field.setAccessible(true);
            int timeSinceIgnited = (int) field.get(this);
            if (timeSinceIgnited == 30 && FMLCommonHandler.instance().getSide().isClient()) {
                Minecraft.getMinecraft().world.playRecord(this.getPosition(), SoundEvents.RECORD_13);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        super.onUpdate();
    }

    @Override
    public void onDeath(DamageSource cause) {
        if (!this.world.isRemote) {
            this.dropItem(Items.RECORD_13, 1);
        }
        super.onDeath(cause);
    }

/*    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {

        return true;
    }*/
}
