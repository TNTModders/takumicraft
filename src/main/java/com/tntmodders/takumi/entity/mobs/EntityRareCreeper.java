package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

import java.util.Random;

public class EntityRareCreeper extends EntityTakumiAbstractCreeper {
    
    public EntityRareCreeper(World worldIn) {
        super(worldIn);
        this.setSize(0.6F * 3, 1.7F * 3);
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100);
    }
    
    @Override
    public void takumiExplode() {
        if (!this.world.isRemote) {
            for (int t = 0; t < (this.getPowered() ? 75 : 40); t++) {
                Random rand = new Random();
                int i = this.getPowered() ? 50 : 30;
                double x = this.posX + this.rand.nextInt(i * 2) - i;
                double y = this.posY + this.rand.nextInt(i) - i / 2;
                double z = this.posZ + this.rand.nextInt(i * 2) - i;
                this.world.createExplosion(this, x, y, z, this.getPowered() ? 5 : 3, true);
            }
        }
    }
    
    @Override
    public EnumTakumiRank takumiRank() {
        return EnumTakumiRank.HIGH;
    }
    
    @Override
    public EnumTakumiType takumiType() {
        return EnumTakumiType.WATER_MD;
    }
    
    @Override
    public int getExplosionPower() {
        return 5;
    }
    
    @Override
    public int getSecondaryColor() {
        return 0x00ff00;
    }
    
    @Override
    public boolean isCustomSpawn() {
        return true;
    }
    
    @Override
    public String getRegisterName() {
        return "rarecreeper";
    }
    
    @Override
    public int getRegisterID() {
        return 407;
    }
    
    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        for (BlockPos pos : event.getAffectedBlocks()) {
            event.getWorld().setBlockState(pos, Blocks.ICE.getDefaultState());
        }
        event.getAffectedBlocks().clear();
        return true;
    }
    
    @Override
    public int getPrimaryColor() {
        return 0x0000ff;
    }
    
    @Override
    public double getSizeAmp() {
        return 3d;
    }
}
