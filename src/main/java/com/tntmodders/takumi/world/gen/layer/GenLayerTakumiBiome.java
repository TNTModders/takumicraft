package com.tntmodders.takumi.world.gen.layer;

import com.tntmodders.takumi.core.TakumiBiomeCore;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.common.BiomeManager.BiomeEntry;

import java.util.ArrayList;
import java.util.Random;

public class GenLayerTakumiBiome extends GenLayerTakumiWorld {
    
    private final WeightedList <BiomeEntry> biomesToGenerate = new WeightedList <>();
    private final int totalWeight;
    
    public GenLayerTakumiBiome(long seed, GenLayer parentGenLayer) {
        super(seed);
        this.biomesToGenerate.add(new BiomeEntry(TakumiBiomeCore.TAKUMI_PLAINS, 2));
        this.biomesToGenerate.add(new BiomeEntry(TakumiBiomeCore.TAKUMI_FOREST, 2));
        this.biomesToGenerate.add(new BiomeEntry(TakumiBiomeCore.TAKUMI_MOUNTAINS, 2));
        this.biomesToGenerate.add(new BiomeEntry(TakumiBiomeCore.TAKUMI_HOTSPRING_MOUNTAINS, 1));
        this.biomesToGenerate.add(new BiomeEntry(TakumiBiomeCore.TAKUMI_LAVA_MOUNTAINS, 1));
        this.biomesToGenerate.add(new BiomeEntry(TakumiBiomeCore.TAKUMI_OCEAN, 1));
        this.biomesToGenerate.add(new BiomeEntry(TakumiBiomeCore.TAKUMI_OBJET, 1));
        this.biomesToGenerate.add(new BiomeEntry(TakumiBiomeCore.TAKUMI_MESA, 1));
        this.totalWeight = this.biomesToGenerate.getTotalWeight();
        this.parent = parentGenLayer;
    }
    
    @Override
    public int[] getInts(int x, int z, int sizeX, int sizeZ) {
        this.parent.getInts(x, z, sizeX, sizeZ);
        int[] ints = IntCache.getIntCache(sizeX * sizeZ);
        
        for (int zz = 0; zz < sizeZ; ++zz) {
            for (int xx = 0; xx < sizeX; ++xx) {
                this.initChunkSeed(xx + x, zz + z);
                ints[xx + zz * sizeX] = Biome.getIdForBiome(this.biomesToGenerate.getRandomItem(this.nextInt(this.totalWeight)).biome);
            }
        }
        
        return ints;
    }
    
    private class WeightedList <T extends BiomeEntry> extends ArrayList <T> {
        
        private int totalWeight;
        
        @Override
        public boolean add(T obj) {
            boolean b = super.add(obj);
            recalculateWeight();
            return b;
        }
        
        @Override
        public T remove(int index) {
            T is = super.remove(index);
            recalculateWeight();
            return is;
        }
        
        @Override
        public boolean remove(Object o) {
            boolean b = super.remove(o);
            recalculateWeight();
            return b;
        }
        
        public void recalculateWeight() {
            totalWeight = 0;
            for (T obj : this) { totalWeight += obj.itemWeight; }
        }
        
        public int getTotalWeight() {
            return totalWeight;
        }
        
        public T getRandomItem(Random rand) {
            if (totalWeight == 0) { return null; }
            int i = rand.nextInt(totalWeight);
            
            for (T obj : this) {
                i -= obj.itemWeight;
                if (i < 0) { return obj; }
            }
            return null;
        }
        
        public T getRandomItem(int weight) {
            if (totalWeight == 0) { return null; }
            
            for (T obj : this) {
                weight -= obj.itemWeight;
                if (weight < 0) { return obj; }
            }
            return null;
        }
    }
}
