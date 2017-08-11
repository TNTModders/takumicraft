package com.tntmodders.takumi.entity;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ITakumiEntity {
    void takumiExplode();

    boolean takumiExplodeEvent(ExplosionEvent.Detonate event);

    EnumTakumiRank takumiRank();

    EnumTakumiType takumiType();

    int getExplosionPower();

    int getPrimaryColor();

    int getSecondaryColor();

    boolean isCustomSpawn();

    @SideOnly(Side.CLIENT)
    RenderLiving getRender(RenderManager manager);

    @SideOnly(Side.CLIENT)
    ResourceLocation getArmor();

    String getRegisterName();

    enum EnumTakumiRank {
        LOW(1, 5, 10, 40),
        MID(2, 10, 50, 4),
        HIGH(3, 100, 250, 0),
        BOSS(4, 500, 500, 0),
        TAKUMI(0, 0, 0, 0);

        private final int level;
        private final int experiment;
        private final int point;
        private final int spawnWeight;

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

        EnumTakumiRank(int lv, int exp, int poi, int sw) {
            this.level = lv;
            this.experiment = exp;
            this.point = poi;
            this.spawnWeight = sw;
        }
    }

    enum EnumTakumiType {
        TAKUMI(0),
        FIRE(1),
        GRASS(2),
        GROUND(3),
        WIND(4),
        WATER(5),
        DRAGON(6),
        NORMAL(7);

        private final int id;

        EnumTakumiType(int i) {
            this.id = i;
        }

        private boolean isMagic = false;
        private boolean isDest = false;

        public boolean isMagic() {
            return isMagic;
        }

        public EnumTakumiType setMagic(boolean magic) {
            isMagic = magic;
            return this;
        }

        public boolean isDest() {
            return isDest;
        }

        public EnumTakumiType setDest(boolean dest) {
            isDest = dest;
            return this;
        }

        public boolean getStrong(EnumTakumiType enemyType) {
            if (this.id != 0 && this.id != 6 && enemyType.id != 0 && enemyType.id != 6) {
                if (enemyType.id - this.id == 1 || enemyType.id - this.id == -4) {
                    return true;
                }
            } else if (this.id == 6 && enemyType.id != 0) {
                return true;
            } else if (this.id == 0 && enemyType.id == 6) {
                return true;
            } else if (this.id == 7 && enemyType.id == 7) {
                return true;
            }
            return false;
        }
    }
}
