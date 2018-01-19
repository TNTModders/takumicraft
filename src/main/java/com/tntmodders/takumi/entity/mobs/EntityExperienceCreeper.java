package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import com.tntmodders.takumi.entity.item.EntityTakumiExpOrb;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.world.World;

public class EntityExperienceCreeper extends EntityTakumiAbstractCreeper {
    
    public EntityExperienceCreeper(World worldIn) {
        super(worldIn);
    }
    
    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            int i = this.experienceValue * 2;
            while (i > 0) {
                int j = EntityXPOrb.getXPSplit(i);
                i -= j;
                for (int i1 = 0; i1 < 10; i1++) {
                    this.world.spawnEntity(new EntityTakumiExpOrb(this.world, this.posX, this.posY, this.posZ, j));
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
        return EnumTakumiType.NORMAL_M;
    }
    
    @Override
    public int getExplosionPower() {
        return 3;
    }
    
    @Override
    public int getSecondaryColor() {
        return 0x88ff88;
    }
    
    @Override
    public boolean isCustomSpawn() {
        return false;
    }
    
    @Override
    public String getRegisterName() {
        return "expcreeper";
    }
    
    @Override
    public int getRegisterID() {
        return 253;
    }
}
