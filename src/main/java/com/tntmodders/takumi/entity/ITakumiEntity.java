package com.tntmodders.takumi.entity;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ITakumiEntity {
    
    void additionalSpawn();
    
    boolean canRegister();
    
    void takumiExplode();
    
    boolean takumiExplodeEvent(Detonate event);
    
    EnumTakumiRank takumiRank();
    
    EnumTakumiType takumiType();
    
    void customSpawn();
    
    boolean isAnimal();
    
    int getExplosionPower();
    
    int getPrimaryColor();
    
    int getSecondaryColor();
    
    boolean isCustomSpawn();
    
    @SideOnly(Side.CLIENT)
    Object getRender(RenderManager manager);
    
    @SideOnly(Side.CLIENT)
    ResourceLocation getArmor();
    
    String getRegisterName();
    
    /**
     * @return LOW     : 1 ~ 200
     * MID      : 201 ~ 400
     * HIGH    : 401 ~ 500
     * BOSS    :501 ~ 600
     * COLL     :700 ~
     * TAK       :777
     * ITEM     :900~
     */
    int getRegisterID();
    
    enum EnumTakumiRank {
        LOW(1, 5, 10, 70), MID(2, 10, 50, 35), HIGH(3, 100, 250, 0), BOSS(4, 500, 500, 0), TAKUMI(0, 0, 0, 0);
        
        private final int level;
        private final int experiment;
        private final int point;
        private final int spawnWeight;
        
        EnumTakumiRank(int lv, int exp, int poi, int sw) {
            this.level = lv;
            this.experiment = exp;
            this.point = poi;
            this.spawnWeight = sw;
        }
        
        public int getSpawnWeight() {
            return spawnWeight;
        }
        
        public int getLevel() {
            return level;
        }
        
        public int getExperiment() {
            return experiment;
        }
        
        public int getPoint() {
            return point;
        }
    }
    
    enum EnumTakumiType {
        TAKUMI(0), FIRE(1), GRASS(2), GROUND(3), WIND(4), WATER(5), DRAGON(6), NORMAL(7), TAKUMI_D(0, true, false), FIRE_D(1, true, false), GRASS_D
                (2, true, false), GROUND_D(3, true, false), WIND_D(4, true, false), WATER_D(5, true, false), DRAGON_D(6, true, false), NORMAL_D(7,
                true, false), TAKUMI_M(0, false, true), FIRE_M(1, false, true), GRASS_M(2, false, true), GROUND_M(3, false, true), WIND_M(4, false,
                true), WATER_M(5, false, true), DRAGON_M(6, false, true), NORMAL_M(7, false, true), TAKUMI_MD(0, true, true), FIRE_MD(1, true,
                true), GRASS_MD(2, true, true), GROUND_MD(3, true, true), WIND_MD(4, true, true), WATER_MD(5, true, true), DRAGON_MD(6, true, true)
        , NORMAL_MD(7, true, true), CERULEAN(8, false, false), YUKARI(9, false, false);
        
        private final int id;
        private boolean isMagic;
        private boolean isDest;
        
        EnumTakumiType(int i, boolean dest, boolean magic) {
            this(i);
            this.isDest = dest;
            this.isMagic = magic;
        }
        
        EnumTakumiType(int i) {
            this.id = i;
        }
        
        public int getId() {
            return id;
        }
        
        public boolean isMagic() {
            return isMagic;
        }
        
        public boolean isDest() {
            return isDest;
        }
        
        public String getName() {
            String s = this.name().toLowerCase();
            if (s.contains("_")) {
                s = s.split("_")[0];
            }
            return s;
        }
        
        public boolean getStrong(EnumTakumiType enemyType) {
            if (this.id == 7 && enemyType.id == 7) {
                return this.getStrongMD(enemyType);
            } else if (this.id != 0 && this.id != 6 && enemyType.id != 0 && enemyType.id != 6) {
                if (enemyType.id - this.id == 1 || enemyType.id - this.id == -4) {
                    return this.getStrongMD(enemyType);
                }
            } else if (this.id == 6 && enemyType.id != 0) {
                return this.getStrongMD(enemyType);
            } else if (this.id == 0 && enemyType.id == 6) {
                return this.getStrongMD(enemyType);
            }
            return false;
        }
        
        private boolean getStrongMD(EnumTakumiType enemyType) {
            return !this.isDest && !this.isMagic && !enemyType.isDest && !enemyType.isMagic || this.isDest && enemyType.isMagic || this.isMagic &&
                    enemyType.isMagic;
        }
    }
}
