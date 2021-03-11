package com.tntmodders.takumi.world.provider;

import com.tntmodders.takumi.core.TakumiBlockCore;
import com.tntmodders.takumi.core.TakumiWorldCore;
import com.tntmodders.takumi.world.chunk.TakumiWorldChunkGenerator;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TakumiWorldProvider extends WorldProvider {

    private final Vec3d fogColor = new Vec3d(0, 0.25, 0);
    private IChunkGenerator chunkGenerator;

    public TakumiWorldProvider() {
        super();
        //this.hasSkyLight = false;
    }

    @Override
    protected void generateLightBrightnessTable() {
        for (int i = 0; i <= 15; ++i) {
            float f1 = 1.0F - (float) i / 15.0F;
            this.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * 1.0F * 0.05f;
        }
    }

    @Override
    public float getSunBrightness(float par1) {
        return 0;
    }

    @Override
    public float getSunBrightnessFactor(float par1) {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getStarBrightness(float par1)
    {
        return 0;
    }

    @Override
    protected void init() {
        this.hasSkyLight = false;
        this.doesWaterVaporize = false;
        this.chunkGenerator = new TakumiWorldChunkGenerator(this.world, this.getSeed(), true,
                this.world.getWorldInfo().getGeneratorOptions());
        this.biomeProvider = new TakumiBiomeProvider(this.world.getWorldInfo());
    }

    @Override
    public IChunkGenerator createChunkGenerator() {
        return this.chunkGenerator;
    }

    @Override
    public boolean canCoordinateBeSpawn(int x, int z) {
        BlockPos blockpos = new BlockPos(x, 0, z);
        return this.world.getBiome(blockpos).ignorePlayerSpawnSuitability() ||
                this.world.getGroundAboveSeaLevel(blockpos).getBlock() == TakumiBlockCore.TAKUMI_GRASS;
    }

    @Override
    public float calculateCelestialAngle(long worldTime, float partialTicks) {
        return 0.625f;
    }

    @Override
    public int getMoonPhase(long worldTime) {
        return 0;
    }

    @Override
    public Vec3d getFogColor(float p_76562_1_, float p_76562_2_) {
        return this.fogColor;
    }

    @Override
    public boolean canRespawnHere() {
        return false;
    }

    @Override
    public BiomeProvider getBiomeProvider() {
        return this.biomeProvider;
    }

    @Override
    public double getMovementFactor() {
        return 1;
    }

    @Override
    public boolean shouldMapSpin(String entity, double x, double z, double rotation) {
        return true;
    }

    @Override
    public Vec3d getSkyColor(Entity cameraEntity, float partialTicks) {
        return this.fogColor;
    }

    @Override
    public DimensionType getDimensionType() {
        return TakumiWorldCore.TAKUMI_WORLD;
    }

    @Override
    public boolean isSkyColored() {
        return true;
    }

    @Override
    public double getVoidFogYFactor() {
        return 256;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean doesXZShowFog(int x, int z) {
        return true;
    }

    @Override
    public boolean hasSkyLight() {
        return false;
    }

  @Override
    public boolean isDaytime() {
        return false;
    }

    /*    @Override
    public void onWorldUpdateEntities() {
        super.onWorldUpdateEntities();
        this.world.getWorldInfo().setRaining(true);
        this.world.getWorldInfo().setRainTime(10000);
        this.world.getWorldInfo().setThundering(true);
        this.world.getWorldInfo().setThunderTime(10000);
    }*/

    /*@Override
    public void resetRainAndThunder() {
        this.world.getWorldInfo().setRaining(true);
        this.world.getWorldInfo().setRainTime(Integer.MAX_VALUE - 1);
        this.world.getWorldInfo().setThundering(true);
        this.world.getWorldInfo().setThunderTime(Integer.MAX_VALUE - 1);
    }

    @Override
    public void onPlayerAdded(EntityPlayerMP player) {
        this.world.getWorldInfo().setRaining(true);
        this.world.getWorldInfo().setRainTime(Integer.MAX_VALUE - 1);
        this.world.getWorldInfo().setThundering(true);
        this.world.getWorldInfo().setThunderTime(Integer.MAX_VALUE - 1);
    }*/
}
