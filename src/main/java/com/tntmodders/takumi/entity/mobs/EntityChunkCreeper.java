package com.tntmodders.takumi.entity.mobs;

import com.tntmodders.takumi.entity.EntityTakumiAbstractCreeper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

public class EntityChunkCreeper extends EntityTakumiAbstractCreeper {
    
    public EntityChunkCreeper(World worldIn) {
        super(worldIn);
    }
    
    @Override
    public boolean takumiExplodeEvent(ExplosionEvent.Detonate event) {
        event.getAffectedBlocks().clear();
        if (this.getPowered()) {
            for (int i = 0; i < 4 + this.rand.nextInt(5); i++) {
                this.clearChunk((this.rand.nextInt(5) - 2) * 16, (this.rand.nextInt(5) - 2) * 16, (this.rand.nextInt(5) - 2) * 16);
            }
        }
        this.clearChunk(0, 0, 0);
        return true;
    }
    
    private void clearChunk(int offX, int offY, int offZ) {
        for (int x = -8 + offX; x < 8 + offX; x++) {
            for (int y = -8 + offY; y < 8 + offY; y++) {
                for (int z = -8 + offZ; z < 8 + offZ; z++) {
                    BlockPos pos = this.getPosition().add(x, y, z);
                    if (this.world.getBlockState(pos).getBlockHardness(this.world, pos) > -1) {
                        this.world.setBlockToAir(pos);
                    }
                }
            }
        }
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
        return 0xaaaaaa;
    }
    
    @Override
    public boolean isCustomSpawn() {
        return false;
    }
    
    @Override
    public String getRegisterName() {
        return "chunkcreeper";
    }
    
    @Override
    public int getRegisterID() {
        return 224;
    }
}
