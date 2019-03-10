package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityBoneCreeper extends EntityTakumiAbstractCreeper {

    public EntityBoneCreeper(World worldIn) {
        super(worldIn);
    }

    @Override
    public void onDeath(DamageSource source) {
        if (!this.world.isRemote) {
            this.dropItem(Item.getItemFromBlock(Blocks.BONE_BLOCK), this.rand.nextBoolean() ? 0 : this.rand.nextInt(2));
        }
        super.onDeath(source);
    }
/*
    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        event.getAffectedEntities().forEach(entity -> {
            if (!this.world.isRemote && entity instanceof EntityPlayer) {
                entity.attackEntityFrom(DamageSource.causeMobDamage(this).setExplosion(), 10f);
                if (((EntityPlayer) entity).getHealth() <= 0f) {
                    EntityBoneDummy dummy = new EntityBoneDummy(this.world);
                    dummy.setPosition(entity.posX, entity.posY, entity.posZ);
                    this.world.spawnEntity(dummy);
                }
            }
        });
        return true;
    }*/

    @Override
    public void takumiExplode() {
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
        return 4;
    }

    @Override
    public int getPrimaryColor() {
        return 0x99aa99;
    }

    @Override
    public int getSecondaryColor() {
        return 0xafafaf;
    }

    @Override
    public boolean isCustomSpawn() {
        return false;
    }

    @Override
    public String getRegisterName() {
        return "bonecreeper";
    }

    @Override
    public int getRegisterID() {
        return 69;
    }
}
