package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.utils.TakumiUtils;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public class EntityWrylyCreeper extends EntityTakumiAbstractCreeper {
    
    public EntityWrylyCreeper(World worldIn) {
        super(worldIn);
    }
    
    @Override
    protected void despawnEntity() {
        Result result; if ((this.idleTime & 0x1F) == 0x1F && (result = ForgeEventFactory.canEntityDespawn(this)) != Result.DEFAULT) {
            if (result == Result.DENY) {
                this.idleTime = 0;
            } else {
                this.setHealth(0);
                this.setDead();
            }
        } else {
            Entity entity = this.world.getClosestPlayerToEntity(this, -1.0D);
        
            if (entity != null) {
                double d0 = entity.posX - this.posX;
                double d1 = entity.posY - this.posY;
                double d2 = entity.posZ - this.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
            
                if (this.canDespawn() && d3 > 16384.0D) {
                    this.setHealth(0);
                    this.setDead();
                }
            
                if (this.idleTime > 600 && this.rand.nextInt(800) == 0 && d3 > 1024.0D && this.canDespawn()) {
                    this.setHealth(0);
                    this.setDead();
                } else if (d3 < 1024.0D) {
                    this.idleTime = 0;
                }
            }
        }
    }
    
    @Override
    public void setDead() {
        if (!(this.getHealth() <= 0 || this.world.getDifficulty() == EnumDifficulty.PEACEFUL)) {
            if (!this.world.isRemote) {
                EntityWrylyCreeper wrylyCreeper = new EntityWrylyCreeper(this.world);
                wrylyCreeper.copyLocationAndAnglesFrom(this);
                if (this.getPowered()) {
                    TakumiUtils.takumiSetPowered(wrylyCreeper, true);
                }
                this.world.spawnEntity(wrylyCreeper);
            }
        }
        super.setDead();
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
        return 3;
    }
    
    @Override
    public int getSecondaryColor() {
        return 8913151;
    }
    
    @Override
    public boolean isCustomSpawn() {
        return false;
    }
    
    @Override
    public String getRegisterName() {
        return "wrylycreeper";
    }
    
    @Override
    public int getRegisterID() {
        return 209;
    }
}
