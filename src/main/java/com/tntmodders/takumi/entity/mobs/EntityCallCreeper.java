package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.world.World;

import java.util.List;

public class EntityCallCreeper extends EntityTakumiAbstractCreeper {
    
    public EntityCallCreeper(World worldIn) {
        super(worldIn);
    }
    
    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            List <Entity> entities = this.world.loadedEntityList;
            for (Entity entity : entities) {
                if (entity instanceof EntityCreeper && this.getDistanceSqToEntity(entity) < (this.getPowered() ? 2500 : 900)) {
                    ((EntityCreeper) entity).setAttackTarget(this.getAttackTarget());
                    ((EntityCreeper) entity).ignite();
                }
            }
        }
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
        return 0xffff;
    }
    
    @Override
    public boolean isCustomSpawn() {
        return false;
    }
    
    @Override
    public String getRegisterName() {
        return "callcreeper";
    }
    
    @Override
    public int getRegisterID() {
        return 230;
    }
}
